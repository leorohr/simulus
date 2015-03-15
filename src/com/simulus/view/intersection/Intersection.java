package com.simulus.view.intersection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;
import com.simulus.util.ResourceBuilder;
import com.simulus.util.enums.Direction;
import com.simulus.view.Tile;
import com.simulus.view.TileGroup;
import com.simulus.view.map.Map;

public class Intersection extends Group implements TileGroup, Runnable {
	
	public IntersectionTile[][] tiles = new IntersectionTile[4][4];
	private long switchTime;
	private int tileSize = Configuration.getTileSize();
	private ArrayList<CustomPath> turningPaths;
	
	/**
	 * @param xPos x coordinate of the top left tile of the intersection in the grid
	 * @param yPos y coordinate of the top left tile of the intersection in the grid
	 */
	public Intersection(int xPos, int yPos) {
		switchTime = (long) (4000 + Math.random()*3000);
		turningPaths = new ArrayList<>();
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = new IntersectionTile((xPos+i)*tileSize, (yPos+j)*tileSize, tileSize, tileSize, xPos+i, yPos+j);
				tiles[i][j].getFrame().setFill(new ImagePattern(ResourceBuilder.getBoxjunctionTexture()));
				
				this.getChildren().add(SimulationController.getInstance().getMap().getTiles()[i][j]);
			}
		}
		
	}
	
	public void addTurningPaths(Tile[][] m){


		double arcDistanceShort = 90*(Math.PI/180)*(tileSize/2);
		double arcDistanceMedium = 90*(Math.PI/180)*(tileSize*1.5);
		double arcDistanceLong = 90*(Math.PI/180)*(tileSize*2.5);
		double arcDistanceVeryLong = 90*(Math.PI/180)*(tileSize*3.5);
		double straightDistance = 4*tileSize;

		//Left, top
		IntersectionTile t = tiles[0][0];
		//Turn left
		t.getTurningPaths().add(new CustomPath(	"left",arcDistanceShort, m[t.getGridPosX()-1][t.getGridPosY()], m[t.getGridPosX()][t.getGridPosY()-1], Direction.NORTH, new MoveTo(t.getX(), t.getCenterY()),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														tiles[0][0].getCenterX(), tiles[0][0].getY(),
														false, false)));
		
		//Turn right
		t.getTurningPaths().add(new CustomPath(	"right",arcDistanceVeryLong, m[t.getGridPosX()-1][t.getGridPosY()], m[t.getGridPosX()+3][t.getGridPosY()+4], Direction.SOUTH, new MoveTo(t.getX(), t.getCenterY()),				
											new ArcTo(	tileSize*3.5, tileSize*3.5,
														0.0d,
														tiles[3][3].getCenterX(), tiles[3][3].getY() + tileSize,
														false, true)));
		
		//Straight
		t.getTurningPaths().add(new CustomPath(	"straight", straightDistance, m[t.getGridPosX()-1][t.getGridPosY()], m[t.getGridPosX()+4][t.getGridPosY()], Direction.EAST,new MoveTo(t.getX(), t.getCenterY()),				
											new LineTo(tiles[3][0].getX() + tileSize, tiles[3][0].getCenterY())));
		
		//Left, second from top
		t = tiles[0][1];
		//Turn right
		t.getTurningPaths().add(new CustomPath("right",	arcDistanceLong, m[t.getGridPosX()-1][t.getGridPosY()], m[t.getGridPosX()+2][t.getGridPosY()+3], Direction.SOUTH,new MoveTo(t.getX(), t.getCenterY()),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[2][3].getCenterX(), tiles[2][3].getY() + tileSize,
														false, true)));
		
		//Turn left
		t.getTurningPaths().add(new CustomPath("left",	arcDistanceMedium, m[t.getGridPosX()-1][t.getGridPosY()], m[t.getGridPosX()+1][t.getGridPosY()-2], Direction.NORTH,new MoveTo(t.getX(), t.getCenterY()),				
											new ArcTo(	tileSize*1.5, tileSize*1.5,
														0.0d,
														tiles[1][0].getCenterX(), tiles[1][0].getY(),
														false, false)));
		
		//Straight
		t.getTurningPaths().add(new CustomPath(	"straight",straightDistance, m[t.getGridPosX()-1][t.getGridPosY()], m[t.getGridPosX()+4][t.getGridPosY()], Direction.EAST,new MoveTo(t.getX(), t.getCenterY()),				
											new LineTo(tiles[3][1].getX() + tileSize, tiles[3][1].getCenterY())));

		
		//Bottom, leftmost
		t = tiles[0][3];
		//Turn left
		t.getTurningPaths().add(new CustomPath("left",arcDistanceShort, m[t.getGridPosX()][t.getGridPosY()+1], m[t.getGridPosX()-1][t.getGridPosY()], Direction.WEST, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														t.getX(), t.getCenterY(),
														false, false)));

		//Turn right
		t.getTurningPaths().add(new CustomPath("right", arcDistanceVeryLong, m[t.getGridPosX()][t.getGridPosY()+1], m[t.getGridPosX()+4][t.getGridPosY()-3], Direction.EAST, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new ArcTo(	tileSize*3.5, tileSize*3.5,
														0.0d,
														tiles[3][0].getX() + tileSize, tiles[3][0].getCenterY(),
														false, true)));
		//Straight
		t.getTurningPaths().add(new CustomPath( "straight",straightDistance, m[t.getGridPosX()][t.getGridPosY()+1], m[t.getGridPosX()][t.getGridPosY()-4], Direction.NORTH, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new LineTo(t.getCenterX(), tiles[0][0].getY())));

		//Bottom, second from left
		t = tiles[1][3];
		//Turn right
		t.getTurningPaths().add(new CustomPath("right",arcDistanceLong, m[t.getGridPosX()][t.getGridPosY()+1], m[t.getGridPosX()+3][t.getGridPosY()-2],Direction.EAST, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[3][1].getX() + tileSize, tiles[3][1].getCenterY(),
														false, true)));
		
		//Turn left
		t.getTurningPaths().add(new CustomPath("left",arcDistanceMedium, m[t.getGridPosX()][t.getGridPosY()+1], m[t.getGridPosX()-2][t.getGridPosY()-1],Direction.WEST, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new ArcTo(	tileSize*1.5, tileSize*1.5,
														0.0d,
														tiles[0][2].getX(), tiles[0][2].getCenterY(),
														false, false)));

		//Straight
		t.getTurningPaths().add(new CustomPath("straight",straightDistance, m[t.getGridPosX()][t.getGridPosY()+1], m[t.getGridPosX()][t.getGridPosY()-4], Direction.NORTH,new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new LineTo(t.getCenterX(), tiles[0][0].getY())));

		
		//Right, bottom
		t = tiles[3][3];
		//Turn left
		t.getTurningPaths().add(new CustomPath("left",arcDistanceShort, m[t.getGridPosX()+1][t.getGridPosY()], m[t.getGridPosX()][t.getGridPosY()+1],Direction.SOUTH,new MoveTo(t.getX()+tileSize, t.getCenterY()),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														tiles[3][3].getCenterX(), tiles[3][3].getY()+ tileSize,
														false, false)));
		
		//Turn right
		t.getTurningPaths().add(new CustomPath("right",arcDistanceVeryLong, m[t.getGridPosX()+1][t.getGridPosY()], m[t.getGridPosX()-3][t.getGridPosY()-4],Direction.NORTH, new MoveTo(t.getX()+tileSize, t.getCenterY()),				
											new ArcTo(	tileSize*3.5, tileSize*3.5,
														0.0d,
														tiles[0][0].getCenterX(), tiles[0][0].getY(),
														false, true)));
		
		//Straight
		t.getTurningPaths().add(new CustomPath("straight",straightDistance, m[t.getGridPosX()+1][t.getGridPosY()], m[t.getGridPosX()-4][t.getGridPosY()],Direction.WEST,new MoveTo(t.getX() + tileSize, t.getCenterY()),				
											new LineTo(tiles[0][3].getX(), t.getCenterY())));
		
		//Right, second from bottom
		t = tiles[3][2];
		//Turn right
		t.getTurningPaths().add(new CustomPath("right",arcDistanceLong, m[t.getGridPosX()+1][t.getGridPosY()], m[t.getGridPosX()-2][t.getGridPosY()-3],Direction.NORTH,new MoveTo(t.getX()+tileSize, t.getCenterY()),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[1][0].getCenterX(), tiles[1][0].getY(),
														false, true)));
		
		//Turn right
		t.getTurningPaths().add(new CustomPath("left",arcDistanceMedium, m[t.getGridPosX()+1][t.getGridPosY()], m[t.getGridPosX()-1][t.getGridPosY()+2],Direction.SOUTH, new MoveTo(t.getX()+tileSize, t.getCenterY()),				
											new ArcTo(	tileSize*1.5, tileSize*1.5,
														0.0d,
														tiles[2][3].getCenterX(), tiles[2][3].getY() + tileSize,
														false, false)));
		
		//Straight
		t.getTurningPaths().add(new CustomPath("straight",straightDistance, m[t.getGridPosX()+1][t.getGridPosY()], m[t.getGridPosX()-4][t.getGridPosY()],Direction.WEST,new MoveTo(t.getX() + tileSize, t.getCenterY()),				
											new LineTo(tiles[0][3].getX(), t.getCenterY())));
		
		//Top, rightmost
		t = tiles[3][0];
		//Turn left
		t.getTurningPaths().add(new CustomPath("left",arcDistanceShort, m[t.getGridPosX()][t.getGridPosY()-1], m[t.getGridPosX()+1][t.getGridPosY()], Direction.EAST,new MoveTo(t.getCenterX(), t.getY()),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														tiles[3][0].getX() + tileSize, tiles[3][0].getCenterY(),
														false, false)));
		//Turn right
		t.getTurningPaths().add(new CustomPath("right",arcDistanceVeryLong, m[t.getGridPosX()][t.getGridPosY()-1], m[t.getGridPosX()-4][t.getGridPosY()+3], Direction.WEST,new MoveTo(t.getCenterX(), t.getY()),				
											new ArcTo(	tileSize*3.5, tileSize*3.5,
														0.0d,
														tiles[0][3].getX(), tiles[0][3].getCenterY(),
														false, true)));

		
		//Straight
		t.getTurningPaths().add(new CustomPath("straight",straightDistance, m[t.getGridPosX()][t.getGridPosY()-1], m[t.getGridPosX()][t.getGridPosY()+4],Direction.SOUTH,new MoveTo(t.getCenterX(), t.getY()),				
											new LineTo(tiles[3][3].getCenterX(), tiles[3][3].getY() + tileSize)));
		
		//Top, second from right
		t = tiles[2][0];
		//Turn right
		t.getTurningPaths().add(new CustomPath("right",arcDistanceLong, m[t.getGridPosX()][t.getGridPosY()-1], m[t.getGridPosX()-3][t.getGridPosY()+2],Direction.WEST,new MoveTo(t.getCenterX(), t.getY()),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[0][2].getX(), tiles[0][2].getCenterY(),
														false, true)));
		//Straight
		t.getTurningPaths().add(new CustomPath("straight",straightDistance, m[t.getGridPosX()][t.getGridPosY()-1], m[t.getGridPosX()][t.getGridPosY()+4],Direction.SOUTH,new MoveTo(t.getCenterX(), t.getY()),				
											new LineTo(tiles[2][3].getCenterX(), tiles[2][3].getY() + tileSize)));

		//Turn left
		t.getTurningPaths().add(new CustomPath("left", arcDistanceMedium, m[t.getGridPosX()][t.getGridPosY()-1], m[t.getGridPosX()+2][t.getGridPosY()+1], Direction.EAST, new MoveTo(t.getCenterX(), t.getY()),				
				new ArcTo(	tileSize*1.5, tileSize*1.5,
							0.0d,
							tiles[3][1].getX() + tileSize, tiles[3][1].getCenterY(),
							false, false)));
		
		
		//Add all paths to the intersection's list
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				IntersectionTile t1 = tiles[i][j];
				turningPaths.addAll(t1.getTurningPaths());
			}
		}
	}
	
	/**
	 * Visualise all paths - used in debug mode.
	 */
	public void showAllPaths() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				IntersectionTile t = tiles[i][j];
                t.getFrame().setFill(Color.TRANSPARENT);
				for(CustomPath p : t.getTurningPaths()) {
					if(p.getActive()) {
						p.setStroke(Color.GREEN);
						p.setFill(Color.TRANSPARENT);
						t.getChildren().add(p);
					}
				}
			}
		}
	}

	/**
	 * Hides the visualisation of the paths through an intersection. 
	 * Is used when leaving the debug mode.
	 */
    public void hideAllPaths() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
            	IntersectionTile t = tiles[i][j];
                t.getFrame().setFill(new ImagePattern(ResourceBuilder.getBoxjunctionTexture()));
                for(Path p : t.getTurningPaths()) {
                    t.getChildren().remove(p);
                }
            }
        }
    }
    
    /**
     * Returns a the tile of the intersection at the given position.
     * @param posX The x-coordinate of the desired tile.
     * @param posY The y-coordinate of the desired tile.
     * @return 
     */
    public IntersectionTile getTileAt(double posX, double posY){
    	for(int i = 0; i < tiles.length; i++)
    		for(int j = 0; j < tiles[0].length; j++)
    			if(posX < tiles[i][j].getBoundsInParent().getMaxX() && posX > tiles[i][j].getBoundsInParent().getMinX()
    					&& posY < tiles[i][j].getBoundsInParent().getMaxY() && posY > tiles[i][j].getBoundsInParent().getMinY())
    				return tiles[i][j];
    	return null;
    }
    
	@Override
	public void run() {
		
		//Check whether the intersection has an active straight path
		//if not, it does not need trafficlights
		boolean needsLights = false;
		for(CustomPath p : turningPaths) {
			if(p.getActive() && p.getTurn().equals("straight"))
				needsLights = true;
		}
		if(!needsLights)
			return;
		
		while(!Thread.currentThread().isInterrupted()){
			try{
			
			Platform.runLater(() ->{
				
				Map map = SimulationController.getInstance().getMap();

                map.setRedTrafficLight(tiles[0][0].getGridPosX() - 1, tiles[0][0].getGridPosY(), Direction.EAST);
                map.setRedTrafficLight(tiles[0][0].getGridPosX() - 1, tiles[0][1].getGridPosY(), Direction.EAST);

                map.setRedTrafficLight(tiles[3][2].getGridPosX() + 1, tiles[3][2].getGridPosY(), Direction.WEST);
                map.setRedTrafficLight(tiles[3][3].getGridPosX() + 1, tiles[3][3].getGridPosY(), Direction.WEST);
			});

			//red lights overlap for 1.25seconds to allow vehicles to clear the junction
			Thread.sleep(1250);
            
            Platform.runLater(() ->{
            	
            	Map map = SimulationController.getInstance().getMap();

            	map.setGreenTrafficLight(tiles[0][3].getGridPosX(), tiles[0][3].getGridPosY() + 1);
                map.setGreenTrafficLight(tiles[1][3].getGridPosX(), tiles[1][3].getGridPosY() + 1);

                map.setGreenTrafficLight(tiles[2][0].getGridPosX(), tiles[2][0].getGridPosY() - 1);
                map.setGreenTrafficLight(tiles[3][0].getGridPosX(), tiles[3][0].getGridPosY() - 1);
			});
	    	
			Thread.sleep( getSwitchTime());
	    		
	    	Platform.runLater(() ->{
	    		  Map map = SimulationController.getInstance().getMap();
	    		
                  map.setRedTrafficLight(tiles[0][3].getGridPosX(), tiles[0][3].getGridPosY() + 1, Direction.NORTH);
                  map.setRedTrafficLight(tiles[1][3].getGridPosX(), tiles[1][3].getGridPosY() + 1, Direction.NORTH);

                  map.setRedTrafficLight(tiles[2][0].getGridPosX(), tiles[2][0].getGridPosY() - 1, Direction.SOUTH);
                  map.setRedTrafficLight(tiles[3][0].getGridPosX(), tiles[3][0].getGridPosY() - 1, Direction.SOUTH);
	    	});
	    	
	    	Thread.sleep(1750);
	    	
	    	Platform.runLater(() ->{
	    		Map map = SimulationController.getInstance().getMap();
	    		
	    		map.setGreenTrafficLight(tiles[0][0].getGridPosX() - 1, tiles[0][0].getGridPosY());
	            map.setGreenTrafficLight(tiles[0][0].getGridPosX() - 1, tiles[0][1].getGridPosY());

	            map.setGreenTrafficLight(tiles[3][2].getGridPosX() + 1, tiles[3][2].getGridPosY());
	            map.setGreenTrafficLight(tiles[3][3].getGridPosX() + 1, tiles[3][3].getGridPosY());
	    	});
	    	
	    	Thread.sleep( getSwitchTime());
	    	
			}catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public List<Tile> getTiles() {
		List<Tile> l = new ArrayList<>();
		for (int i = 0; i < tiles.length; i++) {
            Collections.addAll(l, tiles[i]);
		}
		return l;
	}
	
	public ArrayList<CustomPath> getTurningPaths(){
		return turningPaths;
	}
	
	public long getSwitchTime(){
		return switchTime;
	}
	
	public void setSwitchTime(long switchTime) {
		this.switchTime = switchTime;
	}
}
