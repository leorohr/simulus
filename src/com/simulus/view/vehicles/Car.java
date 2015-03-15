package com.simulus.view.vehicles;

import javafx.scene.paint.Color;

import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;
import com.simulus.view.intersection.IntersectionTile;
import com.simulus.view.map.Lane;

/**
 * Describes a car object on the GUI
 */
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
		
	
		 
		double speedInMps = ((double)SimulationController.getInstance().getMaxCarSpeed()*1000)/3600;
		//2.32m/s^2; 1m=tilesize/5; 1 tick = 1/10s
		acceleration = (2.32d * Configuration.tileSize/5)/10;
		//Any speed within the maxspeed range, at least 10km/h (i.e. 1.111px per tick)
		maxSpeed = rand.nextDouble()*((speedInMps * (Configuration.tileSize/5))/10 -1.111d) + 1.111d;
		addToCanvas();
	}
	
	/**
	 * Translates the vehicle according to the current direction
	 */
	public void moveVehicle() {
		
		if(isTransitioning()){
			checkTransitionBlockage();
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
                break;
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
				if(nextTile.isBlock())
					changeLane();
			} else if(nextTile instanceof IntersectionTile) { 
				if(currentTile instanceof Lane) {
					IntersectionTile t = (IntersectionTile) nextTile;
				 	currentIntersection = t.getIntersection();
				 	followPath(t.getRandomPath());
				 }
			} else tempDir = getDirection(); //if next tile is not occupied and not an intersection, carry on.
			

            //Slow the car down if cautious and slow car in front
            if(tempBehavior == Behavior.CAUTIOUS)
                if(nextTile != null && nextTile.getOccupier()!=null)
                    setVehicleSpeed(nextTile.getOccupier().getVehicleSpeed());

		} catch (ArrayIndexOutOfBoundsException e) {
			SimulationController.getInstance().removeVehicle(this);
		}
		
		move(tempDir);
	}
	
}
