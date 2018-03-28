package org.tju.request;

public class RequestInfo {
	
	private String requestFileName;
	private int generateTime;
	private int responseTime;
	private int qos;
	
	
	
	/**
	 * @param requestId
	 * @param requestFileName
	 * @param generateTime
	 * @param responseTime
	 * @param qos
	 */
	public RequestInfo(String requestFileName,
			int generateTime, int responseTime, int qos) {
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
	public int getGenerateTime() {
		return generateTime;
	}


	/**
	 * @param generateTime the generateTime to set
	 */
	public void setGenerateTime(int generateTime) {
		this.generateTime = generateTime;
	}


	/**
	 * @return the responseTime
	 */
	public int getResponseTime() {
		return responseTime;
	}


	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}


	/**
	 * @return the qos
	 */
	public int getQos() {
		return qos;
	}


	/**
	 * @param qos the qos to set
	 */
	public void setQos(int qos) {
		this.qos = qos;
	}
	
}
