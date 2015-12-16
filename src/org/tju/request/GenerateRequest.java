package org.tju.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

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
	
	//get requests' window NO.
	public int windowNum = valueOfConfigureFile.getWindowNum();
	
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
	public HashMap<Integer, HashMap<String, RequestInfo>> generateRequest(){
		
		//Request List of sliding window
		HashMap<String, RequestInfo> requestInSlidingWindowList = new HashMap<String, RequestInfo>();
		HashMap<String, RequestInfo> requestPerWindowList = new HashMap<String, RequestInfo>();

		//Request List of all
		HashMap<Integer, HashMap<String, RequestInfo>> requestList = new HashMap<Integer, HashMap<String, RequestInfo>>();

		//generate request
		for(int i=0; i<requestAmount; ){
			if (mode.equalsIgnoreCase("light")) {
				requestInSlidingWindowList = generateRequestInLight(requestGenerateRule);
				
				int j = 0;
				for(HashMap.Entry<String, RequestInfo> entry : requestInSlidingWindowList.entrySet()) {
					requestPerWindowList.put(entry.getKey(), entry.getValue());
					
					if(++j == requestInSlidingWindowList.size()/windowNum){
						requestList.put(i++, requestPerWindowList);
						requestPerWindowList.clear();
					}
				}			
			} else if (mode.equalsIgnoreCase("normal")) {
				requestInSlidingWindowList = generateRequestInNormal(requestGenerateRule);
				
				int j = 0;
				for(HashMap.Entry<String, RequestInfo> entry : requestInSlidingWindowList.entrySet()) {
					requestPerWindowList.put(entry.getKey(), entry.getValue());
					
					if(++j == requestInSlidingWindowList.size()/windowNum){
						requestList.put(i++, requestPerWindowList);
						requestPerWindowList.clear();
					}
				}			
			} else if (mode.equalsIgnoreCase("mix")) {
				requestInSlidingWindowList = generateRequestInMix(requestGenerateRule);
				
				int j = 0;
				for(HashMap.Entry<String, RequestInfo> entry : requestInSlidingWindowList.entrySet()) {
					requestPerWindowList.put(entry.getKey(), entry.getValue());
					
					if(++j == requestInSlidingWindowList.size()/windowNum){
						requestList.put(i++, requestPerWindowList);
						requestPerWindowList.clear();
					}
				}			
			}
		}	
		
		return requestList;
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
			
			requestInfos[requestCorrelation-i] = new RequestInfo(requestFileName, 0, 0, 0);
		}
		
		blockId = skyzone*(timeAmount/fileInBlock) + observeTime/fileInBlock;
		requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(observeTime);

		requestInfos[requestCorrelation] = new RequestInfo(requestFileName, 0, 0, 0);
		
		
		for(int i=1; i<=requestCorrelation; i++){
			
			blockId = skyzone*(timeAmount/fileInBlock) + (observeTime+i)/fileInBlock;

			requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(observeTime+i);
			
			requestInfos[requestCorrelation+i] = new RequestInfo(requestFileName, 0, 0, 0);
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
			
			requestInfos[requestCorrelation-i] = new RequestInfo(requestFileName, 0, 0, 0);
		}
		
		blockId = skyzone*(timeAmount/fileInBlock) + (timeAmount/fileInBlock);

		requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(observeTime);

		requestInfos[requestCorrelation] = new RequestInfo(requestFileName, 0, 0, 0);
		
		
		for(int i=1; i<=requestCorrelation; i++){
			
			blockId = (skyzone+i)*(timeAmount/fileInBlock) + (timeAmount/fileInBlock);

			requestFileName = String.valueOf(diskId)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone+i)+"-"+String.valueOf(observeTime);
			
			requestInfos[requestCorrelation+i] = new RequestInfo(requestFileName, 0, 0, 0);
		}
			
		return requestInfos;
		
	}
	
	
	//Generate request by Skyzone & time  ===>>NULL<<===
	public RequestInfo[] generateRequestByMix(){
		
		RequestInfo[] requestInfos = new RequestInfo[requestCorrelation*2+1];
			
		return requestInfos;
		
	}
	
	
	//Generate request in light mode
	public HashMap<String, RequestInfo> generateRequestInLight(String rule){
		
		HashMap<String, RequestInfo> requestInSlidingWindowList = new HashMap<String, RequestInfo>();
		
		int requestErr = random.nextInt(err);
		int realRequestArrivalRate = lightRequestArrivalRate + requestErr;
		
		for(int i=0; i<windowNum; i++){
			if(rule.equalsIgnoreCase("time")){
				for(int j=0; j<realRequestArrivalRate; j++){
					RequestInfo[] requests = generateRequestByTime();
					for(int k=0; k<requests.length; k++){
						requestInSlidingWindowList.put(requests[k].getRequestFileName(), requests[k]);
					}
				}
			} else if(rule.equalsIgnoreCase("skyzone")){
				for(int j=0; j<realRequestArrivalRate; j++){
					RequestInfo[] requests = generateRequestBySkyzone();
					for(int k=0; k<requests.length; k++){
						requestInSlidingWindowList.put(requests[k].getRequestFileName(), requests[k]);
					}
				}
			} else if(rule.equalsIgnoreCase("mix")){
				for(int j=0; j<realRequestArrivalRate; j++){
					RequestInfo[] requests = generateRequestByMix();
					for(int k=0; k<requests.length; k++){
						requestInSlidingWindowList.put(requests[k].getRequestFileName(), requests[k]);
					}
				}
			}		
		}
		
		return requestInSlidingWindowList;
		
	}
	
	
	//Generate request in normal mode
	public HashMap<String, RequestInfo> generateRequestInNormal(String rule){
		
		HashMap<String, RequestInfo> requestInSlidingWindowList = new HashMap<String, RequestInfo>();
		
		int requestErr = random.nextInt(err);
		int realRequestArrivalRate = normalRequestArrivalRate + requestErr;
		
		for(int i=0; i<windowNum; i++){
			if(rule.equalsIgnoreCase("time")){
				for(int j=0; j<realRequestArrivalRate; j++){
					RequestInfo[] requests = generateRequestByTime();
					for(int k=0; k<requests.length; k++){
						requestInSlidingWindowList.put(requests[k].getRequestFileName(), requests[k]);
					}
				}
			} else if(rule.equalsIgnoreCase("skyzone")){
				for(int j=0; j<realRequestArrivalRate; j++){
					RequestInfo[] requests = generateRequestBySkyzone();
					for(int k=0; k<requests.length; k++){
						requestInSlidingWindowList.put(requests[k].getRequestFileName(), requests[k]);
					}
				}
			} else if(rule.equalsIgnoreCase("mix")){
				for(int j=0; j<realRequestArrivalRate; j++){
					RequestInfo[] requests = generateRequestByMix();
					for(int k=0; k<requests.length; k++){
						requestInSlidingWindowList.put(requests[k].getRequestFileName(), requests[k]);
					}
				}
			}		
		}
		
		
		return requestInSlidingWindowList;
		
	}
	
	
	//Generate request in Mix(ligth & normal) mode
	public HashMap<String, RequestInfo> generateRequestInMix(String rule){
		
		int i = random.nextInt(2);
		
		if(i == 0){
			return generateRequestInLight(requestGenerateRule);
		} else {
			return generateRequestInNormal(requestGenerateRule);
		}	
		
	}
	
	
	//Specifies the requests' generation time
	public void specifiesRequestTime(HashMap<Integer, HashMap<String, RequestInfo>> requestsList){
		
		for(int i=0; i<requestAmount; i++){
			HashMap<String, RequestInfo> requests = requestsList.get(i);
			Iterator<Entry<String, RequestInfo>> iter = requests.entrySet().iterator();
			
			while (iter.hasNext()){
				Entry<String, RequestInfo> entry = iter.next();
				
				RequestInfo request = entry.getValue();
				
				int genTime = i*slidingWindowSize + random.nextInt(slidingWindowSize);

				request.setGenerateTime(genTime);
				
				requests.put(request.getRequestFileName(), request);				
			}
		}
	}
    
	
	
	
	//test
	public static void main(String[] args){
		GenerateRequest gen = new GenerateRequest();	
		
//		HashMap<String, RequestInfo> list = gen.generateRequestInNormal("time");
//
//		for (HashMap.Entry<String, RequestInfo> entry : list.entrySet()) {
//
//		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//
//		}
		
		HashMap<Integer, HashMap<String, RequestInfo>> list = gen.generateRequest();
		
		HashMap<String, RequestInfo> list1 = list.get(0);
		
		for (HashMap.Entry<String, RequestInfo> entry : list1.entrySet()) {

		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

		}
		
		System.out.println(list.size()+"===="+list1.size());
		
		
	}
	
}
