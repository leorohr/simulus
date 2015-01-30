package com.simulus.model.enums;

public enum Orientation {
	
	NORTHSOUTH, EASTWEST;
	
	/**
	 * @return Randomly returns one of the two direction included by the orientation.
	 */
	public Direction randomDirection() {
		if(this == NORTHSOUTH) {
			if(Math.random() > 0.5)
				return Direction.NORTH;
			else return Direction.SOUTH;
		} else {
			if(Math.random() > 0.5)
				return Direction.EAST;
			else return Direction.WEST;
		}
		
		
	}

}
