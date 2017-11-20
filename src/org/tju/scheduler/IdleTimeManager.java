package org.tju.scheduler;

//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map.Entry;
//
//import org.tju.bean.BlockInfo;
//import org.tju.bean.DiskInfo;
import org.tju.util.ValueOfConfigureFile;

public class IdleTimeManager {
	private static int[] idleTimeList;
	private static int currentIndex; //指向下一个记录的位置
	private static int idleTimeTh;
	
	private static ValueOfConfigureFile valueOfConfigureFile = new ValueOfConfigureFile();
	
	//初始化
	static {
		//System.out.println("idleTimeManager initial");
		idleTimeList = new int[100];
		for(int i=0; i<100;i++){
			idleTimeList[i]=-1;
		}
		currentIndex = 0;
		idleTimeTh = valueOfConfigureFile.getIdleTimeTh();
		//System.out.println("idleTimeManager initial end");
	}
	
	
	public static int getIdleTimeTh(){
		
		return idleTimeTh;
	}
	
	//将采集到的新idleTime写入到idleTimeList[]，并在每次写满时计算idleTimeTh
	public static void updateIdleTime(int idleTime){
		System.out.println("          A new idleTime is added to idleTimeList: "+idleTime + " at location "+currentIndex);
		if(idleTime <= 0)
			return;
		idleTimeList[currentIndex] =  idleTime;
		
		if(currentIndex != 99){
			currentIndex = (currentIndex + 1) % 100;
		}else{
			currentIndex = (currentIndex + 1) % 100;
			setIdleTime();
		}
	}
	
	//根据idleTimeList[]生成idleTime
	public static void setIdleTime(){
		
		int index = 0;
		int sum = 0;
		for(int i=currentIndex; i<(100+currentIndex); i++){
			index = i % 100;
			sum += idleTimeList[index];
		}
		System.out.print("                    idleTimeTh is changed from "+idleTimeTh );
		idleTimeTh = sum/100*3;
		System.out.println(" to "+idleTimeTh );
	}
	
	
	public static void showIdleTimeManager(){
		System.out.println("\nidleTimeManager.idleTimeList:");
		for(int i=0; i<100;i++){
			System.out.println("idleTimeList ["+i+"] = "+idleTimeList[i]);
		}
		System.out.println("idleTimeManager.currentIndex: "+currentIndex);
		System.out.println("idleTimeManager.idleTimeTh: "+idleTimeTh+"\n");
	}
	
}