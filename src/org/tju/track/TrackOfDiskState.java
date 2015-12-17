package org.tju.track;

import org.tju.track.bean.DiskState;
import org.tju.util.FileOperation;

/**
 * @author yuan
 *
 * @date 2015年12月13日 上午11:47:15
 */
public class TrackOfDiskState {
	
	//get file operation
	public FileOperation fileOper= new FileOperation();
	
	
	//Track of Data Disks' state
	public void TrackOfDataDiskStateFile(String filePath, DiskState diskState){
		
		String content = diskState.getTime() + "," + diskState.getContent() + "\n";
		
		AppendDiskStateFile(filePath, content);
		
	}
	
	
	//Create File Of track of Disks' state
	public void CreateFileOfDiskState(String filePath){
		
		fileOper.CreateFile(filePath);
		
	}
	
	
	//Add header of Track of Disks' state file
	public void HeaderOfDiskState(String filePath, String[] lables){
		
		fileOper.HeaderOfFile(filePath, lables);
		
	}
	
	
	//Append content of Track of Disks' state file
	public void AppendDiskStateFile(String filePath, String content){
		
		fileOper.FileAppend(filePath, content);
		
	}

}
