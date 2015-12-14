package org.tju.scheduler;

import org.tju.bean.DiskInfo;

/**
 * @author yuan
 *
 * @date 2015年12月14日 下午3:06:28
 */
public class DiskOperation {
	
	//Close data disks
	public static void closeDisks(DiskInfo[] disks){
		
		for(int i=0; i<disks.length; i++){
			closeDisk(disks[i]);
		}
		
	}
	
	
	//Close disk
	public static void closeDisk(DiskInfo disk){
		
		disk.setDiskState(0);
		
	}
	
	
	//Open disk
	public static void openDisk(DiskInfo disk){
		
		disk.setDiskState(1);
		
	}
	
}
