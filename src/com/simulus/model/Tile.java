package com.simulus.model;

/**
 * The superclass for all kinds of tiles in the grid.
 */
public class Tile {
	
	public Road content;
	private int xPos;
	private int yPos;

	/**
	 * Create a tile with <code>content</code> in it at position (xPos,yPos)
	 * @param xPos
	 * @param yPos
	 * @param content
	 */
	public Tile(int xPos, int yPos, Road content) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.content = content;
	}
	
	/**
	 * Create an empty tile at position (xPos,yPos)
	 * @param xPos
	 * @param yPos
	 */
	public Tile(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.content = null;
	}
	
	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}
}
