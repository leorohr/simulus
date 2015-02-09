package com.simulus.model;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;

public class Road {
	
	private static final int LANECOUNT = 2;
	//West-/Northbound, depending on the oriesntation of the road
	private Lane[] lanes1 = new Lane[LANECOUNT]; 	
	//East-/Southbound, depending on the orientation of the road
	private Lane[] lanes2 = new Lane[LANECOUNT];	
	
	private final Seed seed;
	
	private static int roadCount = 0;
	private int id;
	
	
	public Road(Seed orientation) {
		roadCount++;
		id = roadCount;
		
		this.seed = orientation;
		
		for(int i=0; i<LANECOUNT; i++) {
			if(orientation == Seed.NORTHSOUTH) {
				lanes1[i] = new Lane(Direction.NORTH, i, this);
				lanes2[i] = new Lane(Direction.SOUTH, i, this);
			} else if(orientation == Seed.WESTEAST) {
				lanes1[i] = new Lane(Direction.WEST, i, this);
				lanes2[i] = new Lane(Direction.EAST, i, this);
			} else if(orientation == Seed.INTERSECTION) {
				lanes1[i] = new Lane(Direction.NONE, i, this);
				lanes2[i] = new Lane(Direction.NONE, i, this);
			}
		}
	}
	
	/**
	 * @return Check all lanes and return the first empty lane
	 * or <code>null</code> if no empty lane is present.
	 */
	public Lane getEmptyLane() {
		
		int rnd = (int)Math.round(Math.random());
		if(lanes1[rnd].getVehicle() == null)
			return lanes1[rnd];
		if(lanes2[rnd].getVehicle() == null)
			return lanes2[rnd];
	
		for(int i=0; i<lanes1.length; i++)
			if(lanes1[i].getVehicle() == null)
				return lanes1[i];
		
		for(int i=0; i<lanes1.length; i++)
			if(lanes2[1].getVehicle() == null)
				return lanes2[i];
		
		return null;
	}
	
	public Lane[] getLanes1() {
		return lanes1;
	}

	public Lane[] getLanes2() {
		return lanes2;
	}

	public Seed getSeed() {
		return seed;
	}
	public int getId(){
		return id;
	}
}
