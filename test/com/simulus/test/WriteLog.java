package com.simulus.test;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class WriteLog {
	
	private String filePath;
	private boolean append_to_file = false;
	
	/**
	 * Specify file path
	 * @param filePath
	 */
	public WriteLog(String filePath){
		this.filePath = filePath;
	}
	
	/**
	 * 
	 * @param filePath File PATH
	 * @param append_to_file whether to write log to existing file
	 */
	public WriteLog(String filePath, boolean append_to_file){
		this.filePath = filePath;
		this.append_to_file = append_to_file;
		
	}
	
	public void WriteToLog(String text) throws IOException {
		FileWriter write = new FileWriter (filePath, append_to_file);
		
		
		PrintWriter printLine = new PrintWriter(write);
		
		printLine.printf("%s" + "%n", text);
		
		printLine.close();
		
	}
	
	public void flush(){
		PrintWriter writer;
		try {
			writer = new PrintWriter(filePath);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
