package org.tju.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class Excel {
	
	//Create data to excel file
	public void createExcel(String filePath, String[] lables) throws WriteException, IOException{
		
		WritableWorkbook book = null;
		
		try { 
			//Open File
		    book= Workbook.createWorkbook(new File(filePath)); 
		    
		    //Generate the first sheet
		    WritableSheet sheet=book.createSheet("First Sheet",0); 
		    
		    
		    //First Row
		    for(int i=0; i<lables.length; i++){
		    	Label label = new Label(i, 0, lables[i]);
		    	sheet.addCell(label);	    	
		    }
		    
		    //Write the data to files 
		    book.write(); 
	    } catch(Exception e) { 
	        System.out.println(e); 
	    } finally{
		    book.close();
	    }	
		
	}
	
	
	//Read excel file
	public void readExcel(String filePath){
		jxl.Workbook rwb = null;
	    try {
	      //构建Workbook对象, 只读Workbook对象
	      //直接从本地文件创建Workbook
	      //从输入流创建Workbook
	      InputStream is = new FileInputStream(filePath);
	      rwb = Workbook.getWorkbook(is);

	      //Sheet(术语：工作表)就是Excel表格左下角的Sheet1,Sheet2,Sheet3但在程序中
	      //Sheet的下标是从0开始
	      //获取第一张Sheet表
	       Sheet rs = rwb.getSheet(0);
	       //获取Sheet表中所包含的总列数
	       int rsColumns = rs.getColumns();
	       //获取Sheet表中所包含的总行数
	       int rsRows = rs.getRows();
	       //获取指定单元格的对象引用
	       for(int i=0;i<rsRows;i++){
	         for(int j=0;j<rsColumns;j++){
	           Cell cell = rs.getCell(j,i);
	           System.out.print(cell.getContents()+" ");
	         }
	         System.out.println();
	       }
	    } catch(Exception e) {
	      e.printStackTrace();
	    } finally {
	      //操作完成时，关闭对象，释放占用的内存空间
	      rwb.close();
	    }
	}
	
	
	//Update Excel file
	public void updateExcel(String filePString){
		jxl.Workbook rwb = null;
	    try{	    	
	       //构建Workbook对象, 只读Workbook对象
	       //直接从本地文件创建Workbook
	       //从输入流创建Workbook
	       InputStream is = new FileInputStream(filePString);
	       rwb = Workbook.getWorkbook(is);
	       //利用已经创建的Excel工作薄创建新的可写入的Excel工作薄
	       jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePString),rwb);
	       //读取第一张工作表
	       jxl.write.WritableSheet ws = wwb.getSheet(0);

	       //获得第一个单元格对象
	       jxl.write.WritableCell wc = ws.getWritableCell(0, 0);
	       //判断单元格的类型, 做出相应的转化
	       if (wc.getType() == CellType.LABEL) {
	         Label l = (Label) wc;
	         l.setString("The value has been modified.");
	       }
	       //写入Excel对象
	       wwb.write();
	       wwb.close();

	    }catch(Exception e){
	      e.printStackTrace();
	    }
	    finally{
	      rwb.close();
	    }
		
	}

}
