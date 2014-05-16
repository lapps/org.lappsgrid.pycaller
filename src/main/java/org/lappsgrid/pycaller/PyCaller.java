package org.lappsgrid.pycaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.razorvine.pickle.PickleUtils;
import net.razorvine.pickle.Pickler;
import net.razorvine.pickle.Unpickler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shi on 5/15/14.
 */
public class PyCaller {
    static final Logger logger = LoggerFactory.getLogger(PyCaller.class);

    static final String pythonBridger = "lapps_pickle_io.py";

    public static File preparePythonBridge() {
        File file = new File(pythonBridger);
        if(!file.exists()) {
            try{
                InputStream in = PyCaller.class.getResourceAsStream("/" + pythonBridger);
                FileOutputStream out = new FileOutputStream(file, false);
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
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    //
    // running python file with string input
    // and string output
    //
    public static Object call(String pyPath, Object ... params) throws PyCallerException {
        PickleBridge pb = new PickleBridge();
        pb.put("params", params);
        pb.put("path", pyPath);
        logger.info("call(): result=" + pb);
        File pickleFile = null;
        try {
            pickleFile = pb.toPickleFile(pb);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Write pickle file failure ", e);
            throw new PyCallerException("Write Pickle File Failure.", e);
        }
        File file = preparePythonBridge();
        String jsonResult = call(file, pickleFile.toString());
        System.out.println("jsonResult="+jsonResult);
        Map map = json2map(jsonResult);
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

    static Map json2map(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return (Map)mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Json serialization error. ", e);
        }
        return null;
    }

    static String map2json(Map map) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        }catch (IOException e) {
            e.printStackTrace();
            logger.error("Json de-serialization error. ", e);
        }
        return null;
    }

    //
    // running python file with string input and string output
    //
    private static String call(File pyFile, String ... params) throws PyCallerException{
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
        logger.info("call(): result=" + result.toString());
        return map2json(result);
    }


}
