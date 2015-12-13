package org.tju.track;

import org.tju.bean.DiskInfo;
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
	public void TrackOfDataDiskStateFile(String filePath, int time, DiskInfo[] dataDisks){
		
		String content = time + ",";
		
		int i=0;
		for( ; i<dataDisks.length-1; i++){						
			content += dataDisks[i].getDiskState() + ",";
		}
		
		if(i == dataDisks.length-1){
			content += dataDisks[i].getDiskState() + "\n";
		}
		
		AppendDiskStateFile(filePath, content);
		
	}
	
	
	//Track of Cache Disks' state
	public void TrackOfCacheDiskStateFile(String filePath, int time, DiskInfo[] cacheDisks){
		
		String content = time + ",";
		
		int i=0;
		for( ; i<cacheDisks.length-1; i++){						
			content += cacheDisks[i].getDiskState() + ",";
		}
		
		if(i == cacheDisks.length-1){
			content += cacheDisks[i].getDiskState() + "\n";
		}
		
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
	
	
	
	
	//test
	public static void main(String[] args){
		
		TrackOfDiskState track = new TrackOfDiskState();
		
		String filePath = "track/DataDiskState.csv";
		
		DiskInfo disk = new DiskInfo(0, 0, 0);
		DiskInfo[] disks = {disk};

		String[] lables = {"Time", "0"};
		
		
		track.CreateFileOfDiskState(filePath);
		
		track.HeaderOfDiskState(filePath, lables);
		
		track.TrackOfDataDiskStateFile(filePath, 0, disks);
		
		
		filePath = "track/CacheDiskState.csv";
		
		track.CreateFileOfDiskState(filePath);
		
		track.HeaderOfDiskState(filePath, lables);
		
		track.TrackOfCacheDiskStateFile(filePath, 0, disks);
		
		
	}

}
