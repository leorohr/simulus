/*
 * @(#) Vehicle.java 
 * 
 * Copyright (c) 2015 Team Simulus at King's College London. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary of Team Simulus 
 * 7CCSMGPR Group Project(14~15 SEM2 1) at Kings' College London. 
 * You shall not disclose such confidential information and shall use it only in accordance 
 * with the agreement and regulation set by Simulus and the Department of Informatics at King's College London. 
 * 
 * The software is set to be reviewed by module coordinator of 7CCSMGPR Group Project(14~15 SEM2 1)
 * 
 */

/* 
 * VERSION HISTORY:
 * 
 * 1.00 03/02/2015 Initial Version  
 */

package com.simulus.model;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

/**
 * The base class for all kinds of vehicles.
 *@author  Leonhard Rohr, Jerry Wang
 *@since 1.00
 *@version 1.00 06/02/2015
 */

public abstract class Vehicle {

	private static int count = 0; 
	protected int maxSpeed;
	protected Direction currentDir;
	protected Direction nextDir;
	protected int id;
	protected Lane lane;
<<<<<<< HEAD
	protected Tile[][] map;
	protected int currentXPos;
	protected int currentYPos;
	protected int nextMoveXPos;
	protected int nextMoveYPos;


	boolean blockageDectected;
	boolean intersectionDetected;



=======
	protected Tile[][] grid;
	
>>>>>>> branch 'master' of https://github.com/leorohr/simulus.git
	/**
	 * 
	 * @param map The map, 2D-array of tiles the vehicle is moving on.
	 * @param xPos The vehicle's x coordinate in the map.
	 * @param yPos The vehicle's y coordinate in the map.
	 * @param currentDir The vehicle's initial direction.
	 * @param lane The number of the lane (from left to right) the vehicle is set in.
	 */
<<<<<<< HEAD
	public Vehicle(Tile[][] map, int xPos, int yPos, Lane lane) {

=======
	public Vehicle(Tile[][] grid, int xPos, int yPos, Direction dir, Lane lane) {
		
>>>>>>> branch 'master' of https://github.com/leorohr/simulus.git
		this.id = count++;
<<<<<<< HEAD

		this.map = map;
		this.currentXPos = xPos;
		this.currentYPos = yPos;
=======
		
		this.grid = grid;
		this.xPos = xPos;
		this.yPos = yPos;
>>>>>>> branch 'master' of https://github.com/leorohr/simulus.git
		this.lane = lane;
		this.currentDir = lane.getDirection();

		blockageDectected = false;
		intersectionDetected = false;
	}

	/**
	 * Calculate the position for next movement based on current position and current direction
	 */
<<<<<<< HEAD
	private void CalculateNextForwardMovement(){

		switch(currentDir){

		case NORTH:
			nextMoveYPos = currentYPos-1;
			break;
		case SOUTH:
			nextMoveYPos = currentYPos+1;
			break;
		case EAST:
			nextMoveXPos = currentXPos+1;
			break;
		case WEST:
			nextMoveXPos = currentXPos-1;
			break;
		case NONE:
			break;
		default:
=======
	public void move() {
		
		//TODO turning at intersection
		switch(dir) {		
		case NONE : {
>>>>>>> branch 'master' of https://github.com/leorohr/simulus.git
			break;
		}
<<<<<<< HEAD
=======
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
		
>>>>>>> branch 'master' of https://github.com/leorohr/simulus.git
	}

	/**
	 * Calculate the position for the next left movement based on current position and current direction
	 */
	private void CalculateNextLeftMovement(){
		switch(currentDir){

		case NORTH:
			nextMoveXPos = currentXPos-1; // Number 1 will be replace by i in future, depending on the tick
			nextMoveYPos = currentYPos-1;
			nextDir = Direction.WEST;
			break;
		case SOUTH:
			nextMoveXPos = currentXPos+1;
			nextMoveYPos = currentYPos+1;
			nextDir = Direction.EAST;
			break;
		case WEST:
			nextMoveXPos = currentXPos-1;
			nextMoveYPos = currentYPos+1;
			nextDir = Direction.SOUTH;
			break;
		case EAST:
			nextMoveXPos = currentXPos+1;
			nextMoveYPos = currentYPos-1;
			nextDir = Direction.NORTH;
			break;
		}


	}

	/**
	 * Calculate the position for the next right movement based on current position and current direction
	 */
	private void CalculateNextRightMovement(){
		switch(currentDir){

		case NORTH:
			nextMoveXPos = currentXPos+1; // Number 1 will be replace by i in future, depending on the tick
			nextMoveYPos = currentYPos-1;
			nextDir = Direction.EAST;
			break;
		case SOUTH:
			nextMoveXPos = currentXPos-1;
			nextMoveYPos = currentYPos+1;
			nextDir = Direction.WEST;
			break;
		case WEST:
			nextMoveXPos = currentXPos-1;
			nextMoveYPos = currentYPos+1;
			nextDir = Direction.SOUTH;
			break;
		case EAST:
			nextMoveXPos = currentXPos+1;
			nextMoveYPos = currentYPos-1;
			nextDir = Direction.NORTH;
			break;
		}


	}

	/**
	 * Move the vehicle one step in its current direction.
	 */
	public void moveForward(){
		//TODO before calling this method, there should be a method call to check the validity 
		// of the next movement 

		map[nextMoveXPos][nextMoveYPos].content.getLanes1()[lane.getLaneId()].setVehicle(this);	//copy car to next cell
		map[currentXPos][currentYPos].content.getLanes1()[lane.getLaneId()].setVehicle(null);		//remove from old cell

		// The new location is now set as the currentLocation
		currentXPos = nextMoveXPos;
		currentYPos = nextMoveYPos;
	}

	/**
	 * is called turn the vehicle to the left 
	 */
	//Might consider to merge this method into one with <method>moveForward</method>,<method>turnLeft</method>,<method>turnRight</method>
	public void turnLeft(){

		map[nextMoveXPos][nextMoveYPos].content.getLanes1()[lane.getLaneId()].setVehicle(this);	//copy car to next cell
		map[currentXPos][currentYPos].content.getLanes1()[lane.getLaneId()].setVehicle(null);		//remove from old cell

		currentDir = nextDir; // Set new current Direction of the vehicle 

		// The new location is now set as the currentLocation
		currentXPos = nextMoveXPos;
		currentYPos = nextMoveYPos;
	}

	/**
	 * is called to turn the vehicle to Right
	 */
	//Might consider to merge this method into one with <method>moveForward</method>,<method>turnLeft</method>,<method>turnRight</method>
	public void turnRight(){

		map[nextMoveXPos][nextMoveYPos].content.getLanes1()[lane.getLaneId()].setVehicle(this);	//copy car to next cell
		map[currentXPos][currentYPos].content.getLanes1()[lane.getLaneId()].setVehicle(null);		//remove from old cell

		currentDir = nextDir; // Set new current Direction of the vehicle 

		// The new location is now set as the currentLocation
		currentXPos = nextMoveXPos;
		currentYPos = nextMoveYPos;
	}



	/**
	 * The function checks the blockage for next movement 
	 * N.B. the <method>checkInterSection </method> should be evoked before this method
	 */
	private void checkBlockage(){
		//TODO Check if next Move is blocked, any tile in front the car is occupied, i.e. is not EMPTY

		//Need to add Seed content 
		if (map[nextMoveXPos][nextMoveYPos].content.getLanes1()[lane.getLaneId()].occupied == true){ 
			blockageDectected = true;
		}
		else {
			blockageDectected = false;
		}

	}

	private void checkInterSection(){
		//TODO Check if any tile in front the car is content Intersection
		//return true;
		if (map[nextMoveXPos][nextMoveYPos].content.getSeed() == Seed.INTERSECTION){
			intersectionDetected = true;
		}
		else{
			intersectionDetected = false;
		}

	}


	public Direction getDirection() {
		return this.currentDir;
	}
	public void setDirection(Direction dir) {
		this.currentDir = dir;
	}

	public int getMaxSpeed() {
		return this.maxSpeed;
	}

	public int getId() {
		return this.id;
	}


}
