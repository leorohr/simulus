package com.simulus.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

import com.simulus.util.enums.VehicleColorOption;
import com.simulus.view.Map;

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

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		numVehiclesSeries = new LineChart.Series<Number, Number>();
        numVehiclesChart.getData().addAll(numVehiclesSeries);
        
        avgSpeedSeries = new LineChart.Series<Number, Number>();
        avgSpeedChart.getData().addAll(avgSpeedSeries);
        
        congestionSeries = new LineChart.Series<Number, Number>();
        congestionChart.getData().addAll(congestionSeries);
        
        waitingTimeSeries = new LineChart.Series<Number, Number>();
        waitingTimeChart.getData().addAll(waitingTimeSeries);

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
	
		
				
		startButton.setOnAction((event) -> {
			simulationController.startSimulation();
		});
		
		stopButton.setOnAction((event) -> {
			simulationController.stopSimulation();
		});

        resetButton.setOnAction((event) -> {
           simulationController.resetSimulation();
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
        if(numVehiclesSeries.getData().size() > MAX_DATA_POINTS) { //enforce only MAX_DATA_POINTS-many values are displayed
            numVehiclesSeries.getData().remove(0, 10);
            ((NumberAxis)numVehiclesChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)numVehiclesChart.getXAxis()).setUpperBound(dataCount);
        }
    	
    	//Update Avg. Speed Chart
        avgSpeedSeries.getData().add(new LineChart.Data<>(dataCount, Math.round(map.getAverageSpeed())));
        if(avgSpeedSeries.getData().size() > MAX_DATA_POINTS) {
            avgSpeedSeries.getData().remove(0, 10);
            ((NumberAxis)avgSpeedChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)avgSpeedChart.getXAxis()).setUpperBound(dataCount);
        }
        
        //Update Congestion Chart
        congestionSeries.getData().add(new LineChart.Data<>(dataCount, Math.round(map.getCongestionValue()*100)));
        if(congestionSeries.getData().size() > MAX_DATA_POINTS) {
            congestionSeries.getData().remove(0, 10);
            ((NumberAxis)congestionChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)congestionChart.getXAxis()).setUpperBound(dataCount);
        }
        
        //Update Waiting Time Chart
        waitingTimeSeries.getData().add(new LineChart.Data<>(dataCount, Math.round(map.getAvgWaitingTime()*1.8))); //1 tick simulates 1.8secs in real-time
        if(waitingTimeSeries.getData().size() > MAX_DATA_POINTS) {
            waitingTimeSeries.getData().remove(0, 10);
            ((NumberAxis)waitingTimeChart.getXAxis()).setLowerBound((0 > dataCount-MAX_DATA_POINTS ? 0 : dataCount-MAX_DATA_POINTS));
            ((NumberAxis)waitingTimeChart.getXAxis()).setUpperBound(dataCount);
        }   
	}
	
    public Color getCarColor() {
    	return carcolorPicker.getValue();
    }
    
    public Color getTruckColor() {
    	return truckcolorPicker.getValue();
    }

}
