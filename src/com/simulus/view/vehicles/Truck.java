package com.simulus.view.vehicles;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;
import com.simulus.view.intersection.CustomPath;
import com.simulus.view.intersection.IntersectionTile;
import com.simulus.view.map.Lane;

public class Truck extends Vehicle {

	public static final int TRUCKWIDTH = 12;
	public static final int TRUCKLENGTH = 20;
	
	private static final Color COLOUR = Color.LIGHTSALMON;
	
	public Truck(double posX, double posY, Direction dir) {
		super(posX, posY, TRUCKWIDTH, TRUCKLENGTH, dir);

		behavior = Behavior.getRandomBehavior();
		
		switch (dir) {
		case NORTH:
		case SOUTH:
			setWidth(TRUCKWIDTH);
			setHeight(TRUCKLENGTH);
			break;
		case EAST:
		case WEST:
			setWidth(TRUCKLENGTH);
			setHeight(TRUCKWIDTH);
			break;
		default:
			break;
		}
	
		setFill(COLOUR);
		Random rand = new Random();
		maxSpeed = rand.nextInt(2)+3;
		
		//0.4px/tick acceleration equals a real-life acceleration of ~0,62m/s^2, i.e 0-100km/h in 45 secs.
		acceleration = 0.4d;
		vehicleSpeed = 0.0d;
		
		addToCanvas();
	}

	@Override
	public void moveVehicle() {
			
		if(isTransitioning()){
			return;
		}
		
		Translate trans = new Translate();
		
		final double dx;
		final double dy;

		Direction temp = getDirection();
		Behavior tempBehavior = behavior;
		
		if(tempBehavior == Behavior.SEMI)
			if(Math.random()>0.5)
				tempBehavior = Behavior.RECKLESS;
			else tempBehavior = Behavior.CAUTIOUS;


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
			} else if(nextTile instanceof IntersectionTile) { 
				if(currentTile instanceof Lane && Math.random()>0.6) {
					IntersectionTile t = (IntersectionTile) nextTile;
				 	currentIntersection = t.getIntersection();
				 	CustomPath p = t.getTurningPaths().get(rand.nextInt(t.getTurningPaths().size()));
				 	if(p.getActive())
				 		followPath(p);
//	          		isTransitioning = true;
	          		return;
				 }
			} else tempDir = getDirection(); //if next tile is not occupied and not an intersection, carry on.
		} catch (ArrayIndexOutOfBoundsException e) {
            SimulationController.getInstance().removeVehicle(this);
		}

		//Accelerate
		if(temp != Direction.NONE && vehicleSpeed+acceleration < maxSpeed)
			vehicleSpeed += acceleration;
		else if(temp == Direction.NONE)
			vehicleSpeed = 0;  
		
		//Moves the car in the direction it should go.
		move(temp);
	}

	

}
