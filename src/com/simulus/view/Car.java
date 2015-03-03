package com.simulus.view;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.Random;

/**
 * Describes a car object on the GUI
 */
@SuppressWarnings("deprecation")
public class Car extends Vehicle {

	public static final int CARWIDTH = 10;
	public static final int CARLENGTH = 15;	

	private static final int ARCHEIGHT = 10;
	private static final int ARCWIDTH = 10;
	
	
	

	private static final Color COLOUR = Color.LIGHTSEAGREEN;

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
	public Car(double posX, double posY, Direction dir) {
		super(posX, posY, CARWIDTH, CARLENGTH, dir);
		
		behavior = Behavior.getRandomBehavior();
		
		switch (dir) {
		case NORTH:
		case SOUTH:
			 setWidth(CARWIDTH);
			 setHeight(CARLENGTH);
			break;
		case EAST:
		case WEST:
			 setWidth(CARLENGTH);
			 setHeight(CARWIDTH);
			break;
		default:
			break;
		}
		 setArcHeight(ARCHEIGHT);
		 setArcWidth(ARCWIDTH);
		 setFill(COLOUR);
		Random rand = new Random();
		vehicleSpeed = rand.nextInt(SimulationController.getInstance().getMaxCarSpeed()-3)+3;
		addToCanvas();
	}
	
	/**
	 * Translates the vehicle according to the current direction
	 */
	public void moveVehicle() {
		
		if(isOvertaking){
			return;
		}

		
		tempDir = getDirection();
		tempBehavior = behavior;
		
		if(tempBehavior == Behavior.SEMI)
			if(Math.random()>0.5)
				tempBehavior = Behavior.RECKLESS;
			else tempBehavior = Behavior.CAUTIOUS;

//		Checks if the tile 2 tiles ahead of the car is taken for overtaking.
		if(tempBehavior == Behavior.RECKLESS){
			attemptOvertake();
		}
		
		try {
            Tile nextTile = null;
			switch (getDirection()) {
			case NORTH:
                if(currentTile.getGridPosY()-1 < 0) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY() - 1];
                if (nextTile.isOccupied()) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			case SOUTH:
                if(currentTile.getGridPosY()+1 >= map.length) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY() + 1];
                if (nextTile.isOccupied()) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			case EAST:
                if(currentTile.getGridPosX()+1 >= map.length) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX() + 1][getCurrentTile().getGridPosY()];
				if (nextTile.isOccupied()) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			case WEST:
                if(currentTile.getGridPosX()-1 < 0) {
                    SimulationController.getInstance().removeVehicle(this);
                    break;
                }

                nextTile = map[getCurrentTile().getGridPosX() - 1][getCurrentTile().getGridPosY()];
				if (nextTile.isOccupied()) {
					tempDir = Direction.NONE;
				} else
					tempDir = getDirection();
				break;

			default:
				break;
			}

            //Slow the car down if cautious and slow car in front
            if(tempBehavior == Behavior.CAUTIOUS)
                if(nextTile != null && nextTile.getOccupier()!=null)
                    setVehicleSpeed(nextTile.getOccupier().getVehicleSpeed());

		} catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
		}
		
		if(isPaused)
			tempDir = Direction.NONE;
		
		move(tempDir);
		
		
	}
	
	
	public void attemptOvertake(){
		try {
			switch(getDirection()){
			case NORTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
				                						.getGridPosY() - 2].isOccupied() && map[getCurrentTile().getGridPosX()][getCurrentTile()
				                						            					                						.getGridPosY() - 2].getOccupier() != null) {
					if(map[getCurrentTile().getGridPosX()][getCurrentTile()
				                			.getGridPosY() - 2].getOccupier().getDirection() == getDirection())
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 0){
								//Overtake RIGHT
								if(!map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;
								}
							}
						}
				}
				break;
			case SOUTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
				                						.getGridPosY() + 2].isOccupied()&& map[getCurrentTile().getGridPosX()][getCurrentTile()
				                						            					                						.getGridPosY() + 2].getOccupier() != null) {
					if(map[getCurrentTile().getGridPosX()][getCurrentTile()
								                			.getGridPosY() + 2].getOccupier().getDirection() == getDirection())
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 2){
								//Overtake RIGHT
								if(!map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}
						}
				}
				break;
			case EAST:
				if (map[getCurrentTile().getGridPosX() + 2][getCurrentTile()
				                    						.getGridPosY()].isOccupied()&& map[getCurrentTile().getGridPosX()+2][getCurrentTile()
				                						            					                						.getGridPosY()].getOccupier() != null) {
					if(map[getCurrentTile().getGridPosX() + 2][getCurrentTile()
								                			.getGridPosY()].getOccupier().getDirection() == getDirection())
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 0){
								//Overtake DOWN
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;	
								}
							}
						}
				}
				break;
			case WEST:
				if (map[getCurrentTile().getGridPosX() - 2][getCurrentTile()
				                    						.getGridPosY()].isOccupied()&& map[getCurrentTile().getGridPosX()-2][getCurrentTile()
				                						            					                						.getGridPosY()].getOccupier() != null) {
					if(map[getCurrentTile().getGridPosX() - 2][getCurrentTile()
								                			.getGridPosY()].getOccupier().getDirection() == getDirection())
						if(getCurrentTile() instanceof Lane){
							if(((Lane)getCurrentTile()).getLaneNo() == 2){
								//Overtake DOWN
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1]);
									isOvertaking = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
									isOvertaking = true;	
								}
							}
						}
				}
				break;
			default:break;
			}
		}catch(ArrayIndexOutOfBoundsException e){

		}
	}
	
	public void setBehavior(Behavior b){
		behavior = b;
	}
	
	public Behavior getBehavior(){
		return behavior;
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
							new MoveTo( getX() - 50,  getY()),
							new CubicCurveTo( getX(),  getY(),  getX(),
									 getY() - 100,  getX() - 100,  getY() - 95))
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
