package org.tju.simulation;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.scheduler.BlockOperation;
import org.tju.scheduler.DiskOperation;

/**
 * @author yuan
 *
 * @date 2015年12月16日 上午10:33:45
 */
public class SearchInDisks {
	
	//Search In SSD Disks
	public static boolean searchInSSD(DiskInfo[] SSDDisks, String fileName, int blockId){
		
		for(int i=0; i<SSDDisks.length; i++){
			BlockInfo block = SSDDisks[i].getBlockList().get(blockId);
			if(block!=null){
				block.setIdleTime(-1);
				block.setRequestNum(block.getRequestNum()+1);
				SSDDisks[i].getBlockList().put(blockId, block);
				return true;
			}
		}
			
		return false;
	}
	
	
	//Search In Cache Disks
	public static boolean searchInCache(DiskInfo[] cacheDisks, String fileName, int blockId){
		
		for(int i=0; i<cacheDisks.length; i++){
			BlockInfo block = cacheDisks[i].getBlockList().get(blockId);
			if(block!=null){
				block.setIdleTime(-1);
				block.setRequestNum(block.getRequestNum()+1);
				cacheDisks[i].getBlockList().put(blockId, block);
				return true;
			}
		}
			
		return false;
	}
	
	
	//Search In data Disks
	public static boolean searchInDD(DiskInfo[] dataDisks, String fileName, int diskId, int blockId){
		
		BlockInfo block = dataDisks[diskId].getBlockList().get(blockId);
		
		if(block!=null){
			if(dataDisks[diskId].getDiskState()!=1){
				DiskOperation.openDisk(dataDisks[diskId]);
				BlockOperation.initBlocksList(dataDisks[diskId]);
			}
			return true;
		}
		
		return false;		
	}

}
