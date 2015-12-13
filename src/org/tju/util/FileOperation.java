package org.tju.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author yuan
 *
 * @date 2015年12月13日 上午11:24:39
 */
public class FileOperation {
	
	//Create file
	public void CreateFile(String filePath){
		new File(filePath);
	}
	
	//Add Headers of the file
	public void HeaderOfFile(String filePath, String[] lables){
		
		String content = "";
		
		int i=0;
		for(; i<lables.length-1; i++){
			content += lables[i] + ",";
		}
		
		if(i == lables.length-1){
			content += lables[i] + "\n";
		}
		
		//Add to file
		FileAppend(filePath, content);
	}
	
	
	//append content to the end of file
	public void FileAppend(String filePath, String content){
		
		try {
			FileWriter writer = new FileWriter(filePath, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
