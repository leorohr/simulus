package com.simulus;

import com.simulus.controller.ControlsController;
import com.simulus.controller.SimulationController;
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

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
    private ControlsController controlsController;
	private Pane canvas;
	private int canvasWidth = 800;
	private int canvasHeight = 800;
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

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Simulus");
		this.primaryStage.setResizable(false);
		
		initRootLayout();
		showControls();

        //Setup simulation
        SimulationController.init();
	}

	/**
	 * Initialise the root layout
	 */
	private void initRootLayout() {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = loader.load();

			canvas = new Pane();
			canvas.setMinSize(canvasWidth, canvasHeight);
			canvas.setPrefSize(canvasWidth, canvasHeight);
			canvas.setMaxSize(canvasWidth, canvasHeight);
			rootLayout.setCenter(canvas);

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void showControls() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Controls.fxml"));
			AnchorPane controls = loader.load();
			rootLayout.setRight(controls);
            controlsController = loader.getController();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void resetCanvas() {
        canvas = new Pane();
        canvas.setMinSize(canvasWidth, canvasHeight);
        canvas.setPrefSize(canvasWidth, canvasHeight);
        canvas.setMaxSize(canvasWidth, canvasHeight);
        rootLayout.setCenter(canvas);
    }

	public Pane getCanvas() {
		return canvas;
	}

    public ControlsController getControlsController() {
        return controlsController;
    }

	public static void main(String[] args) {
		launch(args);
	}
}
