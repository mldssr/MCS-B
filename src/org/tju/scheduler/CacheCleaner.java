package org.tju.scheduler;

//import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;

public class CacheCleaner {
	
	
	//Clear Cache contains cache disks && SSD，将指定磁盘组中priority的绝对值低于阀值的block清除
	public static void clearCache(DiskInfo[] disks, double lowPriorityTh){
		
		for(int i=0; i<disks.length; i++){
			clearBlockList(disks[i], lowPriorityTh);
		}
		
	}
	
	
	//Clear the Block List，将指定磁盘中priority的绝对值低于阀值的block清除
	public static void clearBlockList(DiskInfo disk, double lowPriorityTh){
		
	//	HashMap<Integer, BlockInfo> lowPriorityBlock = new HashMap<Integer, BlockInfo>();
		
		List<Integer> lowPriorityBlockId = new LinkedList<Integer>();
		
		Iterator<Entry<Integer, BlockInfo>> iter = disk.getBlockList().entrySet().iterator();
		
		while (iter.hasNext()){
			Entry<Integer, BlockInfo> entry = iter.next();
			BlockInfo block = entry.getValue();
			
			if(block == null){
				continue ;
			}

			if(Math.abs(block.getPriority()) < lowPriorityTh){
	//			lowPriorityBlock.put(block.getBlockId(), block);
				lowPriorityBlockId.add(block.getBlockId());
			}		
		}
		
		for(int i=0; i<lowPriorityBlockId.size(); i++){
			disk.getBlockList().remove(lowPriorityBlockId.get(i));
		}
		System.out.println("Remove " + lowPriorityBlockId.size() + "blocks of disk " + disk.getDiskId());
		
		disk.setBlockAmount(disk.getBlockAmount()-lowPriorityBlockId.size());
		
	}

}
