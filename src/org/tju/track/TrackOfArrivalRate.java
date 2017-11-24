package org.tju.track;

import org.tju.track.bean.ArrivalRate;
import org.tju.util.FileOperation;

public class TrackOfArrivalRate {
	
	//get file operation
	public FileOperation fileOper= new FileOperation();
	
	
	//Track of Arrival Rate: Append
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
	
	
	public void CloseArrivalRateFile() {

		fileOper.CloseFile();

	}

}
