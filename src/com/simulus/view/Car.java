package com.simulus.view;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Describes a car object on the GUI
 * @author ebrahim K1463831
 */
public class Car extends Vehicle{

	private static final int WIDTH = 40;
	private static final int HEIGHT = 40;
	private static final int ARCHEIGHT = 20;
	private static final int ARCWIDTH = 50;
	
	private static final Color COLOUR = Color.GRAY;
	
	/**
	 * Sets the appearance and position of the car.
	 * @param posX the position of the top left point of the car on X
	 * @param posY the position of the top left point of the car on Y
	 */
	public Car(int posX, int posY) {
		super(posX, posY, WIDTH, HEIGHT);
		setWidth(WIDTH); 
		setHeight(HEIGHT);
		setArcHeight(ARCHEIGHT);
		setArcWidth(ARCWIDTH);
		setFill(COLOUR);
	}
	
	/**
	 * Describes a single upwards translation taken by the car
	 * @return TranslateTransition
	 */
	@Override
	public TranslateTransition moveVehicle() {
		//Translate the car only if it exists on the scene
		if(getY()< 0-HEIGHT ){
			return null;
		}else{
			TranslateTransition transition = new TranslateTransition(Duration.millis(2000), this);
			transition.setFromY(getY());
			transition.setToY(getY()-50);
			transition.setInterpolator(Interpolator.LINEAR);
			setY(getY()-1);
			return transition;
		}
	}
	
}
