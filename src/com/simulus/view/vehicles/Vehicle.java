	package com.simulus.view.vehicles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.TurningDirection;
import com.simulus.view.Tile;
import com.simulus.view.intersection.CustomPath;
import com.simulus.view.intersection.Intersection;
import com.simulus.view.map.Lane;

/**
 * The base class for all vehicles represented in the simulation.
 * Vehicles are an extension to {@link javafx.scene.shape.Rectangle}.
 */
public abstract class Vehicle extends Rectangle {

	protected Direction dir;
	protected int mapSize;
	protected Tile[][] map;
	protected MainApp parent;
	protected Tile currentTile;
	protected List<Tile> occupiedTiles;
	protected Behavior behavior;
	
	protected Intersection currentIntersection;
	protected CustomPath currentPath;
	protected PathTransition pathTransition;
	protected Transition currentTransition;
	
	protected Random rand;

	protected int tileWidth;
	protected Tile moveToTile;
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
	 * @param dir The direction the vehicle should move in
	 */
	public Vehicle(double posX, double posY, double width, double height,
			Direction dir) {
		super(posX, posY, width, height);
		this.parent = MainApp.getInstance();
		this.dir = dir;
		map = SimulationController.getInstance().getMap().getTiles();
		tileWidth = Configuration.getTileSize();
		mapSize = map.length;
		occupiedTiles = new ArrayList<>();
		
		try {
			currentTile = map[(int) (posX / mapSize)][(int) (posY / mapSize)];
		} catch (ArrayIndexOutOfBoundsException e) {
			currentTile = null;
		}
		
		rand = new Random();
	}

	/**
	 * Describes a vehicle translation
	 */
	public abstract void moveVehicle();

	/**
	 * Moves the vehicle, i.e. the rectangle in its current direction.
	 * The vehicle accelerates with each call of this method and is then moved according 
	 * to the resulting speed.
	 * @param d The direction the vehicle is moving in. 
	 */
	public void move(Direction d) {
		
		//Accelerate
		if(tempDir != Direction.NONE && vehicleSpeed+acceleration < maxSpeed)
			vehicleSpeed += acceleration;
		else if(tempDir == Direction.NONE)
			vehicleSpeed = 0; 
		
		// Moves the car in the direction it should go.
		switch (d) {
		case NORTH:
			setY(getY() - vehicleSpeed);
			break;
		case SOUTH:
			setY(getY() + vehicleSpeed);			
			break;
		case EAST:
			setX(getX() + vehicleSpeed);
			break;
		case WEST:
			setX(getX() - vehicleSpeed);
			break;
		case NONE:
			//Increase the waitingcounter to keep track of how often cars have to stop.
			waitedCounter++;
			break;
		}
	}

	/**
	 * Moves the vehicle to the passed tile with a transition.
	 * @param moveToTile The tile to move to.
	 */
	public void overtake(Tile moveToTile){
		getTransforms().clear();
		CustomPath path = new CustomPath(
                		//from 
                		new MoveTo(getCurrentTile().getCenterX(), getCurrentTile().getCenterY()),
                        new LineTo(moveToTile.getCenterX(), moveToTile.getCenterY()));
		
               
        path.setStroke(Color.DODGERBLUE);
        path.getStrokeDashArray().setAll(5d,5d);
        currentPath = path;
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
            }
        });
        setCurrentTile(moveToTile);
        this.moveToTile = moveToTile;
        pathTransition.setRate(50/SimulationController.getInstance().getTickTime());
        currentTransition = pathTransition;
        pathTransition.play();
	}
	
	/**
	 * Makes the vehicle change into its adjacent lane if possible
	 */
	public void changeLane() {
		
		int vX = currentTile.getGridPosX();
		int vY = currentTile.getGridPosY();

		if(! (currentTile instanceof Lane))
			return;
		
		Lane currentLane = (Lane) currentTile;
		
		switch(currentLane.getLaneNo()) {
		case 0:
			if(dir == Direction.NORTH) {
				//instanceof-check ensure that cars dont merge within an intersection
				//only merge if adjacent and next tile is free
				if(map[vX+1][vY-2] instanceof Lane && !map[vX+1][vY-1].isOccupied() && !map[vX+1][vY].isOccupied()) 
					overtake(map[vX+1][vY-1]);										
				}
			else if(dir == Direction.EAST) {
				if(map[vX+2][vY+1] instanceof Lane && !map[vX+1][vY+1].isOccupied() && !map[vX][vY+1].isOccupied())
					overtake(map[vX+1][vY+1]);
			}
			break;
		case 1:
			if(dir == Direction.NORTH) {
				//instanceof-check ensure that cars dont merge within an intersection
				if(map[vX-1][vY-2] instanceof Lane && !map[vX-1][vY-1].isOccupied() && !map[vX-1][vY].isOccupied()) 
					overtake(map[vX-1][vY-1]);										
				}
			else if(dir == Direction.EAST) {
				if(map[vX+2][vY-1] instanceof Lane && !map[vX+1][vY-1].isOccupied() && !map[vX][vY-1].isOccupied())
					overtake(map[vX+1][vY-1]);
			}
			break;
		case 2:
			if(dir == Direction.SOUTH) {
				if(map[vX+1][vY+2] instanceof Lane && !map[vX+1][vY+1].isOccupied() && !map[vX+1][vY].isOccupied())
					overtake(map[vX+1][vY+1]);
			}
			else if(dir == Direction.WEST) {
				if(map[vX-2][vY+1] instanceof Lane && !map[vX-1][vY+1].isOccupied() && !map[vX][vY+1].isOccupied())
					overtake(map[vX-1][vY+1]);
			}
			break;
		case 3:
			if(dir == Direction.SOUTH) {
				if(map[vX-1][vY+2] instanceof Lane && !map[vX-1][vY+1].isOccupied() && !map[vX-1][vY].isOccupied())
					overtake(map[vX-1][vY+1]);
			}
			else if(dir == Direction.WEST) {
				if(map[vX-2][vY-1] instanceof Lane && !map[vX-1][vY-1].isOccupied() && !map[vX][vY-1].isOccupied())
					overtake(map[vX-1][vY-1]);
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

	/**
	 * Checks the intersection this vehicle is currently in for occupied tiles and occupies the tiles
	 * that this vehicle currently intersects. If the vehicle touches on a tile that is occupied 
	 * by another vehicle, the transition is paused.
	 */
	public void checkTransitionBlockage(){
		if(currentIntersection != null && isTransitioning()) {
			for(int i = 0; i < currentIntersection.tiles.length ; i++) {
				for(int j = 0; j< currentIntersection.tiles[0].length;j++) {
					if(currentIntersection.tiles[i][j].isOccupied() && currentIntersection.tiles[i][j].getOccupier() != null && currentIntersection.tiles[i][j].getOccupier() != this && currentIntersection.tiles[i][j].getOccupier().isTransitioning()) {
						if(currentPath.getBoundsInParent().intersects(currentIntersection.tiles[i][j].getBoundsInParent())) {
							if(currentTransition.getCurrentTime().lessThan(currentIntersection.tiles[i][j].getOccupier().getCurrentTransition().getCurrentTime())) {
								currentTransition.setRate(0);
								return;
							}else if(currentIntersection.tiles[i][j].getOccupier().getCurrentTransition() == null){
								currentTransition.setRate(0);
								return;
							}else if(currentPath.getEndTile().isOccupied()){
								currentTransition.setRate(0);
							}
						}
					}
					
					//Occupy correct tiles while transitioning
					if(currentIntersection.tiles[i][j].getFrame().getBoundsInParent().intersects(this.getBoundsInParent())) {
						currentIntersection.tiles[i][j].setOccupied(true, this);
					}
					else currentIntersection.tiles[i][j].setOccupied(false, this);
				}
			}
		}
		currentTransition.setRate(50/SimulationController.getInstance().getTickTime());
	}
	
	/**
	 * Makes the vehicle follow a provided {@link com.simulus.view.intersection.CustomPath}.
	 * Is used to make vehicles move through intersections as soon as they encounter an
	 * {@link com.simulus.view.intersection.IntersectionTile} with active paths.
	 * @param p The path to follow
	 */
	public void followPath(CustomPath p){
		
		getTransforms().clear();
		currentPath = p;
		
		pathTransition = new PathTransition(Duration.millis(1250), p, this);
		pathTransition.setInterpolator(Interpolator.LINEAR);
		pathTransition.setOrientation(OrientationType.NONE);
		
		Transition transition;
		RotateTransition rt = new RotateTransition(Duration.UNKNOWN, this);
		if(p.getTurn() == TurningDirection.RIGHT){
			rt.setToAngle(getRotate() + 90);
			rt.setDuration(Duration.millis(1250));
			pathTransition.setDuration(Duration.millis(1250));
			transition = new ParallelTransition(this, rt, pathTransition);
		} else if(p.getTurn() == TurningDirection.LEFT){
			rt.setToAngle(getRotate() - 90);
			rt.setDuration(Duration.millis(750));
			pathTransition.setDuration(Duration.millis(750));			
			transition = new ParallelTransition(this, rt, pathTransition);
		} else transition = pathTransition;
		        
        transition.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				for(Tile t : occupiedTiles)
					t.setOccupied(false);
				
				if(SimulationController.getInstance().getAnimationThread().isAlive()){
					 //Manually unoccupy the final tile leaving the transition, to ensure that paths are cleared
	                switch(p.getEndDirection()){
	                case NORTH:
	                	SimulationController.getInstance().getMap().getTiles()[p.getEndTile().getGridPosX()][p.getEndTile().getGridPosY()+1].setOccupied(false, Vehicle.this);
	                	break;
	                case SOUTH:
	                	SimulationController.getInstance().getMap().getTiles()[p.getEndTile().getGridPosX()][p.getEndTile().getGridPosY()-1].setOccupied(false, Vehicle.this);
	                	break;
	                case EAST:
	                	SimulationController.getInstance().getMap().getTiles()[p.getEndTile().getGridPosX()-1][p.getEndTile().getGridPosY()].setOccupied(false, Vehicle.this);
	                	break;
	                case WEST:
	                	SimulationController.getInstance().getMap().getTiles()[p.getEndTile().getGridPosX()+1][p.getEndTile().getGridPosY()].setOccupied(false, Vehicle.this);
	                	break;
					default:
						break;
	                }
	               
	                setCurrentTile(p.getEndTile());
	                p.getEndTile().setOccupied(true, Vehicle.this);
	                dir = p.getEndDirection();
				}
			}
		});
        
        transition.setRate(50/SimulationController.getInstance().getTickTime());
        currentTransition = transition;
        transition.play();
	}
	
	/**
	 * Checks whether it is save to overtake the vehicle / blockage in front of this vehicle.
	 * If so, {@link Vehicle#overtake(Tile)} will be executed. 
	 */
	protected void attemptOvertake(){
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
							
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
								
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
								
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake LEFT
								if(!map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()+1]);
								
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
								
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 1){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1]);
									
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
							
								}
							}else if(((Lane)getCurrentTile()).getLaneNo() == 3){
								//Overtake UP
								if(!map[getCurrentTile().getGridPosX()][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()+1][getCurrentTile().getGridPosY()-1].isOccupied()
										&& !map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1].isOccupied()){
									overtake(map[getCurrentTile().getGridPosX()-1][getCurrentTile().getGridPosY()-1]);
									
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

	/**
	 * Removes this car from the canvas it is drawn on.
	 */
	public void removeFromCanvas() {
        if (parent.getCanvas().getChildren().contains(this))
            parent.getCanvas().getChildren().remove(this);
	}
	
	/**
	 * Adds this vehicle to the canvas that is set as its parent.
	 * @see Rectangle#parentProperty() 
	 */
	public void addToCanvas() {
		if (!parent.getCanvas().getChildren().contains(this))
			parent.getCanvas().getChildren().add(this);
	}
	
	/*
	 * Getters & Setters
	 */
	
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

	public Tile getCurrentTile() {
		return currentTile;
	}
	
	/**
	 * Adds a tile to the set of tiles occupied by this vehicle. 
	 * @param t The tile to be added.
	 */
	private void addTile(Tile t){
        synchronized(this) {
            if (!occupiedTiles.contains(t))
                occupiedTiles.add(t);
        }
	}
	
	/**
	 * Removes a tile from the set of tiles occupied by this vehicle. 
	 * @param t The tile to be removed.
	 */
	public void removeTile(Tile t){
        synchronized (this) {
            if (occupiedTiles.contains(t))
                occupiedTiles.remove(t);
        }
	}
	
	/**
	 * @return A list of the tiles that this vehicle currently occupies.
	 */
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

	public int getWaitedCounter() {
		return waitedCounter;
	}
	
	/**
	 * @return {@code true} if this vehicle is currently executing a transition, {@code false} otherwise.
	 */
	public boolean isTransitioning() {
		if(currentTransition != null && currentTransition.getStatus() == Animation.Status.RUNNING)
			return true;
		return false;
	}
	
	public Transition getCurrentTransition() {
		return currentTransition;
	}
	
	public CustomPath getCurrentPath(){
		return currentPath;
	}
	
	public String toString(){
		String s = " ";
		if(this instanceof Truck)
			s+="Truck: ";
		else s+="Car: ";
		s+= "Dir: "+ dir+ "Current Tile: "+ currentTile + "Transitioning: " + isTransitioning();
		return s;
	}
}
