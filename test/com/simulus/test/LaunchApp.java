package com.simulus.test;

import java.util.ArrayList;

import com.simulus.Startup;
import com.simulus.controller.SimulationController;
import com.simulus.view.vehicles.Car;
import com.simulus.view.vehicles.EmergencyCar;
import com.simulus.view.vehicles.Truck;
import com.simulus.view.vehicles.Vehicle;

import javafx.application.Application;
import javafx.application.Platform;


/**
 * This Class launches the Startup class in a thread
 *
 */
public class LaunchApp {

	private Thread appThread;
	Thread returnedThread;
	boolean threadFound = false;
	boolean isAlive = false;
	boolean isInterrupted = false;
	ArrayList<Double> vihecleSpeed = new ArrayList<>();

	public void launchApp(){

		appThread = new Thread("JavaFX Init Thread") {
			public void run() {

				Application.launch(Startup.class, new String[0]);
			}
		};

		appThread.setDaemon(true);
		appThread.start();
	}

	
	
	public Thread threadStatus(String threadName) {

		for(Thread allThread : Thread.getAllStackTraces().keySet()){
			if(allThread.getName().equals(threadName));

			threadFound = true;
			returnedThread = allThread;

			chceckThreadStatus();
		}

		return returnedThread;

	}


	private void chceckThreadStatus( ) {
		if(returnedThread.isAlive()){
			isAlive = true;
		}
		if(returnedThread.isInterrupted()){
			isInterrupted = true;
		}
	}

	public Thread  getThreadByName(String threadName) {
		Thread threadFound = null;

		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName)) 
				threadFound = t;
		}
		return threadFound;
	}

	public boolean truckIsSpawnedOnMap (){

		for(Vehicle v : SimulationController.getInstance().getMap().getVehicles())
			if(v instanceof Truck){
				return true;
			}

		return false;
	}

	public boolean carIsSpawnedOnMap(){
		for(Vehicle v : SimulationController.getInstance().getMap().getVehicles())
			if(v instanceof Car){
				return true;
			}

		return false;

	}

	public boolean debugBoxSelected(){
		return	SimulationController.getInstance().isDebug();

	}

	public boolean AmbulanceIsSpawnedOnMap(){
		for (Vehicle v : SimulationController.getInstance().getMap().getVehicles())
			if(v instanceof EmergencyCar){
				return true;
			}
		return false;


	}


	public Vehicle removeEmergencyCar(){
		for (Vehicle v : SimulationController.getInstance().getMap().getVehicles())
			if(v instanceof EmergencyCar){
				Platform.runLater(new Runnable() {

					@Override
					public void run() {

						SimulationController.getInstance().removeVehicle(v);
					}
				});

			}
		return null;

	}


	public Vehicle measureVihecleSpeed(){
		for (Vehicle v : SimulationController.getInstance().getMap().getVehicles())
			if(v instanceof Car || v instanceof Truck){
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						vihecleSpeed.add(v.getVehicleSpeed());
					}
				});
			}



		return null;

	}

	public ArrayList<Double> getVehicleSpeed(){
		return vihecleSpeed;
	}

	public boolean getIsAlive(){
		return isAlive;
	}
	
	

}
