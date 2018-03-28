package org.tju.simulation;

import org.tju.bean.BlockInfo;
import org.tju.bean.DiskInfo;
import org.tju.scheduler.BlockOperation;
import org.tju.scheduler.DiskOperation;
import org.tju.scheduler.IdleTimeManager;

public class SearchInDisks {

	// Search In SSD Disks，block的idleTime变为-1，requestNum +1，disk的requestNum +1
	public static boolean searchInSSD(DiskInfo[] SSDDisks, String fileName, int blockId) {

		for (int i = 0; i < SSDDisks.length; i++) {
			BlockInfo block = SSDDisks[i].getBlockList().get(blockId);
			if (block != null) {
				if (block.getIdleTime() >= 0) {
					block.setIdleTime(-1);
				}
				block.setRequestNum(block.getRequestNum() + 1);
				SSDDisks[i].getBlockList().put(blockId, block);
				SSDDisks[i].setRequestNum(SSDDisks[i].getRequestNum() + 1);
				// added by xx begin
				if (SSDDisks[i].getIdleTime() >= 0) {
					SSDDisks[i].setIdleTime(-1);
				}
				// added by xx end
				return true;
			}
		}

		return false;
	}

	// Search In Cache Disks，block的idleTime变为-1，requestNum +1，disk的requestNum +1
	public static boolean searchInCache(DiskInfo[] cacheDisks, String fileName, int blockId) {

		for (int i = 0; i < cacheDisks.length; i++) {
			BlockInfo block = cacheDisks[i].getBlockList().get(blockId);
			if (block != null) {
				if (block.getIdleTime() >= 0) {
					block.setIdleTime(-1);
				}
				block.setRequestNum(block.getRequestNum() + 1);
				cacheDisks[i].getBlockList().put(blockId, block);
				cacheDisks[i].setRequestNum(cacheDisks[i].getRequestNum() + 1);
				// added by xx begin
				if (cacheDisks[i].getIdleTime() >= 0) {
					cacheDisks[i].setIdleTime(-1);
				}
				// added by xx end
				return true;
			}
		}

		return false;
	}

	// Search In data Disks
	public static boolean searchInDD(DiskInfo[] dataDisks, String fileName, int diskId, int blockId) {
		// added by xx begin
		diskId = searchBetterInDD(dataDisks, blockId);
		// added by xx end

		BlockInfo block = dataDisks[diskId].getBlockList().get(blockId);

		if (block != null) {
			if (block.getIdleTime() >= 0) {
				block.setIdleTime(-1);
			}
			block.setRequestNum(block.getRequestNum() + 1);
			// added by xx begin
			if (dataDisks[diskId].getDiskState() == 1 && dataDisks[diskId].getIdleTime() >= 0) {
				System.out.println("[SERCH] The DataDisk " + diskId + " is hitted when completely opened, idleTime = " + dataDisks[diskId].getIdleTime());
//				IdleTimeManager.updateIdleTime(dataDisks[diskId].getIdleTime());
				dataDisks[diskId].setIdleTime(-1);
			}
			// added by xx end
			if (dataDisks[diskId].getDiskState() != 1) {
				DiskOperation.openDisk(dataDisks[diskId]);
				BlockOperation.initBlocksList(dataDisks[diskId]);
			}
			if (dataDisks[diskId].getIdleTime() >= 0) {
				dataDisks[diskId].setIdleTime(-1);
			}
			dataDisks[diskId].setRequestNum(dataDisks[diskId].getRequestNum() + 1);
			return true;
		}

		return false;
	}

	// 找到更合适的磁盘, added by xx
	public static int searchBetterInDD(DiskInfo[] dataDisks, int blockId) {

		int diskId0 = 0, diskId1 = 0, transTime0 = 1000, transTime1 = 1000;
		for (int i = 0; i < dataDisks.length; i++) {
			BlockInfo block = dataDisks[i].getBlockList().get(blockId);
			if (block != null && transTime0 == 1000) {
				transTime0 = block.getTransmissionTime();
				diskId0 = i;
			} else if (block != null && transTime1 == 1000) {
				transTime1 = block.getTransmissionTime();
				diskId1 = i;
			}
		}

		if (transTime0 < transTime1) {
			return diskId0;
		} else {
			return diskId1;
		}
	}

}
