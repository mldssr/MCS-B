package org.tju.initialization;

import java.util.HashMap;
import java.util.Random;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.bean.FileInfo;
import org.tju.scheduler.BlockOperation;
import org.tju.util.ValueOfConfigureFile;

public class InitEnvironment {
	
	//Random
	public Random random = new Random();
	
	//Value of configure files
	public ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//the start nums
	public int skyzone = 0;
	public int fileId = 0;
	public int blockId = 0;
	public int diskId = 0;
	
	//the disks' starting ID
	public int dataDiskStartId = valueOfConfigureFile.getDataDiskStartId();
	public int cacheDiskStartId = valueOfConfigureFile.getCacheDiskStartId();
	public int SSDDiskStartId = valueOfConfigureFile.getSSDDiskStartId();
	
	//get files' info
	public int fileAmount = valueOfConfigureFile.getFileAmount();
	public int fileBasicSize = valueOfConfigureFile.getFileBasicSize();
	public int fileSizeErr = valueOfConfigureFile.getFileSizeErr();
	
	//get blocks' info
	public int blockAmount = valueOfConfigureFile.getBlockAmount();
	public int blockSize = valueOfConfigureFile.getBlockSize();
	public int fileInBlock = valueOfConfigureFile.getFileInBlock();
	
	//get observe info (skyzone and time)
	public int timeAmount = valueOfConfigureFile.getTimeAmount();
	public int skyzoneAmount = valueOfConfigureFile.getSkyzoneAmount();
	
	//get duplicate amount
	public int duplicateAmount = valueOfConfigureFile.getDuplicateAmount();
	
	/**get disks' info**/
	//get disks' amount
	public int SSDAmount = valueOfConfigureFile.getSSDAmount();
	public int cacheAmount = valueOfConfigureFile.getCacheAmount();
	public int dataDiskAmount = valueOfConfigureFile.getDataDiskAmount();
	
	//Declare disks' info
	public DiskInfo[] SSDDisk = new DiskInfo[SSDAmount];
	public DiskInfo[] cacheDisks = new DiskInfo[cacheAmount];
	public DiskInfo[] dataDisks = new DiskInfo[dataDiskAmount];
	
	//get HDD disks' parameters
	public int diskSize = valueOfConfigureFile.getDiskSize();
	public double diskOperPower = valueOfConfigureFile.getDiskOperPower();
	
	//get SSD disks' parameters
	public int SSDSize = valueOfConfigureFile.getSSDSize();
	public double SSDOperPower = valueOfConfigureFile.getSSDOperPower();
	
	//get disks' capacity
	public int blockInDisk = valueOfConfigureFile.getBlockInDisk();
	public int skyzoneInDisk = valueOfConfigureFile.getSkyzoneInDisk();
	
	//disks' open time
	public int openTime = valueOfConfigureFile.getOpenTime();
	
	
	//initialize the basic environment
	public void initEnvironment() {
		
		System.out.println("========>>>>Start Initialize Environment!!!<<<<========\n");		
		
		//Initialize First Level Cache Disk
		initSSDDisk();
		
		//Initialize First Level Cache Disk
		initCacheDisk();
		
		//Initialize data Disk
		initDataDisk();
		
		//Initialize Environment End
		System.out.println("========>>>>Initialize Environment Success!!!<<<<========\n");		
		
	}
	
	
	
	//Initialize First Level Cache Disk
	public void initSSDDisk(){
		
		System.out.println("========>>>>Start Initialize SSD Disk!!!<<<<========\n");

		HashMap<Integer, BlockInfo> SSDBlockList = new HashMap<Integer, BlockInfo>();
		for(int i=SSDDiskStartId; i<SSDDiskStartId+SSDAmount; i++){
			SSDDisk[i-SSDDiskStartId] = new DiskInfo(i, 0, 0, SSDSize, SSDSize, 0, 0, 0, SSDOperPower, SSDBlockList, 0, 0);
		}
		
		//Initialize First Level Cache Disk End
		System.out.println("========>>>>Initialize SSD Disk Success!!!<<<<========\n");
		
	}
	
	
	//Initialize Second Level Cache Disk
	public void initCacheDisk(){
		
		System.out.println("========>>>>Start Initialize Cache Disks!!!<<<<========\n");

		HashMap<Integer, BlockInfo> cacheBlockList = new HashMap<Integer, BlockInfo>();
		for(int i=cacheDiskStartId; i<cacheDiskStartId+cacheAmount; i++){
			cacheDisks[i-cacheDiskStartId] = new DiskInfo(i, 1, 0, diskSize, diskSize, 0, 0, 0,diskOperPower, cacheBlockList, 0, 0);
		}
		
		//Initialize Second Level Cache Disk End
		System.out.println("========>>>>Initialize Cache Disks Success!!!<<<<========\n");
				
	}
	
	
	//Initialize data Disk
	public void initDataDisk(){
		
		System.out.println("========>>>>Start Initialize Data Disks!!!<<<<========\n");

		//duplicate block list
		HashMap<Integer, BlockInfo> duplicateBlocksList = new HashMap<Integer, BlockInfo>();	
		//first duplicate block list
		HashMap<Integer, BlockInfo> firstduplicateBlocksList = new HashMap<Integer, BlockInfo>();	
				
		//initialize data disks
		for(int i=dataDiskStartId; i<dataDiskStartId+dataDiskAmount; i++){
			
			//disk left space
			int diskLeftSpace = diskSize;
			
			//the blocks in disk
			HashMap<Integer, BlockInfo> blocksList = new HashMap<Integer, BlockInfo>();	
			
			//add duplicate block list to disk block list
			if(duplicateBlocksList.size()!=0){
				blocksList.putAll(duplicateBlocksList);
				duplicateBlocksList = new HashMap<Integer, BlockInfo>();
			}
			
			//order by skyzone, if duplicateAmount == 1, skyzone will be arranged as follows:
			//skyzone in disk0:   0- 49
			//skyzone in disk1:  49- 98
			//skyzone in disk0:  98-147
			for( ; skyzone < skyzoneInDisk*(i-dataDiskStartId+1)-(i-dataDiskStartId) && skyzone<skyzoneAmount; skyzone++){
				//initialize block
				//the files in block
				HashMap<String, FileInfo> filesList = new HashMap<String, FileInfo>();

				//block left space
				int blockLeftSpace = blockSize;
				
				//initialize file					
				for(int l=0; l<timeAmount; l++){
					String fileName = String.valueOf(i)+"-"+String.valueOf(blockId)+"-"+String.valueOf(skyzone)+"-"+String.valueOf(l);
					int fileSize = fileBasicSize + random.nextInt()%fileSizeErr;
					
					FileInfo file = new FileInfo(fileId++, fileName, fileSize, l, skyzone, blockId, 0, 0, 0);
					filesList.put(fileName, file);
					
					blockLeftSpace -= fileSize;
					//如果生成的文件刚好组成一个block，则。。。如果l不是fileInBlock的整倍数，多余的文件会被抛弃
					if((l+1)%fileInBlock == 0){
						BlockInfo block = new BlockInfo(blockId++, blockSize, blockLeftSpace, l+1-fileInBlock, skyzone, i, 0, 0, 0, 0, openTime, fileInBlock, filesList);
//						System.out.println("A new block is created, contains " + filesList.size()+" files.");
//						System.out.println("blockLeftSpace: " + blockLeftSpace);
						//add:
						filesList.clear();
						blocksList.put(block.getBlockId(), block);
						blockLeftSpace = blockSize;
						diskLeftSpace -= blockSize;
//						System.out.println("diskLeftSpace: " + diskLeftSpace);
						
						//add duplicate to duplicate block list which would be added into next disk later
						if(skyzone+duplicateAmount >= skyzoneInDisk*(i+1)-i){
							duplicateBlocksList.put(block.getBlockId(), block);
						}
						
						//add first duplicate to duplicate block list
						if(skyzone < duplicateAmount) {
							firstduplicateBlocksList.put(block.getBlockId(), block);
						}
					}
				}
			}
			
			//add the first duplicate to the last disk
			if(i == dataDiskAmount-1){
				blocksList.putAll(firstduplicateBlocksList);
			}

			dataDisks[i] = new DiskInfo(i, 2, 0, diskSize, diskLeftSpace, blockInDisk, 0, 0, diskOperPower, blocksList, 0, 0);
		
		    System.out.println("The Disk-"+ i + " ready!");
		    System.out.println("skyznone = " + (skyzone-1));
		    System.out.println("BlockId = " + (blockId-1));
		    System.out.println("FileId = " + (fileId-1));
//		    System.out.println();
//		    System.out.println(blocksList.keySet());
		    System.out.println();
		}
		
		//initialize data disks' blocks' list 
		BlockOperation.initBlocksList(dataDisks);
		
		//Initialize Data Disks End
		System.out.println("========>>>>Initialize Data Disks Success!!!<<<<========\n");
			
	}
	
	
	public static void main(String[] args){
		System.out.println("Test begin!");
		InitEnvironment init = new InitEnvironment();
		init.initEnvironment();
	}
	
	
	/**
	 * @return the random
	 */
	public Random getRandom() {
		return random;
	}

	
	/**
	 * @param random the random to set
	 */
	public void setRandom(Random random) {
		this.random = random;
	}


	/**
	 * @return the skyzone
	 */
	public int getSkyzone() {
		return skyzone;
	}


	/**
	 * @param skyzone the skyzone to set
	 */
	public void setSkyzone(int skyzone) {
		this.skyzone = skyzone;
	}


	/**
	 * @return the fileId
	 */
	public int getFileId() {
		return fileId;
	}


	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}


	/**
	 * @return the blockId
	 */
	public int getBlockId() {
		return blockId;
	}


	/**
	 * @param blockId the blockId to set
	 */
	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}


	/**
	 * @return the diskId
	 */
	public int getDiskId() {
		return diskId;
	}

	
	/**
	 * @param diskId the diskId to set
	 */
	public void setDiskId(int diskId) {
		this.diskId = diskId;
	}


	/**
	 * @return the dataDiskStartId
	 */
	public int getDataDiskStartId() {
		return dataDiskStartId;
	}


	/**
	 * @param dataDiskStartId the dataDiskStartId to set
	 */
	public void setDataDiskStartId(int dataDiskStartId) {
		this.dataDiskStartId = dataDiskStartId;
	}


	/**
	 * @return the cacheDiskStartId
	 */
	public int getCacheDiskStartId() {
		return cacheDiskStartId;
	}


	/**
	 * @param cacheDiskStartId the cacheDiskStartId to set
	 */
	public void setCacheDiskStartId(int cacheDiskStartId) {
		this.cacheDiskStartId = cacheDiskStartId;
	}


	/**
	 * @return the sSDDiskStartId
	 */
	public int getSSDDiskStartId() {
		return SSDDiskStartId;
	}


	/**
	 * @param sSDDiskStartId the sSDDiskStartId to set
	 */
	public void setSSDDiskStartId(int sSDDiskStartId) {
		SSDDiskStartId = sSDDiskStartId;
	}


	/**
	 * @return the fileAmount
	 */
	public int getFileAmount() {
		return fileAmount;
	}


	/**
	 * @param fileAmount the fileAmount to set
	 */
	public void setFileAmount(int fileAmount) {
		this.fileAmount = fileAmount;
	}


	/**
	 * @return the fileBasicSize
	 */
	public int getFileBasicSize() {
		return fileBasicSize;
	}


	/**
	 * @param fileBasicSize the fileBasicSize to set
	 */
	public void setFileBasicSize(int fileBasicSize) {
		this.fileBasicSize = fileBasicSize;
	}


	/**
	 * @return the fileSizeErr
	 */
	public int getFileSizeErr() {
		return fileSizeErr;
	}


	/**
	 * @param fileSizeErr the fileSizeErr to set
	 */
	public void setFileSizeErr(int fileSizeErr) {
		this.fileSizeErr = fileSizeErr;
	}


	/**
	 * @return the blockAmount
	 */
	public int getBlockAmount() {
		return blockAmount;
	}


	/**
	 * @param blockAmount the blockAmount to set
	 */
	public void setBlockAmount(int blockAmount) {
		this.blockAmount = blockAmount;
	}


	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}


	/**
	 * @param blockSize the blockSize to set
	 */
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}


	/**
	 * @return the fileInBlock
	 */
	public int getFileInBlock() {
		return fileInBlock;
	}


	/**
	 * @param fileInBlock the fileInBlock to set
	 */
	public void setFileInBlock(int fileInBlock) {
		this.fileInBlock = fileInBlock;
	}


	/**
	 * @return the timeAmount
	 */
	public int getTimeAmount() {
		return timeAmount;
	}


	/**
	 * @param timeAmount the timeAmount to set
	 */
	public void setTimeAmount(int timeAmount) {
		this.timeAmount = timeAmount;
	}


	/**
	 * @return the skyzoneAmount
	 */
	public int getSkyzoneAmount() {
		return skyzoneAmount;
	}


	/**
	 * @param skyzoneAmount the skyzoneAmount to set
	 */
	public void setSkyzoneAmount(int skyzoneAmount) {
		this.skyzoneAmount = skyzoneAmount;
	}


	/**
	 * @return the duplicateAmount
	 */
	public int getDuplicateAmount() {
		return duplicateAmount;
	}


	/**
	 * @param duplicateAmount the duplicateAmount to set
	 */
	public void setDuplicateAmount(int duplicateAmount) {
		this.duplicateAmount = duplicateAmount;
	}


	/**
	 * @return the sSDAmount
	 */
	public int getSSDAmount() {
		return SSDAmount;
	}


	/**
	 * @param sSDAmount the sSDAmount to set
	 */
	public void setSSDAmount(int sSDAmount) {
		SSDAmount = sSDAmount;
	}


	/**
	 * @return the cacheAmount
	 */
	public int getCacheAmount() {
		return cacheAmount;
	}


	/**
	 * @param cacheAmount the cacheAmount to set
	 */
	public void setCacheAmount(int cacheAmount) {
		this.cacheAmount = cacheAmount;
	}


	/**
	 * @return the dataDiskAmount
	 */
	public int getDataDiskAmount() {
		return dataDiskAmount;
	}


	/**
	 * @param dataDiskAmount the dataDiskAmount to set
	 */
	public void setDataDiskAmount(int dataDiskAmount) {
		this.dataDiskAmount = dataDiskAmount;
	}


	/**
	 * @return the sSDDisk
	 */
	public DiskInfo[] getSSDDisk() {
		return SSDDisk;
	}


	/**
	 * @param sSDDisk the sSDDisk to set
	 */
	public void setSSDDisk(DiskInfo[] sSDDisk) {
		SSDDisk = sSDDisk;
	}


	/**
	 * @return the cacheDisks
	 */
	public DiskInfo[] getCacheDisks() {
		return cacheDisks;
	}


	/**
	 * @param cacheDisks the cacheDisks to set
	 */
	public void setCacheDisks(DiskInfo[] cacheDisks) {
		this.cacheDisks = cacheDisks;
	}


	/**
	 * @return the dataDisks
	 */
	public DiskInfo[] getDataDisks() {
		return dataDisks;
	}


	/**
	 * @param dataDisks the dataDisks to set
	 */
	public void setDataDisks(DiskInfo[] dataDisks) {
		this.dataDisks = dataDisks;
	}


	/**
	 * @return the diskSize
	 */
	public int getDiskSize() {
		return diskSize;
	}


	/**
	 * @param diskSize the diskSize to set
	 */
	public void setDiskSize(int diskSize) {
		this.diskSize = diskSize;
	}


	/**
	 * @return the diskOperPower
	 */
	public double getDiskOperPower() {
		return diskOperPower;
	}


	/**
	 * @param diskOperPower the diskOperPower to set
	 */
	public void setDiskOperPower(double diskOperPower) {
		this.diskOperPower = diskOperPower;
	}


	/**
	 * @return the sSDSize
	 */
	public int getSSDSize() {
		return SSDSize;
	}


	/**
	 * @param sSDSize the sSDSize to set
	 */
	public void setSSDSize(int sSDSize) {
		SSDSize = sSDSize;
	}


	/**
	 * @return the sSDOperPower
	 */
	public double getSSDOperPower() {
		return SSDOperPower;
	}


	/**
	 * @param sSDOperPower the sSDOperPower to set
	 */
	public void setSSDOperPower(double sSDOperPower) {
		SSDOperPower = sSDOperPower;
	}


	/**
	 * @return the blockInDisk
	 */
	public int getBlockInDisk() {
		return blockInDisk;
	}


	/**
	 * @param blockInDisk the blockInDisk to set
	 */
	public void setBlockInDisk(int blockInDisk) {
		this.blockInDisk = blockInDisk;
	}


	/**
	 * @return the skyzoneInDisk
	 */
	public int getSkyzoneInDisk() {
		return skyzoneInDisk;
	}


	/**
	 * @param skyzoneInDisk the skyzoneInDisk to set
	 */
	public void setSkyzoneInDisk(int skyzoneInDisk) {
		this.skyzoneInDisk = skyzoneInDisk;
	}
	
	
}
