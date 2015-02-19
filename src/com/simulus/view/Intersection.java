package com.simulus.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import com.simulus.MainApp;

public class Intersection extends Group implements TileGroup {
	
	public Tile[][] tiles = new Tile[4][4];
	
	/**
	 * @param xPos x coordinate of the top left tile of the intersection in the grid
	 * @param yPos y coordinate of the top left tile of the intersection in the grid
	 */
	public Intersection(int xPos, int yPos) {
		int tileSize = MainApp.getInstance().getTileSize();
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				tiles[i][j] = new Tile((xPos+i)*tileSize, (yPos+j)*tileSize, tileSize, tileSize, xPos+i, yPos+j);
				tiles[i][j].getFrame().setFill(Color.BLACK);
				MainApp.getInstance().getCanvas().getChildren().add(tiles[i][j]);
				this.getChildren().add(tiles[i][j]);
			}
		}
		
		Tile t = tiles[0][3];
		Path p = new Path(	new MoveTo(t.getCenterX(), t.getCenterY()),
							new ArcTo(	(double)tileSize/2, (double)tileSize/2,
										0.0d,
										t.getCenterX() - tileSize/2, t.getCenterY(),
										false, true));
		
		p.setFill(Color.BLACK);
		t.getChildren().add(p);
		
		
//		ArcTo swNarrow = new ArcTo(radiusX, radiusY, xAxisRotation, x, y, largeArcFlag, sweepFlag)
		
		
	}

	@Override
	public Tile[][] getTiles() {
		return tiles;
	}
	
	
}