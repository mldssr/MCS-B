package org.tju.track;

import org.tju.bean.DiskInfo;
import org.tju.util.FileOperation;

public class TrackOfResponse {
	
	//get file operation
	public FileOperation fileOper= new FileOperation();
	
	
	//Track of requests' generation
	public void TrackOfResponseLoc(String filePath, DiskInfo[] SSDDisks, DiskInfo[] cacheDisks, DiskInfo[] dataDisks){
		
		String content = "";
		
		for(int i=0; i<SSDDisks.length; i++){
			content += SSDDisks[i].getRequestNum() + ",";
		}
		
		for(int i=0; i<cacheDisks.length; i++){
			content += cacheDisks[i].getRequestNum() + ",";
		}
		
		for(int i=0; i<dataDisks.length; i++){
			content += dataDisks[i].getRequestNum() + ",";
		}
		
		content += "\n";
		
		AppendResponseFile(filePath, content);		
		
	}
	
	
	//Create File Of track of requests' generation
	public void CreateFileOfResponse(String filePath){
		
		fileOper.CreateFile(filePath);
		
	}
	
	
	//Add header of Track of requests' generation file
	public void HeaderOfResponse(String filePath, String[] lables){
		
		fileOper.HeaderOfFile(filePath, lables);
		
	}
	
	
	//Append content of Track of requests' generation file
	public void AppendResponseFile(String filePath, String content){
		
		fileOper.FileAppend(filePath, content);
		
	}
	
	
	public void CloseResponseFile() {

		fileOper.CloseFile();

	}

}
