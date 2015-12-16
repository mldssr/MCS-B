package org.tju.track;

import org.tju.request.RequestInfo;
import org.tju.util.FileOperation;

/**
 * @author yuan
 *
 * @date 2015年12月13日 上午10:26:43
 */
public class TrackOfRequest {
	
	//get file operation
	public FileOperation fileOper= new FileOperation();
	
	
	//Track of requests' generation
	public void TrackOfRequestGen(String filePath, RequestInfo request){
		
		String content = request.getRequestFileName() + "," + 
							request.getGenerateTime() + "\n";
		
		AppendRequestGenFile(filePath, content);		
		
	}
	
	
	//Create File Of track of requests' generation
	public void CreateFileOfRequestGen(String filePath){
		
		fileOper.CreateFile(filePath);
		
	}
	
	
	//Add header of Track of requests' generation file
	public void HeaderOfRequestGen(String filePath, String[] lables){
		
		fileOper.HeaderOfFile(filePath, lables);
		
	}
	
	
	//Append content of Track of requests' generation file
	public void AppendRequestGenFile(String filePath, String content){
		
		fileOper.FileAppend(filePath, content);
		
	}
	
	
	//Track of requests' statistic info
	public void TrackOfRequestSta(String filePath, RequestInfo request){
		
		String content = request.getRequestFileName() + "," + 
							request.getGenerateTime() + "," +
							request.getResponseTime() + "," +
							request.getQos() + "\n";
		
		AppendRequestGenFile(filePath, content);		
		
	}
	
	
	//Create File Of track of requests' statistic info
	public void CreateFileOfRequestSta(String filePath){
		
		fileOper.CreateFile(filePath);
		
	}
	
	
	//Add header of Track of requests' statistic info
	public void HeaderOfRequestSta(String filePath, String[] lables){
		
		fileOper.HeaderOfFile(filePath, lables);
		
	}
	
	
	//Append content of Track of requests' statistic info
	public void AppendRequestStaFile(String filePath, String content){
		
		fileOper.FileAppend(filePath, content);
		
	}
	
	
	
	//test
	public static void main(String[] args){
		
		TrackOfRequest track = new TrackOfRequest();
		
		String filePath = "track/requestGen.csv";
		
		String[] lables = {"RequestFileName", "GenerateTime"};
		
		RequestInfo request = new RequestInfo("test", 0, 0, 2);
		
		track.CreateFileOfRequestGen(filePath);
		
		track.HeaderOfRequestGen(filePath, lables);	
		
		track.TrackOfRequestGen(filePath, request);
		
		
		
		//test statistic
		TrackOfRequest track1 = new TrackOfRequest();
			
		filePath = "track/requestSta.csv";		

		String[] lables1 = {"RequestFileName", "GenerateTime", "ResponseTime", "Qos"};
		
		track1.CreateFileOfRequestSta(filePath);
		
		track1.HeaderOfRequestSta(filePath, lables1);	
		
		track1.TrackOfRequestSta(filePath, request);	
		
	}
	
	

}
