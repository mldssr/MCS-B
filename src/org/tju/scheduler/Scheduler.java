package org.tju.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

public class Scheduler {
	
	//get parameters
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	public static double lowPriorityTh = valueOfConfigureFile.getLowPriorityTh();
	public static int blockInCache = valueOfConfigureFile.getBlockInCache();
	
	
	/**
	 * Scheduler of All Disks, when is time to refresh
	 * 计算一二级缓存的优先级，并清除低优先级的块，Refresh时使用
	 * @param SSDDisks
	 * @param cacheDisks
	 */
	public static void SchedulerOfDisks(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks){
		
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
	}
	
	
	/**
	 * Scheduler of All Disks per second<br>
	 * 如果磁盘开着，将其中每个非空block的idleTime加1;<br>
	 * 计算优先级
	 * @param SSDDisks
	 * @param cacheDisks
	 * @param dataDisks
	 */
	public static void SchedulerOfSecond(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks, DiskInfo[] dataDisks){
	
		//update blocks' info in cache disks
		//update blocks' info in SSD
		SchedulerOfSecond(SSDDisks);
		
		//update blocks' info in Cache Disks
		SchedulerOfSecond(cacheDisks);
		
		//update blocks' info in data Disks
		SchedulerOfSecond(dataDisks);
		
	}
	
	
	/**
	 * Scheduler of Disks per second<br>
	 * 如果磁盘开着，将其中每个非空block的idleTime加1;<br>
	 * 计算优先级
	 * @param Disks
	 */
	public static void SchedulerOfSecond(DiskInfo[] Disks){
		
		//update blocks' info in cache disks and data disks
		for(int i=0; i<Disks.length; i++){
			//tmp blocks' List
			HashMap<Integer, BlockInfo> tmpBlocks = new HashMap<Integer, BlockInfo>();
			//如果磁盘开着，将其中每个非空block的idleTime加1
			if(Disks[i].getDiskState()==1){
				HashMap<Integer, BlockInfo> blocks = Disks[i].getBlockList();
				Iterator<Entry<Integer, BlockInfo>> iter = blocks.entrySet().iterator();
				
				while (iter.hasNext()){
					Entry<Integer, BlockInfo> entry = iter.next();
					
					BlockInfo block = entry.getValue();
					
					if(block == null){
						continue ;
					} else {
						block.setIdleTime(block.getIdleTime()+1);
						tmpBlocks.put(block.getBlockId(), block);
					}			
				}
				
				//Add to SSD
				Disks[i].setBlockList(new HashMap<Integer, BlockInfo>());
				Disks[i].setBlockList(tmpBlocks);
//				tmpBlocks.clear();
				
				// add
				Disks[i].setStartedTime(Disks[i].getStartedTime() + 1);
			}	
		}
		
			
		//Calculate blocks' priority
		for(int i=0; i<Disks.length; i++){
			PriorityOperation.calculatePriority(Disks[i]);
		}
			
	}

}
