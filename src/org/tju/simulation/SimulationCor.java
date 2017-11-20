package org.tju.simulation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import org.tju.bean.DiskInfo;
import org.tju.initialization.InitEnvironment;
import org.tju.request.GenerateRequest;
import org.tju.request.RequestCorrelation;
import org.tju.request.RequestInfo;
import org.tju.scheduler.DiskOperation;
import org.tju.scheduler.Scheduler;
import org.tju.track.Track;
import org.tju.track.bean.ArrivalRate;
import org.tju.track.bean.DiskState;
import org.tju.util.ValueOfConfigureFile;

public class SimulationCor {
	
	//Random
	public static Random random = new Random();
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//Disks' response time
	public static int SSDResponseTime = valueOfConfigureFile.getSSDResponseTime();
	public static int cacheResponseTime = valueOfConfigureFile.getCacheResponseTime();
	public static int dataResponseTime = valueOfConfigureFile.getDataResponseTime();
	
	//Refresh time
	public static int refreshTime = valueOfConfigureFile.getRefreshTime();
	
	
	//initialize basic environment
	public static InitEnvironment init = new InitEnvironment();
	
	//Generate Requests
	public static GenerateRequest requests = new GenerateRequest();
	
	//Track of all requests
	public static HashMap<String, RequestInfo> requestsTrack = new HashMap<String, RequestInfo>();
	
	//Track of arrivalRate
	public static HashMap<Integer, ArrivalRate> arrivalRateTrack = new HashMap<Integer, ArrivalRate>();
	
	//Track of data disks' state
	public static HashMap<Integer, DiskState> dataDiskStateTrack = new HashMap<Integer, DiskState>();
	
	//Track of cache disks' state
	public static HashMap<Integer, DiskState> cacheDiskStateTrack = new HashMap<Integer, DiskState>();
	
	//Transmission time
	public static int transTime = 0;
	
	//Request correlations: key---blockId, value---correlate blockIds
//	public static HashMap<Integer, Integer[]> requestCorrelatins = new HashMap<Integer, Integer[]>();
	
	
	//Main function
	public static void main(String[] args){
		
		//Initialize basic environment
		init.initEnvironment();
		
		//get Disks Info && Blocks' info
		DiskInfo[] SSDDisks = init.getSSDDisk();
		DiskInfo[] cacheDisks = init.getCacheDisks();
		DiskInfo[] dataDisks = init.getDataDisks();
		
		
		//Lables of Data Disks: Time Disk-1 Disk-2 ... Disk-dataDisks.length Total
		String[] dataDiskLables = new String[dataDisks.length+2];
		dataDiskLables[0] = "Time";
		for(int i=1; i<dataDiskLables.length-1; i++){
			dataDiskLables[i] = "Disk-" + i;
		}
		
		dataDiskLables[dataDiskLables.length-1] = "Total";
		
			
		//Lables of Cache Disks: Time Cache-1 Cache-2 ... Cache-cacheDisks.length Total
		String[] cacheDiskLables = new String[cacheDisks.length+2];
		cacheDiskLables[0] = "Time";
		for(int i=1; i<cacheDiskLables.length-1; i++){
			cacheDiskLables[i] = "Cache-" + i;
		}
		
		cacheDiskLables[cacheDiskLables.length-1] = "Total";
		
		
		//Lables of Response: SSD-0 ... Cache-0... DataDisk-0
		String[] responseLables = new String[SSDDisks.length+cacheDisks.length+dataDisks.length];
		
		int m = 0;
		for( ; m<SSDDisks.length; m++){
			responseLables[m] = "SSD-" + m;
		}
		for( ; m-SSDDisks.length<cacheDisks.length; m++){
			responseLables[m] = "Cache-" + (m-SSDDisks.length);
		}
		for( ; m-SSDDisks.length-cacheDisks.length<dataDisks.length; m++){
			responseLables[m] = "DataDisk-" + (m-SSDDisks.length-cacheDisks.length);
		}
		
		
		//Generate Requests
		//Requests List of all
		HashMap<Integer, HashMap<String, RequestInfo>> requestsList = requests.generateRequest();
		
		//Specifies the requests' generation time
		requests.specifiesRequestTime(requestsList);
		
		System.out.println("requestsList.size()" + requestsList.size());
		
		// add
		HashMap<String, RequestInfo> reqCorTrain = new HashMap<String, RequestInfo>();
		
		
		System.out.println("Start to run!");
		//Start to run
		for(int i=0; i<requestsList.size(); i++){//iΪtimeWindow
			HashMap<String, RequestInfo> requestsPerWindow = requestsList.get(i);
			
			// add
			reqCorTrain.putAll(requestsPerWindow);
			if (i % 50 == 49) {
				RequestCorrelation.updateCorr(reqCorTrain);
				reqCorTrain.clear();
			}
			
			double arrivalRate = ((double)requestsPerWindow.size())/requests.slidingWindowSize;
			
			//Track of arrivalRate
			ArrivalRate R = new ArrivalRate(i, arrivalRate);
			arrivalRateTrack.put(i, R);
			//j means time point
			for(int j=i*requests.slidingWindowSize; j<(i+1)*requests.slidingWindowSize; j++){
				
				//added by xx begin
				System.out.println("**** time "+ j +" ****");
				//added by xx end
				
				//refresh Countdown   ?????
				int refreshCountdown = (j+1)%refreshTime;
				
				//update blocks' transmissionTime in cache disks
				//update blocks' transmissionTime in SSD
				TransTimeOperation.UpdateTranTime(SSDDisks);
				
				//update blocks' transmissionTime in Cache
				TransTimeOperation.UpdateTranTime(cacheDisks);
						
				
				Iterator<Entry<String, RequestInfo>> iter = requestsPerWindow.entrySet().iterator();
				
				while (iter.hasNext()){
					Entry<String, RequestInfo> entry = iter.next();
					
					RequestInfo request = entry.getValue();
					
					if(request.getGenerateTime() == j){
						//start service
						String fileName = request.getRequestFileName();
						//diskID-blockId-skyzone-observeTime
						String[] names = fileName.split("-");
						int diskId = Integer.valueOf(names[0]);
						int blockId = Integer.valueOf(names[1]);					
						
						//search Disks
						if(SearchInDisks.searchInSSD(SSDDisks, fileName, blockId)){//find in SSD
							transTime = TransTimeOperation.getTransTime(SSDDisks, blockId);
							
							//update requests' info
							request.setResponseTime(j+transTime+SSDResponseTime);
							request.setQos(transTime+SSDResponseTime);
							
							//Add to track
							requestsTrack.put(request.getRequestFileName(), request);
							
							//Message
							System.out.println("     "+request.getRequestFileName() + " : Found In SSD!");
						} else if (SearchInDisks.searchInCache(cacheDisks, fileName, blockId)) {//find in Cache
							transTime = TransTimeOperation.getTransTime(cacheDisks, blockId);
							
							//update requests' info
							request.setResponseTime(j+transTime+cacheResponseTime);
							request.setQos(transTime+cacheResponseTime);
							
							//Add to track
							requestsTrack.put(request.getRequestFileName(), request);
							
							//Message
							System.out.println("     "+request.getRequestFileName() + " : Found In Cache Disk!");
						} else if (SearchInDisks.searchInDD(dataDisks, fileName, diskId, blockId)) {//find in dataDisk
							diskId = SearchInDisks.searchBetterInDD(dataDisks, blockId);//add
							transTime = TransTimeOperation.getTransTime(dataDisks[diskId], blockId);
							
							//update requests' info
							request.setResponseTime(j+transTime+cacheResponseTime);
							request.setQos(transTime+cacheResponseTime);
							
							//Add to track
							requestsTrack.put(request.getRequestFileName(), request);
							
							//Message
							System.out.println("     "+request.getRequestFileName() + " : Found In Data Disk " + diskId + "!");	
						
							
							//Data Migration: From Data Disk To Cache Disks
							Cache.DataMigration(SSDDisks, cacheDisks, dataDisks[diskId], blockId, refreshCountdown);
							
							
							//Disk Operation
							PreheatDisk.PreheatCaches(SSDDisks, cacheDisks, arrivalRate);
						
						} else {
							//update requests' info
							request.setResponseTime(j);
							request.setQos(0);
							
							//Add to track
							requestsTrack.put(request.getRequestFileName(), request);				
							
							//Message
							System.out.println("     "+request.getRequestFileName() + " : Not Found!");	
						}													
					}//end if  if(request.getGenerateTime() == j){
				}
				
				//update blocks' info		
				Scheduler.SchedulerOfSecond(SSDDisks, cacheDisks, dataDisks);
				
				//update data disks
				//update data disks state
				DiskOperation.updateDDs(dataDisks);
				
				//check data disks' idle time
				DiskOperation.checkDDs(dataDisks);
				
				
				//Refresh of Cache: SSD && Cache Disks
				if(refreshCountdown == 0){
					Refresh.RefreshOfCache(SSDDisks, cacheDisks);
				}
				
				
				//Track of Disks' state
				//Track of Data Disks state
				String content = "";
				int total = 0;
				for(int l=0; l<dataDisks.length; l++){
					total += dataDisks[l].getDiskState();
					content += dataDisks[l].getDiskState() + ",";
				}
				content += total;
				DiskState diskStateInfo = new DiskState(j, content);
				dataDiskStateTrack.put(j, diskStateInfo);
				
				//Track of Cache Disks state
				content = "";
				total = 0;
				for(int l=0; l<cacheDisks.length; l++){
					total += cacheDisks[l].getDiskState();
					content += cacheDisks[l].getDiskState() + ",";
				}
				content += total;
				diskStateInfo = new DiskState(j, content);
				cacheDiskStateTrack.put(j, diskStateInfo);
			}
		}
		
		// add
		RequestCorrelation.display();
		System.out.println("Started time of cacheDisks");
		for (int i = 0; i < 4; i++) {
			System.out.print(cacheDisks[i].getStartedTime() + " ");
		}
		System.out.println("\nStarted count of cacheDisks");
		for (int i = 0; i < 4; i++) {
			System.out.print(cacheDisks[i].getStartedCount() + " ");
		}
		System.out.println("\nStarted time of dataDisks");
		for (int i = 0; i < 20; i++) {
			System.out.print(dataDisks[i].getStartedTime() + " ");
		}
		System.out.println("\nStarted count of dataDisks");
		for (int i = 0; i < 20; i++) {
			System.out.print(dataDisks[i].getStartedCount() + " ");
		}
		
		//Track
		Track.trackOfRequest(requestsTrack);
		Track.trackOfArrivalRate(arrivalRateTrack);
		Track.trackOfDataDiskState(dataDiskStateTrack, dataDiskLables);
		Track.trackOfCacheDiskState(cacheDiskStateTrack, cacheDiskLables);
		Track.trackOfResponse(SSDDisks, cacheDisks, dataDisks, responseLables);
				
	}

}
