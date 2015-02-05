package com.simulus.model.listeners;

import com.simulus.model.Tile;

public interface MapUpdateListener {
	
	public void mapUpdated(Tile[][] currentMap);

}
