package org.tju.simulation;

import org.tju.bean.DiskInfo;
import org.tju.scheduler.DataMigration;
import org.tju.scheduler.Scheduler;

public class Refresh {
	
	//Refresh
	public static void RefreshOfCache(DiskInfo[] SSDDisks, DiskInfo[] cacheDisks){
		
		//Clear Cache Disks
		Scheduler.SchedulerOfDisks(SSDDisks, cacheDisks);
		
		//Data migration
		DataMigration.RefreshReplacement(SSDDisks, cacheDisks);
		
	}

}
