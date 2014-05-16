#                                                #
# @file:    lapps_pickle_io.py
# @author:    shicq@cs.brandeis.edu
# @brief:   base io functions
# @date:    Apr. 15, 2014
#                                                #


import sys, os, imp
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

def runModuleFunc(module, method, *args, **kwargs):
    _callable = getattr(module, method)
    if callable(_callable):
        return _callable(*args, **kwargs)

def runPythonFunc(pyFile, method, *args, **kwargs):
    _module = loadModule(pyFile)
    return runModuleFunc(_module, method, *args, **kwargs)

def runPythonFuncWithPickle(infil):
    data = pickleLoad(infil)
    pyfil = data['path']
    params =  data['params']
    ret = runPythonFunc(pyfil, *params)
    data['result'] = ret
    outfil = infil + '.out'
    pickleDump(data, outfil)
    return outfil

def main():
    print runPythonFuncWithPickle(sys.argv[1])
    
if __name__ == "__main__":
    main()

