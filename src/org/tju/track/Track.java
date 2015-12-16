package org.tju.track;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.request.RequestInfo;
import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月16日 下午7:35:42
 */
public class Track {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//get Path info from FilePathInfo.xml
	public static String requestFilePath = valueOfConfigureFile.getRequestFilePath();
	public static String requestStaFilePath = valueOfConfigureFile.getRequestStaFilePath();
	public static String blockExchangeFilePath = valueOfConfigureFile.getBlockExchangeFilePath();
	public static String dataDiskStateFilePath = valueOfConfigureFile.getDataDiskStateFilePath();
	public static String cacheDiskStateFilePath = valueOfConfigureFile.getCacheDiskStateFilePath();

	//get files' labels' Info from FileLableInfo.xml
	public String[] requestGenLables = valueOfConfigureFile.getRequestGenLables();
	public String[] requestStaLables = valueOfConfigureFile.getRequestStaLables();
	public String[] blockExchangeLables = valueOfConfigureFile.getBlockExchangeLables();
	public String[] dataDiskStateLables = valueOfConfigureFile.getDataDiskStateLables();
	public String[] cacheDiskStateLables = valueOfConfigureFile.getCacheDiskStateLables();
	
	
	//Track of requests
	public static TrackOfRequest request = new TrackOfRequest();
//	public static 
	
	//
	public static void trackOfRequest(HashMap<String, RequestInfo> requestsTrack){
		
			
		String[] lables1 = {"RequestFileName", "GenerateTime", "ResponseTime", "Qos"};
		
		request.CreateFileOfRequestSta(requestStaFilePath);
		
		request.HeaderOfRequestSta(requestStaFilePath, lables1);	
		
		Iterator<Entry<String, RequestInfo>> iter = requestsTrack.entrySet().iterator();
		
		while (iter.hasNext()){
			Entry<String, RequestInfo> entry = iter.next();
			
			RequestInfo requestInfo = entry.getValue();
		
		    request.TrackOfRequestSta(requestStaFilePath, requestInfo);
		}	
	}

}
