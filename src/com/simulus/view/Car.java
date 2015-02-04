package com.simulus.view;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.PathTransitionBuilder;
import javafx.animation.TranslateTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import com.simulus.model.enums.Direction;

/**
 * Describes a car object on the GUI
 */
public class Car extends Vehicle {

	private static final int WIDTH = 40;
	private static final int HEIGHT = 40;
	private static final int ARCHEIGHT = 20;
	private static final int ARCWIDTH = 50;

	/**
	 * Amount of pixel movements per tick
	 */
	private int speed = 1;
	

	private static final Color COLOUR = Color.GRAY;

	/**
	 * Sets the appearance, position and direction of the car.
	 * @param posX the X position of the car on the scene
	 * @param posY the Y position of the car on the scene
	 * @param dir the direction the car should start moving in
	 */
	public Car(double posX, double posY, Direction dir) {
		super(posX, posY, WIDTH, HEIGHT, dir);
		setWidth(WIDTH);
		setHeight(HEIGHT);
		setArcHeight(ARCHEIGHT);
		setArcWidth(ARCWIDTH);
		setFill(COLOUR);
	}
	/**
	 * Translates the vehicle according to the current direction
	 */
	public void moveVehicle(){
		Translate trans = new Translate();
		int dx;
		int dy;
		
		switch(getDirection()){
		case NORTH:
			
			dx = 0;
			dy = -speed;
			
			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			
			break;
		case SOUTH:
			dx = 0;
			dy = speed;
			
			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			
			break;
		case EAST:
			dx = speed;
			dy = 0;
			
			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			break;
		case WEST:
			dx = -speed;
			dy = 0;
			
			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			
			break;
		case NONE:
			dx = 0;
			dy = 0;
			
			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			break;
		}
	}
	
	//TO DO Curve the car to the northwest
	public PathTransition curveNorthWest() {
		PathTransition pathTransition;
		switch (getDirection()) {
		case NORTH:
			Path path = PathBuilder
					.create()
					.elements(
							// from
							new MoveTo(getX()-50, getY()),
							new CubicCurveTo(getX(), getY(),
									getX(), getY() - 100,
									getX() - 100, getY() - 95)
					).build();
			path.setStroke(Color.DODGERBLUE);
			path.getStrokeDashArray().setAll(5d, 5d);
			
			pathTransition = PathTransitionBuilder.create()
					.duration(Duration.seconds(2)).path(path).node(this)
					.orientation(OrientationType.NONE)
					.interpolator(Interpolator.LINEAR)
					// .cycleCount(Timeline.INDEFINITE)
					.build();
			setDirection(Direction.WEST);
			return pathTransition;
			default:
				return null;
		}
	}
}
