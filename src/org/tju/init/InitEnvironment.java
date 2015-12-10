package org.tju.init;

import java.util.HashMap;
import java.util.Random;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.bean.FileInfo;
import org.tju.tool.ReadXml;

/**
 * Name: InitEnvironment
 * Description: Initialization Simulation Environment
 * 
 * @author yuan
 *
 * @date 2015年12月10日 上午11:13:15
 */
public class InitEnvironment {
	
	//Random
	Random random = new Random();
	
	//the start nums
	public int skyzone = 0;
	public int fileId = 0;
	public int blockId = 0;
	public int diskId = 0;
	
	//the disks' starting ID
	public int dataDiskStartId = Integer.valueOf(ReadXml.readname("config/DiskNum.xml", "datadisk"));
	public int cacheDiskStartId = Integer.valueOf(ReadXml.readname("config/DiskNum.xml", "cache"));
	public int SSDDiskStartId = Integer.valueOf(ReadXml.readname("config/DiskNum.xml", "ssd"));
	
	//get files' info
	public int fileAmount = Integer.valueOf(ReadXml.readname("config/FileAmount.xml", "amount"));
	public int fileBasicSize = Integer.valueOf(ReadXml.readname("config/FileInfo.xml", "basic"));
	public int fileSizeErr = Integer.valueOf(ReadXml.readname("config/FileInfo.xml", "err"));
	
	//get blocks' info
	public int blockAmount = Integer.valueOf(ReadXml.readname("config/BlockAmount.xml", "amount"));
	public int blockSize = Integer.valueOf(ReadXml.readname("config/BlockAmount.xml", "size"));
	public int fileInBlock = Integer.valueOf(ReadXml.readname("config/BlockAmount.xml", "fileamount"));
	
	//get observe info (skyzone and tme)
	public int timeAmount = Integer.valueOf(ReadXml.readname("config/ObserveInfo.xml", "time"));
	public int skyzoneAmount = Integer.valueOf(ReadXml.readname("config/ObserveInfo.xml", "skyzone"));
	
	//get duplicate amount
	public int duplicateAmount = Integer.valueOf(ReadXml.readname("config/Duplicate.xml", "amount"));
	
	/**get disks' info**/
	//get disks' amount
	public int SSDAmount = Integer.valueOf(ReadXml.readname("config/DiskAmount.xml", "ssd"));
	public int cacheAmount = Integer.valueOf(ReadXml.readname("config/DiskAmount.xml", "cachedisk"));
	public int dataDiskAmount = Integer.valueOf(ReadXml.readname("config/DiskAmount.xml", "datadisk"));
	
	//Declare disks' info
	public DiskInfo[] SSDDisk = new DiskInfo[SSDAmount];
	public DiskInfo[] cacheDisks = new DiskInfo[cacheAmount];
	public DiskInfo[] dataDisks = new DiskInfo[dataDiskAmount];
	
	//get HDD disks' parameters
	public int diskSize = Integer.valueOf(ReadXml.readname("config/HDDDisk.xml", "size"));
	public double diskOperPower = Double.valueOf(ReadXml.readname("config/HDDDisk.xml", "operpower"));
	
	//get SSD disks' parameters
	public int SSDSize = Integer.valueOf(ReadXml.readname("config/SSDDisk.xml", "size"));
	public double SSDOperPower = Double.valueOf(ReadXml.readname("config/SSDDisk.xml", "operpower"));
	
	//get disks' capacity
	public int blockInDisk = Integer.valueOf(ReadXml.readname("config/DiskCapacity.xml", "blockamount"));
	public int skyzoneInDisk = Integer.valueOf(ReadXml.readname("config/DiskCapacity.xml", "skyzone"));
	
	
	//initialize the basic environment
	public void initEnvironment() {
		
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
				duplicateBlocksList.clear();
			}
			
			//order by skyzone
			for( ; skyzone<skyzoneInDisk*(i+1)-i && skyzone<skyzoneAmount; skyzone++){
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
					
					if((l+1)%fileInBlock == 0){
						BlockInfo block = new BlockInfo(blockId++, blockSize, blockLeftSpace, l+1-fileInBlock, skyzone, i, 0, 0, 0, 0, fileInBlock, filesList);
						blocksList.put(block.getBlockId(), block);
						blockLeftSpace = blockSize;
						diskLeftSpace -= blockSize;
						
						//add duplicate to duplicate block list
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

			dataDisks[i] = new DiskInfo(i, 2, 0, diskSize, diskLeftSpace, blockInDisk, 0, diskOperPower, blocksList);
		
		    System.out.println("The Disk-"+ i + " ready!");
		    System.out.println("skyznone = " + (skyzone-1));
		    System.out.println("BlockId = " + (blockId-1));
		    System.out.println("FileId = " + (fileId-1));
		    System.out.println();
		}
		
		//Initialize Data Disks End
		System.out.println("========>>>>Initialize Data Disks Success!!!<<<<========\n");
		
		
		
		//Initialize First Level Cache Disk
		HashMap<Integer, BlockInfo> SSDBlockList = new HashMap<Integer, BlockInfo>();
		for(int i=SSDDiskStartId; i<SSDDiskStartId+SSDAmount; i++){
			SSDDisk[i-SSDDiskStartId] = new DiskInfo(i, 0, 0, SSDSize, SSDSize, 0, 0, SSDOperPower, SSDBlockList);
		}
		
		//Initialize First Level Cache Disk End
		System.out.println("========>>>>Initialize SSD Disk Success!!!<<<<========\n");
				
		
		//Initialize Second Level Cache Disk
		HashMap<Integer, BlockInfo> cacheBlockList = new HashMap<Integer, BlockInfo>();
		for(int i=cacheDiskStartId; i<cacheDiskStartId+cacheAmount; i++){
			cacheDisks[i-cacheDiskStartId] = new DiskInfo(i, 1, 0, diskSize, diskSize, 0, 0, diskOperPower, cacheBlockList);
		}
		
		//Initialize First Level Cache Disk End
		System.out.println("========>>>>Initialize HDD Disks Success!!!<<<<========\n");
				
		
		//Initialize Environment End
		System.out.println("========>>>>Initialize Environment Success!!!<<<<========\n");		
		
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
