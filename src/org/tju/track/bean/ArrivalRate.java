package org.tju.track.bean;

/**
 * @author yuan
 *
 * @date 2015年12月17日 下午3:02:24
 */
public class ArrivalRate {
	
	private int windowNum;
	private double arrivalRate;
	
	
	/**
	 * @param windowNum
	 * @param arrivalRate
	 */
	public ArrivalRate(int windowNum, double arrivalRate) {
		super();
		this.windowNum = windowNum;
		this.arrivalRate = arrivalRate;
	}


	/**
	 * @return the windowNum
	 */
	public int getWindowNum() {
		return windowNum;
	}


	/**
	 * @param windowNum the windowNum to set
	 */
	public void setWindowNum(int windowNum) {
		this.windowNum = windowNum;
	}


	/**
	 * @return the arrivalRate
	 */
	public double getArrivalRate() {
		return arrivalRate;
	}


	/**
	 * @param arrivalRate the arrivalRate to set
	 */
	public void setArrivalRate(double arrivalRate) {
		this.arrivalRate = arrivalRate;
	}
	
}
