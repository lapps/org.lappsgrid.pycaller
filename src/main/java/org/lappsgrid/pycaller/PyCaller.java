/**********************************************************************************************************************
 Copyright [2014] [Chunqi SHI (chunqi.shi@hotmail.com)]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **********************************************************************************************************************/
package org.lappsgrid.pycaller;

//import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by shi on 5/15/14.
 */
public class PyCaller {
    static final Logger logger = LoggerFactory.getLogger(PyCaller.class);

    static final String pythonBridger = "lapps_pickle_io.py";
    static File pythonBridgerFile = null;

    public static File preparePythonBridge() throws IOException {
        if(pythonBridgerFile == null || !pythonBridgerFile.exists()) {
            pythonBridgerFile = File.createTempFile("lapps_pickle_io", ".py");
            InputStream in = PyCaller.class.getResourceAsStream("/" + pythonBridger);
            FileOutputStream out = new FileOutputStream(pythonBridgerFile, false);
            try {
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            pythonBridgerFile.deleteOnExit();
        }
        return pythonBridgerFile;
    }

    public static Object call(File pyFile, String method, Object... params) throws PyCallerException{
        return call(pyFile.getAbsolutePath(), method, params, new HashMap());
    }

    //
    // running python file with string input
    // and string output
    //
    public static Object call(String pyPath, String method, Object [] params, Map paraMap) throws PyCallerException {
        PickleBridge pb = new PickleBridge();
        pb.put("params", params);
        pb.put("method", method);
        pb.put("map", paraMap);
        pb.put("path", pyPath);
//        logger.info("call(): pickle=" + map2json(pb));
        File pickleFile = null;
        try {
            pickleFile = pb.toPickleFile(pb);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Write pickle file failure ", e);
            throw new PyCallerException("Write Pickle File Failure.", e);
        }
        File file = null;
        try {
            file = preparePythonBridge();
        } catch (IOException e) {
            e.printStackTrace();
            throw new PyCallerException("Create file error : " + file, e);
        }
//        String jsonResult = callIO(file, pickleFile.toString());
//        logger.info("jsonResult="+jsonResult);
//        Map map = json2map(jsonResult);
        Map map = callIO(file, pickleFile.toString());
        File outputFile = new File((String)map.get("output"));
        if (pickleFile.exists()) {
            pickleFile.delete();
        }
        Map res = null;
        try {
            res = (Map)pb.fromPickleFile(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Read pickle file failure " + outputFile, e);
            throw new PyCallerException("Read Pickle File Failure: " + outputFile, e);
        } finally {
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }
        Object results = res.get("result");
        logger.info("call(): result=" + res);
        return results;
    }

//    static Map json2map(String json){
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return (Map)mapper.readValue(json, Map.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("Json serialization error. ", e);
//        }
//        return null;
//    }

//    static String map2json(Map map) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.writeValueAsString(map);
//        }catch (IOException e) {
//            e.printStackTrace();
//            logger.error("Json de-serialization error. ", e);
//        }
//        return null;
//    }

    //
    // running python file with string input and string output
    //
    private static Map callIO(File pyFile, String ... params) throws PyCallerException{
        Map result = new LinkedHashMap();
        List<String> callAndArgs = new ArrayList<String>(params.length + 2);
        callAndArgs.add("python");
        callAndArgs.add(pyFile.getAbsolutePath());
        logger.info("call(): python=" + pyFile);
        for (String param: params) {
            callAndArgs.add(param);
        }
        result.put("python", pyFile.getAbsolutePath());
        result.put("params", params);
        try{
            ProcessBuilder builder = new ProcessBuilder(callAndArgs);
            Process p = builder.start();
            BufferedReader stdInput =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError =
                    new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String st = null;
            StringBuilder sb = new StringBuilder();
            while ((st = stdInput.readLine()) != null) {
                sb.append(st);
            }
            result.put("output", sb.toString());
            sb.setLength(0);
            while ((st = stdError.readLine()) != null) {
                sb.append(st);
            }
            result.put("error", sb.toString());
            int retcode = p.waitFor();
            result.put("retcode", retcode);
            p.destroy();
        }catch(Exception e){
            e.printStackTrace();
            throw new PyCallerException(e);
        }
//        logger.info("call(): result=" + map2json(result));
//        return map2json(result);
        return result;
    }


}
