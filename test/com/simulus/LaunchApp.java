package com.simulus;

import javafx.application.Application;

public class LaunchApp {

	private Thread appThread;
	Thread returnedThread;
	boolean threadFound = false;
	boolean isAlive = false;
	boolean isInterrupted = false;

	public void launchApp(){

		appThread = new Thread("JavaFX Init Thread") {
			public void run() {
			
				Application.launch(MainApp.class, new String[0]);
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

}