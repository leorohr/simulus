package com.simulus.controller;

import com.simulus.MainApp;

public class StatisticsController {

    private static StatisticsController instance;
    public static StatisticsController getInstance() {
        if(instance == null)
            instance = new StatisticsController();
        return instance;
    }

    private SimulationController simulationController = SimulationController.getInstance();
    		
    
    private StatisticsController() {
    }
    
    public void update() {
    	
    	MainApp.getInstance().getControlsController()
    		.addNumCarData(simulationController.getMap().getVehicleCount());

    }
}
