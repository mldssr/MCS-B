package org.tju.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class DiskInfo {
	
	private int diskId;
	private int diskType;			//(0,1,2)is(First Level Cache,Second Level Cache, Data Disks)
	private int diskState;			//(0,1)is(Down,Up)
	private int totalSpace;
	private int leftSpace;			// Need up-to-date, In MB
	private int blockAmount;		// Need up-to-date
	private int idleTime;			// Need up-to-date
	private int requestNum;			// Need up-to-date
	
	private double operPower;       //Operational Power
	
	// add
	private int startedTime;
	private int startedCount;
	
	private HashMap<Integer, BlockInfo> blocksList;		// Store Blocks List, <blockId, BlockInfo>

	/**
	 * @param diskId
	 * @param diskType
	 * @param diskState
	 */
	public DiskInfo(int diskId, int diskType, int diskState) {
		super();
		this.diskId = diskId;
		this.diskType = diskType;
		this.diskState = diskState;
	}


	
	/**
	 * @param diskId
	 * @param diskType
	 * @param diskState
	 * @param totalSpace
	 * @param leftSpace
	 * @param blockAmount
	 * @param idleTime
	 * @param operPower
	 * @param blocksList
	 */
	public DiskInfo(int diskId, int diskType, int diskState, int totalSpace,
			int leftSpace, int blockAmount, int idleTime, int requestNum,
			double operPower, HashMap<Integer, BlockInfo> blocksList) {
		super();
		this.diskId = diskId;
		this.diskType = diskType;
		this.diskState = diskState;
		this.totalSpace = totalSpace;
		this.leftSpace = leftSpace;
		this.blockAmount = blockAmount;
		this.idleTime = idleTime;
		this.operPower = operPower;
		this.blocksList = blocksList;
	}

	/**
	 * @param diskId
	 * @param diskType
	 * @param diskState
	 * @param totalSpace
	 * @param leftSpace
	 * @param blockAmount
	 * @param idleTime
	 * @param operPower
	 * @param blocksList
	 * @param startedTime
	 * @param startedCount
	 */
	public DiskInfo(int diskId, int diskType, int diskState, int totalSpace,
			int leftSpace, int blockAmount, int idleTime, int requestNum,
			double operPower, HashMap<Integer, BlockInfo> blocksList, int startedTime, int startedCount) {
		super();
		this.diskId = diskId;
		this.diskType = diskType;
		this.diskState = diskState;
		this.totalSpace = totalSpace;
		this.leftSpace = leftSpace;
		this.blockAmount = blockAmount;
		this.idleTime = idleTime;
		this.operPower = operPower;
		this.blocksList = blocksList;
		this.startedTime = startedTime;
		this.startedCount = startedCount;
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
	 * @return the diskType
	 */
	public int getDiskType() {
		return diskType;
	}


	/**
	 * @param diskType the diskType to set
	 */
	public void setDiskType(int diskType) {
		this.diskType = diskType;
	}


	/**
	 * @return the diskState
	 */
	public int getDiskState() {
		return diskState;
	}


	/**
	 * @param diskState the diskState to set
	 */
	public void setDiskState(int diskState) {
		this.diskState = diskState;
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
	 * @return the operPower
	 */
	public double getOperPower() {
		return operPower;
	}


	/**
	 * @param operPower the operPower to set
	 */
	public void setOperPower(double operPower) {
		this.operPower = operPower;
	}


	/**
	 * @return the blocksList
	 */
	public HashMap<Integer, BlockInfo> getBlockList() {
		return blocksList;
	}


	/**
	 * @param blocksList the blocksList to set
	 */
	public void setBlockList(HashMap<Integer, BlockInfo> blocksList) {
		this.blocksList = blocksList;
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



	public int getStartedTime() {
		return startedTime;
	}



	public void setStartedTime(int startedTime) {
		this.startedTime = startedTime;
	}



	public int getStartedCount() {
		return startedCount;
	}



	public void setStartedCount(int startedCount) {
		this.startedCount = startedCount;
	}
	
	
}
