package org.tju.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.request.RequestCorrelation;
import org.tju.util.ValueOfConfigureFile;

public class DataMigration {

	// Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();

	// get disks' capacity from DiskCapacity.xml
	public static int blockInSSD = valueOfConfigureFile.getBlockInSSD();
	public static int blockInCache = valueOfConfigureFile.getBlockInCache();

	// get Cache Correlation
	public static int cacheCorrelation = valueOfConfigureFile.getCacheCorrelation();

	// Data Disk ====>> SSD Replacement
	// Strategy,把目标块及其前后各cacheCorrelation块迁移进SSD
	public static void DD2SSD(DiskInfo dataDisk, DiskInfo SSDDisk, int blockId) {

		BlockInfo block = dataDisk.getBlockList().get(blockId);
		SSDDisk.getBlockList().put(blockId, block);
		SSDDisk.setBlockAmount(SSDDisk.getBlockAmount() + 1);
		SSDDisk.setLeftSpace(SSDDisk.getLeftSpace() - 1);
		System.out.println("Data Disk ====>> SSD (normal), blockId: " + blockId);

		// add
		// 把相关块迁移进SSD
		if (RequestCorrelation.hasKey(blockId)) {
			Integer[] corrs = RequestCorrelation.getCorr(blockId);
			for (int i = 0; i < RequestCorrelation.getCorrelationNum(); i++) {
				if (corrs[i] != null) {
					block = dataDisk.getBlockList().get(corrs[i]);
					if (block != null) {
						SSDDisk.getBlockList().put(corrs[i], block);// ???????
						SSDDisk.setBlockAmount(SSDDisk.getBlockAmount() + 1);
						SSDDisk.setLeftSpace(SSDDisk.getLeftSpace() - 1);
						System.out.println("Data Disk ====>> SSD (related), blockId: " + corrs[i]);
					}
				}
			}
		}

		// for(int i=cacheCorrelation; i>0; i--){
		// block = dataDisk.getBlockList().get(blockId-i);
		// if(block!=null){
		// SSDDisk.getBlockList().put(blockId-i, block);//???????
		// SSDDisk.setBlockAmount(SSDDisk.getBlockAmount()+1);
		// SSDDisk.setLeftSpace(SSDDisk.getLeftSpace()-1);
		// }
		// }
		//
		// for(int i=cacheCorrelation; i>0; i--){
		// block = dataDisk.getBlockList().get(blockId+i);
		// if(block!=null){
		// SSDDisk.getBlockList().put(blockId+i, block);
		// SSDDisk.setBlockAmount(SSDDisk.getBlockAmount()+1);
		// SSDDisk.setLeftSpace(SSDDisk.getLeftSpace()-1);
		// }
		// }

	}

	// Data Disk ====>> Cache Disk Replacement Strategy,
	// 把目标块及其前后各cacheCorrelation块迁移进CacheDisk
	public static void DD2Cache(DiskInfo dataDisk, DiskInfo cacheDisk, int blockId) {

		BlockInfo block = dataDisk.getBlockList().get(blockId);
		cacheDisk.getBlockList().put(blockId, block);
		cacheDisk.setBlockAmount(cacheDisk.getBlockAmount() + 1);
		cacheDisk.setLeftSpace(cacheDisk.getLeftSpace() - 1);
		System.out.println("Data Disk ====>> Cache Disk (normal), blockId: " + blockId);

		// add
		// 把相关块迁移进SSD
		if (RequestCorrelation.hasKey(blockId)) {
			Integer[] corrs = RequestCorrelation.getCorr(blockId);
			for (int i = 0; i < RequestCorrelation.getCorrelationNum(); i++) {
				if (corrs[i] != null) {
					block = dataDisk.getBlockList().get(corrs[i]);
					if (block != null) {
						cacheDisk.getBlockList().put(corrs[i], block);// ???????
						cacheDisk.setBlockAmount(cacheDisk.getBlockAmount() + 1);
						cacheDisk.setLeftSpace(cacheDisk.getLeftSpace() - 1);
						System.out.println("Data Disk ====>> Cache Disk (related), blockId: " + corrs[i]);
					}
				}
			}
		}

		// for(int i=cacheCorrelation; i>0; i--){
		// block = dataDisk.getBlockList().get(blockId-i);
		// if(block!=null){
		// cacheDisk.getBlockList().put(blockId-i, block);
		// cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		// cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);
		// }
		// }
		// 把目标块及其前后各1块迁移进CacheDisk
		/*
		 * BlockInfo block = dataDisk.getBlockList().get(blockId-1);
		 * cacheDisk.getBlockList().put(blockId-1, block);
		 * cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		 * cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);
		 */

		// for(int i=cacheCorrelation; i>0; i--){
		// block = dataDisk.getBlockList().get(blockId+i);
		// if(block!=null){
		// cacheDisk.getBlockList().put(blockId+i, block);
		// cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		// cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);
		// }
		// }
		// , 把目标块及其前后各1块迁移进CacheDisk
		/*
		 * block = dataDisk.getBlockList().get(blockId+1);
		 * cacheDisk.getBlockList().put(blockId+1, block);
		 * cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		 * cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);
		 */

	}

	// Cache Disk ====>> SSD Replacement
	// Strategy，将Cache中优先级高于highPriorityTh的块移动到SSD，并删除原有备份，未被使用
	public static void Cache2SSD(DiskInfo cacheDisk, DiskInfo SSDDisk, double highPriorityTh) {

		HashMap<Integer, BlockInfo> highPriorityBlock = new HashMap<Integer, BlockInfo>();

		List<Integer> highPriorityBlockId = new LinkedList<Integer>();

		Iterator<Entry<Integer, BlockInfo>> iter = cacheDisk.getBlockList().entrySet().iterator();

		while (iter.hasNext()) {
			Entry<Integer, BlockInfo> entry = iter.next();
			BlockInfo block = entry.getValue();

			if (block.getPriority() > highPriorityTh) {
				highPriorityBlock.put(block.getBlockId(), block);
				highPriorityBlockId.add(block.getBlockId());
			}
		}

		SSDDisk.getBlockList().putAll(highPriorityBlock);
		SSDDisk.setBlockAmount(SSDDisk.getBlockAmount() + highPriorityBlock.size());

		for (int i = 0; i < highPriorityBlockId.size(); i++) {
			cacheDisk.getBlockList().remove(highPriorityBlockId.get(i));
		}
		cacheDisk.setBlockAmount(cacheDisk.getBlockAmount() - highPriorityBlockId.size());

	}

	// Cache Disk ====>>Cache Disk Replacement Strategy，将数据从开头排满，未被使用
	public static void Cache2Cache(DiskInfo[] cacheDisks, int blockInCache) {

		// All Blocks Info
		HashMap<Integer, BlockInfo> totalBlocks = new HashMap<Integer, BlockInfo>();

		// Clear BolckList of each cache disks && close disk
		for (int i = 0; i < cacheDisks.length; i++) {
			totalBlocks.putAll(cacheDisks[i].getBlockList());
			cacheDisks[i].setBlockList(new HashMap<Integer, BlockInfo>());
			cacheDisks[i].setDiskState(0);
		}

		Iterator<Entry<Integer, BlockInfo>> iterTotal = totalBlocks.entrySet().iterator();

		int cacheId = 0;

		while (iterTotal.hasNext()) {
			Entry<Integer, BlockInfo> entry = iterTotal.next();
			// 如果当前cache磁盘没满
			if (cacheDisks[cacheId].getBlockAmount() <= blockInCache) {
				cacheDisks[cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount() + 1);
			} else {// 如果满了，换下一块
				cacheDisks[++cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount() + 1);
			}
		}

	}

	// Refresh Replacement Strategy
	public static void RefreshReplacement(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks) {
		// All Blocks Info
		HashMap<Integer, BlockInfo> totalBlocks = new HashMap<Integer, BlockInfo>();

		// Clear BlockList of SSD
		for (int i = 0; i < SSDDisks.length; i++) {
			totalBlocks.putAll(SSDDisks[i].getBlockList());
			SSDDisks[i].setBlockList(new HashMap<Integer, BlockInfo>());
			DiskOperation.closeCache(SSDDisks[i]);
		}

		// Clear BolckList of each cache disks && close disk
		for (int i = 0; i < cacheDisks.length; i++) {
			totalBlocks.putAll(cacheDisks[i].getBlockList());
			cacheDisks[i].setBlockList(new HashMap<Integer, BlockInfo>());
			DiskOperation.closeCache(cacheDisks[i]);
		}
		// 去除空块
		if (totalBlocks.containsValue(null)) {
			HashMap<Integer, BlockInfo> nullBlocks = new HashMap<Integer, BlockInfo>();
			// clear null
			Iterator<Entry<Integer, BlockInfo>> iter = totalBlocks.entrySet().iterator();

			while (iter.hasNext()) {
				Entry<Integer, BlockInfo> entry = iter.next();
				if (entry.getValue() == null) {
					nullBlocks.put(entry.getKey(), entry.getValue());
				}
			}

			iter = nullBlocks.entrySet().iterator();

			while (iter.hasNext()) {
				Entry<Integer, BlockInfo> entry = iter.next();
				totalBlocks.remove(entry.getKey(), entry.getValue());
			}
		}

		// A bug here: This method does not have blockList sorted by priority
		// correctly
		// Sorted By Blocks' Priority
		// PriorityOperation.sortedByPriority(totalBlocks);

		// add begin

		List<Entry<Integer, BlockInfo>> list = new ArrayList<Entry<Integer, BlockInfo>>(totalBlocks.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<Integer, BlockInfo>>() {
			public int compare(Map.Entry<Integer, BlockInfo> e1, Map.Entry<Integer, BlockInfo> e2) {
				double v1 = (double) ((Map.Entry<Integer, BlockInfo>) e1).getValue().getPriority();
				double v2 = (double) ((Map.Entry<Integer, BlockInfo>) e2).getValue().getPriority();
				double flag = v2 - v1;
				if (flag > 0.0) {
					return 1;
				} else if (flag == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		int SSDId = 0;
		int cacheId = 0;
		for (int i = 0; i < list.size(); i++) {
			if (SSDDisks[SSDId].getBlockAmount() <= blockInSSD) {// not full:，应为<
				SSDDisks[SSDId].setDiskState(1);
				SSDDisks[SSDId].getBlockList().put(list.get(i).getKey(), list.get(i).getValue());
				SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount() + 1);
				SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace() - 1);
			} else if (SSDId + 1 < SSDDisks.length) {// if not the last one:
				SSDDisks[++SSDId].setDiskState(1);
				SSDDisks[SSDId].getBlockList().put(list.get(i).getKey(), list.get(i).getValue());
				SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount() + 1);
				SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace() - 1);
			} else if (cacheDisks[cacheId].getBlockAmount() <= blockInCache) {// not full:
				cacheDisks[cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(list.get(i).getKey(), list.get(i).getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount() + 1);
				cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace() - 1);
			} else if (cacheId + 1 < cacheDisks.length) {// if not the last one:
				cacheDisks[++cacheId].setDiskState(1);
				cacheDisks[cacheId].getBlockList().put(list.get(i).getKey(), list.get(i).getValue());
				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount() + 1);
				cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace() - 1);
			}
		}
		System.out.println("RefreshReplacement finished.");

		// add end

		// Iterator<Entry<Integer,BlockInfo>> iterTotal =
		// totalBlocks.entrySet().iterator();
		//
		// int SSDId = 0;
		// int cacheId = 0;
		//
		// while (iterTotal.hasNext()){
		// Entry<Integer,BlockInfo> entry = iterTotal.next();
		//
		// if(SSDDisks[SSDId].getBlockAmount() <=blockInSSD){//not full:，应为<
		// SSDDisks[SSDId].setDiskState(1);
		// SSDDisks[SSDId].getBlockList().put(entry.getKey(), entry.getValue());
		// SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount()+1);
		// SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace()-1);
		// } else if(SSDId+1 < SSDDisks.length){//if not the last one:
		// SSDDisks[++SSDId].setDiskState(1);
		// SSDDisks[SSDId].getBlockList().put(entry.getKey(), entry.getValue());
		// SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount()+1);
		// SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace()-1);
		// } else if(cacheDisks[cacheId].getBlockAmount() <= blockInCache){//not
		// full:
		// cacheDisks[cacheId].setDiskState(1);
		// cacheDisks[cacheId].getBlockList().put(entry.getKey(),
		// entry.getValue());
		// cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount()+1);
		// cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace()-1);
		// }else if (cacheId+1 < cacheDisks.length){//if not the last one:
		// cacheDisks[++cacheId].setDiskState(1);
		// cacheDisks[cacheId].getBlockList().put(entry.getKey(),
		// entry.getValue());
		// cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount()+1);
		// cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace()-1);
		// }
		// }
	}

}
