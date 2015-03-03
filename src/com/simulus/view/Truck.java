package com.simulus.view;

import java.util.Random;

import javafx.scene.paint.Color;

import com.simulus.controller.SimulationController;
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
		maxSpeed = rand.nextInt(2)+3;
		
		//0.4px/tick acceleration equals a real-life acceleration of ~0,62m/s^2, i.e 0-100km/h in 45 secs.
		acceleration = 0.4d;
		vehicleSpeed = 0.0d;
		
		addToCanvas();
	}

	@Override
	public void moveVehicle() {

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
					if(tempBehavior == Behavior.CAUTIOUS)
						if(map[getCurrentTile().getGridPosX()][getCurrentTile()
						                         						.getGridPosY() - 1].getOccupier()!=null)
						setVehicleSpeed(map[getCurrentTile().getGridPosX()][getCurrentTile()
						                         						.getGridPosY() - 1].getOccupier().getVehicleSpeed());
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
		if(isPaused)
			temp = Direction.NONE;
		
		//Moves the car in the direction it should go.
		move(temp);
	}

}
