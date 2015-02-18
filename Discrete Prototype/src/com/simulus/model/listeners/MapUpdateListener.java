package com.simulus.model.listeners;

import com.simulus.model.Intersection;


public interface MapUpdateListener {
	
	public void mapUpdated();
	public void lightSwitched(Intersection is);

}
