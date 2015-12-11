package org.tju.request;

import java.util.HashMap;
import java.util.Random;

import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月11日 上午10:06:09
 */
public class GenerateRequest {
	
	//Random
	Random random = new Random();
	
	//Value of configure files
	public ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
			
	//get sliding window size
	public int slidingWindowSize = valueOfConfigureFile.getSlidingWindowSize();
	
	//get mode: single(light, normal) || mixing
	public String mode = valueOfConfigureFile.getMode();
	
	//get requests' amount
	public int requestAmount = valueOfConfigureFile.getRequestAmount();

	//get requests' arrival rate & err
	public int lightRequestArrivalRate = valueOfConfigureFile.getLightRequestArrivalRate();
	public int normalRequestArrivalRate = valueOfConfigureFile.getNormalRequestArrivalRate();
	
	public int err = valueOfConfigureFile.getErr();
	
	//get requests' generate rule(time, space, mix & random)
	public String requestGenerateRule = valueOfConfigureFile.getRequestGenerateRule();
	
	//get requests' correlation size
	public int requestCorrelation = valueOfConfigureFile.getRequestCorrelation();
	
	//Request group
	public RequestInfo[] requestInfo = new RequestInfo[requestCorrelation*2+1];
	
	//get data disks' amount
	public int dataDiskAmount = valueOfConfigureFile.getDataDiskAmount();
	
	//get disks' capacity
	public int blockInDisk = valueOfConfigureFile.getBlockInDisk();
	public int skyzoneInDisk = valueOfConfigureFile.getSkyzoneInDisk();
	
	//get the disks' starting ID
	public int dataDiskStartId = valueOfConfigureFile.getDataDiskStartId();
	
	//get observe info (skyzone and time)
	public int timeAmount = valueOfConfigureFile.getTimeAmount();
	public int skyzoneAmount = valueOfConfigureFile.getSkyzoneAmount();
	
	//get file in block
	public int fileInBlock = valueOfConfigureFile.getFileInBlock();
	
	//get block amount
	public int blockAmount = valueOfConfigureFile.getBlockAmount();
	
	
	//Generate request function
	public void generateRequest(){
		
		//Request List of one sliding window
		HashMap<String, RequestInfo> requestInSlidingWindowList = new HashMap<String, RequestInfo>();
		
		//Request List of all
		HashMap<String, RequestInfo> requestList = new HashMap<String, RequestInfo>();

		
				
				
				
	}
	
	
	//Generate request by time
	public RequestInfo[] generateRequestByTime(){
		
		RequestInfo[] requestInfos = new RequestInfo[requestCorrelation*2+1];
		
		int diskId = dataDiskStartId + random.nextInt(dataDiskAmount);
		int skyzone = skyzoneInDisk*(diskId-dataDiskStartId)-(diskId-dataDiskStartId) + random.nextInt(skyzoneInDisk);
		
		//avoid illegal request
		while(true){
			if(skyzone>skyzoneAmount){
				skyzone = skyzoneInDisk*(diskId-dataDiskStartId)-(diskId-dataDiskStartId) + random.nextInt(skyzoneInDisk);
			} else {
				break;
			}
		}
		
		int observeTime = requestCorrelation + random.nextInt(timeAmount - 2*requestCorrelation);
		
		int blockId;
			
		
		//Request info
		String requestFileName;
		
		for(int i=requestCorrelation; i>0; i--){
			
			blockId = skyzone*(timeAmount/fileInBlock) + (observeTime-i)/fileInBlock;
			
			requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(observeTime-i);
			
			requestInfos[requestCorrelation-i] = new RequestInfo(requestFileName, null, null, 0);
		}
		
		blockId = skyzone*(timeAmount/fileInBlock) + observeTime/fileInBlock;
		requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(observeTime);

		requestInfos[requestCorrelation] = new RequestInfo(requestFileName, null, null, 0);
		
		
		for(int i=1; i<=requestCorrelation; i++){
			
			blockId = skyzone*(timeAmount/fileInBlock) + (observeTime+i)/fileInBlock;

			requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(observeTime+i);
			
			requestInfos[requestCorrelation+i] = new RequestInfo(requestFileName, null, null, 0);
		}
			
		return requestInfos;
		
	}
	
	
	//Generate request by Skyzone
	public RequestInfo[] generateRequestBySkyzone(){
		
		RequestInfo[] requestInfos = new RequestInfo[requestCorrelation*2+1];
		
		int diskId = dataDiskStartId + random.nextInt(dataDiskAmount);
		int blockId;
		int skyzone = skyzoneInDisk*(diskId-dataDiskStartId)-(diskId-dataDiskStartId) + random.nextInt(skyzoneInDisk - requestCorrelation);
		
		//avoid illegal request
		while(true){
			if(skyzone>skyzoneAmount-requestCorrelation){
				skyzone = skyzoneInDisk*(diskId-dataDiskStartId)-(diskId-dataDiskStartId) + random.nextInt(skyzoneInDisk - requestCorrelation);
			} else {
				break;
			}
		}
		
			
		int observeTime = random.nextInt(timeAmount);
		
		//Request info
		String requestFileName;
		
		for(int i=requestCorrelation; i>0; i--){
			
			blockId = (skyzone-i)*(timeAmount/fileInBlock) + (timeAmount/fileInBlock);
			
			requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone-i)+"-"+String.valueOf(observeTime);
			
			requestInfos[requestCorrelation-i] = new RequestInfo(requestFileName, null, null, 0);
		}
		
		blockId = skyzone*(timeAmount/fileInBlock) + (timeAmount/fileInBlock);

		requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(observeTime);

		requestInfos[requestCorrelation] = new RequestInfo(requestFileName, null, null, 0);
		
		
		for(int i=1; i<=requestCorrelation; i++){
			
			blockId = (skyzone+i)*(timeAmount/fileInBlock) + (timeAmount/fileInBlock);

			requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone+i)+"-"+String.valueOf(observeTime);
			
			requestInfos[requestCorrelation+i] = new RequestInfo(requestFileName, null, null, 0);
		}
			
		return requestInfos;
		
	}
	
	
	
	//test
	public static void main(String[] args){
		GenerateRequest gen = new GenerateRequest();
		RequestInfo[] requests = gen.generateRequestByTime();
		
		for(int i=0; i<requests.length; i++){
			System.out.println(requests[i].getRequestFileName());
		}
		
		
		RequestInfo[] requests1 = gen.generateRequestBySkyzone();
		
		for(int i=0; i<requests1.length; i++){
			System.out.println(requests1[i].getRequestFileName());
		}
		
	}
	
	
	

	
	

}
