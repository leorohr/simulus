package com.simulus.view;

import com.simulus.controller.SimulationController;
import com.simulus.util.enums.Direction;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Intersection extends Group implements TileGroup, Runnable {
	
	public Tile[][] tiles = new Tile[4][4];
	private long switchTime;
	private int tileSize = Map.TILESIZE;
	
	
	
	/**
	 * @param xPos x coordinate of the top left tile of the intersection in the grid
	 * @param yPos y coordinate of the top left tile of the intersection in the grid
	 */
	public Intersection(int xPos, int yPos) {
		switchTime = (long) (2000 + Math.random()*3000);
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = new Tile((xPos+i)*tileSize, (yPos+j)*tileSize, tileSize, tileSize, xPos+i, yPos+j);
				tiles[i][j].getFrame().setFill(Color.BLACK);
				
				//this.getChildren().add(SimulationController.getInstance().getMap().getTiles()[i][j]);
			}
		}
		
	}
	
	public void addTurningPaths(Tile[][] m){
		//Left, top
				Tile t = tiles[0][0];
				double arcDistanceShort = 90*(Math.PI/180)*(tileSize/2);
				double arcDistanceLong = 90*(Math.PI/180)*(tileSize*2.5);
				double straightDistance = 4*tileSize;
				
				//Turn left
				t.getTurningPaths().add(new CustomPath(	"left",arcDistanceShort, t, m[t.getGridPosX()][t.getGridPosY()-1], Direction.NORTH, new MoveTo(t.getX(), t.getCenterY()),				
													new ArcTo(	tileSize/2, tileSize/2,
																0.0d,
																tiles[0][0].getCenterX(), tiles[0][0].getY(),
																false, false)));
				
				/*//Straight
				t.getTurningPaths().add(new CustomPath(	straightDistance, t, m[t.getGridPosX()+4][t.getGridPosY()], Direction.EAST,new MoveTo(t.getX(), t.getCenterY()),				
													new LineTo(tiles[3][0].getX() + tileSize, tiles[3][0].getCenterY())));*/
				
				//Left, second from top
				t = tiles[0][1];
				//Turn right
				t.getTurningPaths().add(new CustomPath("right",	arcDistanceLong, t, m[t.getGridPosX()+2][t.getGridPosY()+3], Direction.SOUTH,new MoveTo(t.getX(), t.getCenterY()),				
													new ArcTo(	tileSize*2.5, tileSize*2.5,
																0.0d,
																tiles[2][3].getCenterX(), tiles[2][3].getY() + tileSize,
																false, true)));
				/*//Straight
				t.getTurningPaths().add(new CustomPath(	straightDistance, t, m[t.getGridPosX()+4][t.getGridPosY()], Direction.EAST,new MoveTo(t.getX(), t.getCenterY()),				
													new LineTo(tiles[3][1].getX() + tileSize, tiles[3][1].getCenterY())));*/

				
				//Bottom, leftmost
				t = tiles[0][3];
				//Turn left
				t.getTurningPaths().add(new CustomPath("left",arcDistanceShort, t, m[t.getGridPosX()-1][t.getGridPosY()], Direction.WEST, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
													new ArcTo(	tileSize/2, tileSize/2,
																0.0d,
																t.getX(), t.getCenterY(),
																false, false)));
				/*//Straight
				t.getTurningPaths().add(new CustomPath( straightDistance, t, m[t.getGridPosX()][t.getGridPosY()-4], Direction.NORTH, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
													new LineTo(t.getCenterX(), tiles[0][0].getY())));*/

				//Bottom, second from left
				t = tiles[1][3];
				//Turn right
				t.getTurningPaths().add(new CustomPath("right",arcDistanceLong, t, m[t.getGridPosX()+3][t.getGridPosY()-2],Direction.EAST, new MoveTo(t.getCenterX(), t.getY() + tileSize),				
													new ArcTo(	tileSize*2.5, tileSize*2.5,
																0.0d,
																tiles[3][1].getX() + tileSize, tiles[3][1].getCenterY(),
																false, true)));
				/*//Straight
				t.getTurningPaths().add(new CustomPath(straightDistance, t, m[t.getGridPosX()][t.getGridPosY()-4], Direction.NORTH,new MoveTo(t.getCenterX(), t.getY() + tileSize),				
													new LineTo(t.getCenterX(), tiles[0][0].getY())));*/

				
				//Right, bottom
				t = tiles[3][3];
				//Turn left
				t.getTurningPaths().add(new CustomPath("left",arcDistanceShort, t, m[t.getGridPosX()][t.getGridPosY()+1],Direction.SOUTH,new MoveTo(t.getX()+tileSize, t.getCenterY()),				
													new ArcTo(	tileSize/2, tileSize/2,
																0.0d,
																tiles[3][3].getCenterX(), tiles[3][3].getY()+ tileSize,
																false, false)));
				/*//Straight
				t.getTurningPaths().add(new CustomPath(straightDistance, t, m[t.getGridPosX()-4][t.getGridPosY()],Direction.WEST,new MoveTo(t.getX() + tileSize, t.getCenterY()),				
													new LineTo(tiles[0][3].getX(), t.getCenterY())));*/
				
				//Right, second from bottom
				t = tiles[3][2];
				//Turn right
				t.getTurningPaths().add(new CustomPath("right",arcDistanceLong, t, m[t.getGridPosX()-2][t.getGridPosY()-3],Direction.NORTH,new MoveTo(t.getX()+tileSize, t.getCenterY()),				
													new ArcTo(	tileSize*2.5, tileSize*2.5,
																0.0d,
																tiles[1][0].getCenterX(), tiles[1][0].getY(),
																false, true)));
				/*//Straight
				t.getTurningPaths().add(new CustomPath(straightDistance, t, m[t.getGridPosX()-4][t.getGridPosY()],Direction.WEST,new MoveTo(t.getX() + tileSize, t.getCenterY()),				
													new LineTo(tiles[0][3].getX(), t.getCenterY())));*/
				
				//Top, rightmost
				t = tiles[3][0];
				//Turn left
				t.getTurningPaths().add(new CustomPath("left",arcDistanceShort, t, m[t.getGridPosX()+1][t.getGridPosY()], Direction.EAST,new MoveTo(t.getCenterX(), t.getY()),				
													new ArcTo(	tileSize/2, tileSize/2,
																0.0d,
																tiles[3][0].getX() + tileSize, tiles[3][0].getCenterY(),
																false, false)));
			/*	//Straight
				t.getTurningPaths().add(new CustomPath(straightDistance, t, m[t.getGridPosX()][t.getGridPosY()+4],Direction.SOUTH,new MoveTo(t.getCenterX(), t.getY()),				
													new LineTo(tiles[3][3].getCenterX(), tiles[3][3].getY() + tileSize)));*/
				
				//Top, second from right
				t = tiles[2][0];
				//Turn right
				t.getTurningPaths().add(new CustomPath("right",arcDistanceLong, t, m[t.getGridPosX()-3][t.getGridPosY()+2],Direction.WEST,new MoveTo(t.getCenterX(), t.getY()),				
													new ArcTo(	tileSize*2.5, tileSize*2.5,
																0.0d,
																tiles[0][2].getX(), tiles[0][2].getCenterY(),
																false, true)));
				/*//Straight
				t.getTurningPaths().add(new CustomPath(straightDistance, t, m[t.getGridPosX()][t.getGridPosY()+4],Direction.SOUTH,new MoveTo(t.getCenterX(), t.getY()),				
													new LineTo(tiles[2][3].getCenterX(), tiles[2][3].getY() + tileSize)));*/
	}
	
	//Display all paths - used in debug mode
	public void showAllPaths() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				Tile t = tiles[i][j];
                t.getFrame().setFill(Color.TRANSPARENT);
				for(Path p : t.getTurningPaths()) {
					p.setStroke(Color.GREEN);
					p.setFill(Color.TRANSPARENT);
					t.getChildren().add(p);
				}
			}
			
		}
	}

    public void hideAllPaths() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                Tile t = tiles[i][j];
                t.getFrame().setFill(Color.BLACK);
                for(Path p : t.getTurningPaths()) {
                    t.getChildren().remove(p);
                }
            }
        }
    }
    

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			try{
			
			
			Platform.runLater(() ->{
				
				Map map = SimulationController.getInstance().getMap();

                map.setRedTrafficLight(tiles[0][0].getGridPosX() - 1, tiles[0][0].getGridPosY());
                map.setRedTrafficLight(tiles[0][0].getGridPosX() - 1, tiles[0][1].getGridPosY());

                map.setRedTrafficLight(tiles[3][2].getGridPosX() + 1, tiles[3][2].getGridPosY());
                map.setRedTrafficLight(tiles[3][3].getGridPosX() + 1, tiles[3][3].getGridPosY());
			});
			
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
	    		
                  map.setRedTrafficLight(tiles[0][3].getGridPosX(), tiles[0][3].getGridPosY() + 1);
                  map.setRedTrafficLight(tiles[1][3].getGridPosX(), tiles[1][3].getGridPosY() + 1);

                  map.setRedTrafficLight(tiles[2][0].getGridPosX(), tiles[2][0].getGridPosY() - 1);
                  map.setRedTrafficLight(tiles[3][0].getGridPosX(), tiles[3][0].getGridPosY() - 1);
	    	});
	    	
	    	Thread.sleep(1250);
	    	
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
	
	public long getSwitchTime(){
		return switchTime;
	}
	
	public void setSwitchTime(long switchTime) {
		this.switchTime = switchTime;
	}
}