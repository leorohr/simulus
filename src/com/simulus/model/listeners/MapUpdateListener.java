package com.simulus.model.listeners;


public interface MapUpdateListener {
	
	public void mapUpdated();
	public void carSpawned(int x, int y, int laneId, int carId);

}
