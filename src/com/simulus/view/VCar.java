package com.simulus.view;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransitionBuilder;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import com.simulus.MainApp;
import com.simulus.util.enums.Direction;

/**
 * Describes a car object on the GUI
 */
public class VCar extends VVehicle {

	public static final int CARWIDTH = 20;
	public static final int CARHEIGHT = 25;

	private static final int ARCHEIGHT = 10;
	private static final int ARCWIDTH = 30;
	


	/**
	 * Amount of pixel movements per tick
	 */
	private double speed = 2;

	private static final Color COLOUR = Color.PINK;

	/**
	 * Sets the appearance, position and direction of the car.
	 * 
	 * @param posX
	 *            the X position of the car on the scene
	 * @param posY
	 *            the Y position of the car on the scene
	 * @param dir
	 *            the direction the car should start moving in
	 */
	public VCar(double posX, double posY, Direction dir, MainApp gui) {
		super(posX, posY, CARWIDTH, CARHEIGHT, dir, gui);
		switch (dir) {
		case NORTH:
		case SOUTH:
			setWidth(CARWIDTH);
			setHeight(CARHEIGHT);
			break;
		case EAST:
		case WEST:
			setWidth(CARHEIGHT);
			setHeight(CARWIDTH);
			break;
		}
		setArcHeight(ARCHEIGHT);
		setArcWidth(ARCWIDTH);
		setFill(COLOUR);
		Random rand = new Random();
		speed = rand.nextInt(5)+1;
		addToCanvas();
	}
	
	

	/**
	 * Translates the vehicle according to the current direction
	 */
	public void moveVehicle() {
		Translate trans = new Translate();
		
		
		final double dx;
		final double dy;

		Direction temp = getDirection();

		//Checks if the tile ahead of the car is taken.
		try {
			switch (getDirection()) {
			case NORTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() - 1].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			case SOUTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() + 1].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			case EAST:
				if (map[getCurrentTile().getGridPosX() + 1][getCurrentTile()
						.getGridPosY()].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			case WEST:
				if (map[getCurrentTile().getGridPosX() - 1][getCurrentTile()
						.getGridPosY()].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				setOnScreen(true);
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("out of screen");
			setOnScreen(false);
		}

		//Moves the car in the direction it should go.
		switch (temp) {
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

	public void removeFromCanvas() {
		switch (getDirection()) {
		case NORTH:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		case SOUTH:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		case EAST:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		case WEST:
			if (parent.getCanvas().getChildren().contains(this)) {
				parent.getCanvas().getChildren().remove(this);
			}
			break;
		}
	}

	// TODO Curve the car to the northwest
	public PathTransition curveNorthWest() {
		PathTransition pathTransition;
		switch (getDirection()) {
		case NORTH:
			Path path = PathBuilder
					.create()
					.elements(
							// from
							new MoveTo(getX() - 50, getY()),
							new CubicCurveTo(getX(), getY(), getX(),
									getY() - 100, getX() - 100, getY() - 95))
					.build();
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
