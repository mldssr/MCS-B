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
 * @date 2015年12月14日 上午10:57:13
 */
public class Scheduler {
	
	//get parameters
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	public static double lowPriorityTh = valueOfConfigureFile.getLowPriorityTh();
	public static int blockInCache = valueOfConfigureFile.getBlockInCache();
	
	
	//Scheduler of All Disks, when is time to refresh
	public static void SchedulerOfDisks(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks, DiskInfo[] dataDisks){
		
		//Calculate blocks' priority 
		//Calculate blocks' priority in SSD
		for(int i=0; i<SSDDisks.length; i++){
			PriorityOperation.calculatePriority(SSDDisks[i]);
		}
			
		//Calculate blocks' priority in cache disks
		for(int i=0; i<cacheDisks.length; i++){
			PriorityOperation.calculatePriority(cacheDisks[i]);
		}
		
		
		//Clear Cache Disks
		CacheCleaner.clearCache(SSDDisks, lowPriorityTh);
		CacheCleaner.clearCache(cacheDisks, lowPriorityTh);		
		
		//Data Exchange
		DataExchange.Cache2CacheReplacement(cacheDisks, blockInCache);
			
	}
	
	
	//Scheduler of All Disks per second
	public static void SchedulerOfSecond(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks){
	
		//update blocks' info in cache disks
		//update blocks' info in SSD
		SchedulerOfSecond(SSDDisks);
		
		//update blocks' info in Cache Disks
		SchedulerOfSecond(cacheDisks);
		
	}
	
	
	//Scheduler of Disks per second
	public static void SchedulerOfSecond(DiskInfo[] Disks){
		
		//tmp blocks' List
		HashMap<Integer, BlockInfo> tmpBlocks = new HashMap<Integer, BlockInfo>();
		
		
		//update blocks' info in cache disks
		for(int i=0; i<Disks.length; i++){
			HashMap<Integer, BlockInfo> blocks = Disks[i].getBlockList();
			Iterator<Entry<Integer, BlockInfo>> iter = blocks.entrySet().iterator();
			
			while (iter.hasNext()){
				Entry<Integer, BlockInfo> entry = iter.next();
				
				BlockInfo block = entry.getValue();
				block.setIdleTime(block.getObserveTime()+1);
				block.setRequestNum(block.getRequestNum()+1);
				
				tmpBlocks.put(block.getBlockId(), block);
			}
			
			//Add to SSD
			Disks[i].setBlockList(null);
			Disks[i].setBlockList(tmpBlocks);
			tmpBlocks.clear();
		}
		
			
		//Calculate blocks' priority 
		for(int i=0; i<Disks.length; i++){
			PriorityOperation.calculatePriority(Disks[i]);
		}
			
	}

}
