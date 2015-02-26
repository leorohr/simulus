package com.simulus.view;

import com.simulus.MainApp;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Map extends Group {
	
	private static final int NUM_ROWS = 40;
	private static final int NUM_COLUMNS = 40;
	public static final int TILESIZE = (int) (MainApp.getInstance().getCanvas().getWidth()/NUM_ROWS);
	
	private Tile[][] tiles = new Tile[NUM_COLUMNS][NUM_ROWS];
	private ArrayList<Intersection> intersections = new ArrayList<>();
	private ArrayList<Lane> entryPoints = new ArrayList<>(); //TODO no object copies but rather coordinates in tiles[][]
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ArrayList<Vehicle> toBeRemoved = new ArrayList<>();

	public Map() {		
		
//		createBasicMap();
		createHashStyleMap();
		
		
		//Add map to canvas
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
				MainApp.getInstance().getCanvas().getChildren().add(tiles[i][p]);
			}
		}
		
	}
	
	public void readMap(){
		//TODO Read map from XML
	}
	
	//Hardcoded map for testing
	@SuppressWarnings("unused")
	private void createBasicMap() {
			
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
				tiles[i][p] = new Tile(	i * TILESIZE, p * TILESIZE,
										TILESIZE, TILESIZE,
										i, p);
			}
		}
		
		for(int i=0; i<tiles.length; i++) {
			addGroup(new Road(18, i, Seed.NORTHSOUTH));
			addGroup(new Road(i, 18, Seed.WESTEAST));
		}
		addGroup(new Intersection(18, 18));
	}
	
	private void createHashStyleMap() {
		//init all tiles
		for (int i = 0; i < tiles.length; i++) {
			for (int p = 0; p < tiles[0].length; p++) {
				tiles[i][p] = new Tile(	i * TILESIZE, p * TILESIZE,
										TILESIZE, TILESIZE,
										i, p);
			}
		}
		
		for(int i=0; i<tiles.length; i++) {
			addGroup(new Road(i, 7, Seed.WESTEAST));
			addGroup(new Road(7, i, Seed.NORTHSOUTH));
			
			addGroup(new Road(i, 18, Seed.WESTEAST));
			addGroup(new Road(18, i, Seed.NORTHSOUTH));
			
			addGroup(new Road(i, 29, Seed.WESTEAST));
			addGroup(new Road(29, i, Seed.NORTHSOUTH));
		}
		
		addGroup(new Intersection(7, 7));
		addGroup(new Intersection(7, 18));
		addGroup(new Intersection(7, 29));
		
		addGroup(new Intersection(18, 7));
		addGroup(new Intersection(18, 18));
		addGroup(new Intersection(18, 29));
		
		addGroup(new Intersection(29, 7));
		addGroup(new Intersection(29, 18));
		addGroup(new Intersection(29, 29));

        tiles[29][10].getFrame().setFill(Color.ALICEBLUE);
	}
	
	/**
	 * Spawns a car at a randomly selected entrypoint of the map.
	 */
	public void spawnRandomCar() {
        Lane l;
        if( (l = selectRandomEntryPoint()) == null)
            return;
		
		//Car adds itself to the canvas
        Car c = null;
		if(l.getDirection() == Direction.NORTH || l.getDirection() == Direction.SOUTH) {
            c = new Car(l.getGridPosX() * Map.TILESIZE + Map.TILESIZE / 2 - Car.CARWIDTH / 2,
                    l.getGridPosY() * Map.TILESIZE + Map.TILESIZE - Car.CARLENGTH,
                    l.getDirection());
        } else if(l.getDirection() == Direction.WEST || l.getDirection() == Direction.EAST) {
            c = new Car(l.getGridPosX() * Map.TILESIZE,
                    l.getGridPosY() * Map.TILESIZE + Map.TILESIZE / 2 - Car.CARWIDTH / 2,
                    l.getDirection());
        }

        c.setCurrentTile(l);
        c.setMap(tiles);
        l.setOccupied(true, c);
        synchronized (vehicles) {
            vehicles.add(c);
        }
	}
	
	/**
	 * Spawns a truck at a randomly selected entrypoint of the map.
	 */
	public void spawnRandomTruck() {
        Lane l;
        if( (l = selectRandomEntryPoint()) == null)
            return;

		//Truck adds itself to the canvas
        Truck t = null;
		if(l.getDirection() == Direction.NORTH || l.getDirection() == Direction.SOUTH) {
            t = new Truck(l.getGridPosX() * Map.TILESIZE + Map.TILESIZE / 2 - Truck.TRUCKWIDTH / 2,
                    l.getGridPosY() * Map.TILESIZE + Map.TILESIZE - Truck.TRUCKLENGTH,
                    l.getDirection());
        }
		else if(l.getDirection() == Direction.WEST || l.getDirection() == Direction.EAST) {
            t = new Truck(l.getGridPosX() * Map.TILESIZE,
                    l.getGridPosY() * Map.TILESIZE + Map.TILESIZE / 2 - Truck.TRUCKWIDTH / 2,
                    l.getDirection());
        }

        t.setCurrentTile(l);
        t.setMap(tiles);
        l.setOccupied(true, t);
        synchronized (vehicles) {
            vehicles.add(t);
        }
	}

    /**
     * @return  A random entrypoint (Lane) or <code>null</code> if no free entrypoint has been found after a certain
     *          number of tries.
     */
    public Lane selectRandomEntryPoint() {
        Random rand = new Random();
        Lane l;
        int tries = 0;
        do {
            if(++tries > entryPoints.size()) //break if 70% of the entrypoints were tried unsuccessfully
                return null;

            l = entryPoints.get(rand.nextInt(entryPoints.size()));
        } while(l.isOccupied());

        return l;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void spawnTesterCar() {
		
		Lane l = entryPoints.get(4);
		
		//Car adds itself to the canvas
		new Car(l.getGridPosX() * Map.TILESIZE + Car.CARWIDTH / 4,
				l.getGridPosY() * Map.TILESIZE + Car.CARLENGTH / 8,
				l.getDirection());	
		
		Lane a = entryPoints.get(0);
		
		//Car adds itself to the canvas
		new Car(a.getGridPosX() * Map.TILESIZE + Car.CARWIDTH / 4,
				a.getGridPosY() * Map.TILESIZE + Car.CARLENGTH / 8,
				a.getDirection());	
	}

	private void addGroup(TileGroup g) {
		
		List<Tile> l = g.getTiles();
		for(Tile t : l) {
			tiles[t.getGridPosX()][t.getGridPosY()] = t;
			
			if(g instanceof Road) {
				if(t.getGridPosX() == tiles.length-1 && ((Road) g).getOrientation() == Seed.WESTEAST && ((Lane) t).getDirection() == Direction.WEST) {
					entryPoints.add((Lane)t);
				}
				else if(t.getGridPosX() == 0 && ((Road) g).getOrientation() == Seed.WESTEAST && ((Lane) t).getDirection() == Direction.EAST) {
					entryPoints.add((Lane)t);
				}
				else if(t.getGridPosY() == tiles.length-1 && ((Road) g).getOrientation() == Seed.NORTHSOUTH && ((Lane) t).getDirection() == Direction.NORTH) {
					entryPoints.add((Lane)t);
				}
				else if(t.getGridPosY() == 0 && ((Road) g).getOrientation() == Seed.NORTHSOUTH && ((Lane) t).getDirection() == Direction.SOUTH) {
					entryPoints.add((Lane)t);
				}
			}	
		}
		
		if(g instanceof Intersection)
			intersections.add((Intersection)g);
	}
	
	/**
	 * Used only for testing purposes Simulates the effect of traffic lights on
	 * cars
	 */
    @SuppressWarnings("UnusedDeclaration")
	public void createBlockage(int tileX, int tileY) {
			tiles[tileX][tileY].setOccupied(true);
	}

	/**
	 * Computes one simulation step.
	 */
	public void updateMap() {

        Vehicle v;
        Iterator<Vehicle> iter = vehicles.iterator();
        while(iter.hasNext()) {

            v = iter.next();
            int vX = v.getCurrentTile().getGridPosX();
            int vY = v.getCurrentTile().getGridPosY();
            Tile nextTile = null;

            //In order to prevent ConcurrentModificationExceptions
            //vehicles have to be removed in this loop.
            if(toBeRemoved.contains(v)) {
                iter.remove();
                continue;
            }

            switch (v.getDirection()) {
                case NORTH:
                    if (tiles[vX][vY - 1].getFrame().intersects(v.getBoundsInParent())) {
                        nextTile = tiles[vX][vY - 1];
                    }
                    break;
                case EAST:
                    if (tiles[vX + 1][vY].getFrame().intersects(v.getBoundsInParent())) {
                        nextTile = tiles[vX + 1][vY];
                    }
                    break;
                case SOUTH:
                    if (tiles[vX][vY + 1].getFrame().intersects(v.getBoundsInParent())) {
                        nextTile = tiles[vX][vY + 1];
                    }
                    break;
                case WEST:
                    if (tiles[vX - 1][vY].getFrame().intersects(v.getBoundsInParent())) {
                        nextTile = tiles[vX - 1][vY];
                    }
                    break;
                case NONE:
                    break;
            }

           if (nextTile != null) { //if the car is intersecting its front tile
               //Free tiles
               Iterator i = v.getOccupiedTiles().iterator();
               while(i.hasNext()) {
                   Tile t = (Tile)i.next();
                   if(!v.getBoundsInParent().intersects(t.getFrame().getBoundsInParent())) {
                       t.setOccupied(false, v);
                       i.remove();
                   }
               }

               nextTile.setOccupied(true, v);
               v.setCurrentTile(nextTile);
               v.setMap(tiles);

           }

            v.moveVehicle();
        }
    }

    /**
     * Removes a vehicle from the screen
     * @param v Vehicle to be removed
     */
    public void removeVehicle(Vehicle v) {

        v.removeFromCanvas();
        v.getCurrentTile().setOccupied(false, v);

        for (Tile t : v.getOccupiedTiles())
            t.setOccupied(false, v);

        toBeRemoved.add(v);
    }

    public void showAllIntersectionPaths() {
        for(Intersection is : intersections)
            is.showAllPaths();
    }

    public void hideAllIntersectionsPaths() {
        for(Intersection is : intersections) {
            is.hideAllPaths();
        }
    }

	public Tile[][] getTiles() {
		return tiles;
	}

    public int getVehicleCount() {
        return vehicles.size();
    }

}
