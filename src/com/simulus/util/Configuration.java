package com.simulus.util;

import java.io.File;
import java.io.IOException;

public class Configuration {
	
	public static final int CANVAS_SIZE = 800; //Size of the canvas in px
	
	public static int gridSize = 40; //Number of rows/columns
	public static int tileSize = CANVAS_SIZE/gridSize; //size of one tile in px
	
	//Temporary file for statistics
	private static File tmpStats;
	static {
		tmpStats = new File("bin/resources/tmpStats.csv");
		try {
			tmpStats.createNewFile();
		} catch (IOException e) {e.printStackTrace();}
	}
	public static File getTempStatsFile() {
		return tmpStats;
	}
	
	
}
