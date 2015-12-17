package org.tju.track;

import org.tju.util.FileOperation;

/**
 * @author yuan
 *
 * @date 2015年12月17日 下午2:49:37
 */
public class TrackOfArrivalRate {
	
	//get file operation
	public FileOperation fileOper= new FileOperation();
	
	
	//Track of Arrival Rate
	public void TrackOfRate(String filePath, ArrivalRate arrivalRate){
		
		String content = arrivalRate.getWindowNum() + "," + arrivalRate.getArrivalRate() + "\n";
		
		AppendArrivalRateFile(filePath, content);		
		
	}
	
	
	//Create File Of track of Arrival Rate
	public void CreateFileOfArrivalRate(String filePath){
		
		fileOper.CreateFile(filePath);
		
	}
	
	
	//Add header of Track of requests' Arrival Rate
	public void HeaderOfArrivalRate(String filePath, String[] lables){
		
		fileOper.HeaderOfFile(filePath, lables);
		
	}
	
	
	//Append content of Track of requests' generation file
	public void AppendArrivalRateFile(String filePath, String content){
		
		fileOper.FileAppend(filePath, content);
		
	}

}
