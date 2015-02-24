package com.simulus.controller;

import com.simulus.view.Map;
import com.simulus.view.Vehicle;
import javafx.application.Platform;

/**
 * TODO
 */
public class SimulationController {

    //Simulation Parameters
    private int tickTime = 200; //in ms
    private int spawnRate = 5; //a new car spawns every spawnRate'th frame
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

        //TODO ensure fixed tickrate
        animationThread = new AnimationThread();

//        AnimationTimer animationTimer = new AnimationTimer() {
//            // When the timer is started, this method loops endlessly
//            private int frameNo = 0;
//
//            @Override
//            public void handle(long now) { // Increment the frame number
//                frameNo++;
//
//                if (frameNo % spawnRate == 0 && map.getVehicles().size() < maxCars) {
//                    //If the car-truck ratio is not correct, spawn a truck, otherwise a car.
//                    if (truckCount < (1 - carTruckRatio) * map.getVehicles().size()) {
//                        Platform.runLater(() -> map.spawnRandomTruck());
//                        truckCount++;
//                    } else {
//                        Platform.runLater(() -> map.spawnRandomCar());
//                    }
//                }
//                Platform.runLater(() -> map.updateMap());
//            }
//        };
//        animationTimer.start();
    }

    public void startSimulation() {
        if(!animationThread.isAlive())
            animationThread = new AnimationThread();

        animationThread.start();
    }

    public void stopSimulation() {
        animationThread.interrupt();
    }

    private class AnimationThread extends Thread {

        @Override
        public void run() {

            while(!isInterrupted()) {

                if (map.getVehicles().size() < maxCars) {
                    //If the car-truck ratio is not correct, spawn a truck, otherwise a car.
                    if (truckCount < (1 - carTruckRatio) * map.getVehicles().size()) {
                        Platform.runLater(() -> map.spawnRandomTruck());
                        truckCount++;
                    } else {
                        Platform.runLater(() -> map.spawnRandomCar());
                    }
                }
                Platform.runLater(() -> map.updateMap());

                try {
                    Thread.sleep(tickTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
    }

    /* Getter & Setter */
    public boolean isDebug() {
        return debugFlag;
    }

    public void setDebugFlag(boolean debugFlag) {
        this.debugFlag = debugFlag;
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


