package org.tju.scheduler;

import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月14日 下午3:06:28
 */
public class DiskOperation {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//disks' open time
	public static int openTime = valueOfConfigureFile.getOpenTime();
	
	//data disks' idle time threshold
	public static int idleTimeTh = valueOfConfigureFile.getIdleTimeTh();
	
	
	//Close data disks
	public static void closeDisks(DiskInfo[] disks){
		
		for(int i=0; i<disks.length; i++){
			closeDisk(disks[i]);
		}
		
	}
	
	
	//Close disk
	public static void closeDisk(DiskInfo disk){
		
		disk.setDiskState(0);
		disk.setIdleTime(0-openTime);
		
	}
	
	
	//Open data disks
	public static void openDisks(DiskInfo[] disks){
		for(int i=0; i<disks.length; i++){
			openDisk(disks[i]);
		}
	}
	
	
	//Open disk
	public static void openDisk(DiskInfo disk){
		
		disk.setDiskState(1);
		disk.setIdleTime(0-openTime);
		
	}
	
	
	//Open cache disk
	public static void openCache(DiskInfo disk){
		
		disk.setDiskState(1);
		disk.setIdleTime(0);
		disk.setBlockAmount(0);
		disk.setBlockList(null);
		disk.setLeftSpace(disk.getTotalSpace());
		
	}
	
	
	//update Data Disks' info: idleTime
	public static void updateDDs(DiskInfo[] dataDisks){
		
		for(int i=0; i<dataDisks.length; i++){
			updateDD(dataDisks[i]);
		}
		
	}
	
	
	//update Data Disk's info: idleTime
	public static void updateDD(DiskInfo dataDisk){
		
		if(dataDisk.getDiskState()==1){
			dataDisk.setIdleTime(dataDisk.getIdleTime()+1);
		}
		
	}
	
	
	//Check disks' idle time: if idleTime > TH then close disk endif
	public static void checkDDs(DiskInfo[] dataDisks){
		for(int i=0; i<dataDisks.length; i++){
			checkDD(dataDisks[i]);
		}
	}
	
	
	//Check disk's idle time: if idleTime > TH then close disk endif
	public static void checkDD(DiskInfo dataDisk){
		
		if(dataDisk.getIdleTime() > idleTimeTh){
			closeDisk(dataDisk);			
		}
		
	}
	
}
