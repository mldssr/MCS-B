package org.tju.track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.request.RequestInfo;
import org.tju.track.bean.ArrivalRate;
import org.tju.track.bean.DiskState;
import org.tju.util.ValueOfConfigureFile;

public class Track {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//get Path info from FilePathInfo.xml
	public static String requestFilePath = valueOfConfigureFile.getRequestFilePath();
	public static String requestStaFilePath = valueOfConfigureFile.getRequestStaFilePath();
	public static String blockExchangeFilePath = valueOfConfigureFile.getBlockExchangeFilePath();
	public static String dataDiskStateFilePath = valueOfConfigureFile.getDataDiskStateFilePath();
	public static String cacheDiskStateFilePath = valueOfConfigureFile.getCacheDiskStateFilePath();
	public static String arrivalRateFilePath = valueOfConfigureFile.getArrivalRateFilePath();
	public static String responseFilePath = valueOfConfigureFile.getResponseFilePath(); 
	
	//get files' labels' Info from FileLableInfo.xml
	public static String[] requestGenLables = valueOfConfigureFile.getRequestGenLables();
	public static String[] requestStaLables = valueOfConfigureFile.getRequestStaLables();
	public static String[] blockExchangeLables = valueOfConfigureFile.getBlockExchangeLables();
	public static String[] dataDiskStateLables = valueOfConfigureFile.getDataDiskStateLables();
	public static String[] cacheDiskStateLables = valueOfConfigureFile.getCacheDiskStateLables();
	public static String[] arrivalRateLables = valueOfConfigureFile.getArrivalRateLables();
	
	//Track of requests
	public static TrackOfRequest request = new TrackOfRequest();
	
	
	//Track of request
	public static void trackOfRequest(HashMap<String, RequestInfo> requestsTrack){
				
		request.CreateFileOfRequestSta(requestStaFilePath);
		
		request.HeaderOfRequestSta(requestStaFilePath, requestStaLables);
		
		List<Entry<String, RequestInfo>> list = new ArrayList<Entry<String, RequestInfo>>(requestsTrack.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<String, RequestInfo>>() {
			public int compare(Map.Entry<String, RequestInfo> e1, Map.Entry<String, RequestInfo> e2) {
				int v1 = (int) ((Map.Entry<String, RequestInfo>) e1).getValue().getGenerateTime();
				int v2 = (int) ((Map.Entry<String, RequestInfo>) e2).getValue().getGenerateTime();
				int flag = v1 - v2;
				if (flag > 0) {
					return 1;
				} else if (flag == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		requestsTrack.clear();
		/*
		for(int i=0; i<list.size(); i++){
			requestsTrack.put(list.get(i).getKey(), list.get(i).getValue());
		}*/
		for(Iterator<Map.Entry<String,RequestInfo>> it=list.iterator(); it.hasNext();)  
        {  
			RequestInfo requestInfo = it.next().getValue();  
			request.TrackOfRequestSta(requestStaFilePath, requestInfo);
        }
		/*
		Iterator<Entry<String, RequestInfo>> iter = requestsTrack.entrySet().iterator();
		
		while (iter.hasNext()){
			Entry<String, RequestInfo> entry = iter.next();
			
			RequestInfo requestInfo = entry.getValue();
		
		    request.TrackOfRequestSta(requestStaFilePath, requestInfo);
		}	*/
	}
	
	
	//Track of Arrival Rate
	public static TrackOfArrivalRate arrivalRate = new TrackOfArrivalRate();
	
	//Track of request
	public static void trackOfArrivalRate(HashMap<Integer, ArrivalRate> arrivalRateTrack){
				
		arrivalRate.CreateFileOfArrivalRate(arrivalRateFilePath);
		
		arrivalRate.HeaderOfArrivalRate(arrivalRateFilePath, arrivalRateLables);	
		
		Iterator<Entry<Integer, ArrivalRate>> iter = arrivalRateTrack.entrySet().iterator();
		
		while (iter.hasNext()){
			Entry<Integer, ArrivalRate> entry = iter.next();
			
			ArrivalRate arrivalRateInfo = entry.getValue();
			
			arrivalRate.TrackOfRate(arrivalRateFilePath, arrivalRateInfo);
		}	
	} 
	
	
	//Track of Disks' state
	public static TrackOfDiskState diskState = new TrackOfDiskState();
	
	//Track of Data Disks' state
	public static void trackOfDataDiskState(HashMap<Integer, DiskState> dataDiskStateTrack, String[] lables){
		
		diskState.CreateFileOfDiskState(dataDiskStateFilePath);
		
		diskState.HeaderOfDiskState(dataDiskStateFilePath, lables);
		
		Iterator<Entry<Integer, DiskState>> iter = dataDiskStateTrack.entrySet().iterator();
		
		while (iter.hasNext()){
			Entry<Integer, DiskState> entry = iter.next();
			
			DiskState diskStateInfo = entry.getValue();
			
			diskState.TrackOfDataDiskStateFile(dataDiskStateFilePath, diskStateInfo);
		}	
		
	}
		
	//Track of Disks' state
	public static TrackOfDiskState cacheDiskState = new TrackOfDiskState();

	//Track of cache Disks' state
	public static void trackOfCacheDiskState(HashMap<Integer, DiskState> cacheDiskStateTrack, String[] lables){
		
		cacheDiskState.CreateFileOfDiskState(cacheDiskStateFilePath);
		
		cacheDiskState.HeaderOfDiskState(cacheDiskStateFilePath, lables);
		
		Iterator<Entry<Integer, DiskState>> iter = cacheDiskStateTrack.entrySet().iterator();
		
		while (iter.hasNext()){
			Entry<Integer, DiskState> entry = iter.next();
			
			DiskState diskStateInfo = entry.getValue();
			
			cacheDiskState.TrackOfDataDiskStateFile(cacheDiskStateFilePath, diskStateInfo);
		}	
		
	}
	
	//Track of Response
	public static TrackOfResponse response = new TrackOfResponse();

	//Track of Response
	public static void trackOfResponse(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks, DiskInfo[] dataDisks, String[] lables){
		
		response.CreateFileOfResponse(responseFilePath);
		
		response.HeaderOfResponse(responseFilePath, lables);
		
		response.TrackOfResponseLoc(responseFilePath, SSDDisks, cacheDisks, dataDisks);
		
	}
	
	public static void CloseAllTrackFile() {
		request.CloseRequestStaFile();
		arrivalRate.CloseArrivalRateFile();
		diskState.CloseDiskStateFile();
		cacheDiskState.CloseDiskStateFile();
		response.CloseResponseFile();
	}
	
}
