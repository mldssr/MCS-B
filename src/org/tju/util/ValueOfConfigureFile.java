package org.tju.util;

/**
 * @author yuan
 *
 * @date 2015年12月11日 下午7:45:08
 */
public class ValueOfConfigureFile {
	
	//get blocks' info from BlockAmount.xml
	public int blockAmount = Integer.valueOf(ReadXml.readname("config/BlockAmount.xml", "amount"));
	public int blockSize = Integer.valueOf(ReadXml.readname("config/BlockAmount.xml", "size"));
	public int fileInBlock = Integer.valueOf(ReadXml.readname("config/BlockAmount.xml", "fileamount"));
	
	//get disks' amount from DiskAmount.xml
	public int SSDAmount = Integer.valueOf(ReadXml.readname("config/DiskAmount.xml", "ssd"));
	public int cacheAmount = Integer.valueOf(ReadXml.readname("config/DiskAmount.xml", "cachedisk"));
	public int dataDiskAmount = Integer.valueOf(ReadXml.readname("config/DiskAmount.xml", "datadisk"));
	
	//get disks' capacity from DiskCapacity.xml
	public int blockInDisk = Integer.valueOf(ReadXml.readname("config/DiskCapacity.xml", "blockamount"));
	public int skyzoneInDisk = Integer.valueOf(ReadXml.readname("config/DiskCapacity.xml", "skyzone"));
	
	//the disks' starting ID from DiskNum.xml
	public int dataDiskStartId = Integer.valueOf(ReadXml.readname("config/DiskNum.xml", "datadisk"));
	public int cacheDiskStartId = Integer.valueOf(ReadXml.readname("config/DiskNum.xml", "cache"));
	public int SSDDiskStartId = Integer.valueOf(ReadXml.readname("config/DiskNum.xml", "ssd"));
	
	//get duplicate amount from Duplicate.xml
	public int duplicateAmount = Integer.valueOf(ReadXml.readname("config/Duplicate.xml", "amount"));
	
	//get files' amount from FileAmount.xml
	public int fileAmount = Integer.valueOf(ReadXml.readname("config/FileAmount.xml", "amount"));
	
	//get files' info from FileInfo.xml
	public int fileBasicSize = Integer.valueOf(ReadXml.readname("config/FileInfo.xml", "basic"));
	public int fileSizeErr = Integer.valueOf(ReadXml.readname("config/FileInfo.xml", "err"));
	
	//get HDD disks' parameters from HDDDisk.xml
	public int diskSize = Integer.valueOf(ReadXml.readname("config/HDDDisk.xml", "size"));
	public double diskOperPower = Double.valueOf(ReadXml.readname("config/HDDDisk.xml", "operpower"));
	
	//get observe info (skyzone and time) from ObserveInfo.xml
	public int timeAmount = Integer.valueOf(ReadXml.readname("config/ObserveInfo.xml", "time"));
	public int skyzoneAmount = Integer.valueOf(ReadXml.readname("config/ObserveInfo.xml", "skyzone"));
	
	//get refresh time from RefreshTime.xml
	public int refreshTime = Integer.valueOf(ReadXml.readname("config/RefreshTime.xml", "refreshtime"));

	//get request information from RequestInfo.xml 
	//get sliding window size
	public int slidingWindowSize = Integer.valueOf(ReadXml.readname("config/RequestInfo.xml", "slidingwindow"));
	
	//get mode: single(light, normal) || mixing
	public String mode = ReadXml.readname("config/RequestInfo.xml", "mode");
	
	//get requests' amount
	public int requestAmount = Integer.valueOf(ReadXml.readname("config/RequestInfo.xml", "amount"));

	//get requests' arrival rate & err
	public int lightRequestArrivalRate = Integer.valueOf(ReadXml.readname("config/RequestInfo.xml", "light"));
	public int normalRequestArrivalRate = Integer.valueOf(ReadXml.readname("config/RequestInfo.xml", "normal"));
	
	public int err = Integer.valueOf(ReadXml.readname("config/RequestInfo.xml", "err"));
	
	//get requests' generate rule(time, space, mix & random)
	public String requestGenerateRule = ReadXml.readname("config/RequestInfo.xml", "rule");
	
	//get requests' correlation size
	public int requestCorrelation = Integer.valueOf(ReadXml.readname("config/RequestInfo.xml", "correlation"));
	
	//get window NO.
	public int windowNum = Integer.valueOf(ReadXml.readname("config/RequestInfo.xml", "windownum"));

	
	//get SSD disks' parameters from SSDDisk.xml
	public int SSDSize = Integer.valueOf(ReadXml.readname("config/SSDDisk.xml", "size"));
	public double SSDOperPower = Double.valueOf(ReadXml.readname("config/SSDDisk.xml", "operpower"));
	
	
	//get Path info from FilePathInfo.xml
	public String requestFilePath = ReadXml.readname("config/FilePathInfo.xml", "request");
	public String requestStaFilePath = ReadXml.readname("config/FilePathInfo.xml", "requeststa");
	public String blockExchangeFilePath = ReadXml.readname("config/FilePathInfo.xml", "blockexchange");
	public String dataDiskStateFilePath = ReadXml.readname("config/FilePathInfo.xml", "datadiskstate");
	public String cacheDiskStateFilePath = ReadXml.readname("config/FilePathInfo.xml", "cachediskstate");

	
	//get files' labels' Info from FileLableInfo.xml
	public String[] requestGenLables = {ReadXml.readname("config/FileLableInfo.xml", "requestgen1"), 
											ReadXml.readname("config/FileLableInfo.xml", "requestgen2")};
	
	public String[] requestStaLables = {ReadXml.readname("config/FileLableInfo.xml", "requeststa1"), 
											ReadXml.readname("config/FileLableInfo.xml", "requeststa2"),
											ReadXml.readname("config/FileLableInfo.xml", "requeststa3"),
											ReadXml.readname("config/FileLableInfo.xml", "requeststa4")};

	public String[] blockExchangeLables = {ReadXml.readname("config/FileLableInfo.xml", "blockexchange1"),
											ReadXml.readname("config/FileLableInfo.xml", "blockexchange2"),
											ReadXml.readname("config/FileLableInfo.xml", "blockexchange3"),
											ReadXml.readname("config/FileLableInfo.xml", "blockexchange4"),
											ReadXml.readname("config/FileLableInfo.xml", "blockexchange5"),
											ReadXml.readname("config/FileLableInfo.xml", "blockexchange6")};
	
	//get data disk state lables
	public String[] getDataDiskStateLables(){
		
		String[] dataDiskStateLables = new String[dataDiskAmount+1];
		
		dataDiskStateLables[0] = "Time";
		
		for(int i=1; i<=dataDiskAmount; i++){
			dataDiskStateLables[i] = String.valueOf(i-1);
		}	
		
		return dataDiskStateLables;

	}
	
	
	//get cache disk state lables
	public String[] getCacheDiskStateLables(){
		
		String[] cacheDiskStateLables = new String[cacheAmount+1];
		
		cacheDiskStateLables[0] = "Time";
		
		for(int i=1; i<=cacheAmount; i++){
			cacheDiskStateLables[i] = String.valueOf(i-1);
		}	
		
		return cacheDiskStateLables;

	}
	
	
	//get All Threshold from Threshold.xml
	public int SSDSizeTh = Integer.valueOf(ReadXml.readname("config/Threshold.xml", "ssdsizeth"));
	public double arrivalRateTh = Double.valueOf(ReadXml.readname("config/Threshold.xml", "arrivalrateth"));
	public double priorityTh = Double.valueOf(ReadXml.readname("config/Threshold.xml", "priorityth"));
	public double highPriorityTh = Double.valueOf(ReadXml.readname("config/Threshold.xml", "highpriorityth"));
	public int idleTimeTh = Integer.valueOf(ReadXml.readname("config/Threshold.xml", "idletimeth"));	
	


	
	/**
	 * @return the requestFilePath
	 */
	public String getRequestFilePath() {
		return requestFilePath;
	}


	/**
	 * @return the blockAmount
	 */
	public int getBlockAmount() {
		return blockAmount;
	}
	
	
	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}
	
	
	/**
	 * @return the fileInBlock
	 */
	public int getFileInBlock() {
		return fileInBlock;
	}
	
	
	/**
	 * @return the sSDAmount
	 */
	public int getSSDAmount() {
		return SSDAmount;
	}
	
	
	/**
	 * @return the cacheAmount
	 */
	public int getCacheAmount() {
		return cacheAmount;
	}
	
	
	/**
	 * @return the dataDiskAmount
	 */
	public int getDataDiskAmount() {
		return dataDiskAmount;
	}
	
	
	/**
	 * @return the blockInDisk
	 */
	public int getBlockInDisk() {
		return blockInDisk;
	}
	
	
	/**
	 * @return the skyzoneInDisk
	 */
	public int getSkyzoneInDisk() {
		return skyzoneInDisk;
	}
	
	
	/**
	 * @return the dataDiskStartId
	 */
	public int getDataDiskStartId() {
		return dataDiskStartId;
	}
	
	
	/**
	 * @return the cacheDiskStartId
	 */
	public int getCacheDiskStartId() {
		return cacheDiskStartId;
	}
	
	
	/**
	 * @return the sSDDiskStartId
	 */
	public int getSSDDiskStartId() {
		return SSDDiskStartId;
	}
	
	
	/**
	 * @return the duplicateAmount
	 */
	public int getDuplicateAmount() {
		return duplicateAmount;
	}
	
	
	/**
	 * @return the fileAmount
	 */
	public int getFileAmount() {
		return fileAmount;
	}
	
	
	/**
	 * @return the fileBasicSize
	 */
	public int getFileBasicSize() {
		return fileBasicSize;
	}
	
	
	/**
	 * @return the fileSizeErr
	 */
	public int getFileSizeErr() {
		return fileSizeErr;
	}
	
	
	/**
	 * @return the diskSize
	 */
	public int getDiskSize() {
		return diskSize;
	}
	
	
	/**
	 * @return the diskOperPower
	 */
	public double getDiskOperPower() {
		return diskOperPower;
	}
	
	
	/**
	 * @return the timeAmount
	 */
	public int getTimeAmount() {
		return timeAmount;
	}
	
	
	/**
	 * @return the skyzoneAmount
	 */
	public int getSkyzoneAmount() {
		return skyzoneAmount;
	}
	
	
	/**
	 * @return the refreshTime
	 */
	public int getRefreshTime() {
		return refreshTime;
	}
	
	
	/**
	 * @return the slidingWindowSize
	 */
	public int getSlidingWindowSize() {
		return slidingWindowSize;
	}
	
	
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	
	
	/**
	 * @return the requestAmount
	 */
	public int getRequestAmount() {
		return requestAmount;
	}
	
	
	/**
	 * @return the lightRequestArrivalRate
	 */
	public int getLightRequestArrivalRate() {
		return lightRequestArrivalRate;
	}
	
	
	/**
	 * @return the normalRequestArrivalRate
	 */
	public int getNormalRequestArrivalRate() {
		return normalRequestArrivalRate;
	}
	
	
	/**
	 * @return the err
	 */
	public int getErr() {
		return err;
	}
	
	
	/**
	 * @return the requestGenerateRule
	 */
	public String getRequestGenerateRule() {
		return requestGenerateRule;
	}
	
	
	/**
	 * @return the requestCorrelation
	 */
	public int getRequestCorrelation() {
		return requestCorrelation;
	}
	
	
	/**
	 * @return the windowNum
	 */
	public int getWindowNum() {
		return windowNum;
	}


	/**
	 * @return the sSDSize
	 */
	public int getSSDSize() {
		return SSDSize;
	}
	
	
	/**
	 * @return the sSDOperPower
	 */
	public double getSSDOperPower() {
		return SSDOperPower;
	}


	/**
	 * @return the requestStaFilePath
	 */
	public String getRequestStaFilePath() {
		return requestStaFilePath;
	}


	/**
	 * @return the blockExchangeFilePath
	 */
	public String getBlockExchangeFilePath() {
		return blockExchangeFilePath;
	}


	/**
	 * @return the dataDiskStateFilePath
	 */
	public String getDataDiskStateFilePath() {
		return dataDiskStateFilePath;
	}


	/**
	 * @return the cacheDiskStateFilePath
	 */
	public String getCacheDiskStateFilePath() {
		return cacheDiskStateFilePath;
	}


	/**
	 * @return the requestGenLables
	 */
	public String[] getRequestGenLables() {
		return requestGenLables;
	}


	/**
	 * @return the requestStaLables
	 */
	public String[] getRequestStaLables() {
		return requestStaLables;
	}


	/**
	 * @return the blockExchangeLables
	 */
	public String[] getBlockExchangeLables() {
		return blockExchangeLables;
	}


	/**
	 * @return the sSDSizeTh
	 */
	public int getSSDSizeTh() {
		return SSDSizeTh;
	}


	/**
	 * @return the arrivalRateTh
	 */
	public double getArrivalRateTh() {
		return arrivalRateTh;
	}


	/**
	 * @return the priorityTh
	 */
	public double getPriorityTh() {
		return priorityTh;
	}


	/**
	 * @return the highPriorityTh
	 */
	public double getHighPriorityTh() {
		return highPriorityTh;
	}


	/**
	 * @return the idleTimeTh
	 */
	public int getIdleTimeTh() {
		return idleTimeTh;
	}
	
}
