package org.tju.initialization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.bean.FileInfo;
import org.tju.scheduler.BlockOperation;
import org.tju.util.ValueOfConfigureFile;

public class InitEnvByTime {
	
	public static HashMap<String, Integer> fileName_BlockId = new HashMap<String, Integer>();

	// Random
	public Random random = new Random();

	// Value of configure files
	public ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();

	// the start nums
	public int skyzone = 0;
	public int fileId = 0;
	public int blockId = 0;
	public int diskId = 0;

	// the disks' starting ID
	public int dataDiskStartId = valueOfConfigureFile.getDataDiskStartId();
	public int cacheDiskStartId = valueOfConfigureFile.getCacheDiskStartId();
	public int SSDDiskStartId = valueOfConfigureFile.getSSDDiskStartId();

	// get files' info
	public int fileAmount = valueOfConfigureFile.getFileAmount();
	public int fileBasicSize = valueOfConfigureFile.getFileBasicSize();
	public int fileSizeErr = valueOfConfigureFile.getFileSizeErr();

	// get blocks' info
	public int blockAmount = valueOfConfigureFile.getBlockAmount();
	public int blockSize = valueOfConfigureFile.getBlockSize();
	public int fileInBlock = valueOfConfigureFile.getFileInBlock();

	// get observe info (skyzone and time)
	public int timeAmount = valueOfConfigureFile.getTimeAmount();
	public int skyzoneAmount = valueOfConfigureFile.getSkyzoneAmount();

	// get duplicate amount
	public int duplicateAmount = valueOfConfigureFile.getDuplicateAmount();

	/** get disks' info **/
	// get disks' amount
	public int SSDAmount = valueOfConfigureFile.getSSDAmount();
	public int cacheAmount = valueOfConfigureFile.getCacheAmount();
	public int dataDiskAmount = valueOfConfigureFile.getDataDiskAmount();

	// Declare disks' info
	public DiskInfo[] SSDDisk = new DiskInfo[SSDAmount];
	public DiskInfo[] cacheDisks = new DiskInfo[cacheAmount];
	public DiskInfo[] dataDisks = new DiskInfo[dataDiskAmount];

	// get HDD disks' parameters
	public int diskSize = valueOfConfigureFile.getDiskSize();
	public double diskOperPower = valueOfConfigureFile.getDiskOperPower();

	// get SSD disks' parameters
	public int SSDSize = valueOfConfigureFile.getSSDSize();
	public double SSDOperPower = valueOfConfigureFile.getSSDOperPower();

	// get disks' capacity
	public int blockInDisk = valueOfConfigureFile.getBlockInDisk();
	public int skyzoneInDisk = valueOfConfigureFile.getSkyzoneInDisk();

	// disks' open time
	public int openTime = valueOfConfigureFile.getOpenTime();

	// initialize the basic environment
	public void initEnvironment() {

		System.out.println("========>>>>Start Initialize Environment!!!<<<<========\n");

		// Initialize First Level Cache Disk
		initSSDDisk();

		// Initialize First Level Cache Disk
		initCacheDisk();

		// Initialize data Disk
		initDataDisk();

		// Initialize Environment End
		System.out.println("========>>>>Initialize Environment Success!!!<<<<========\n");

	}

	// Initialize First Level Cache Disk
	public void initSSDDisk() {

		System.out.println("========>>>>Start Initialize SSD Disk!!!<<<<========\n");

		HashMap<Integer, BlockInfo> SSDBlockList = new HashMap<Integer, BlockInfo>();
		for (int i = SSDDiskStartId; i < SSDDiskStartId + SSDAmount; i++) {
			SSDDisk[i - SSDDiskStartId] = new DiskInfo(i, 0, 0, SSDSize, SSDSize, 0, 0, 0, SSDOperPower, SSDBlockList,
					0, 0);
		}

		// Initialize First Level Cache Disk End
		System.out.println("========>>>>Initialize SSD Disk Success!!!<<<<========\n");

	}

	// Initialize Second Level Cache Disk
	public void initCacheDisk() {

		System.out.println("========>>>>Start Initialize Cache Disks!!!<<<<========\n");

		HashMap<Integer, BlockInfo> cacheBlockList = new HashMap<Integer, BlockInfo>();
		for (int i = cacheDiskStartId; i < cacheDiskStartId + cacheAmount; i++) {
			cacheDisks[i - cacheDiskStartId] = new DiskInfo(i, 1, 0, diskSize, diskSize, 0, 0, 0, diskOperPower,
					cacheBlockList, 0, 0);
		}

		// Initialize Second Level Cache Disk End
		System.out.println("========>>>>Initialize Cache Disks Success!!!<<<<========\n");

	}
	
	
	private void addFile(DiskInfo disk, FileInfo file) {
//		System.out.println("[INIT]  Add the following file to disk " + diskId);
		boolean findBlock = false;
		Iterator<Entry<Integer, BlockInfo>> iter = disk.getBlockList().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, BlockInfo> entry = iter.next();
			BlockInfo block = entry.getValue();
			if (block.getSkyZone() == file.getSkyZone()) {
				// Maybe find the target block
				if (block.getFileAmount() < fileInBlock
						&& java.lang.Math.abs(block.getObserveTime() - file.getObserveTime()) < fileInBlock) {
					// Find the target block
					file.setBlockId(block.getBlockId());
					fileName_BlockId.put(file.getFileName(), block.getBlockId());
					block.getFilesList().put(file.getFileName(), file);
					block.setFileAmount(block.getFileAmount() + 1);
					block.setLeftSpace(block.getLeftSpace() - file.getSize());
					findBlock = true;
//					System.out.println("[INIT]  into existing block " + block.getBlockId());
//					System.out.println(file);
					break;
				}
			}
		}
		if (!findBlock) {
			HashMap<String, FileInfo> filesList = new HashMap<String, FileInfo>();
			filesList.put(file.getFileName(), file);
			BlockInfo newBlock = new BlockInfo(blockId, blockSize, blockSize - file.getSize(), file.getObserveTime(),
					file.getSkyZone(), diskId, 0, 0, 0, 0, openTime, 1, filesList);
			file.setBlockId(blockId);
			fileName_BlockId.put(file.getFileName(), newBlock.getBlockId());
			disk.getBlockList().put(newBlock.getBlockId(), newBlock);
			blockId++;
//			System.out.println("[INIT]  into a new block " + newBlock.getBlockId());
//			System.out.println(file);
		}
	}

	// Initialize data Disk
	public void initDataDisk() {

		System.out.println("========>>>>Start Initialize Data Disks!!!<<<<========\n");

		// Decide a single DataDisk can hold how many days of files
		int optTimes = timeAmount / dataDiskAmount + 1;
		int maxTimes = diskSize / skyzoneAmount / (fileBasicSize + fileSizeErr);
		if (optTimes > maxTimes) {
			System.out.println("[ERROR] DataDisks can not hold all the data!");
		}
		System.out.println("optTimes: " + optTimes + " maxTimes: " + maxTimes);
		// Suppose dataDiskStartId equals to 0 as default configed
		for (diskId = 0; diskId < dataDiskAmount; diskId++) {
			HashMap<Integer, BlockInfo> blocksList = new HashMap<Integer, BlockInfo>();
			dataDisks[diskId] = new DiskInfo(diskId, 2, 0, diskSize, diskSize, 0, 0, 0, diskOperPower, blocksList, 0,
					0);
			// Each disk
			for (int time = optTimes * diskId; time < optTimes * (diskId + 1) && time < timeAmount; time++) {
				for (int skyzone = 0; skyzone < skyzoneAmount; skyzone++) {
					// fileName: skyzone-observeTime
					String fileName = String.valueOf(skyzone) + "-" + String.valueOf(time);
					int fileSize = fileBasicSize + random.nextInt() % fileSizeErr;
					FileInfo file = new FileInfo(fileId++, fileName, fileSize, time, skyzone, -1, 0, 0, 0);
					addFile(dataDisks[diskId], file);
				}
			}
		}

/*
		// duplicate block list
		HashMap<Integer, BlockInfo> duplicateBlocksList = new HashMap<Integer, BlockInfo>();
		// first duplicate block list
		HashMap<Integer, BlockInfo> firstduplicateBlocksList = new HashMap<Integer, BlockInfo>();

		// initialize data disks
		for (int i = dataDiskStartId; i < dataDiskStartId + dataDiskAmount; i++) {

			// disk left space
			int diskLeftSpace = diskSize;

			// the blocks in disk
			HashMap<Integer, BlockInfo> blocksList = new HashMap<Integer, BlockInfo>();

			// add duplicate block list to disk block list
			if (duplicateBlocksList.size() != 0) {
				// System.out.println("[DUPLI] Add " +
				// duplicateBlocksList.size() + " dup blocks to disk " + i);
				blocksList.putAll(duplicateBlocksList);
				duplicateBlocksList = new HashMap<Integer, BlockInfo>();
			}

			// order by skyzone, if duplicateAmount == 1, skyzone will be
			// arranged as follows:
			// skyzone in disk0: 0- 49
			// skyzone in disk1: 49- 98
			// skyzone in disk0: 98-147
			// skyzone in disk0: 931-979, 0
			for (; skyzone < (skyzoneInDisk - duplicateAmount) * (i - dataDiskStartId) + skyzoneInDisk
					&& skyzone < skyzoneAmount; skyzone++) {
				// initialize block
				// the files in block
				HashMap<String, FileInfo> filesList = new HashMap<String, FileInfo>();

				// block left space
				int blockLeftSpace = blockSize;

				// initialize file
				// l means observeTime
				for (int l = 0; l < timeAmount; l++) {
					// fileName: diskId-blockId-skyzone-observeTime
					String fileName = String.valueOf(i) + "-" + String.valueOf(blockId) + "-" + String.valueOf(skyzone)
							+ "-" + String.valueOf(l);
					int fileSize = fileBasicSize + random.nextInt() % fileSizeErr;

					FileInfo file = new FileInfo(fileId++, fileName, fileSize, l, skyzone, blockId, 0, 0, 0);
					filesList.put(fileName, file);

					blockLeftSpace -= fileSize;
					// 如果生成的文件刚好组成一个block，则。。。如果l不是fileInBlock的整倍数，多余的文件会被抛弃
					if ((l + 1) % fileInBlock == 0) {
						BlockInfo block = new BlockInfo(blockId++, blockSize, blockLeftSpace, l + 1 - fileInBlock,
								skyzone, i, 0, 0, 0, 0, openTime, fileInBlock, filesList);
						// System.out.println("A new block is created, contains
						// " + block.getFilesList().size() +" files.");
						// System.out.println("blockLeftSpace: " +
						// blockLeftSpace);
						// System.out.println(block);
						// add:
						// filesList.clear();
						filesList = new HashMap<String, FileInfo>();
						blocksList.put(block.getBlockId(), block);
						blockLeftSpace = blockSize;
						diskLeftSpace -= blockSize;
						// System.out.println("diskLeftSpace: " +
						// diskLeftSpace);

						// add duplicate to duplicate block list which would be
						// added into next disk later
						if (skyzone + duplicateAmount >= skyzoneInDisk * (i + 1) - i) {
							BlockInfo dupBlock = block.deepClone();
							duplicateBlocksList.put(dupBlock.getBlockId(), dupBlock);
							// System.out.println("[DUPLI] Skyzone: " + skyzone
							// + " blockId: " + dupBlock.getBlockId() + " total
							// dup blocks " + duplicateBlocksList.size());
						}

						// add first duplicate to duplicate block list
						if (skyzone < duplicateAmount) {
							BlockInfo dupBlock = block.deepClone();
							firstduplicateBlocksList.put(dupBlock.getBlockId(), dupBlock);
						}
					}
				} // end l (timeAmount)
			} // end skyzone

			// add the first duplicate to the last disk
			if (i == dataDiskAmount - 1) {
				blocksList.putAll(firstduplicateBlocksList);
			}

			dataDisks[i] = new DiskInfo(i, 2, 0, diskSize, diskLeftSpace, blockInDisk, 0, 0, diskOperPower, blocksList,
					0, 0);

			System.out.println("The Disk-" + i + " ready!");
			System.out.println("skyznone = " + (skyzone - 1));
			System.out.println("BlockId = " + (blockId - 1));
			System.out.println("FileId = " + (fileId - 1));
			// System.out.println();
			// System.out.println(blocksList.keySet());
			System.out.println();
		} // end i (diskId)
*/
		// initialize data disks' blocks' list
		BlockOperation.initBlocksList(dataDisks);

		// Initialize Data Disks End
		System.out.println("========>>>>Initialize Data Disks Success!!!<<<<========\n");

	}

	public static void main(String[] args) {
		System.out.println("Test begin!");
		InitEnvByTime init = new InitEnvByTime();
		init.initEnvironment();
	}

	/**
	 * @return the sSDDisk
	 */
	public DiskInfo[] getSSDDisk() {
		return SSDDisk;
	}

	/**
	 * @return the cacheDisks
	 */
	public DiskInfo[] getCacheDisks() {
		return cacheDisks;
	}

	/**
	 * @return the dataDisks
	 */
	public DiskInfo[] getDataDisks() {
		return dataDisks;
	}

}
