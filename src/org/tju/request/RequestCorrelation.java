package org.tju.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tju.initialization.InitEnvByTime;

/**
 * @author Lxx
 *
 */
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
	
	
	
	public static void putIntoCorr(Integer keyBlockId, Integer valueBlockId) {
//		System.out.println("[RECOR] Going to put in ( " + keyBlockId + " " + valueBlockId + " )");
		// NOTE: not keyBlockId != valueBlockId
		if (keyBlockId.intValue() != valueBlockId.intValue()) {
			if (correlations.containsKey(keyBlockId)) {
				Integer[] corr = correlations.get(keyBlockId);
				for (int k = 0; k < correlationNum; k++) {
//					System.out.println("[RECOR] k " + k + " corr[k] " + corr[k]);
					if (corr[k] == null) {
//						System.out.println("[RECOR] Continue!");
						continue;
					}
					if (corr[k] != null && corr[k].intValue() == valueBlockId.intValue()) {
//						System.out.println("[RECOR] Skip for already exists! corr[k] == valueBlockId " + corr[k]);
						return;
					}
				}
				int index = corr[correlationNum];
				corr[index] = valueBlockId;
//				System.out.println("[RECOR] Add " + keyBlockId +" " + valueBlockId);
				corr[correlationNum] = (index + 1) % correlationNum;
			} else {
//				System.out.println("[RECOR] New entry!");
				Integer[] corr = new Integer[correlationNum + 1];
				corr[0] = valueBlockId;
				corr[correlationNum] = 1;// Store next pos
				correlations.put(keyBlockId, corr);
			}
		}
//		else {
//			System.out.println("[RECOR] Skip for no sense!");
//		}
	}
	
	public static boolean hasKey(int key) {
		return correlations.containsKey(key);
	}
	
	public static Integer[] getCorr(Integer keyBlockId) {
		return correlations.get(keyBlockId);
	}

	public static void updateCorr(HashMap<String, RequestInfo> requests) {
		int size = requests.size();
		int[] blockIds = new int[size];
		int[] skyzones = new int[size];
		int[] observeTime = new int[size];

		Iterator<Entry<String, RequestInfo>> iter = requests.entrySet().iterator();
		int index = 0;
		while (iter.hasNext()) {
			Entry<String, RequestInfo> entry = iter.next();
			RequestInfo request = entry.getValue();

			String fileName = request.getRequestFileName();
			// (diskID-blockId-) skyzone-observeTime
			String[] names = fileName.split("-");
			skyzones[index] = Integer.parseInt(names[0]);
			observeTime[index] = Integer.parseInt(names[1]);
			blockIds[index] = InitEnvByTime.fileName_BlockId.get(fileName);
			index ++;
		}
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i != j && skyzones[i] == skyzones[j] && java.lang.Math.abs(observeTime[i] - observeTime[j]) < 4) {
//					System.out.println("skyzone: " + skyzone + "  blockId: " + blockIds[i] + "  " + blockIds[j]);
					putIntoCorr(Integer.valueOf(blockIds[i]), Integer.valueOf(blockIds[j]));
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
		
		List<Entry<Integer, Integer[]>> list = new ArrayList<Entry<Integer, Integer[]>>(correlations.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer[]>>() {
			public int compare(Map.Entry<Integer, Integer[]> e1, Map.Entry<Integer, Integer[]> e2) {
				int v1 = ((Map.Entry<Integer, Integer[]>) e1).getKey().intValue();
				int v2 = ((Map.Entry<Integer, Integer[]>) e2).getKey().intValue();
				int flag = v1 - v2;
				if (flag > 0) {
					return 1;
				} else if (flag == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});
		
		for (int i = 0; i < list.size(); i++) {
			Entry<Integer, Integer[]> entry = list.get(i);
			System.out.print("Key: " + entry.getKey() + "      ");
			Integer[] corr = entry.getValue();
			System.out.print("Value: ");
			for (int j = 0; j < correlationNum; j++) {
				System.out.print(corr[j] + "  ");
			}
			System.out.println("");
		}
	}
	
	public static int getCorrelationNum() {
		return correlationNum;
	}
	
	public static void setCorrelationNum(int correlationNum) {
		RequestCorrelation.correlationNum = correlationNum;
	}
}
