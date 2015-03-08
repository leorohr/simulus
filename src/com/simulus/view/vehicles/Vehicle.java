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
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import com.simulus.MainApp;
import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;
import com.simulus.util.enums.Behavior;
import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;
import com.simulus.view.intersection.CustomPath;
import com.simulus.view.intersection.Intersection;
import com.simulus.view.intersection.IntersectionTile;
import com.simulus.view.map.Lane;

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
	 */
	public Vehicle(double posX, double posY, double width, double height,
			Direction dir) {
		super(posX, posY, width, height);
		this.parent = MainApp.getInstance();
		this.dir = dir;
		map = SimulationController.getInstance().getMap().getTiles();
		tileWidth = Configuration.tileSize;
		mapSize = map.length;
		occupiedTiles = new ArrayList<>();
		
		try { //TODO this is not correct, is it?
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
	
	public void updateTransitionTiles(){
		//Check if the vehicle occupies tiles throughout the transition
		if(currentIntersection != null)
		for(int i = 0; i < currentIntersection.tiles.length; i++)
			for(int j = 0; j< currentIntersection.tiles[0].length;j++)
				if(currentIntersection.tiles[i][j].getFrame().getBoundsInParent().intersects(this.getBoundsInParent()))
					currentIntersection.tiles[i][j].setOccupied(true, this);
				else currentIntersection.tiles[i][j].setOccupied(false, this);
	}
	
	public void checkTransitionBlockage(){
		if(currentIntersection != null && isTransitioning())
			for(int i = 0; i < currentIntersection.tiles.length; i++)
				for(int j = 0; j< currentIntersection.tiles[0].length;j++)
					if(currentIntersection.tiles[i][j].isOccupied() && currentIntersection.tiles[i][j].getOccupier() != null && currentIntersection.tiles[i][j].getOccupier() != this && currentIntersection.tiles[i][j].getOccupier().isTransitioning())
						if(currentPath.getBoundsInParent().intersects(currentIntersection.tiles[i][j].getBoundsInParent()))
							if(currentTransition.getCurrentTime().lessThan(currentIntersection.tiles[i][j].getOccupier().getCurrentTransition().getCurrentTime())){
								currentTransition.setRate(0);
								return;
							}else if(currentIntersection.tiles[i][j].getOccupier().getCurrentTransition() == null){
								currentTransition.setRate(0);
								return;
							}else if(currentPath.getEndTile().isOccupied()){
								currentTransition.setRate(0);
							}
		currentTransition.setRate(50/SimulationController.getInstance().getTickTime());
	}
	
	public void followPath(CustomPath p){
		if(p.getEndTile().isOccupied())
			return;
		
		getTransforms().clear();
		currentPath = p;
		ParallelTransition transition;
		RotateTransition rt = new RotateTransition(Duration.millis(100*(p.getDistance()/getVehicleSpeed())), this);
		double duration = 1000;
		
		if(p.getTurn() == "right"){
			rt.setToAngle(90);
			duration = 1250;
		}
			
		if(p.getTurn() == "left"){
			rt.setToAngle(-90);
			duration = 750;
		}
		rt.setDuration(Duration.millis(duration));
		        
		pathTransition = new PathTransition(Duration.millis(duration), p, this);
		pathTransition.setInterpolator(Interpolator.LINEAR);
		pathTransition.setOrientation(OrientationType.NONE);

        transition = new ParallelTransition(this, rt, pathTransition);
        transition.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				Vehicle v = null;
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
	                
					if(Vehicle.this instanceof Car){
	                v = new Car(p.getEndTile().getGridPosX() * tileWidth + tileWidth / 2
	    					- Car.CARWIDTH / 2, p.getEndTile().getGridPosY() * tileWidth
	    					+ tileWidth - Car.CARLENGTH, p.getEndDirection());
					}else {
						v = new Truck(p.getEndTile().getGridPosX() * tileWidth + tileWidth / 2
	    					- Car.CARWIDTH / 2, p.getEndTile().getGridPosY() * tileWidth
	    					+ tileWidth - Car.CARLENGTH, p.getEndDirection());
						//the truck count is reduced when the truck is removed later on
						SimulationController.getInstance().incTruckCount(); 
					}
	                v.setCurrentTile(p.getEndTile());
	                p.getEndTile().setOccupied(true, v);
	                v.setVehicleSpeed(getVehicleSpeed());
	                v.setBehavior(getBehavior());
	                v.setFill(getFill());
	                v.setMap(SimulationController.getInstance().getMap().getTiles());
	                v.setWaitedCounter(waitedCounter);
	                SimulationController.getInstance().getMap().getVehicles().add(v);
				}
                
                
               
                SimulationController.getInstance().removeVehicle(Vehicle.this);
			}
		});
        transition.setRate(50/SimulationController.getInstance().getTickTime());
        //System.out.println(50/SimulationController.getInstance().getTickTime());
        currentTransition = transition;
        
        transition.play();
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

	public int getWaitedCounter() {
		return waitedCounter;
	}
	
	public void setWaitedCounter(int waitedCounter) {
		this.waitedCounter = waitedCounter;
	}
	
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
