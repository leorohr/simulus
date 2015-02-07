package com.simulus;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.simulus.controller.SimulationController;
import com.simulus.model.Vehicle;
import com.simulus.view.VVehicle;
import com.simulus.util.enums.Direction;
import com.simulus.util.enums.Seed;
import com.simulus.view.VCar;
import com.simulus.view.Road;
import com.simulus.view.TrafficLight;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private int frameNo = 0;
	private ArrayList<VVehicle> cars;
	private VCar car = null;
	private AnchorPane overview;
	private TrafficLight lights;
	private Rectangle rect;
	private ArrayList<Road> roads = new ArrayList<>();
	private Group tile;
	private int gridSize;
	private int windowWidth;
	private int windowHeight;
	private static MainApp instance;
	private SimulationController controller;
	
	private Rectangle[][] tiles = new Rectangle[30][30];
	
	
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
		for(int i = 0; i < 30; i++){
			if(i == 14)
				roads.add(new Road(14*30, i*30, 30,30));
			else
				roads.add(new Road(14*30,i*30,30,30, Seed.NORTHSOUTH) );
			//rootLayout.getChildren().add(roads.get(i));
		}
		
		for(int i = 0; i < 30; i++){
			if(i == 14)
				roads.add(new Road(14*30, i*30, 30,30));
			else
				roads.add(new Road(i*30,14*30,30,30, Seed.WESTEAST) );
			//rootLayout.getChildren().add(roads.get(i));
		}
		
		cars = new ArrayList<VVehicle>();
		
		
		initRootLayout();
		showMainView();
		
		controller = SimulationController.getInstance();
		/**
		 * Ticking loop
		 */
/*		AnimationTimer timer = new AnimationTimer() {
			// When the timer is started, this method loops endlessly
			@Override
			public void handle(long now) {
				//Increment the frame number
				frameNo++;
				//Spawn a car at frame 1
				System.out.println(frameNo);
				if (frameNo == 1) {
					cars.add(new VCar(500, 900, Direction.NORTH));
					cars.add(new VCar(500, 800+(90/3), Direction.NORTH));
					cars.add(new VCar(500, 800+(90/3)+(90/3), Direction.NORTH));
					System.out.println("Start X Position: "+cars.get(cars.size()-1).getX()+"\nStart Y Position: "+ cars.get(cars.size()-1).getY());
					for(int i = 0; i < cars.size(); i++)
						rootLayout.getChildren().add(cars.get(i));
				}
				if(frameNo%8 == 0){
					for(int i = 0; i < cars.size(); i++);
						//cars.get(i).moveVehicle();
				}
			}
		};
		timer.start();*/
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
			Scene scene = new Scene(rootLayout, 900, 900);

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
			//rootLayout.getChildren().add(lights);
			//rootLayout.getChildren().add(tile);
			int count = 90/3;
			
			for(int i = 0; i < 30; i ++){
				for(int p = 0; p <30; p++){
					
					tiles[i][p] = new Rectangle(count*p, count*i, count, count);
					tiles[i][p].setFill(Color.TRANSPARENT);
					tiles[i][p].setStroke(Color.BLACK);
					rootLayout.getChildren().add(tiles[i][p]);
					
				}
			}
			for(int i = 0; i < roads.size(); i++)
				rootLayout.getChildren().add(roads.get(i));
			//rootLayout.getChildren().add(tile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public synchronized void redrawCars(ArrayList<Vehicle> vehicles){
		for(Vehicle v:vehicles){
			VCar car = new VCar(v.getX()*30, v.getY()*30,v.getDirection());
			cars.add(car);
			rootLayout.getChildren().add(car);
		}
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
