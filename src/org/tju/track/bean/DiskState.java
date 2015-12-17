package org.tju.track.bean;

/**
 * @author yuan
 *
 * @date 2015年12月17日 下午4:29:53
 */
public class DiskState {
	
	private int time;
	private String content;
	
	
	/**
	 * @param time
	 * @param content
	 */
	public DiskState(int time, String content) {
		super();
		this.time = time;
		this.content = content;
	}


	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}


	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}


	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}


	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
}
