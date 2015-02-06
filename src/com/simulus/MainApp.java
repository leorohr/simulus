package com.simulus;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.util.enums.Direction;
import com.simulus.view.Car;
import com.simulus.view.TrafficLight;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private int frameNo = 0;
	private ArrayList<Car> cars;
	private Car car = null;
	private AnchorPane overview;
	private TrafficLight lights;
	private Rectangle rect;
	private int gridSize;
	private int windowWidth;
	private int windowHeight;
	private static MainApp instance;
	
	private Rectangle[][] tiles = new Rectangle[10][10];
	
	
	public static MainApp getInstance(){
		return instance;
	}
	
	public MainApp(){
		super();
		synchronized(MainApp.class){
			if(instance != null) throw new UnsupportedOperationException(
					getClass()+" ");
			instance = this;
		}
	}
	@Override
	public void start(final Stage primaryStage) {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Simulus");
		lights = new TrafficLight(100, 100, 20, 60);
		rect = new Rectangle(800, 800, Color.BLACK);
		rect.setFill(Color.TRANSPARENT);
		
		
		
		initRootLayout();
		showMainView();
		cars = new ArrayList<Car>();
		
		/**
		 * Ticking loop
		 */
		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			@Override
			public void handle(long now) {
				//Increment the frame number
				frameNo++;
				//Spawn a car at frame 1
				System.out.println(frameNo);
				if (frameNo == 1) {
					cars.add(new Car(500, 800, Direction.NORTH));
					System.out.println("Start X Position: "+cars.get(cars.size()-1).getX()+"\nStart Y Position: "+ cars.get(cars.size()-1).getY());
					rootLayout.getChildren().add(cars.get(cars.size() - 1));
				}
				if(frameNo%8 == 0){
					cars.get(cars.size()-1).moveVehicle();
				}
			}
		};
		timer.start();
	}

	/**
	 * Initialise the root layout
	 */
	private void initRootLayout() {

		try {

			/*FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			*/
			rootLayout = new BorderPane();
			//rootLayout.setBorder(new Border());
			Scene scene = new Scene(rootLayout, 800, 800);

	        rootLayout.prefHeightProperty().bind(scene.heightProperty());
	        rootLayout.prefWidthProperty().bind(scene.widthProperty());
			Screen screen = Screen.getPrimary();
	        Rectangle2D bounds = screen.getVisualBounds();
			primaryStage.setScene(scene);
			//primaryStage.setWidth(bounds.getHeight()/2);
			//primaryStage.setHeight(bounds.getWidth()/2);
			
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	            @Override
	            public void handle(WindowEvent t) {
	                Platform.exit();
	                System.exit(0);
	            }
	        });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void setGridSize(int size){
		gridSize = size;
	}

	/**
	 * Shows the cashflow overview interface
	 */
	private void showMainView() {
		try {
			//FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(MainApp.class.getResource("view/MainView.fxml"));
			//overview = (AnchorPane) loader.load();
			// Set overview as center widget of the pane
			//rootLayout.setCenter(overview);
			rootLayout.getChildren().add(lights);
			rootLayout.getChildren().add(rect);
			int count = 80;
			
			for(int i = 0; i < 10; i ++){
				for(int p = 0; p <10; p++){
					
					tiles[i][p] = new Rectangle(count*p, count*i, count, count);
					tiles[i][p].setFill(Color.TRANSPARENT);
					tiles[i][p].setStroke(Color.BLACK);
					rootLayout.getChildren().add(tiles[i][p]);
					
					
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
