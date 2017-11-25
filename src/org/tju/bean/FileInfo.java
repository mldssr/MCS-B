package org.tju.bean;

public class FileInfo {
	
	private int fileId;
	private String fileName;			// fileName: diskId-blockId-skyzone-observeTime
	private int size;
	private int observeTime;
	private int skyZone;
	private int blockId;
	private int isHit;                  // (0,1)is(Miss,Hit), not used
	private int requestNum;				// Not used
	private double priority;			// Not used
	
	
	
	/**
	 * @param fileId
	 * @param fileName
	 * @param size
	 * @param blockId
	 */
	public FileInfo(int fileId, String fileName, int size, int blockId) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.size = size;
		this.blockId = blockId;
	}


	/**
	 * @param fileId
	 * @param fileName
	 * @param size
	 * @param observeTime
	 * @param skyZone
	 * @param blockId
	 * @param isHit
	 * @param requestNum
	 * @param priority
	 */
	public FileInfo(int fileId, String fileName, int size, int observeTime,
			int skyZone, int blockId, int isHit, int requestNum, double priority) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.size = size;
		this.observeTime = observeTime;
		this.skyZone = skyZone;
		this.blockId = blockId;
		this.isHit = isHit;
		this.requestNum = requestNum;
		this.priority = priority;
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
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}


	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}


	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}


	/**
	 * @return the observeTime
	 */
	public int getObserveTime() {
		return observeTime;
	}


	/**
	 * @param observeTime the observeTime to set
	 */
	public void setObserveTime(int observeTime) {
		this.observeTime = observeTime;
	}


	/**
	 * @return the skyZone
	 */
	public int getSkyZone() {
		return skyZone;
	}


	/**
	 * @param skyZone the skyZone to set
	 */
	public void setSkyZone(int skyZone) {
		this.skyZone = skyZone;
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
	 * @return the isHit
	 */
	public int getIsHit() {
		return isHit;
	}


	/**
	 * @param isHit the isHit to set
	 */
	public void setIsHit(int isHit) {
		this.isHit = isHit;
	}


	/**
	 * @return the requestNum
	 */
	public int getRequestNum() {
		return requestNum;
	}


	/**
	 * @param requestNum the requestNum to set
	 */
	public void setRequestNum(int requestNum) {
		this.requestNum = requestNum;
	}


	/**
	 * @return the priority
	 */
	public double getPriority() {
		return priority;
	}


	/**
	 * @param priority the priority to set
	 */
	public void setPriority(double priority) {
		this.priority = priority;
	}
	
	
}
