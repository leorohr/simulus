package com.simulus.model;

import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Orientation;

public class Road {
	
	private static final int LANECOUNT = 2;
	//West-/Northbound, depending on the oriesntation of the road
	private Lane[] lanes1 = new Lane[LANECOUNT]; 	
	//East-/Southbound, depending on the orientation of the road
	private Lane[] lanes2 = new Lane[LANECOUNT];	
	private final Orientation orientation;
	
	public Road(Orientation orientation) {
		this.orientation = orientation;
		
		for(int i=0; i<LANECOUNT; i++) {
			if(orientation == Orientation.NORTHSOUTH) {
				lanes1[i] = new Lane(Direction.NORTH, i);
				lanes2[i] = new Lane(Direction.SOUTH, i);
			} else {
				lanes1[i] = new Lane(Direction.WEST, i);
				lanes2[i] = new Lane(Direction.EAST, i);
			}
		}
			
	}
	
	/**
	 * @return Check all lanes and return the first empty lane
	 * or <code>null</code> if no empty lane is present.
	 */
	public Lane getEmptyLane() {
		
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

	public Orientation getOrientation() {
		return orientation;
	}
	
}
