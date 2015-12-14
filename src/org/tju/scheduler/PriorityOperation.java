package org.tju.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;

/**
 * @author yuan
 *
 * @date 2015年12月14日 上午11:18:59
 */
public class PriorityOperation {
	
	
	//Sorted By Blocks' Priority
	public static void sortedByPriority(HashMap<Integer, BlockInfo> blockInfo){
		
		List<Entry<Integer, BlockInfo>> list = new ArrayList<Entry<Integer,BlockInfo>>(blockInfo.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<Integer, BlockInfo>>(){
			                       public int compare(Map.Entry<Integer,BlockInfo> e1, Map.Entry<Integer,BlockInfo> e2){         	   
			                    	   double v1 = (double) ((Map.Entry<Integer,BlockInfo>)e1).getValue().getPriority(); 
			                    	   double v2 = (double) ((Map.Entry<Integer,BlockInfo>)e2).getValue().getPriority(); 
			                    	   double flag = v2-v1;
			                    	   if(flag > 0.0){
			                    		   return 1;
			                    	   }else if(flag == 0){
										return 0;
									   }else {
										return -1;
									}
			                         }
			                       }); 
		
		blockInfo.clear();
		
		for(int i=0; i<list.size(); i++){
			blockInfo.put(list.get(i).getKey(), list.get(i).getValue());
		}
		
	}
	
	
	//Priority Calculation: Calculate blocks' priority in cache disks
	public static void calculatePriority(DiskInfo cacheDisks){
		
		Iterator<Entry<Integer, BlockInfo>> iter = cacheDisks.getBlockList().entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<Integer, BlockInfo> entry = iter.next();
			
			BlockInfo block = entry.getValue();
			
			//P = requestNum/idleTime
			block.setPriority(((double)block.getRequestNum())/(block.getIdleTime()));
			
			cacheDisks.getBlockList().put(entry.getKey(), block);			
		}
		
	}

}
