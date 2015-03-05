package com.simulus.view;

import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

import com.simulus.util.enums.Direction;

public class CustomPath extends Path {

	private Tile startTile;
	private Tile endTile;
	private Direction endDir;
	private double pathDistance;
	private String turn;
	private boolean isActive = true;

	public CustomPath(String turn, double pathDistance, Tile st, Tile et,
			Direction ed, PathElement... elements) {
		super(elements);
		startTile = st;
		endTile = et;
		endDir = ed;
		this.pathDistance = pathDistance;
		this.turn = turn;
	}

	public CustomPath(PathElement... elements) {
		super(elements);
	}

	public void setStartTile(Tile t) {
		startTile = t;
	}

	public void setEndTile(Tile t) {
		endTile = t;
	}

	public Tile getStartTile() {
		return startTile;
	}

	public Tile getEndTile() {
		return endTile;
	}

	public void setEndDir(Direction d) {
		endDir = d;
	}

	public Direction getEndDirection() {
		return endDir;
	}

	public double getDistance() {
		return pathDistance;
	}

	public String getTurn() {
		return turn;
	}

	public boolean getActive() {
		return isActive;
	}

	public void setActive(boolean b) {
		isActive = b;
	}
}
