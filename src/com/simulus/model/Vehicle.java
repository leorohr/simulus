package com.simulus.model;

import com.simulus.util.enums.Direction;

/**
 * The base class for all kinds of vehicles.
 */
public abstract class Vehicle {
	
	private static int count = 0; 
	protected int maxSpeed;
	protected Direction dir;
	protected int id;
	protected int xPos;
	protected int yPos;
	protected Lane lane;
	protected Tile[][] grid;
	
	/**
	 * 
	 * @param map The map, 2D-array of tiles the vehicle is moving on.
	 * @param xPos The vehicle's x coordinate in the map.
	 * @param yPos The vehicle's y coordinate in the map.
	 * @param dir The vehicle's initial direction.
	 * @param lane The number of the lane (from left to right) the vehicle is set in.
	 */
	public Vehicle(Tile[][] grid, int xPos, int yPos, Direction dir, Lane lane) {
		
		this.id = count++;
		
		this.grid = grid;
		this.xPos = xPos;
		this.yPos = yPos;
		this.lane = lane;
		this.dir = dir;
	}
	
	/**
	 * Moves the vehicle one step in its current direction.
	 */
	public void move() {
		
		//TODO turning at intersection
		switch(dir) {		
		case NONE : {
			break;
		}
		case NORTH : {
			if(yPos-1 >= 0 && grid[xPos][yPos-1].content != null) {
				grid[xPos][yPos-1].content.getLanes1()[lane.getLaneId()].setVehicle(this);	//copy car to next cell
				grid[xPos][yPos].content.getLanes1()[lane.getLaneId()].setVehicle(null);		//remove from old cell
				yPos--;
			}
			break;
		}
		case SOUTH : {
			if(yPos+1 >= 0 && grid[xPos][yPos+1].content != null) {
				grid[xPos][yPos+1].content.getLanes1()[lane.getLaneId()].setVehicle(this);	
				grid[xPos][yPos].content.getLanes1()[lane.getLaneId()].setVehicle(null);		
				yPos++;
			}
			break;
		}
		case EAST : {
			if(xPos+1 >= 0 && grid[xPos+1][yPos].content != null) {
				grid[xPos+1][yPos].content.getLanes1()[lane.getLaneId()].setVehicle(this);	
				grid[xPos][yPos].content.getLanes1()[lane.getLaneId()].setVehicle(null);		
				xPos++;
			}
			break;
		}
		case WEST : { 
			if(xPos-1 >= 0 && grid[xPos-1][yPos].content != null) {
				grid[xPos-1][yPos].content.getLanes1()[lane.getLaneId()].setVehicle(this);
				grid[xPos][yPos].content.getLanes1()[lane.getLaneId()].setVehicle(null);		
				xPos--;
			}	
			break;
		}
		}
		
	}
	
	public Direction getDirection() {
		return this.dir;
	}
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
	
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public int getId() {
		return this.id;
	}
	
	
}
