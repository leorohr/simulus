package com.simulus.view;

import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;

/**
 * Describes a car object on the GUI
 */
public class Car extends Vehicle {

	public static final int CARWIDTH = 10;
	public static final int CARLENGTH = 15;	

	private static final int ARCHEIGHT = 10;
	private static final int ARCWIDTH = 10;
	
	private Random rand;
	
	
	

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
		
		rand = new Random();
		acceleration = 1.5d;
		maxSpeed = rand.nextInt(SimulationController.getInstance().getMaxCarSpeed()-2)+3;
		addToCanvas();
	}
	
	/**
	 * Translates the vehicle according to the current direction
	 */
	public void moveVehicle() {
		
		if(isTransitioning){
			updateTransitionTiles();
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
                    return;
                }

                nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY() - 1];
               
				break;

			case SOUTH:
                if(currentTile.getGridPosY()+1 >= map.length) {
                    SimulationController.getInstance().removeVehicle(this);
                    return;
                }

                nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY() + 1];
                
				break;

			case EAST:
                if(currentTile.getGridPosX()+1 >= map.length) {
                    SimulationController.getInstance().removeVehicle(this);
                    return;
                }

                nextTile = map[getCurrentTile().getGridPosX() + 1][getCurrentTile().getGridPosY()];
				

			case WEST:
                if(currentTile.getGridPosX()-1 < 0) {
                    SimulationController.getInstance().removeVehicle(this);
                    return;
                }

                nextTile = map[getCurrentTile().getGridPosX() - 1][getCurrentTile().getGridPosY()];
	
				break;
				
			default:
				break;
			}
			 if (nextTile.isOccupied()) {
             	tempDir = Direction.NONE;
			 }
				 else if(nextTile.getTurningPaths().size() >0 && (getCurrentTile() instanceof Lane) && Math.random()>0.6){
					 	currentIntersection = nextTile.getIntersection();
					 	CustomPath p = nextTile.getTurningPaths().get(rand.nextInt(nextTile.getTurningPaths().size()));
					 	if(p.getActive())
					 		followPath(p);
		          		isTransitioning = true;
		          		return;
					 }
				 }
		          		else tempDir = getDirection();
			

            //Slow the car down if cautious and slow car in front
            if(tempBehavior == Behavior.CAUTIOUS)
                if(nextTile != null && nextTile.getOccupier()!=null)
                    setVehicleSpeed(nextTile.getOccupier().getVehicleSpeed());

		} catch (ArrayIndexOutOfBoundsException e) {
			SimulationController.getInstance().removeVehicle(this);
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
									isTransitioning = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
									isTransitioning = true;
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
									isTransitioning = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1]);
									isTransitioning = true;
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
									isTransitioning = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1]);
									isTransitioning = true;	
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
									isTransitioning = true;
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
									isTransitioning = true;	
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
}
