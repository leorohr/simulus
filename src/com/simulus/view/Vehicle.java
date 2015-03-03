package com.simulus.view;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;

/**
 * Describes a vehicle on the GUI
 */
public abstract class Vehicle extends Rectangle {

	protected Direction dir;
	protected int mapSize;
	protected Tile[][] map;
	protected MainApp parent;
	protected Tile currentTile;
	protected List<Tile> occupiedTiles;
	protected boolean isOvertaking = false;
	protected Behavior behavior;
	protected double temporarySpeed;
	protected PathTransition pathTransition;
	protected boolean isPaused;
	
	protected Behavior tempBehavior;
	protected Direction tempDir;
	protected double acceleration;
	protected double maxSpeed;
	protected double vehicleSpeed = 0;
	protected int waitedCounter = 0;

	/**
	 * Initialises the position and size of the vehicle
	 * 
	 * @param posX
	 *            The position of the vehicle on X
	 * @param posY
	 *            The position of the vehicle on Y
	 * @param width
	 *            The width of the vehicle
	 * @param height
	 *            The height of the vehicle
	 */
	public Vehicle(double posX, double posY, double width, double height,
			Direction dir) {
		super(posX, posY, width, height);
		this.parent = MainApp.getInstance();
		this.dir = dir;
		map = SimulationController.getInstance().getMap().getTiles();
		mapSize = map.length;
		occupiedTiles = new ArrayList<>();
		try {
			currentTile = map[(int) (posX / mapSize)][(int) (posY / mapSize)];
		} catch (ArrayIndexOutOfBoundsException e) {
			currentTile = null;
		}
	}

	/**
	 * Describes a vehicle translation
	 */
	public abstract void moveVehicle();

	public void move(Direction d) {
		Translate trans = new Translate();

		final double dx;
		final double dy;
		
		//Accelerate
		if(tempDir != Direction.NONE && vehicleSpeed+acceleration < maxSpeed)
			vehicleSpeed += acceleration;
		else if(tempDir == Direction.NONE)
			vehicleSpeed = 0; 
		
		// Moves the car in the direction it should go.
		switch (d) {
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
			//Increase the waitingcounter to keep track of how often cars have to stop.
			waitedCounter++;
			
			dx = 0;
			dy = 0;

			trans.setX(dx);
			trans.setY(dy);
			getTransforms().add(trans);
			break;
		}
	}

	/**
	 * Moves the vehicle to the passed tile with a transition.
	 * @param moveToTile The tile to move to.
	 */
	public void overtake(Tile moveToTile){
		getTransforms().clear();
		Path path = new Path(
                		//from 
                		new MoveTo(getCurrentTile().getCenterX(), getCurrentTile().getCenterY()),
                        new LineTo(moveToTile.getCenterX(), moveToTile.getCenterY()));
		
               
        path.setStroke(Color.DODGERBLUE);
        path.getStrokeDashArray().setAll(5d,5d);
        MainApp.getInstance().getCanvas().getChildren().add(path);
       
        double pathDistance = Math.sqrt(Math.pow(path.getBoundsInParent().getMaxX()-path.getBoundsInParent().getMinX(), 2)
        		+Math.pow(path.getBoundsInParent().getMinY()-path.getBoundsInParent().getMaxY(), 2));

        //Ensure that cars are moving before they try to overtake
        if(vehicleSpeed == 0)
        	vehicleSpeed += 2*acceleration; 
        double carSpeed = (getVehicleSpeed()/SimulationController.getInstance().getTickTime());
        		
        double pathTime = pathDistance/carSpeed;
        
        
        pathTransition = new PathTransition(Duration.millis(pathTime), path, this);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setOrientation(OrientationType.NONE);
        
        pathTransition.setOnFinished(new EventHandler<ActionEvent>(){
 
            @Override
            public void handle(ActionEvent arg0) {
            	MainApp.getInstance().getCanvas().getChildren().remove(path);
                isOvertaking = false;
                
            }
        });
        setCurrentTile(moveToTile);
        pathTransition.play();
	};
	
	/**
	 * Makes the vehicle change into the adjacent lane if possible
	 */
	public void changeToLeftLane() {
		
		int vX = currentTile.getGridPosX();
		int vY = currentTile.getGridPosY();

		if(! (currentTile instanceof Lane))
			return;
		
		Lane currentLane = (Lane) currentTile;
		
		switch(currentLane.getLaneNo()) {
		case 1:
			if(dir == Direction.NORTH) {
				if(map[vX-1][vY-2] instanceof Lane && !map[vX-1][vY-1].isOccupied()) //instanceof-check ensure that cars dont merge
					overtake(map[vX-1][vY-1]);										 //within an intersection
				}
			else if(dir == Direction.EAST) {
				if(map[vX+2][vY-1] instanceof Lane && !map[vX+1][vY-1].isOccupied())
					overtake(map[vX+1][vY-1]);
			}
			break;
		case 2:
			if(dir == Direction.SOUTH) {
				if(map[vX+1][vY+2] instanceof Lane && !map[vX+1][vY+1].isOccupied())
					overtake(map[vX+1][vY+1]);
			}
			else if(dir == Direction.WEST) {
				if(map[vX-2][vY+1] instanceof Lane && !map[vX-1][vY+1].isOccupied())
					overtake(map[vX-1][vY+1]);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * Slows down the vehicle and forces a higher distance to the car in front
	 */
	public void allowMerge() {
		
		int vX = currentTile.getGridPosX();
		int vY = currentTile.getGridPosY();
		boolean frontTilesFree = true; 
		
		try {
			switch(dir) {
			case EAST:
				for(int i=1; i<=3; i++)
					frontTilesFree &= !map[vX+i][vY].isOccupied();
				break;
			case NORTH:
				for(int i=1; i<=3; i++)
					frontTilesFree &= !map[vX][vY-i].isOccupied();
				break;
			case SOUTH:
				for(int i=1; i<=3; i++)
					frontTilesFree &= !map[vX][vY+i].isOccupied();
				break;
			case WEST:
				for(int i=1; i<=3; i++)
					frontTilesFree &= !map[vX-i][vY].isOccupied();
				break;
			default:
				break;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			return; //dont stop the car if it is close to the edge of the map.
		}
		
		if(!frontTilesFree)
			vehicleSpeed = 0.0d;
	}

	public Direction getDirection() {
		return dir;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	public void setMap(Tile[][] map) {
		this.map = map;
	}

	public void setCurrentTile(Tile t) {
        currentTile = t;
        addTile(t); //add current tile to list of occupied tiles
    }

	public void removeFromCanvas() {
        if (parent.getCanvas().getChildren().contains(this))
            parent.getCanvas().getChildren().remove(this);
	}

	public Tile getCurrentTile() {
		return currentTile;
	}

	public void addToCanvas() {
		if (!parent.getCanvas().getChildren().contains(this))
			parent.getCanvas().getChildren().add(this);
	}

	private void addTile(Tile t){
        synchronized(this) {
            if (!occupiedTiles.contains(t))
                occupiedTiles.add(t);
        }
	}
	
	public void removeTile(Tile t){
        synchronized (this) {
            if (occupiedTiles.contains(t))
                occupiedTiles.remove(t);
        }
	}

	public List<Tile> getOccupiedTiles(){
        synchronized (this) {
            return occupiedTiles;
        }
    }
	
	public double getVehicleSpeed(){
		return vehicleSpeed;
	}
	
	public void setVehicleSpeed(double d){
		vehicleSpeed = d;
	}
	
	public void setBehavior(Behavior b){
		behavior = b;
	}
	
	public Behavior getBehavior(){
		return behavior;
	}

	public double getWaitedCounter() {
		return waitedCounter;
	}
}
