package org.tju.simulation;

import org.tju.bean.DiskInfo;
import org.tju.scheduler.DiskOperation;
import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月17日 下午1:11:33
 */
public class PreheatDisk {
	
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
		
	//get mode: single(light, normal) || mix
	public static String mode = valueOfConfigureFile.getMode();
	
	//get All Threshold from Threshold.xml
	public static int SSDSizeTh = valueOfConfigureFile.getSSDSizeTh();
	public static int cacheSizeTh = valueOfConfigureFile.getCacheSizeTh();
	public static double arrivalRateTh = valueOfConfigureFile.getArrivalRateTh();
	public static double lowPriorityTh = valueOfConfigureFile.getLowPriorityTh();
	public static double highPriorityTh = valueOfConfigureFile.getHighPriorityTh();
	public static int idleTimeTh = valueOfConfigureFile.getIdleTimeTh();
	public static int refreshTh = valueOfConfigureFile.getRefreshTh();
	
	//get disks' capacity from DiskCapacity.xml 
	public static int blockInSSD = valueOfConfigureFile.getBlockInSSD();
	public static int blockInCache = valueOfConfigureFile.getBlockInCache();
	
	
	//Preheat the cache disks
	public static void PreheatCaches(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks, double arrivalRate){
		
		if(arrivalRate > arrivalRateTh){
			//preheat SSD
			int i=0;
			for( ; i<SSDDisks.length-1; i++){
				if(SSDDisks[i].getLeftSpace()<SSDSizeTh && SSDDisks[i+1].getDiskState()==0){
					PreheatCache(SSDDisks[i+1]);
					return ;
				}		
			}
			
			if(i == SSDDisks.length-1){
				for(int j=0; j<cacheDisks.length-1; j++){
					if(cacheDisks[i].getDiskState() == 0){
						PreheatCache(cacheDisks[i]);
					} else if(cacheDisks[i].getLeftSpace()<cacheSizeTh && cacheDisks[j+1].getDiskState()==0){
						PreheatCache(cacheDisks[i+1]);
						return ;
					}
				}
			}
			
		}
		
	}
	
	
	//Preheat cache disk
	public static void PreheatCache(DiskInfo disk){
		
		DiskOperation.openCache(disk);
		
	}

}
