package com.simulus;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.controller.ControlsController;
import com.simulus.controller.SimulationController;
import com.simulus.util.Configuration;

/**
 * {@code MainApp} represents the Simulator of the Simulus suite.
 * 
 * It should not be used as a run-target but only be started through the {@link com.simulus.Startup} application.
 */
public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
    private ControlsController controlsController;
	private Pane canvas;
	private static MainApp instance;

	public static MainApp getInstance() {
		return instance;
	}

	public MainApp() {
		super();

		// Synchronized call to access the application's instance
		synchronized (MainApp.class) {
			if (instance != null)
				throw new UnsupportedOperationException(getClass() + " ");
			instance = this;
		}
	}
	
	@Override
	public void start(final Stage primaryStage) {
	
		this.setPrimaryStage(primaryStage);
		this.getPrimaryStage().setTitle("Simulus");
		this.getPrimaryStage().setResizable(false);
		
		initRootLayout();
		showControls();
	}
	
	/**
	 * Overloads the MainApp's default start method to prevent it from loading the default map
	 * if that is not necessary, e.g. when the app is started from the editor with user-defined map.
	 * @param primaryStage The stage to start the app on.
	 * @param defaultMap Whether the default map should be loaded or not.
	 */
	public void start(final Stage primaryStage, boolean defaultMap) {
		start(primaryStage);
		
		//Load default map
		if(defaultMap) {
			SimulationController.getInstance().getMap().loadMap(
				new File("resources/maps/default.map"));
		}
		
	}

	/**
	 * Initialise the root layout
	 */
	private void initRootLayout() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/ui/RootLayout.fxml"));
			rootLayout = loader.load();

			
			canvas = new Pane();			
			canvas.setMinSize(Configuration.CANVAS_SIZE, Configuration.CANVAS_SIZE);
			canvas.setPrefSize(Configuration.CANVAS_SIZE, Configuration.CANVAS_SIZE);
			canvas.setMaxSize(Configuration.CANVAS_SIZE, Configuration.CANVAS_SIZE);
			rootLayout.setCenter(canvas);

			Scene scene = new Scene(rootLayout);
			getPrimaryStage().setScene(scene);
			getPrimaryStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});

			getPrimaryStage().show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Loads the right side control panel
	 */
	private void showControls() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ui/Controls.fxml"));
			AnchorPane controls = loader.load();
			rootLayout.setRight(controls);
            controlsController = loader.getController();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recreate and resize the canvas according to the size in {@code Configuration}
	 */
    public void resetCanvas() {
    	canvas = new Pane();
        canvas.setMinSize(Configuration.CANVAS_SIZE, Configuration.CANVAS_SIZE);
        canvas.setPrefSize(Configuration.CANVAS_SIZE, Configuration.CANVAS_SIZE);
        canvas.setMaxSize(Configuration.CANVAS_SIZE, Configuration.CANVAS_SIZE);
        rootLayout.setCenter(canvas);
    }

    /*
     * Getters and Setters
     */
    
	public Pane getCanvas() {
		return canvas;
	}

    public ControlsController getControlsController() {
        return controlsController;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public BorderPane getRootLayout(){
    	return rootLayout;
    }
    
    public void setPrimaryStage(Stage primaryStage){
		this.primaryStage = primaryStage;
    	
    }
    
    public void getInitRobotLayout(){
    	 initRootLayout();
    }
    
    public void getShowControls(){
    	showControls();
    }
    
    
	public static void main(String[] args) {
		launch(args);
	}

	
}
