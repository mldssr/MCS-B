package org.tju.simulation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;

/**
 * @author yuan
 *
 * @date 2015年12月16日 下午1:21:11
 */
public class UpdateTransTime {
	
	
	//update disks' blocks' transmissionTime in cache disks
	public static void UpdateTranTime(DiskInfo[] disks){
		
		for(int i=0; i<disks.length; i++){
			UpdateTranTime(disks[i]);
		}
		
	}
	
	
	//update disk's blocks' transmissionTime
	public static void UpdateTranTime(DiskInfo disk){
		
		HashMap<Integer, BlockInfo> blocks = disk.getBlockList();
		
		Iterator<Entry<Integer, BlockInfo>> iter = blocks.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<Integer, BlockInfo> entry = iter.next();
			
			BlockInfo block = entry.getValue();
			
			if(block.getTransmissionTime()>0){
				block.setTransmissionTime(block.getTransmissionTime()-1);
				disk.getBlockList().put(block.getBlockId(), block);
			}			
		}
		
	}

}
