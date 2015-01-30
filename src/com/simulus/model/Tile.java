package com.simulus.model;

/**
 * The superclass for all kinds of tiles in the grid.
 */
public abstract class Tile {
	
	private Vehicle vehicle = null;
	
	/**
	 * The vehicle that currently occupies this tile.
	 * @return the occupying vehicle, <code>null</code> if the tile is unoccupied. 
	 */
	public Vehicle getVehicle() {
		return this.vehicle;
	}
	public void setVehicle(Vehicle v) {
		this.vehicle = v;
	}
}
