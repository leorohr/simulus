package com.simulus.view;
import com.simulus.model.enums.Direction;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

/**
 * Describes a vehicle on the GUI
 */
public abstract class Vehicle extends Rectangle{
	
	protected Direction dir;
	
	/**
	 * Initialises the position and size of the vehicle
	 * 
	 * @param posX The position of the vehicle on X
	 * @param posY The position of the vehicle on Y
	 * @param width The width of the vehicle
	 * @param height The height of the vehicle
	 */
	public Vehicle(double posX, double posY, double width, double height, Direction dir){
		super(posX, posY, width, height);
		this.dir = dir;
	}
	/**
	 * Describes a vehicle translation
	 */
	public abstract void moveVehicle();
	
	public Direction getDirection(){
		return dir;
	}
	public void setDirection(Direction dir){
		this.dir = dir;
	}
	
}
