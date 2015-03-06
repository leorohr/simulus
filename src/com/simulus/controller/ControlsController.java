package com.simulus.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import com.simulus.MainApp;
import com.simulus.util.ResourceBuilder;
import com.simulus.util.enums.VehicleColorOption;
import com.simulus.view.map.Map;

public class ControlsController implements Initializable {

	@FXML
	Button startButton;
	@FXML
	Button stopButton;
    @FXML
    Button resetButton;
	@FXML
	Slider numCarSlider;
	@FXML
	Slider tickrateSlider;
	@FXML
	Slider maxcarspeedSlider;
	@FXML
	Slider spawnrateSlider;
	@FXML
	Slider cartruckratioSlider;
	@FXML
	Slider recklessnormalSlider;
	@FXML 
	Label numCarLabel;
	@FXML
	Label tickrateLabel;
	@FXML
	Label maxcarspeedLabel;
	@FXML
	Label spawnrateLabel;
	@FXML
	Label cartruckratioLabel;
	@FXML
	Label recklessnormalLabel;
	@FXML
	Button randTrafficLightsButton;
	@FXML
	Button spawnAmbulanceButton;
	@FXML
	ComboBox<VehicleColorOption> carcolorComboBox;
	@FXML
	ColorPicker carcolorPicker;
	@FXML
	ComboBox<VehicleColorOption> truckcolorComboBox;
	@FXML
	ColorPicker truckcolorPicker;
	@FXML
	CheckBox debugCheckbox;

	@FXML
	ImageView resizeIcon;
    @FXML
    TitledPane statisticsPane;
	@FXML
    LineChart<Number, Number> numVehiclesChart;
    @FXML
    LineChart<Number, Number> avgSpeedChart;
    @FXML
    LineChart<Number, Number> congestionChart;
    @FXML
    LineChart<Number, Number> waitingTimeChart;

    private static int MAX_DATA_POINTS = 100;

    private int dataCount = 0;
    private LineChart.Series<Number, Number> numVehiclesSeries;
    private LineChart.Series<Number, Number> avgSpeedSeries;
    private LineChart.Series<Number, Number> congestionSeries;
    private LineChart.Series<Number, Number> waitingTimeSeries;
    private LineChart.Series<Number, Number> emWaitingTimeSeries; //waiting time of emergency vehicles

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Allowing to pop-out the statistics panel
		resizeIcon.setImage(ResourceBuilder.getResizeIcon());
		resizeIcon.setOnMouseClicked((event) -> {

			Stage chartStage = new Stage();
			chartStage.setTitle("Statistics");
			numVehiclesChart.getXAxis().setPrefWidth(400);
			avgSpeedChart.getXAxis().setPrefWidth(400);
			congestionChart.getXAxis().setPrefWidth(400);
			waitingTimeChart.getXAxis().setPrefWidth(400);
			VBox chartBox = new VBox(numVehiclesChart, avgSpeedChart, congestionChart, waitingTimeChart);
			
			chartStage.setScene(new Scene(chartBox, 500, 800));
			chartStage.setX(MainApp.getInstance().getPrimaryStage().getX() + 600);
			chartStage.setY(MainApp.getInstance().getPrimaryStage().getY());
			chartStage.setOnCloseRequest((WindowEvent) -> {
				numVehiclesChart.getXAxis().setPrefWidth(200);
				avgSpeedChart.getXAxis().setPrefWidth(200);
				congestionChart.getXAxis().setPrefWidth(200);
				waitingTimeChart.getXAxis().setPrefWidth(200);
				
				statisticsPane.setContent((new VBox(numVehiclesChart, avgSpeedChart, congestionChart, waitingTimeChart)));
				statisticsPane.expandedProperty().set(true);
			});
			chartStage.show();
		}); 
		
		//Initialise charts
		numVehiclesSeries = new LineChart.Series<Number, Number>();
        numVehiclesChart.getData().addAll(numVehiclesSeries);
        
        avgSpeedSeries = new LineChart.Series<Number, Number>();
        avgSpeedChart.getData().addAll(avgSpeedSeries);
        
        congestionSeries = new LineChart.Series<Number, Number>();
        congestionChart.getData().addAll(congestionSeries);
        
        waitingTimeSeries = new LineChart.Series<Number, Number>();
        waitingTimeSeries.setName("Avg. Waiting Time of Cars/Trucks");
        waitingTimeChart.getData().addAll(waitingTimeSeries);
        
        emWaitingTimeSeries = new LineChart.Series<Number, Number>();
        emWaitingTimeSeries.setName("Avg. Waiting Time of Ambulances");
        waitingTimeChart.getData().addAll(emWaitingTimeSeries);

        //Initialise sliders
        SimulationController simulationController = SimulationController.getInstance();
	
		tickrateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            tickrateLabel.setText(String.valueOf(newValue.intValue()));
            simulationController.setTickTime(newValue.intValue());
        });
		
		numCarSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			numCarLabel.setText(String.valueOf(newValue.intValue()));
			simulationController.setMaxCars(newValue.intValue());
		});
		
		spawnrateSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			spawnrateLabel.setText(String.valueOf(newValue.intValue()));
			simulationController.setSpawnRate(newValue.intValue());
		});
		
		maxcarspeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			maxcarspeedLabel.setText(String.valueOf(newValue.intValue()));
			simulationController.setMaxCarSpeed(newValue.intValue());
		});
		
		cartruckratioSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			double roundedValue = (double) ((int)(newValue.doubleValue()*10))/10;
			cartruckratioLabel.setText(String.valueOf(roundedValue));
			simulationController.setCarTruckRatio(roundedValue);
		});
		
		recklessnormalSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			double roundedValue = (double) ((int)(newValue.doubleValue()*10))/10;
			recklessnormalLabel.setText(String.valueOf(roundedValue));
			simulationController.setRecklessNormalRatio(roundedValue);
		});
		
		randTrafficLightsButton.setOnAction((event) -> {
			simulationController.getMap().randomiseTrafficLights();
		});
		
		spawnAmbulanceButton.setOnAction((event) -> {
			simulationController.spawnAmbulance();
		});
		
		//Initialise Colouroptions
		carcolorComboBox.getItems().addAll(VehicleColorOption.values());
		carcolorComboBox.getSelectionModel().select(0);
		carcolorComboBox.setOnAction((event) -> { 
	
			if(carcolorComboBox.getSelectionModel().getSelectedItem() == VehicleColorOption.USER) {
				carcolorPicker.setDisable(false);
			} else carcolorPicker.setDisable(true);
			
			simulationController.getMap().setCarColorOption(carcolorComboBox.getSelectionModel().getSelectedItem());
		});
		
		truckcolorComboBox.getItems().addAll(VehicleColorOption.values());
		truckcolorComboBox.getSelectionModel().select(0);
		truckcolorComboBox.setOnAction((event) -> { 
			
			if(truckcolorComboBox.getSelectionModel().getSelectedItem() == VehicleColorOption.USER) {
				truckcolorPicker.setDisable(false);
			} else truckcolorPicker.setDisable(true);
			
			simulationController.getMap().setTruckColorOption(truckcolorComboBox.getSelectionModel().getSelectedItem());
		});
				
		//Initialise Buttons
		startButton.setOnAction((event) -> {
			simulationController.startSimulation();
		});
		
		stopButton.setOnAction((event) -> {
			simulationController.stopSimulation();
		});

        resetButton.setOnAction((event) -> {
    		simulationController.resetSimulation(true);
        });
		
		debugCheckbox.setOnAction((event) -> {
			simulationController.setDebugFlag(debugCheckbox.isSelected());
		});
	}

	void updateCharts() {
		
		Map map = SimulationController.getInstance().getMap();
		dataCount++;
    	
    	//Update Vehicle Count Chart
		numVehiclesSeries.getData().add(new LineChart.Data<>(dataCount, map.getVehicleCount()));
    	
    	//Update Avg. Speed Chart
        avgSpeedSeries.getData().add(new LineChart.Data<>(dataCount, Math.round(map.getAverageSpeed())));
        
        //Update Congestion Chart
        congestionSeries.getData().add(new LineChart.Data<>(dataCount, Math.round(map.getCongestionValue()*100)));
        
        //Update Waiting Time Chart
        waitingTimeSeries.getData().add(new LineChart.Data<>(dataCount, Math.round(map.getAvgWaitingTime()*1.8))); //1 tick simulates 1.8secs in real-time
        emWaitingTimeSeries.getData().add(new LineChart.Data<>(dataCount, Math.round(map.getAvgEmWaitingTime()*1.8)));
        
        if(dataCount%MAX_DATA_POINTS == 0) {
            waitingTimeSeries.getData().remove(0, 10);
            emWaitingTimeSeries.getData().remove(0, 10);
            congestionSeries.getData().remove(0, 10);
            avgSpeedSeries.getData().remove(0, 10);
            numVehiclesSeries.getData().remove(0, 10);
            
            ((NumberAxis)congestionChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)congestionChart.getXAxis()).setUpperBound(dataCount);
            ((NumberAxis)waitingTimeChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)waitingTimeChart.getXAxis()).setUpperBound(dataCount);   
            ((NumberAxis)avgSpeedChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)avgSpeedChart.getXAxis()).setUpperBound(dataCount);
            ((NumberAxis)numVehiclesChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)numVehiclesChart.getXAxis()).setUpperBound(dataCount);
        }   
	}
	
    public Color getCarColor() {
    	return carcolorPicker.getValue();
    }
    
    public Color getTruckColor() {
    	return truckcolorPicker.getValue();
    }
    
    public void setAmbulanceButtonDisabled(boolean b) {
    	spawnAmbulanceButton.disableProperty().setValue(b);
    }
    
    /**
     * Resets all chart data.
     */
    public void resetCharts() {
    	numVehiclesSeries.getData().remove(0, numVehiclesSeries.getData().size());
    	avgSpeedSeries.getData().remove(0, avgSpeedSeries.getData().size());
    	congestionSeries.getData().remove(0, congestionSeries.getData().size());
    	waitingTimeSeries.getData().remove(0, waitingTimeSeries.getData().size());
    }

}
