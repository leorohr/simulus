package com.simulus.model;

import com.simulus.model.enums.Orientation;

/**
 * The basic road-tile to represent roads in the grid.
 *
 */
public class Road extends Tile {

	private Orientation orientation;
	
	public Road(Orientation ori) {
		this.orientation = ori;
	}
	
	public Orientation getOrientation() {
		return this.orientation;
	}
}
