package org.tju.scheduler;

//import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.text.DecimalFormat;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;

public class CacheCleaner {
		
	/**
	 * Clear Cache contains cache disks && SSD
	 * 将指定磁盘组中priority的绝对值低于阀值的block清除，Refresh时使用
	 * @param disks
	 * @param lowPriorityTh
	 */
	public static void clearCache(DiskInfo[] disks, double lowPriorityTh){
		
		for(int i=0; i<disks.length; i++){
			clearBlockList(disks[i], lowPriorityTh);
		}
		
	}
	
	
	/**
	 * Clear the Block List
	 * 将指定磁盘中priority的绝对值低于阀值的block清除
	 * @param disk
	 * @param lowPriorityTh
	 */
	public static void clearBlockList(DiskInfo disk, double lowPriorityTh){
		
	//	HashMap<Integer, BlockInfo> lowPriorityBlock = new HashMap<Integer, BlockInfo>();
		
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000000");//格式化设置
		System.out.println("[CLEAN] Clean disk " + disk.getDiskId() + " lowPriorityTh: " + decimalFormat.format(lowPriorityTh));
		
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
			int requestNum = disk.getBlockList().get(lowPriorityBlockId.get(i)).getRequestNum();
			int idleTime = disk.getBlockList().get(lowPriorityBlockId.get(i)).getIdleTime();
			System.out.println("[CLEAN] Remove a block with requestNum: " + requestNum + " idleTime: " + idleTime);
			disk.getBlockList().remove(lowPriorityBlockId.get(i));
		}
		System.out.println("[CLEAN] Remove " + lowPriorityBlockId.size() + " blocks from disk " + disk.getDiskId());
		
		disk.setBlockAmount(disk.getBlockAmount()-lowPriorityBlockId.size());
		
	}

}
