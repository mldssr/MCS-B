package org.tju.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

public class BlockOperation {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//disks' open time
	public static int openTime = valueOfConfigureFile.getOpenTime();
	
	
	//initialize data disks' blocks' list，设置磁盘组中关闭状态磁盘的所有Block的idleTime/transmissionTime/requestNum
	public static void initBlocksList(DiskInfo[] dataDisks){
		
		for(int i=0; i<dataDisks.length; i++){
			initBlocksList(dataDisks[i]);
		}
		
	}
	
	
	//initialize data disk's blocks' list，设置关闭状态磁盘所有Block的idleTime/transmissionTime/requestNum
	public static void initBlocksList(DiskInfo dataDisk){
		
		//tmp blocks' List
		HashMap<Integer, BlockInfo> tmpBlocks = new HashMap<Integer, BlockInfo>();
			
		if(dataDisk.getDiskState()==0){
			HashMap<Integer, BlockInfo> blocks = dataDisk.getBlockList();
			Iterator<Entry<Integer, BlockInfo>> iter = blocks.entrySet().iterator();
			
			while (iter.hasNext()){
				Entry<Integer, BlockInfo> entry = iter.next();
				
				BlockInfo block = entry.getValue();
				block.setIdleTime(0-openTime);	
				block.setTransmissionTime(openTime);	
				block.setRequestNum(1);  //Why not 0?
				tmpBlocks.put(block.getBlockId(), block);
			}
			
			//Add to SSD
			dataDisk.setBlockList(null);
			dataDisk.setBlockList(tmpBlocks);
//			tmpBlocks.clear();
			
			System.out.println("InitBlocksList of DataDisk " + dataDisk.getDiskId());
		}
		
	}

}
