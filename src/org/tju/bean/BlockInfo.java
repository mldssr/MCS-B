package org.tju.bean;

import java.util.HashMap;

public class BlockInfo {
	
	private int blockId;
	private int totalSpace;
	private int leftSpace;
	private int observeTime;
	private int skyZone;
	private int diskId;
	private int isHit;					// (0,1)is(Miss,Hit), not used
	private int requestNum;				// Need up-to-date
	private double priority;			// Need up-to-date
	private int idleTime;				// Need up-to-date
	private int transmissionTime;		// Need up-to-date && if disk is off += diskOpenTime
	
	private int fileAmount;				// The amount of file in this block
	private HashMap<String, FileInfo> filesList;	//Stored Files' List in this block
	
	
	
	/**
	 * @param blockId
	 * @param observeTime
	 * @param skyZone
	 * @param diskId
	 * @param fileAmount
	 * @param filesList
	 */
	public BlockInfo(int blockId, int observeTime, int skyZone, int diskId,
			int transmissionTime, int fileAmount, HashMap<String, FileInfo> filesList) {
		super();
		this.blockId = blockId;
		this.observeTime = observeTime;
		this.skyZone = skyZone;
		this.diskId = diskId;
		this.transmissionTime = transmissionTime;
		this.fileAmount = fileAmount;
		this.filesList = filesList;
	}


	/**
	 * @param blockId
	 * @param totalSpace
	 * @param leftSpace
	 * @param observeTime
	 * @param skyZone
	 * @param diskId
	 * @param isHit
	 * @param requestNum
	 * @param priority
	 * @param idleTime
	 * @param fileAmount
	 * @param filesList
	 */
	public BlockInfo(int blockId, int totalSpace, int leftSpace,
			int observeTime, int skyZone, int diskId, int isHit,
			int requestNum, double priority, int idleTime, int transmissionTime,
			int fileAmount, HashMap<String, FileInfo> filesList) {
		super();
		this.blockId = blockId;
		this.totalSpace = totalSpace;
		this.leftSpace = leftSpace;
		this.observeTime = observeTime;
		this.skyZone = skyZone;
		this.diskId = diskId;
		this.isHit = isHit;
		this.requestNum = requestNum;
		this.priority = priority;
		this.idleTime = idleTime;
		this.transmissionTime = transmissionTime;
		this.fileAmount = fileAmount;
		this.filesList = filesList;
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
	 * @return the totalSpace
	 */
	public int getTotalSpace() {
		return totalSpace;
	}


	/**
	 * @param totalSpace the totalSpace to set
	 */
	public void setTotalSpace(int totalSpace) {
		this.totalSpace = totalSpace;
	}


	/**
	 * @return the leftSpace
	 */
	public int getLeftSpace() {
		return leftSpace;
	}


	/**
	 * @param leftSpace the leftSpace to set
	 */
	public void setLeftSpace(int leftSpace) {
		this.leftSpace = leftSpace;
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


	/**
	 * @return the idleTime
	 */
	public int getIdleTime() {
		return idleTime;
	}


	/**
	 * @param idleTime the idleTime to set
	 */
	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}


	/**
	 * @return the transmissionTime
	 */
	public int getTransmissionTime() {
		return transmissionTime;
	}


	/**
	 * @param transmissionTime the transmissionTime to set
	 */
	public void setTransmissionTime(int transmissionTime) {
		this.transmissionTime = transmissionTime;
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
	 * @return the filesList
	 */
	public HashMap<String, FileInfo> getFilesList() {
		return filesList;
	}


	/**
	 * @param filesList the filesList to set
	 */
	public void setFilesList(HashMap<String, FileInfo> filesList) {
		this.filesList = filesList;
	}
	

}
