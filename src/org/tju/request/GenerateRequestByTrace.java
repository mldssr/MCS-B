package org.tju.request;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author yuan
 *
 * @date 2015年12月28日 上午9:32:00
 */
public class GenerateRequestByTrace {
	
    // CSV's ENCODE
    public static final String ENCODE = "UTF-8";

    private FileInputStream fis = null;
    private InputStreamReader isw = null;
    private BufferedReader br = null;
    
   
    public GenerateRequestByTrace(String filename) throws Exception {
        fis = new FileInputStream(filename);
        isw = new InputStreamReader(fis, ENCODE);
        br = new BufferedReader(isw);
    }
    


    public HashMap<Integer, HashMap<String, RequestInfo>> readLine() throws Exception {

    	HashMap<String, RequestInfo> requests = new HashMap<String, RequestInfo>();
    	HashMap<Integer, HashMap<String, RequestInfo>> requestList = new HashMap<Integer, HashMap<String, RequestInfo>>();
    	
        StringBuffer readLine = new StringBuffer();
        boolean bReadNext = true;

        int i=1;
        
        while (bReadNext) {
            //
            if (readLine.length() > 0) {
                readLine.append("\r\n");
            }
            // 一行
            String strReadLine = br.readLine();
            
            // readLine is Null
            if (strReadLine == null) {
            	requestList.put(i-1, requests);
            	return requestList;
            }
            String[] info = strReadLine.split(",");
            
            if(Integer.valueOf(info[1])>=10*i){
            	requestList.put(i-1, requests);
            	i++;
            	
            	requests = new HashMap<String, RequestInfo>();
            }
            
            RequestInfo req = new RequestInfo(info[0], Integer.valueOf(info[1]), Integer.valueOf(info[2]), Integer.valueOf(info[3]));
            
            requests.put(info[0], req);
          
        }
        return requestList;
    }

}
