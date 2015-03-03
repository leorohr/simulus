package com.simulus.controller;

import javafx.application.Platform;
import javafx.scene.paint.Color;

import com.simulus.MainApp;
import com.simulus.util.enums.Behavior;
import com.simulus.view.Ambulance;
import com.simulus.view.EmergencyCar;
import com.simulus.view.Lane;
import com.simulus.view.Map;
import com.simulus.view.Tile;
import com.simulus.view.Truck;
import com.simulus.view.Vehicle;

/**
 * TODO
 */
public class SimulationController {

    //Simulation Parameters
    private int tickTime = 50; //in ms
    private int spawnRate = 5; //a new car spawns every spawnRate'th tick
    private int maxCars = 25;
    private int maxCarSpeed = 10;
    private double carTruckRatio = 0.7d;		//the desired carCount/truckCount-fraction 
    private double recklessNormalRatio = 0.3d; 	//see above
    private int recklessCount = 0;
    private int truckCount = 0;
    private int ambulanceCount = 0;
    private boolean debugFlag = false;

    private Map map = new Map();
    private AnimationThread animationThread;

    /* Singleton */
    private static SimulationController instance;
    public static SimulationController getInstance() {
        if (instance == null)
            instance = new SimulationController();

        return instance;
    }

    private SimulationController() {

        animationThread = new AnimationThread();
    }

    public void startSimulation() {
        if(animationThread.isInterrupted() || !animationThread.isAlive())
            animationThread = new AnimationThread();
        
        if(!animationThread.isAlive())
        	animationThread.start();
    }

    public void stopSimulation() {
        animationThread.interrupt();
    }

    public void resetSimulation() {
        animationThread.interrupt();
        MainApp.getInstance().resetCanvas();
        map.stopChildThreads();
        map = new Map();
        MainApp.getInstance().getControlsController().resetCharts();
        truckCount = 0;
        recklessCount = 0;
        ambulanceCount = 0;
        animationThread = new AnimationThread();
    }

    private class AnimationThread extends Thread {

        @Override
        public void run() {
            long tickCount = 0;
            while(!Thread.currentThread().isInterrupted()) {
        		
            	if(tickCount * tickTime % 500 == 0) //add data every 500 ms
            		Platform.runLater(() -> MainApp.getInstance().getControlsController().updateCharts());

                if(tickCount++ % spawnRate == 0) {
                    if (map.getVehicleCount() < maxCars) {
                        //If the car-truck ratio is not correct, spawn a truck, otherwise a car.
                        if (truckCount < (1 - carTruckRatio) * map.getVehicleCount()) {
                            Platform.runLater(() -> map.spawnRandomTruck());
                            truckCount++;
                        } else {
                        	//If the reckless-normal-ratio is not correct, spawn a reckless car.
                        	if(recklessCount < recklessNormalRatio * map.getVehicleCount()) {
                        		Platform.runLater(() -> map.spawnRandomCar(Behavior.RECKLESS));
                        		recklessCount++;
                        	}
                        	else Platform.runLater(() -> map.spawnRandomCar(Behavior.CAUTIOUS));
                        }
                    }
                }               
            	
                Platform.runLater(() -> map.updateMap());

                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                //Increase tickCount or reset if overflown
                tickCount = (tickCount == Long.MAX_VALUE ? 0 : tickCount++);
            }
        }

        public AnimationThread() {
            super("AnimationThread");
        }
    }

    /**
     * Offers a clean way to initialise the controller without calling getInstance()
     */
    public static void init() {
        if(instance == null)
            instance = new SimulationController();
    }

    public void removeVehicle(Vehicle v) {
        map.removeVehicle(v);
        if(v instanceof Truck)
            truckCount--;
        if(v instanceof EmergencyCar) {
        	ambulanceCount--;
        	MainApp.getInstance().getControlsController().setAmbulanceButtonDisabled(false);
        }
    }

    /* Getter & Setter */
    public boolean isDebug() {
        return debugFlag;
    }

    public void setDebugFlag(boolean debugFlag) {
        this.debugFlag = debugFlag;
        if(debugFlag) {
            map.showAllIntersectionPaths();
            //Show AoE of ambulance
            for(Vehicle v : map.getVehicles()) {
            	if(v instanceof EmergencyCar) {
            		((Ambulance)v.getParent()).getAoE().setOpacity(0.25d);
            	}
            }
            	
        }
        else {
            map.hideAllIntersectionsPaths();
            //Hide AoE of ambulances
            for(Vehicle v : map.getVehicles()) {
            	if(v instanceof EmergencyCar) {
            		((Ambulance)v.getParent()).getAoE().setOpacity(0.0d);
            	}
            }

            //Clear debuginformation from canvas
            for(Tile[] t : map.getTiles()) {
                for (int i = 0; i < t.length; i++) {
                    if(t[i] instanceof Lane) {
                    	if(t[i].isRedLight())
                    		t[i].getFrame().setFill(Color.RED);
                    	else ((Lane) t[i]).redraw();
                    }
                }
            }
        }
    }
    
    public void spawnAmbulance() {
    	if(ambulanceCount < 5) {
    		Platform.runLater(() -> map.spawnAmbulance());
	    	ambulanceCount++;
    	} else MainApp.getInstance().getControlsController().setAmbulanceButtonDisabled(true);
    }
    
    public int getTickTime() {
        return tickTime;
    }

    public void setTickTime(int tickTime) {
        this.tickTime = tickTime;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
    }

    public int getMaxCars() {
        return maxCars;
    }

    public void setMaxCars(int maxCars) {
        this.maxCars = maxCars;
    }

    public int getMaxCarSpeed() {
        return maxCarSpeed;
    }

    public void setMaxCarSpeed(int maxCarSpeed) {
        this.maxCarSpeed = maxCarSpeed;
    }

    public double getCarTruckRatio() {
        return carTruckRatio;
    }

    public void setCarTruckRatio(double carTruckRatio) {
        this.carTruckRatio = carTruckRatio;
    }

    public void setRecklessNormalRatio(double recklessNormalRatio) {
    	this.recklessNormalRatio = recklessNormalRatio;
    }
    
    public Map getMap() {
        return map;
    }
}


