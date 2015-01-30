package com.simulus.view;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Rectangle;

/**
 * Describes a vehicle on the GUI
 * @author ebrahim K1463831
 *
 */
public abstract class Vehicle extends Rectangle{
	
	/**
	 * Initialises the position and size of the vehicle
	 * @param posX The position of the vehicle on X
	 * @param posY The position of the vehicle on Y
	 * @param width The width of the vehicle
	 * @param height The height of the vehicle
	 */
	public Vehicle(int posX, int posY, double width, double height){
		super(posX, posY, width, height);
	}
	/**
	 * Describes a vehicle translation
	 * @return TranslateTransition
	 */
	public abstract TranslateTransition moveVehicle();
	
	
	
}
