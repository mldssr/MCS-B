package org.tju.track;

/**
 * @author yuan
 *
 * @date 2015年12月13日 上午10:30:54
 */
public class BlockExchange {
	
	private int time;
	private int data2SSD;
	private int data2Cache;
	private int cache2SSD;
	private int cache2Cache;
	private int total;



	/**
	 * @param time
	 * @param data2ssd
	 * @param data2Cache
	 * @param cache2ssd
	 * @param cache2Cache
	 * @param total
	 */
	public BlockExchange(int time, int data2ssd, int data2Cache, int cache2ssd,
			int cache2Cache, int total) {
		super();
		this.time = time;
		data2SSD = data2ssd;
		this.data2Cache = data2Cache;
		cache2SSD = cache2ssd;
		this.cache2Cache = cache2Cache;
		this.total = total;
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
	 * @return the data2SSD
	 */
	public int getData2SSD() {
		return data2SSD;
	}


	/**
	 * @param data2ssd the data2SSD to set
	 */
	public void setData2SSD(int data2ssd) {
		data2SSD = data2ssd;
	}


	/**
	 * @return the data2Cache
	 */
	public int getData2Cache() {
		return data2Cache;
	}


	/**
	 * @param data2Cache the data2Cache to set
	 */
	public void setData2Cache(int data2Cache) {
		this.data2Cache = data2Cache;
	}


	/**
	 * @return the cache2SSD
	 */
	public int getCache2SSD() {
		return cache2SSD;
	}


	/**
	 * @param cache2ssd the cache2SSD to set
	 */
	public void setCache2SSD(int cache2ssd) {
		cache2SSD = cache2ssd;
	}


	/**
	 * @return the cache2Cache
	 */
	public int getCache2Cache() {
		return cache2Cache;
	}


	/**
	 * @param cache2Cache the cache2Cache to set
	 */
	public void setCache2Cache(int cache2Cache) {
		this.cache2Cache = cache2Cache;
	}


	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}


	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	
}
