package org.tju.util;

//import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperation {

	private FileWriter fw;

	// Create file
	public void CreateFile(String filePath) {
//		new File(filePath);
		fw = null;
		try {
			fw = new FileWriter(filePath);
		} catch (IOException e) {
			System.out.println("[ERROR] Create file " + filePath + "failed");
			e.printStackTrace();
		}
	}

	// Add Headers of the file
	public void HeaderOfFile(String filePath, String[] lables) {
		
		String content = "";

		int i = 0;
		for (; i < lables.length - 1; i++) {
			content += lables[i] + ",";
		}

		if (i == lables.length - 1) {
			content += lables[i] + "\n";
		}

		// Add to file
		// FileAppend(filePath, content);
		try {
			fw.write(content);
		} catch (IOException e) {
			System.out.println("[ERROR] Add head to file " + filePath + "failed");
			e.printStackTrace();
		}

	}

	// append content to the end of file
	public void FileAppend(String filePath, String content) {
		try {
			// FileWriter writer = new FileWriter(filePath, true);
			fw.write(content);
			// writer.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Append to file " + filePath + "failed");
			e.printStackTrace();
		}
	}

	public void CloseFile() {
		try {
			// FileWriter writer = new FileWriter(filePath, true);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			System.out.println("[ERROR] Close file failed");
			e.printStackTrace();
		}
	}

}
