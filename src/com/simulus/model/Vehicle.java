package com.simulus.model;

import com.simulus.model.enums.Direction;

/**
 * The base class for all kinds of vehicles.
 */
public abstract class Vehicle {
	
	private static int count = 0; 
	//TODO in what unit is speed meausured? tiles/tick?
	protected int maxSpeed;
	protected Direction dir;
	protected Tile currentTile;
	protected int id;
	
	public Vehicle(Tile tile) {
		this.currentTile = tile;
		this.id = count++;
	}
	
	public Direction getDirection() {
		return this.dir;
	}
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
	
	public Tile getCurrentTile() {
		return this.currentTile;
	}
	public void setCurrentTile(Tile t) {
		this.currentTile = t;
	}
	
	public int getMaxSpeed() {
		return this.maxSpeed;
	}
	
	public int getId() {
		return this.id;
	}
}

