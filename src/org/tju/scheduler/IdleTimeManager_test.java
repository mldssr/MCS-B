package org.tju.scheduler;

//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map.Entry;
//
//import org.tju.bean.BlockInfo;
//import org.tju.bean.DiskInfo;
//import org.tju.util.ValueOfConfigureFile;

public class IdleTimeManager_test {

	public static void main(String[] args) {
		// IdleTimeManager idleTimeManager = new IdleTimeManager();
		// System.out.println(IdleTimeManager.getIdleTime());
		IdleTimeManager.showIdleTimeManager();
		for (int i = 1; i < 101; i++) {
			IdleTimeManager.updateIdleTime(i);
			if (i == 50)
				IdleTimeManager.showIdleTimeManager();
		}
		IdleTimeManager.showIdleTimeManager();
	}

}