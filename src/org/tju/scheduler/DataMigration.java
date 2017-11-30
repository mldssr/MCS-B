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
	
	/**
	 * @param dataDisk
	 * @param SSD_cache_Disk
	 * @param blockId
	 */
	private static boolean cacheBlock(DiskInfo dataDisk, DiskInfo SSD_cache_Disk, int blockId) {
		if (dataDisk.getDiskState() == 0) {
			return false;
		}
		if (SSD_cache_Disk.getBlockList().get(blockId) != null) { // Already cached
			return false;
		}
		BlockInfo block = dataDisk.getBlockList().get(blockId);
		if (block != null) { // Find target block
			block = block.deepClone();
			SSD_cache_Disk.getBlockList().put(blockId, block);// ??????? No problem!
			SSD_cache_Disk.setBlockAmount(SSD_cache_Disk.getBlockAmount() + 1);
			SSD_cache_Disk.setLeftSpace(SSD_cache_Disk.getLeftSpace() - block.getTotalSpace());
			return true;
		}
		return false;
	}

	/**
	 * Suppose that we have learned Correlation of Requests very well, <br>
	 * we cache given blockId's correlate blocks in given DataDisk<br>
	 * FIXME: 980 means the distance of correlate blockIds under default
	 * configuration. I'm a lazy boy.
	 * 
	 * @param dataDisk
	 * @param SSDDisk
	 * @param blockId
	 */
	private static void cahceCorBlocks(DiskInfo dataDisk, DiskInfo SSDDisk, int blockId) {
		if (cacheBlock(dataDisk, SSDDisk, blockId - 980)) {
			System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> SSD or Cache "
					+ SSDDisk.getDiskId() + " blockId: " + (blockId - 980));
		}
		if (cacheBlock(dataDisk, SSDDisk, blockId + 980)) {
			System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> SSD or Cache "
					+ SSDDisk.getDiskId() + " blockId: " + (blockId + 980));
		}
	}

	/**
	 * Data Disk ====>> SSD Replacement Strategy<br>
	 * 把目标块blockId及其前后各cacheCorrelation块dataDisk从迁移进SSDDisk<br>
	 * Make sure the block is in the dataDisk
	 * @param dataDisk
	 * @param SSDDisk
	 * @param blockId
	 */
	public static void DD2SSD(DiskInfo dataDisk, DiskInfo SSDDisk, int blockId) {

		// Note: The block in DD and the cached one are exactly the same one!!
		if (cacheBlock(dataDisk, SSDDisk, blockId)) {
			System.out.println("[CACHE] (normal) Data Disk " + dataDisk.getDiskId() + "====>> SSD "
					+ SSDDisk.getDiskId() + " blockId: " + blockId);
		}

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
		// 把这块DataDisk上的相关块迁移进SSD
		// FIXME
		if (RequestCorrelation.hasKey(blockId)) {
			Integer[] corrs = RequestCorrelation.getCorr(blockId);
			for (int i = 0; i < RequestCorrelation.getCorrelationNum(); i++) {
				if (corrs[i] != null) {
					if (cacheBlock(dataDisk, SSDDisk, corrs[i])) {
						System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> SSD "
								+ SSDDisk.getDiskId() + " blockId: " + corrs[i]);
					}
				}
			}
		}
		cahceCorBlocks(dataDisk, SSDDisk, blockId);

//		// 把目标块之前cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//			if (cacheBlock(dataDisk, SSDDisk, blockId - i)) {
//				System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> SSD "
//						+ SSDDisk.getDiskId() + " blockId: " + (blockId - i));
//			}
//		}
//		// 把目标块之后cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//			if (cacheBlock(dataDisk, SSDDisk, blockId + i)) {
//				System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> SSD "
//						+ SSDDisk.getDiskId() + " blockId: " + (blockId + i));
//			}
//		}

	}

	/**
	 * Data Disk ====>> Cache Disk Replacement Strategy<br>
	 * 把目标块blockId及其前后各cacheCorrelation块从dataDisk迁移进CacheDisk
	 * 
	 * @param dataDisk
	 * @param cacheDisk
	 * @param blockId
	 */
	public static void DD2Cache(DiskInfo dataDisk, DiskInfo cacheDisk, int blockId) {
		// 把目标block缓存
		if (cacheBlock(dataDisk, cacheDisk, blockId)) {
			System.out.println("[CACHE] (normal) Data Disk " + dataDisk.getDiskId() + "====>> Cache Disk "
					+ cacheDisk.getDiskId() + " blockId: " + blockId);
		}

		// add
		// 把（学习到的）相关块迁移进cacheDisk
		// FIXME
		if (RequestCorrelation.hasKey(blockId)) {
			Integer[] corrs = RequestCorrelation.getCorr(blockId);
			for (int i = 0; i < RequestCorrelation.getCorrelationNum(); i++) {
				if (corrs[i] != null) {
					if (cacheBlock(dataDisk, cacheDisk, corrs[i])) {
						System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> Cache Disk "
								+ cacheDisk.getDiskId() + " blockId: " + corrs[i]);
					}
				}
			}
		}
		cahceCorBlocks(dataDisk, cacheDisk, blockId);

//		// 把目标块之前cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//			if (cacheBlock(dataDisk, cacheDisk, blockId - i)) {
//				System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> Cache Disk "
//						+ cacheDisk.getDiskId() + " blockId: " + (blockId - i));
//			}
//		}
//		// 把目标块之后cacheCorrelation个block缓存
//		for (int i = cacheCorrelation; i > 0; i--) {
//			if (cacheBlock(dataDisk, cacheDisk, blockId + i)) {
//				System.out.println("[CACHE] (related) Data Disk " + dataDisk.getDiskId() + "====>> Cache Disk "
//						+ cacheDisk.getDiskId() + " blockId: " + (blockId + i));
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
//			DiskOperation.closeCache(SSDDisks[i]);
		}

		// Clear BolckList of each cache disks && close disk
		for (int i = 0; i < cacheDisks.length; i++) {
			totalBlocks.putAll(cacheDisks[i].getBlockList());
			cacheDisks[i].setBlockList(new HashMap<Integer, BlockInfo>());
			// FIXME: No need to close CacheDisks
//			DiskOperation.closeCache(cacheDisks[i]);
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
