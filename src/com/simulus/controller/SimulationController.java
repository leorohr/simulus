package com.simulus.controller;

import com.simulus.MainApp;
import com.simulus.view.*;
import javafx.application.Platform;

/**
 * TODO
 */
public class SimulationController {

    //Simulation Parameters
    private int tickTime = 50; //in ms
    private int spawnRate = 5; //a new car spawns every spawnRate'th tick
    private int maxCars = 25;
    private int maxCarSpeed = 10;
    private double carTruckRatio = 0.7d;
    private int truckCount = 0;
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
        if(!animationThread.isInterrupted())
            animationThread = new AnimationThread();

        animationThread.start();
    }

    public void stopSimulation() {
        animationThread.interrupt();
    }

    public void resetSimulation() {
        animationThread.interrupt();
        MainApp.getInstance().resetCanvas();
        map = new Map();
        truckCount = 0;
        animationThread = new AnimationThread();
    }

    private class AnimationThread extends Thread {

        @Override
        public void run() {
            long tickCount = 0;
            while(!Thread.currentThread().isInterrupted()) {

                Platform.runLater(() ->
                        MainApp.getInstance().getControlsController().addNumCarData(map.getVehicleCount()));

                if(tickCount++ % spawnRate == 0) {
                    if (map.getVehicleCount() < maxCars) {
                        //If the car-truck ratio is not correct, spawn a truck, otherwise a car.
                        if (truckCount < (1 - carTruckRatio) * map.getVehicleCount()) {
                            Platform.runLater(() -> map.spawnRandomTruck());
                            truckCount++;
                        } else {
                            Platform.runLater(() -> map.spawnRandomCar());
                        }
                    }
                }
                
                if(tickCount++ == 10){
                	Platform.runLater(() -> map.spawnAmbulance());
                }
            	
                Platform.runLater(() -> map.updateMap());

                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                tickCount++;
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
    }

    /* Getter & Setter */
    public boolean isDebug() {
        return debugFlag;
    }

    public void setDebugFlag(boolean debugFlag) {
        this.debugFlag = debugFlag;
        if(debugFlag) {
            map.showAllIntersectionPaths();
        }
        else {
            map.hideAllIntersectionsPaths();

            //Clear debuginformation from canvas
            for(Tile[] t : map.getTiles()) {
                for (int i = 0; i < t.length; i++) {
                    if(t[i] instanceof Lane)
                        t[i].getFrame().setFill(Lane.COLOR);
                }
            }
        }
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

    public Map getMap() {
        return map;
    }
}


