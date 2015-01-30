package com.simulus.model;

import com.simulus.model.enums.Direction;

/**
 * The base class for all kinds of vehicles.
 */
public abstract class Vehicle {
	
	//TODO in what unit is speed meausured? tiles/tick?
	protected int maxSpeed;
	protected Direction dir;
	protected Tile currentTile;
	
	public Vehicle(Tile tile) {
		this.currentTile = tile;
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
}

