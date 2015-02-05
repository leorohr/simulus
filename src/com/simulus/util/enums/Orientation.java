package com.simulus.util.enums;

public enum Orientation {
	
	NORTHSOUTH, WESTEAST, INTERSECTION;
	
	/**
	 * @return Randomly returns one of the two direction included by the orientation or <code>null</code>
	 * if it is called on an intersection.
	 */
	public Direction randomDirection() {
		if(this == NORTHSOUTH) {
			if(Math.random() > 0.5)
				return Direction.NORTH;
			else return Direction.SOUTH;
		} else if(this == WESTEAST) {
			if(Math.random() > 0.5)
				return Direction.EAST;
			else return Direction.WEST;
		}
		
		return null;
	}

}
