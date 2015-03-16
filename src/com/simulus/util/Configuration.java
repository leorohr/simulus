package com.simulus.util;

import java.io.File;

public class Configuration {
	
	public static final int CANVAS_SIZE = 800; //Size of the canvas in px
	private static int gridSize = 60; //Number of rows/columns; default 60
	private static int tileSize = CANVAS_SIZE/gridSize; //size of one tile in px
	
	//Temporary file for statistics
	private static File tmpStats = null;
	public static File getTempStatsFile() {
		if(tmpStats == null) {
			tmpStats = new File("simulusTempStats.csv");
		}
		return tmpStats;
	}
	
	public static int getGridSize() {
		return gridSize;
	}
	
	public static int getTileSize() {
		return tileSize;
	}
	
	public static void setGridSize(int gridSize) {
		Configuration.gridSize = gridSize;
		Configuration.tileSize = CANVAS_SIZE/gridSize;
	}
	
	
}
