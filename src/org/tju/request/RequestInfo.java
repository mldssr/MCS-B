package org.tju.request;

import java.util.Date;

/**
 * Name: RequestInfo
 * Description: Requests' basic Information
 * 
 * @author yuan
 *
 * @date 2015年12月11日 下午6:08:20
 */
public class RequestInfo {
	
	private String requestFileName;
	private Date generateTime;
	private Date responseTime;
	private double qos;
	
	
	
	/**
	 * @param requestId
	 * @param requestFileName
	 * @param generateTime
	 * @param responseTime
	 * @param qos
	 */
	public RequestInfo(String requestFileName,
			Date generateTime, Date responseTime, double qos) {
		super();
		this.requestFileName = requestFileName;
		this.generateTime = generateTime;
		this.responseTime = responseTime;
		this.qos = qos;
	}



	/**
	 * @return the requestFileName
	 */
	public String getRequestFileName() {
		return requestFileName;
	}


	/**
	 * @param requestFileName the requestFileName to set
	 */
	public void setRequestFileName(String requestFileName) {
		this.requestFileName = requestFileName;
	}


	/**
	 * @return the generateTime
	 */
	public Date getGenerateTime() {
		return generateTime;
	}


	/**
	 * @param generateTime the generateTime to set
	 */
	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}


	/**
	 * @return the responseTime
	 */
	public Date getResponseTime() {
		return responseTime;
	}


	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}


	/**
	 * @return the qos
	 */
	public double getQos() {
		return qos;
	}


	/**
	 * @param qos the qos to set
	 */
	public void setQos(double qos) {
		this.qos = qos;
	}
	
}
