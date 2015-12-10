package org.tju.bean;

import java.util.HashMap;

/**
 * Name: DiskInfo
 * Description: Disks' basic Information
 * 
 * @author yuan
 *
 * @date 2015年12月10日 上午10:46:16
 */
public class DiskInfo {
	
	private int diskId;
	private int diskType;            //(0,1,2)is(First Level Cache,Second Level Cache, Data Disks)
	private int diskState;           //(0,1)is(Down,Up)
	private double totalSpace;
	private double leftSpace;
	private int blockAmount;
	private int idleTime;
	
	private double operPower;        //Operational Power
	
	private HashMap<Integer, BlockInfo> blocksList;		//Stored Blocks' List

	
	
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
	public DiskInfo(int diskId, int diskType, int diskState, double totalSpace,
			double leftSpace, int blockAmount, int idleTime, double operPower,
			HashMap<Integer, BlockInfo> blocksList) {
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
	public double getTotalSpace() {
		return totalSpace;
	}


	/**
	 * @param totalSpace the totalSpace to set
	 */
	public void setTotalSpace(double totalSpace) {
		this.totalSpace = totalSpace;
	}


	/**
	 * @return the leftSpace
	 */
	public double getLeftSpace() {
		return leftSpace;
	}


	/**
	 * @param leftSpace the leftSpace to set
	 */
	public void setLeftSpace(double leftSpace) {
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
	
	
}
