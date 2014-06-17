#                                                #
# @file:    lapps_pickle_io.py
# @author:    shicq@cs.brandeis.edu
# @brief:   base io functions
# @date:    Apr. 15, 2014
#                                                #


import sys, os, imp, traceback
import cPickle as pickle



def pickleDump(data, file):
    directory = os.path.dirname(file)
    if not os.path.exists(directory):
        os.makedirs(directory)
    output = open(file, 'wb')
    pickle.dump(data, output)
    output.close()

def pickleLoad(file):
    if os.path.exists(file):
        pkl_file = open(file, 'rb')
        data = pickle.load(pkl_file)
        pkl_file.close()
        return data

def loadModule(pyFile):
    if os.path.exists(pyFile):
        _pyPath = os.path.dirname(os.path.expanduser(pyFile))
        sys.path.append(_pyPath)
        _mod_name, _file_ext = os.path.splitext(os.path.split(pyFile)[-1])
        _mod = imp.load_source(_mod_name, pyFile)
        return _mod

def runPythonFuncWithPickle(infil):
    data = pickleLoad(infil)
    pyfil = data['path']
    method = data['method']
    params =  data['params']
    map =  data['map']
    try:
        _module = loadModule(pyfil)
        _callable = getattr(_module, method)
        if callable(_callable):
            data['result'] =  _callable(*params, **map)
        else:
            data['except'] = "Unknown method: " + method + "() in Python file '" + pyfil + "'"
    # https://docs.python.org/2/library/exceptions.html#exceptions.SyntaxError
    # https://docs.python.org/2/library/traceback.html
    except SyntaxError:
        data['except'] = "Fail to compile Python file '" + pyfil + "'"
    except AttributeError:
        data['except'] = "Unknown method: '" + method + "()' in Python file '" + pyfil + "'"
    except:
        exc_type, exc_value, exc_traceback = sys.exc_info()
        data['except'] = repr(traceback.extract_tb(exc_traceback)[1:]).encode('unicode-escape').decode().replace("\\\\", "\\").replace("\\\\", "\\")
        data['except'] = traceback.extract_tb(exc_traceback)[1:]
    outfil = infil + '.out'
    pickleDump(data, outfil)
    return outfil

def main():
    print runPythonFuncWithPickle(sys.argv[1])
    
if __name__ == "__main__":
    main()

