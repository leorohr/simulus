package com.simulus.view.vehicles;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;

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
		double speedInMps = ((double)SimulationController.getInstance().getMaxCarSpeed()*1000)/3600;
		//0.5m/s^2; 1m=tilesize/5; 1 tick = 1/10s; 0-80
		acceleration = (0.5d * Configuration.tileSize/5)/10;
		//Any speed within the maxspeed range, at least 10km/h (i.e. 1.111px per tick)
		maxSpeed = rand.nextDouble()*((speedInMps * (Configuration.tileSize/5))/10 -1.111d) + 1.111d;

		addToCanvas();
	}

	@Override
	public void moveVehicle() {
			
		if(isTransitioning){
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
			switch (getDirection()) {
			case NORTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() - 1].isOccupied()) {
					temp = Direction.NONE;
					
				} else
					temp = getDirection();
				break;
			case SOUTH:
				if (map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() + 1].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				break;
			case EAST:
				if (map[getCurrentTile().getGridPosX() + 1][getCurrentTile()
						.getGridPosY()].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				break;
			case WEST:
				if (map[getCurrentTile().getGridPosX() - 1][getCurrentTile()
						.getGridPosY()].isOccupied()) {
					temp = Direction.NONE;
				} else
					temp = getDirection();
				break;
			default:
				break;
			}
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
