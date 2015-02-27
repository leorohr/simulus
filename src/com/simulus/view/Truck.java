package com.simulus.view;

import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

import java.util.Random;

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
		vehicleSpeed = rand.nextInt(2)+2;
		addToCanvas();
	}

	@Override
	public void moveVehicle() {
			
		if(isOvertaking){
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

//		//Checks if the tile 2 tiles ahead of the car is taken for overtaking.
//		if(tempBehavior == Behavior.RECKLESS){
//			try {
//				switch(getDirection()){
//				case NORTH:
//					if (map[getCurrentTile().getGridPosX()][getCurrentTile()
//					                						.getGridPosY() - 2].isOccupied()) {
//						if(getCurrentTile() instanceof Lane){
//							if(((Lane)getCurrentTile()).getLaneNo() == 0){
//								//Overtake RIGHT
//								if(!map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
//									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1]);
//									isOvertaking = true;
//								}
//							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
//								//Overtake LEFT
//								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
//									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
//									isOvertaking = true;
//								}
//							}
//						}
//					}
//					break;
//				case SOUTH:
//					if (map[getCurrentTile().getGridPosX()][getCurrentTile()
//					                						.getGridPosY() + 2].isOccupied()) {
//						if(getCurrentTile() instanceof Lane){
//							if(((Lane)getCurrentTile()).getLaneNo() == 2){
//								//Overtake RIGHT
//								if(!map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1].isOccupied()){
//									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()+1]);
//									isOvertaking = true;
//								}
//							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
//								//Overtake LEFT
//								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
//										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
//									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1]);
//									isOvertaking = true;
//								}
//							}
//						}
//					}
//					break;
//				case EAST:
//					if (map[getCurrentTile().getGridPosX() + 2][getCurrentTile()
//					                    						.getGridPosY()].isOccupied()) {
//
//					}
//					break;
//				case WEST:
//					if (map[getCurrentTile().getGridPosX() - 2][getCurrentTile()
//					                    						.getGridPosY()].isOccupied()) {
//
//					}
//					break;
//				default:break;
//				}
//			}catch(ArrayIndexOutOfBoundsException e){
//                SimulationController.getInstance().removeVehicle(this);
//			}
//		}
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

		//Moves the car in the direction it should go.
		switch (temp) {
		case NORTH:

			dx = 0;
			dy = -getVehicleSpeed();

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);

			break;
		case SOUTH:
			dx = 0;
			dy = getVehicleSpeed();

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);

			break;
		case EAST:
			dx = getVehicleSpeed();
			dy = 0;

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			break;
		case WEST:
			dx = -getVehicleSpeed();
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

	@Override
	public void overtake(Tile t) {
		// TODO Auto-generated method stub
		
	}

}
