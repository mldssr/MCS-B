package org.tju.scheduler;

import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

public class DiskOperation {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//disks' open time
	public static int openTime = valueOfConfigureFile.getOpenTime();
	
	//data disks' idle time threshold
	public static int idleTimeTh = valueOfConfigureFile.getIdleTimeTh();
	
	
	//Close data disks, not used
	public static void closeDisks(DiskInfo[] disks){
		
		for(int i=0; i<disks.length; i++){
			closeDisk(disks[i]);
		}
		
	}
	
	
	//Close disk
	public static void closeDisk(DiskInfo disk){
		System.out.println("[DISK]  Power off DataDisk " + disk.getDiskId() + ".");
		System.out.println(disk);
//		IdleTimeManager.updateIdleTime(disk.getIdleTime());
		disk.setDiskState(0);
		disk.setIdleTime(0-openTime);
	}
	
	
	//Open data disks
	public static void openDisks(DiskInfo[] disks){
		for(int i=0; i<disks.length; i++){
			openDisk(disks[i]);
		}
	}
	
	
	/**
	 * Open given DataDisk
	 * @param disk
	 */
	public static void openDisk(DiskInfo disk){
		//
		if(disk.getDiskState()==0){
			disk.setDiskState(1);
			disk.setIdleTime(0-openTime);
			
			// add
			disk.setStartedCount(disk.getStartedCount() + 1);
			disk.setStartedTime(disk.getStartedTime() - 10);
			System.out.println("[DISK]  Power on DataDisk " + disk.getDiskId() + ".");
		}
		
	//	disk.setDiskState(1);
	//	disk.setIdleTime(0-openTime);
		
	}
	
	
	/**
	 * Open given CacheDisk (SSD + Cache)
	 * @param disk
	 */
	public static void openCache(DiskInfo disk){
		
		disk.setDiskState(1);
		disk.setIdleTime(0);
		disk.setBlockAmount(0);
		disk.setBlockList(null);
		disk.setLeftSpace(disk.getTotalSpace());
		
		System.out.println("[DISK]  Power on SSD or Cache Disk " + disk.getDiskId() + ".");
		
		// add
		disk.setStartedCount(disk.getStartedCount() + 1);
		disk.setStartedTime(disk.getStartedTime() - 10);
		
	}
	
	/**
	 * Close given CacheDisk (SSD + Cache)
	 * @param disk
	 */
	public static void closeCache(DiskInfo disk){
		
		disk.setDiskState(0);
		disk.setIdleTime(0);
		disk.setBlockAmount(0);
		disk.setLeftSpace(disk.getTotalSpace());
		
		System.out.println("[DISK]  Power off SSD or Cache Disk " + disk.getDiskId() + ".");
	}
	
	
	/**
	 * Update Data Disks' info: idleTime += 1
	 * @param dataDisks
	 */
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
	
	
	/**
	 * Check disks' idle time: if idleTime > TH then close disk
	 * @param dataDisks
	 */
	public static void checkDDs(DiskInfo[] dataDisks){
		for(int i=0; i<dataDisks.length; i++){
			checkDD(dataDisks[i]);
		}
	}
	
	
	//Check disk's idle time: if idleTime > TH then close disk endif
	public static void checkDD(DiskInfo dataDisk){
		
		if(dataDisk.getIdleTime() > idleTimeTh){
//		if(dataDisk.getIdleTime() > IdleTimeManager.getIdleTimeTh()){//added
			closeDisk(dataDisk);			
		}
		
	}
	
}
