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
import org.tju.bean.FileInfo;
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

	/**
	 * Data Disk ====>> SSD Replacement Strategy<br>
	 * 把目标块blockId及其前后各cacheCorrelation块dataDisk从迁移进SSDDisk
	 * @param dataDisk
	 * @param SSDDisk
	 * @param blockId
	 */
	public static void DD2SSD(DiskInfo dataDisk, DiskInfo SSDDisk, int blockId) {

		// Note: The block in DD and the cached one are exactly the same one!!
	//	BlockInfo block = dataDisk.getBlockList().get(blockId);
		BlockInfo block = dataDisk.getBlockList().get(blockId).deepClone();
		SSDDisk.getBlockList().put(blockId, block);
		SSDDisk.setBlockAmount(SSDDisk.getBlockAmount() + 1);
		SSDDisk.setLeftSpace(SSDDisk.getLeftSpace() - block.getTotalSpace());
		System.out.println("[CACHE] Data Disk " + dataDisk.getDiskId() + "====>> SSD " + SSDDisk.getDiskId() + "(normal), blockId: " + blockId);

		// Test:
//		BlockInfo blockInDD = dataDisk.getBlockList().get(blockId);
//		blockInDD.setIsHit(8);
//		Iterator<Entry<String, FileInfo>> iter = blockInDD.getFilesList().entrySet().iterator();
//		while (iter.hasNext()){
//			Entry<String, FileInfo> entry = iter.next();
//			FileInfo file = entry.getValue();
//			file.setIsHit(9);
//		}
//		BlockInfo blockInSSD = SSDDisk.getBlockList().get(blockId);
//		blockInSSD.setIsHit(10);
//		Iterator<Entry<String, FileInfo>> iter1 = blockInSSD.getFilesList().entrySet().iterator();
//		while (iter1.hasNext()){
//			Entry<String, FileInfo> entry1 = iter1.next();
//			FileInfo file = entry1.getValue();
//			file.setIsHit(11);
//		}
//		System.out.println("[TEST]  Is the cached block in DD the same as in SSD? " + blockInDD.equals(blockInSSD));
//		System.out.println(blockInDD);
//		System.out.println(blockInSSD);
		
		
		// add
		// 把相关块迁移进SSD
		if (RequestCorrelation.hasKey(blockId)) {
			Integer[] corrs = RequestCorrelation.getCorr(blockId);
			for (int i = 0; i < RequestCorrelation.getCorrelationNum(); i++) {
				if (corrs[i] != null) {
				//	block = dataDisk.getBlockList().get(corrs[i]);
					block = dataDisk.getBlockList().get(corrs[i]).deepClone();
					if (block != null) {
						SSDDisk.getBlockList().put(corrs[i], block);// ???????
						SSDDisk.setBlockAmount(SSDDisk.getBlockAmount() + 1);
						SSDDisk.setLeftSpace(SSDDisk.getLeftSpace() - 1);
						System.out.println("[CACHE] Data Disk " + dataDisk.getDiskId() + "====>> SSD " + SSDDisk.getDiskId() + "(related), blockId: " + corrs[i]);
					}
				}
			}
		}

//		// 把目标块之前cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//		//	block = dataDisk.getBlockList().get(blockId - i);
//			block = dataDisk.getBlockList().get(blockId - i).deepClone();
//			if (block != null) {
//				SSDDisk.getBlockList().put(blockId - i, block);// ???????
//				SSDDisk.setBlockAmount(SSDDisk.getBlockAmount() + 1);
//				SSDDisk.setLeftSpace(SSDDisk.getLeftSpace() - 1);
//			}
//		}
//		// 把目标块之后cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//		//	block = dataDisk.getBlockList().get(blockId + i);
//			block = dataDisk.getBlockList().get(blockId + i).deepClone();
//			if (block != null) {
//				SSDDisk.getBlockList().put(blockId + i, block);
//				SSDDisk.setBlockAmount(SSDDisk.getBlockAmount() + 1);
//				SSDDisk.setLeftSpace(SSDDisk.getLeftSpace() - 1);
//			}
//		}

	}

	/**
	 * Data Disk ====>> Cache Disk Replacement Strategy<br>
	 * 把目标块blockId及其前后各cacheCorrelation块从dataDisk迁移进CacheDisk
	 * @param dataDisk
	 * @param cacheDisk
	 * @param blockId
	 */
	public static void DD2Cache(DiskInfo dataDisk, DiskInfo cacheDisk, int blockId) {
		// 把目标block缓存
	//	BlockInfo block = dataDisk.getBlockList().get(blockId);
		BlockInfo block = dataDisk.getBlockList().get(blockId).deepClone();
		cacheDisk.getBlockList().put(blockId, block);
		cacheDisk.setBlockAmount(cacheDisk.getBlockAmount() + 1);
		cacheDisk.setLeftSpace(cacheDisk.getLeftSpace() - 1);
		System.out.println("[CACHE] Data Disk " + dataDisk.getDiskId() + "====>> Cache Disk " + cacheDisk.getDiskId() + "(normal), blockId: " + blockId);

		// add
		// 把（学习到的）相关块迁移进cacheDisk
		if (RequestCorrelation.hasKey(blockId)) {
			Integer[] corrs = RequestCorrelation.getCorr(blockId);
			for (int i = 0; i < RequestCorrelation.getCorrelationNum(); i++) {
				if (corrs[i] != null) {
					block = dataDisk.getBlockList().get(corrs[i]);
					if (block != null) {
						cacheDisk.getBlockList().put(corrs[i], block);// ???????
						cacheDisk.setBlockAmount(cacheDisk.getBlockAmount() + 1);
						cacheDisk.setLeftSpace(cacheDisk.getLeftSpace() - 1);
						System.out.println("[CACHE] Data Disk " + dataDisk.getDiskId() + "====>> Cache Disk " + cacheDisk.getDiskId() + "(related), blockId: " + corrs[i]);
					}
				}
			}
		}
		
//		// 把目标块之前cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//		//	block = dataDisk.getBlockList().get(blockId - i);
//			block = dataDisk.getBlockList().get(blockId - i).deepClone();
//			if (block != null) {
//				cacheDisk.getBlockList().put(blockId - i, block);
//				cacheDisk.setBlockAmount(cacheDisk.getBlockAmount() + 1);
//				cacheDisk.setLeftSpace(cacheDisk.getLeftSpace() - 1);
//			}
//		}
//		
//		// 把目标块之后cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//		//	block = dataDisk.getBlockList().get(blockId + i);
//			block = dataDisk.getBlockList().get(blockId + i).deepClone();
//			if (block != null) {
//				cacheDisk.getBlockList().put(blockId + i, block);
//				cacheDisk.setBlockAmount(cacheDisk.getBlockAmount() + 1);
//				cacheDisk.setLeftSpace(cacheDisk.getLeftSpace() - 1);
//			}
//		}
		
		// 把目标块前1块迁移进CacheDisk
		/*
		 * BlockInfo block = dataDisk.getBlockList().get(blockId-1);
		 * cacheDisk.getBlockList().put(blockId-1, block);
		 * cacheDisk.setBlockAmount(cacheDisk.getBlockAmount()+1);
		 * cacheDisk.setLeftSpace(cacheDisk.getLeftSpace()-1);
		 */
		// 把目标块后1块迁移进CacheDisk
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
	
	private void displaySSDCacheDisk(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks) {
		System.out.println("================ Situation on SSDs and CacheDisks ================");
		
	}

	// Refresh Replacement Strategy
	public static void RefreshReplacement(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks) {
		// All Blocks Info
		HashMap<Integer, BlockInfo> totalBlocks = new HashMap<Integer, BlockInfo>();

		// Clear BlockList of SSD
		for (int i = 0; i < SSDDisks.length; i++) {
			totalBlocks.putAll(SSDDisks[i].getBlockList());
			SSDDisks[i].setBlockList(new HashMap<Integer, BlockInfo>());
			// FIXME: No need to close SSDs
			DiskOperation.closeCache(SSDDisks[i]);
		}

		// Clear BolckList of each cache disks && close disk
		for (int i = 0; i < cacheDisks.length; i++) {
			totalBlocks.putAll(cacheDisks[i].getBlockList());
			cacheDisks[i].setBlockList(new HashMap<Integer, BlockInfo>());
			// FIXME: No need to close CacheDisks
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

//		 A bug here: This method does not have blockList sorted by priority correctly
		 // Sorted By Blocks' Priority
//		 PriorityOperation.sortedByPriority(totalBlocks);

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

//		Iterator<Entry<Integer, BlockInfo>> iterTotal = totalBlocks.entrySet().iterator();
//
//		int SSDId = 0;
//		int cacheId = 0;
//
//		while (iterTotal.hasNext()) {
//			Entry<Integer, BlockInfo> entry = iterTotal.next();
//
//			if (SSDDisks[SSDId].getBlockAmount() <= blockInSSD) {// not
//																	// full:，应为<
//				SSDDisks[SSDId].setDiskState(1);
//				SSDDisks[SSDId].getBlockList().put(entry.getKey(), entry.getValue());
//				SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount() + 1);
//				SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace() - 1);
//			} else if (SSDId + 1 < SSDDisks.length) {// if not the last one:
//				SSDDisks[++SSDId].setDiskState(1);
//				SSDDisks[SSDId].getBlockList().put(entry.getKey(), entry.getValue());
//				SSDDisks[SSDId].setBlockAmount(SSDDisks[SSDId].getBlockAmount() + 1);
//				SSDDisks[SSDId].setLeftSpace(SSDDisks[SSDId].getLeftSpace() - 1);
//			} else if (cacheDisks[cacheId].getBlockAmount() <= blockInCache) {// not
//				full: cacheDisks[cacheId].setDiskState(1);
//				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
//				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount() + 1);
//				cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace() - 1);
//			} else if (cacheId + 1 < cacheDisks.length) {// if not the last one:
//				cacheDisks[++cacheId].setDiskState(1);
//				cacheDisks[cacheId].getBlockList().put(entry.getKey(), entry.getValue());
//				cacheDisks[cacheId].setBlockAmount(cacheDisks[cacheId].getBlockAmount() + 1);
//				cacheDisks[cacheId].setLeftSpace(cacheDisks[cacheId].getLeftSpace() - 1);
//			}
//		}
	}

}
