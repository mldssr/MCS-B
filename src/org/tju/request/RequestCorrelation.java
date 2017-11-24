package org.tju.request;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class RequestCorrelation {
	/**
	 * correlationNum indicates the max number of correlate blocks a block could have;<br>
	 * If overflow, the new one will replace the oldest one.
	 */
	private static int correlationNum = 5;
	
	/***
	 * key: blokcId
	 * value: an array of blockId of correlate blocks;<br>
	 *        First correlationNum(5) elements stores correlate blockIds;<br>
	 *        Last element stores the next position to write at.
	 */
	private static HashMap<Integer, Integer[]> correlations = new HashMap<Integer, Integer[]>();
	
	
	
	public static void put(Integer keyBlockId, Integer valueBlockId) {
		if (keyBlockId != valueBlockId) {
			if (correlations.containsKey(keyBlockId)) {
				Integer[] corr = correlations.get(keyBlockId);
				int index = corr[correlationNum];
				corr[index] = valueBlockId;
				corr[correlationNum] = (index + 1) % correlationNum;
			} else {
				Integer[] corr = new Integer[correlationNum + 1];
				corr[0] = valueBlockId;
				corr[correlationNum] = 1;// Store next pos
				correlations.put(keyBlockId, corr);
			}
		}
	}
	
	public static boolean hasKey(int key) {
		return correlations.containsKey(key);
	}
	
	public static Integer[] getCorr(Integer keyBlockId) {
		return correlations.get(keyBlockId);
	}

	public static void updateCorr(HashMap<String, RequestInfo> requestsPerWindow) {
		int size = requestsPerWindow.size();
		int[] blockIds = new int[size];
		int[] skyzones = new int[size];
		int[] observeTime = new int[size];

		Iterator<Entry<String, RequestInfo>> iter = requestsPerWindow.entrySet().iterator();
		int index = 0;

		while (iter.hasNext()) {
			Entry<String, RequestInfo> entry = iter.next();
			RequestInfo request = entry.getValue();

			String fileName = request.getRequestFileName();
			// diskID-blockId-skyzone-observeTime
			String[] names = fileName.split("-");
			blockIds[index] = Integer.valueOf(names[1]);
			skyzones[index] = Integer.valueOf(names[2]);
			observeTime[index] = Integer.valueOf(names[3]);
			index ++;
		}
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i != j && skyzones[i] == skyzones[j] && java.lang.Math.abs(observeTime[i] - observeTime[j]) < 4) {
//					System.out.println("skyzone: " + skyzone + "  blockId: " + blockIds[i] + "  " + blockIds[j]);
					put(blockIds[i], blockIds[j]);
				}
			}
		}
		
//		// Find the skyzone of correlate requests (num > 1)
//		int skyzone = -1;
//		boolean find = false;
//		for(index = 0; index < size; index++) {
//			skyzone = skyzones[index];
//			for(int j = index + 1; j < size; j++) {
//				if(skyzone == skyzones[j]) {
//					find = true;
//					break;
//				}
//			}
//			if (find) {
//				break;
//			}
//		}
//		// Get a correlate request already: index---index, skyzone---skyzone
//		
//		
//		// Record correlate requests
//		index = 0;
//		for (int i = 0; i < size; i++) {
//			if (skyzones[i] == skyzone) {
//				for (int j = 0; j < size; j++) {
//					if (skyzones[j] == skyzone && i != j) {
//						System.out.println("skyzone: " +skyzone + "  blockId: " + blockIds[i] + "  " + blockIds[j]);
//						put(blockIds[i], blockIds[j]);
//					}
//				}
//			}
//		}
	}

	public static void display() {
		System.out.println("==========>>> Display <<<==========");
		System.out.println("correlationNum: " + correlationNum);
		Iterator<Entry<Integer, Integer[]>> iter = correlations.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Integer, Integer[]> entry = iter.next();
			System.out.print("Key: " + entry.getKey() + "      ");
			Integer[] corr = entry.getValue();
			System.out.print("Value: ");
			for (int i = 0; i < correlationNum; i++) {
				System.out.print(corr[i] + "  ");
			}
			System.out.println("");
		}
		System.out.println("==========>>> Display <<<==========");
	}
	
	public static int getCorrelationNum() {
		return correlationNum;
	}
	
	public static void setCorrelationNum(int correlationNum) {
		RequestCorrelation.correlationNum = correlationNum;
	}
}
