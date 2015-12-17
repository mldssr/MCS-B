package org.tju.simulation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import org.tju.bean.DiskInfo;
import org.tju.initialization.InitEnvironment;
import org.tju.request.GenerateRequest;
import org.tju.request.RequestInfo;
import org.tju.scheduler.DiskOperation;
import org.tju.scheduler.Scheduler;
import org.tju.track.Track;
import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月16日 上午8:48:30
 */
public class Simulation {
	
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
	
	//Transmission time
	public static int transTime = 0;
	
	
	//Main function
	public static void main(String[] args){
		
		//Initialize basic environment
		init.initEnvironment();
		
		//get Disks Info && Blocks' info
		DiskInfo[] SSDDisks = init.getSSDDisk();
		DiskInfo[] cacheDisks = init.getCacheDisks();
		DiskInfo[] dataDisks = init.getDataDisks();
		
		
		
		//Generate Requests
		//Requests List of all
		HashMap<Integer, HashMap<String, RequestInfo>> requestsList = requests.generateRequest();;
		
		//Specifies the requests' generation time
		requests.specifiesRequestTime(requestsList);
		
		
		
		//Start to run
		for(int i=0; i<requestsList.size(); i++){
			HashMap<String, RequestInfo> requestsPerWindow = requestsList.get(i);
			
			double arrivalRate = ((double)requestsList.size())/requests.slidingWindowSize;
			
			for(int j=i*requests.slidingWindowSize; j<(i+1)*requests.slidingWindowSize; j++){
				
				//refresh Countdown
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
						if(SearchInDisks.searchInSSD(SSDDisks, fileName, blockId)){
							transTime = TransTimeOperation.getTransTime(SSDDisks, blockId);
							
							//update requests' info
							request.setResponseTime(j+transTime+SSDResponseTime);
							request.setQos(transTime+SSDResponseTime);
							
							//Add to track
							requestsTrack.put(request.getRequestFileName(), request);
							
							//Message
							System.out.println(request.getRequestFileName() + " : Found In SSD!");
						} else if (SearchInDisks.searchInCache(cacheDisks, fileName, blockId)) {
							transTime = TransTimeOperation.getTransTime(cacheDisks, blockId);
							
							//update requests' info
							request.setResponseTime(j+transTime+cacheResponseTime);
							request.setQos(transTime+cacheResponseTime);
							
							//Add to track
							requestsTrack.put(request.getRequestFileName(), request);
							
							//Message
							System.out.println(request.getRequestFileName() + " : Found In Cache Disk!");
						} else if (SearchInDisks.searchInDD(dataDisks, fileName, diskId, blockId)) {
							transTime = TransTimeOperation.getTransTime(dataDisks[diskId], blockId);
							
							//update requests' info
							request.setResponseTime(j+transTime+cacheResponseTime);
							request.setQos(transTime+cacheResponseTime);
							
							//Add to track
							requestsTrack.put(request.getRequestFileName(), request);
							
							//Message
							System.out.println(request.getRequestFileName() + " : Found In Data Disk " + diskId + "!");	
						
							
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
							System.out.println(request.getRequestFileName() + " : Not Found!");	
						}													
					}				
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
			}
			
		}
		
		//Track
		Track.trackOfRequest(requestsTrack);
				
	}

}
