package com.simulus.util;

import java.io.File;

public class Configuration {
	
	public static final int CANVAS_SIZE = 800; //Size of the canvas in px
	
	public static int gridSize = 40; //Number of rows/columns
	public static int tileSize = CANVAS_SIZE/gridSize; //size of one tile in px
	
	//Temporary file for statistics
	private static File tmpStats = null;
	public static File getTempStatsFile() {
		if(tmpStats == null) {
			tmpStats = new File("simulusTempStats.csv");
		}
		return tmpStats;
	}
	
	
}
