package org.tju.simulation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;

public class TransTimeOperation {
	
	/**
	 * Update disks' blocks' transmissionTime in cache disks
	 * @param disks
	 */
	public static void UpdateTranTime(DiskInfo[] disks){
		
		for(int i=0; i<disks.length; i++){
			UpdateTranTime(disks[i]);
		}
		
	}
	
	
	/**
	 * Update disk's blocks' transmissionTime ������������block��transmissionTime(>0)-1
	 * @param disk
	 */
	public static void UpdateTranTime(DiskInfo disk){
		if (disk.getDiskState() == 0) {
			return;
		}
		
		HashMap<Integer, BlockInfo> blocks = disk.getBlockList();
		
		Iterator<Entry<Integer, BlockInfo>> iter = blocks.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<Integer, BlockInfo> entry = iter.next();
			
			BlockInfo block = entry.getValue();
			
			if(block == null){
				continue ;
			}
			
			if(block.getTransmissionTime()>0){
				block.setTransmissionTime(block.getTransmissionTime()-1);
				disk.getBlockList().put(block.getBlockId(), block);
			}			
		}
		
	}
	
	
	//get Transmission Time of cache disks
	public static int getTransTime(DiskInfo[] disks, int blockId){
		
		int time = 0;
		
		for(int i=0; i<disks.length; ){
			time = getTransTime(disks[i++], blockId);
			
			if(time >= 0){
				return time;
			}		
		}
			
		return time;
	}
	
	//get Transmission Time of cache disk
	public static int getTransTime(DiskInfo disk, int blockId){
		
		BlockInfo block = disk.getBlockList().get(blockId);
		
		if(block == null){
			return -1;
		}
		
		return block.getTransmissionTime();
		
	}
	
	

}
