package com.simulus.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


 
public class CsvExport{

	private static File outputFile;
	
	public CsvExport(File saveFile){
		outputFile = saveFile;
	}
	
   public void generateCsvFile(String header01, String header02,
		   String header03, String header04, String header05){
	   
	try
	{
	    FileWriter writer = new FileWriter(outputFile);
 
	    writer.append(header01);
	    writer.append(',');
	    writer.append(header02);
	    writer.append(',');
	    writer.append(header03);
	    writer.append(',');
	    writer.append(header04);
	    writer.append(',');
	    writer.append(header05);
	    writer.append('\n');
	    
	    writer.flush();
	    writer.close();
	}
	catch(IOException e)
	{
	     e.printStackTrace();
	} 
    
}
   public void appendRow(String value01, String value02,
		   String value03, String value04, String value05){
	   
		try
		{
			if (outputFile.exists()){
			    FileWriter writer = new FileWriter(outputFile, true);
				 
			    writer.append(value01);
			    writer.append(',');
			    writer.append(value02);
			    writer.append(',');
			    writer.append(value03);
			    writer.append(',');
			    writer.append(value04);
			    writer.append(',');
			    writer.append(value05);
			    writer.append('\n');
			    
			    writer.flush();
			    writer.close();
			}

		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	
   }

}