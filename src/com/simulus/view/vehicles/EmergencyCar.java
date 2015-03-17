package com.simulus.view.vehicles;

import javafx.scene.paint.Color;

import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;
import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;

/**
 * An extension to {@link com.simulus.view.vehicles.Car}
 * EmergencyCars have an {@link AreaOfEffect} associated with them. They will always try 
 * to drive with the max. allowed speed and do not obey red lights.
 */
public class EmergencyCar extends Car {

	private static final Color COLOUR = Color.YELLOW;

	public EmergencyCar(double posX, double posY, Direction dir) {
		super(posX, posY, dir);

		setFill(COLOUR);
		
		//3.48m/s^2; 1m=tilesize/5; 1 tick = 1/10s
		acceleration = (3.48 * Configuration.getTileSize()/5)/10;
		double speedInMps = ((double)SimulationController.getInstance().getMaxCarSpeed()*1000)/3600;
		maxSpeed = speedInMps * (Configuration.getTileSize()/5)/10;
		vehicleSpeed = 0;
		setArcHeight(0);
		setArcWidth(0);
	}

	/**
	 * @see com.simulus.view.vehicles.Car#moveVehicle()
	 */
	@Override
	public void moveVehicle() {
		
		tempDir = getDirection();

		try {
			Tile nextTile = null;
			switch (getDirection()) {
			case NORTH:
				if (currentTile.getGridPosY() - 1 < 0) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() - 1];
				
				break;

			case SOUTH:
				if (currentTile.getGridPosY() + 1 >= map.length) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX()][getCurrentTile()
						.getGridPosY() + 1];
				break;

			case EAST:
				if (currentTile.getGridPosX() + 1 >= map.length) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}

				nextTile = map[getCurrentTile().getGridPosX() + 1][getCurrentTile()
						.getGridPosY()];
				break;

			case WEST:
				if (currentTile.getGridPosX() - 1 < 0) {
					SimulationController.getInstance().removeVehicle(this);
					break;
				}
				nextTile = map[getCurrentTile().getGridPosX() - 1][getCurrentTile()
						.getGridPosY()];
				break;

			default:
				break;
			}
			
			if (nextTile != null && nextTile.isOccupied() && nextTile.getOccupier() != null) {
				tempDir = Direction.NONE;
			} else
				tempDir = getDirection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		move(tempDir);
		((Ambulance) getParent()).updateAoE();
	}

	/**
	 * @see com.simulus.view.vehicles.Vehicle#removeFromCanvas()
	 */
	@Override
	public void removeFromCanvas() {
		((Ambulance) getParent()).remove();
	}

}
