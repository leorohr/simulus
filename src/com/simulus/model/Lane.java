package com.simulus.model;

import com.simulus.util.enums.Direction;

public class Lane {
	private final Direction direction;
	private Vehicle vehicle;
	//id of a lane with respect to all other lanes in that direction. starting from leftmost lane being 0, incrementing towards the right.
	private int laneId; 
	private Road road; //the Road object that stores this lance instance
	
	boolean occupied;
	
	/**
	 * 
	 * @param 	direction The driving direction of the lane.
	 * @param 	laneId id of a lane with respect to all other lanes in that direction.
	 *			Starting from leftmost lane being 0, incrementing towards the right.
	 * @param	parent The <code>Road</code> object that stores the instance of this lane.
	 */
	public Lane(Direction direction, int laneId, Road road) {
		this.direction = direction;
		this.laneId = laneId;
		this.road = road;
		
		this.occupied = false;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * @return id of a lane with respect to all other lanes in that direction.
	 * Starting from leftmost lane being 0, incrementing towards the right.
	 */
	public int getLaneId() {
		return laneId;
	}
	
	public Road getRoad() {
		return road;
	}

}
