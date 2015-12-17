package org.tju.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月14日 上午11:17:20
 */
public class DataMigration {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//get disks' capacity from DiskCapacity.xml 
	public static int blockInSSD = valueOfConfigureFile.getBlockInSSD();
	public static int blockInCache = valueOfConfigureFile.getBlockInCache();
	
	
	//Data Disk ====>> SSD Replacement Strategy
	public static void DD2SSD(DiskInfo dataDisk, DiskInfo SSDDisk, int blockId){
		
		BlockInfo block = dataDisk.getBlockList().get(blockId-1);
		SSDDisk.getBlockList().put(blockId-1, block);
		SSDDisk.setBlockAmount(SSDDisk.getBlockAmount()+1);
		SSDDisk.setLeftSpace(SSDDisk.getLeftSpace()-1);
		
		block = dataDisk.getBlockList().get(blockId);
		SSDDisk.getBlockList().put(blockId, block);
		SSDDisk.setBlockAmount(SSDDisk.getBlockAmount()+1);
		SSDDisk.setLeftSpace(SSDDisk.getLeftSpace()-1);

		block = dataDisk.getBlockList().get(blockId+1);
		SSDDisk.getBlockList().put(blockId+1, block);	
		SSDDisk.setBlockAmount(SSDDisk.getBlockAmount()+1);
		SSDDisk.setLeftSpace(SSDDisk.getLeftSpace()-1);
		
	}
	
	
	//Data Disk ====>> Cache Disk Replacement Strategy
	public static void DD2Cache(DiskInfo dataDisk, DiskInfo cacheDisk, int blockId){
		
		BlockInfo block = dataDisk.getBlockList().get(blockId-1);
		cacheDisk.getBlockList().put(blockId-1, block);
		cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);

		block = dataDisk.getBlockList().get(blockId);
		cacheDisk.getBlockList().put(blockId, block);
		cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);

		block = dataDisk.getBlockList().get(blockId+1);
		cacheDisk.getBlockList().put(blockId+1, block);	
		cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);

	}
	

	//Cache Disk ====>> SSD Replacement Strategy
	public static void Cache2SSD(DiskInfo cacheDisk, DiskInfo SSDDisk, double highPriorityTh){
		
		HashMap<Integer, BlockInfo> highPriorityBlock = new HashMap<Integer, BlockInfo>();
		
		List<Integer> highPriorityBlockId = new LinkedList<Integer>();
		
		Iterator<Entry<Integer, BlockInfo>> iter = cacheDisk.getBlockList().entrySet().iterator();
		
		while (iter.hasNext()){
			Entry<Integer, BlockInfo> entry = iter.next();
			BlockInfo block = entry.getValue();

			if(block.getPriority() > highPriorityTh){
				highPriorityBlock.put(block.getBlockId(), block);
				highPriorityBlockId.add(block.getBlockId());
			}		
		}
		
		SSDDisk.getBlockList().putAll(highPriorityBlock);
		SSDDisk.setBlockAmount(SSDDisk.getBlockAmount()+highPriorityBlock.size());

		for(int i=0; i<highPriorityBlockId.size(); i++){
			cacheDisk.getBlockList().remove(highPriorityBlockId.get(i));
		}
		cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()-highPriorityBlockId.size());
		
	}
	
	
	//Cache Disk ====>>Cache Disk Replacement Strategy
	public static void Cache2Cache(DiskInfo[] cacheDisks, int blockInCache){
		
		//All Blocks Info
		HashMap<Integer,BlockInfo> totalBlocks = new HashMap<Integer, BlockInfo>();
		
		//Clear BolckList of each cache disks && close disk
		for (int i=0; i<cacheDisks.length; i++) {
			totalBlocks.putAll(cacheDisks[i].getBlockList());
			cacheDisks[i].getBlockList().clear();
			cacheDisks[i].setDiskState(0);
		}
			
		Iterator<Entry<Integer,BlockInfo>> iterTotal = totalBlocks.entrySet().iterator();
		
		int cacheId = 0;
		
		while (iterTotal.hasNext()){
			Entry<Integer,BlockInfo> entry = iterTotal.next();
			
			if(cacheDisks[cacheId].getBlockAmount() <= blockInCache){
				cacheDisks[cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount()+1);
			}else {
				cacheDisks[++cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount()+1);
			}
		}
	
	}
	
	
	//Refresh Replacement Strategy
	public static void RefreshReplacement(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks){
		//All Blocks Info
		HashMap<Integer,BlockInfo> totalBlocks = new HashMap<Integer, BlockInfo>();
				
		//Clear BlockList of SSD
		for(int i=0; i<SSDDisks.length; i++){
			totalBlocks.putAll(SSDDisks[i].getBlockList());
			SSDDisks[i].getBlockList().clear();
		}
		
		//Clear BolckList of each cache disks && close disk
		for(int i=0; i<cacheDisks.length; i++) {
			totalBlocks.putAll(cacheDisks[i].getBlockList());
			cacheDisks[i].getBlockList().clear();
			cacheDisks[i].setDiskState(0);
		}
		
		//Sorted By Blocks' Priority
		PriorityOperation.sortedByPriority(totalBlocks);
				
		Iterator<Entry<Integer,BlockInfo>> iterTotal = totalBlocks.entrySet().iterator();
		
		int SSDId = 0;
		int cacheId = 0;
		
		while (iterTotal.hasNext()){
			Entry<Integer,BlockInfo> entry = iterTotal.next();
			
			if(SSDDisks[SSDId].getBlockAmount() <=blockInSSD){
				SSDDisks[SSDId].setDiskState(1);
				SSDDisks[SSDId].getBlockList().put(entry.getKey(), entry.getValue());
				SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount()+1);
				SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace()-1);
			} else if(SSDId+1 < SSDDisks.length){
				SSDDisks[++SSDId].setDiskState(1);
				SSDDisks[SSDId].getBlockList().put(entry.getKey(), entry.getValue());
				SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount()+1);
				SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace()-1);
			} else if(cacheDisks[cacheId].getBlockAmount() <= blockInCache){
				cacheDisks[cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount()+1);
				cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace()-1);
			}else if (cacheId+1 < cacheDisks.length){
				cacheDisks[++cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount()+1);
				cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace()-1);
			}
		}
	}
		
	
}
