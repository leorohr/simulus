package com.simulus.mapping;
public class TileXML {
	
	public String type;
	public String direction;
	
	public TileXML(String typeIn, String directionIn){
			
		type = typeIn;
		direction = directionIn;
		
	}
	
	public String getType(){
		return type;
	}
	
	public String getDirection(){
		return direction;
	}
	
	public String toString(){
		return "type: " + type + " direction: " + direction;
	}
	
}