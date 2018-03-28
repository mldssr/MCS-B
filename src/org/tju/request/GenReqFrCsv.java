package org.tju.request;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GenReqFrCsv {

	// CSV's ENCODE
	public static final String ENCODE = "UTF-8";

	private FileInputStream fis = null;
	private InputStreamReader isw = null;
	private BufferedReader br = null;

	public GenReqFrCsv(String filename) { // throws Exception

		try {
			fis = new FileInputStream(filename);
			isw = new InputStreamReader(fis, ENCODE);
			br = new BufferedReader(isw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, HashMap<String, RequestInfo>> readLine() { // throws IOException

		HashMap<String, RequestInfo> requests = new HashMap<String, RequestInfo>();
		HashMap<Integer, HashMap<String, RequestInfo>> requestList = new HashMap<Integer, HashMap<String, RequestInfo>>();
		try {
			// Throw first line ---- lables
			br.readLine();
			int i = 1;
			while (true) {
				// Ò»ÐÐ
				String strReadLine = br.readLine();

				// readLine is Null ---- End of file
				if (strReadLine == null) {
					requestList.put(i - 1, requests);
					return requestList;
				}
				String[] info = strReadLine.split(",");

				if (Integer.valueOf(info[1]) >= 10 * i) {
					requestList.put(i - 1, requests);
					i++;

					requests = new HashMap<String, RequestInfo>();
				}
				RequestInfo req = new RequestInfo(info[0], Integer.valueOf(info[1]), 0, 0);

				requests.put(info[0], req);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestList;
	}

	public static void main(String[] args) {
		GenReqFrCsv recoveyRequest = new GenReqFrCsv("track/0_ReqGen.csv");

		HashMap<Integer, HashMap<String, RequestInfo>> requestsList = recoveyRequest.readLine();

		System.out.println();
		int totalRequestsNum = 0;
		for (HashMap.Entry<Integer, HashMap<String, RequestInfo>> entry : requestsList.entrySet()) {
			HashMap<String, RequestInfo> requests = entry.getValue();
			System.out.println("Key = " + entry.getKey() + ", Size = " + requests.size());
			totalRequestsNum += requests.size();
			for (HashMap.Entry<String, RequestInfo> entry3 : requests.entrySet()) {
				System.out.println("     Key = " + entry3.getKey() + ", Value = " + entry3.getValue());
			}
		}
		System.out.println("totalRequestsNum: " + totalRequestsNum);
	}

}
