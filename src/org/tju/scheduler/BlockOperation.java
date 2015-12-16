package org.tju.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月16日 下午3:55:15
 */
public class BlockOperation {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//disks' open time
	public static int openTime = valueOfConfigureFile.getOpenTime();
	
	
	//initialize data disks' blocks' list
	public static void initBlocksList(DiskInfo[] dataDisks){
		
		for(int i=0; i<dataDisks.length; i++){
			initBlocksList(dataDisks[i]);
		}
		
	}
	
	
	//initialize data disk's blocks' list
	public static void initBlocksList(DiskInfo dataDisk){
		
		//tmp blocks' List
		HashMap<Integer, BlockInfo> tmpBlocks = new HashMap<Integer, BlockInfo>();
			
		if(dataDisk.getDiskState()==1){
			HashMap<Integer, BlockInfo> blocks = dataDisk.getBlockList();
			Iterator<Entry<Integer, BlockInfo>> iter = blocks.entrySet().iterator();
			
			while (iter.hasNext()){
				Entry<Integer, BlockInfo> entry = iter.next();
				
				BlockInfo block = entry.getValue();
				block.setIdleTime(0-openTime);	
				block.setTransmissionTime(openTime);	
				tmpBlocks.put(block.getBlockId(), block);
			}
			
			//Add to SSD
			dataDisk.setBlockList(null);
			dataDisk.setBlockList(tmpBlocks);
			tmpBlocks.clear();
		}
		
	}

}
