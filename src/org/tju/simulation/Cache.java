package org.tju.simulation;

import org.tju.bean.DiskInfo;
import org.tju.scheduler.DataMigration;
import org.tju.util.ValueOfConfigureFile;

/**
 * @author yuan
 *
 * @date 2015年12月17日 上午10:34:19
 */
public class Cache {
	
	//Value of configure files
	public static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
		
	//get mode: single(light, normal) || mix
	public static String mode = valueOfConfigureFile.getMode();
	
	//get All Threshold from Threshold.xml
	public static int SSDSizeTh = valueOfConfigureFile.getSSDSizeTh();
	public static double arrivalRateTh = valueOfConfigureFile.getArrivalRateTh();
	public static double lowPriorityTh = valueOfConfigureFile.getLowPriorityTh();
	public static double highPriorityTh = valueOfConfigureFile.getHighPriorityTh();
	public static int idleTimeTh = valueOfConfigureFile.getIdleTimeTh();
	public static int refreshTh = valueOfConfigureFile.getRefreshTh();
	
	//get disks' capacity from DiskCapacity.xml 
	public static int blockInSSD = valueOfConfigureFile.getBlockInSSD();
	public static int blockInCache = valueOfConfigureFile.getBlockInCache();
	
	
	//Data Migration: From Data Disk To Cache Disks
	public static void DataMigration(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks, DiskInfo dataDisk, int blockId, int refreshCountdown){
		
		//Data Disk ====>> SSD
		for(int i=0; i<SSDDisks.length; i++){
			if(SSDDisks[i].getLeftSpace() > SSDSizeTh){
				DataMigration.DD2SSD(dataDisk, SSDDisks[i], blockId);
				return ;
			} else if(SSDDisks[i].getBlockAmount()<blockInSSD && refreshCountdown<refreshTh){
				DataMigration.DD2SSD(dataDisk, SSDDisks[i], blockId);
				return ;
			}
		}
		
		//Data Disk ====>> Cache Disk
		for(int i=0; i<cacheDisks.length; i++){
			if(cacheDisks[i].getBlockAmount()<blockInCache){
				DataMigration.DD2Cache(dataDisk, cacheDisks[i], blockId);
				return;
			}
		}		
		
	}

}
