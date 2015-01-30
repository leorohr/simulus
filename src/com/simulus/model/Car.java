package com.simulus.model;

import com.simulus.model.enums.Direction;

public class Car extends Vehicle {

	public Car(Tile tile, Direction dir) {
		super(tile);
		
		this.dir = dir; 
	}
	
	
}
