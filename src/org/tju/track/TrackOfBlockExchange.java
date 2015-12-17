package org.tju.track;

import org.tju.track.bean.BlockExchange;
import org.tju.util.FileOperation;

/**
 * @author yuan
 *
 * @date 2015年12月13日 上午11:48:45
 */
public class TrackOfBlockExchange {
	
	//get file operation
	public FileOperation fileOper= new FileOperation();
	
	
	//Track of Blocks' Exchange
	public void TrackOfBlockExchangeFile(String filePath, BlockExchange blockExchange){
		
		String content = blockExchange.getTime() + "," +
							blockExchange.getData2SSD() + "," +
							blockExchange.getData2Cache() + "," +
							blockExchange.getCache2SSD() + "," +
							blockExchange.getCache2Cache() + "," +
							blockExchange.getTotal() + "\n";
		
		AppendBlockExchangeFile(filePath, content);		
		
	}
	
	
	//Create File Of track of Blocks' Exchange
	public void CreateFileOfBlockExchange(String filePath){
		
		fileOper.CreateFile(filePath);
		
	}
	
	
	//Add header of Track of Blocks' Exchange file
	public void HeaderOfBlockExchange(String filePath, String[] lables){
		
		fileOper.HeaderOfFile(filePath, lables);
		
	}
	
	
	//Append content of Track of Blocks' Exchange file
	public void AppendBlockExchangeFile(String filePath, String content){
		
		fileOper.FileAppend(filePath, content);
		
	}
	
	
	
	
	//test
	public static void main(String[] args){
		
		TrackOfBlockExchange track = new TrackOfBlockExchange();
		
		String filePath = "track/BlockExchange.csv";
		
		BlockExchange block = new BlockExchange(1, 1, 1, 1, 1, 3);

		String[] lables = {"Time", "Data2SSD", "Data2Cache", "Cache2SSD", "Cache2Cache", "Total"};
		
		
		track.CreateFileOfBlockExchange(filePath);
		
		track.HeaderOfBlockExchange(filePath, lables);
		
		track.TrackOfBlockExchangeFile(filePath, block);
			
	}

}
