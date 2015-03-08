package com.simulus.util;

import java.io.File;

public class Configuration {
	
	public static final int CANVAS_SIZE = 800; //Size of the canvas in px
	
	public static int gridSize = 40; //Number of rows/columns
	public static int tileSize = CANVAS_SIZE/gridSize; //size of one tile in px
	
	public static File tempStatsCsv = new File("C:\\Users\\Administrator\\git\\simulus\\bin\\resources\\tmpStats.csv");
	
	
}
