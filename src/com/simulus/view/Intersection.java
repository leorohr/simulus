package com.simulus.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Intersection extends Group implements TileGroup {
	
	public Tile[][] tiles = new Tile[4][4];
	
	/**
	 * @param xPos x coordinate of the top left tile of the intersection in the grid
	 * @param yPos y coordinate of the top left tile of the intersection in the grid
	 */
	public Intersection(int xPos, int yPos) {
		int tileSize = Map.TILESIZE;
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = new Tile((xPos+i)*tileSize, (yPos+j)*tileSize, tileSize, tileSize, xPos+i, yPos+j);
				tiles[i][j].getFrame().setFill(Color.TRANSPARENT);
				this.getChildren().add(tiles[i][j]);
			}
		}
		
		//Left, top
		Tile t = tiles[0][0];
		//Turn left
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX(), t.getCenterY()),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														tiles[0][0].getCenterX(), tiles[0][0].getY(),
														false, false)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX(), t.getCenterY()),				
											new LineTo(tiles[3][0].getX() + tileSize, tiles[3][0].getCenterY())));
		
		//Left, second from top
		t = tiles[0][1];
		//Turn right
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX(), t.getCenterY()),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[2][3].getCenterX(), tiles[2][3].getY() + tileSize,
														false, true)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX(), t.getCenterY()),				
											new LineTo(tiles[3][1].getX() + tileSize, tiles[3][1].getCenterY())));

		
		//Bottom, leftmost
		t = tiles[0][3];
		//Turn left
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														t.getX(), t.getCenterY(),
														false, false)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new LineTo(t.getCenterX(), tiles[0][0].getY())));

		//Bottom, second from left
		t = tiles[1][3];
		//Turn right
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[3][1].getX() + tileSize, tiles[3][1].getCenterY(),
														false, true)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY() + tileSize),				
											new LineTo(t.getCenterX(), tiles[0][0].getY())));

		
		//Right, bottom
		t = tiles[3][3];
		//Turn leftr
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX()+tileSize, t.getCenterY()),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														tiles[3][3].getCenterX(), tiles[3][3].getY()+ tileSize,
														false, false)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX() + tileSize, t.getCenterY()),				
											new LineTo(tiles[0][3].getX(), t.getCenterY())));
		
		//Right, second from bottom
		t = tiles[3][2];
		//Turn right
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX()+tileSize, t.getCenterY()),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[1][0].getCenterX(), tiles[1][0].getY(),
														false, true)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getX() + tileSize, t.getCenterY()),				
											new LineTo(tiles[0][3].getX(), t.getCenterY())));
		
		//Top, rightmost
		t = tiles[3][0];
		//Turn left
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY()),				
											new ArcTo(	tileSize/2, tileSize/2,
														0.0d,
														tiles[3][0].getX() + tileSize, tiles[3][0].getCenterY(),
														false, false)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY()),				
											new LineTo(tiles[3][3].getCenterX(), tiles[3][3].getY() + tileSize)));
		
		//Top, second from right
		t = tiles[2][0];
		//Turn right
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY()),				
											new ArcTo(	tileSize*2.5, tileSize*2.5,
														0.0d,
														tiles[0][2].getX(), tiles[0][2].getCenterY(),
														false, true)));
		//Straight
		t.getTurningPaths().add(new Path(	new MoveTo(t.getCenterX(), t.getY()),				
											new LineTo(tiles[2][3].getCenterX(), tiles[2][3].getY() + tileSize)));
		
		
		
		
		
		showAllPaths();
		
	}
	
	//debugging
	private void showAllPaths() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				Tile t = tiles[i][j];
				for(Path p : t.getTurningPaths()) {
					p.setStroke(Color.GREEN);
					p.setFill(Color.TRANSPARENT);
					t.getChildren().add(p);
				}
			}
			
		}
	}

	@Override
	public List<Tile> getTiles() {
		
		List<Tile> l = new ArrayList<Tile>();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				l.add(tiles[i][j]);
			}
		}
		return l;
	}
	
	
}