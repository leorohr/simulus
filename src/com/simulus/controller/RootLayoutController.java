package com.simulus.controller;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.simulus.EditorApp;
import com.simulus.MainApp;
import com.simulus.io.FileIO;
import com.simulus.io.SimConfigXML;
import com.simulus.util.Configuration;
import com.simulus.util.enums.VehicleColorOption;

public class RootLayoutController implements Initializable {

    @FXML
    MenuBar menuBar;
    @FXML
    MenuItem newMapMItem;
    @FXML
    MenuItem openMapMItem;
    @FXML
    MenuItem editMapMItem;
    @FXML
    MenuItem openSimMItem;
    @FXML
    MenuItem saveSimMItem;
    @FXML 
    MenuItem exportStatsMItem;
    @FXML
    MenuItem closeMItem;
    @FXML
    MenuItem aboutMItem;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        menuBar.setUseSystemMenuBar(true);

        closeMItem.setOnAction((event) -> Platform.exit());

        newMapMItem.setOnAction((event) ->{
        	Stage editorStage = new Stage();
            EditorApp editor = null;
			try {
				editor = EditorApp.class.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
            editor.start(editorStage);
        });

        /*
         * Opens the current map in the map editor. Shuts down the simulator.
         */
        editMapMItem.setOnAction((event) -> {
        	
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.initOwner(MainApp.getInstance().getPrimaryStage());
        	alert.setTitle("Opening Editor");
        	alert.setHeaderText("Opening the Editor will close the current simulation.");
        	alert.setContentText("Continue?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() != ButtonType.OK)
        		return;

        	EditorApp editor = EditorApp.getInstance(); 
        	if(editor == null)
        		editor = new EditorApp();
            editor.start(new Stage());
            editor.loadMap(SimulationController.getInstance().getLastLoadedMap().toPath().toString());
            
            try {
				MainApp.getInstance().getPrimaryStage().close();
				MainApp.getInstance().stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
        });

        openMapMItem.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Map...");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Simulus Map Files", "*.map"));
            fileChooser.setInitialFileName("CustomMap.map");
            File selectedFile = fileChooser.showOpenDialog(MainApp.getInstance().getPrimaryStage());
            SimulationController.getInstance().getMap().loadMap(selectedFile);            
        });
        
        /*
         * Exports the current simulation's statistics to a user-defined csv file. 
         */
        exportStatsMItem.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Map XML...");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV File (*.csv)", "*.csv"));
            fileChooser.setInitialFileName("simStatExport.csv");
            File selectedFile = fileChooser.showSaveDialog(MainApp.getInstance().getPrimaryStage());
            File sourceFile = Configuration.getTempStatsFile();
    		
    			try {
    				FileIO fio = new FileIO();
					fio.copyFile(sourceFile, selectedFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
    		
        });
        
        /*
         * Reads and applies a simulation configuration file.
         */
    	openSimMItem.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Simulation XML...");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Simulus Sim Files", "*.sim"));
            fileChooser.setInitialFileName("CustomSim.sim");
            File selectedFile = fileChooser.showOpenDialog(MainApp.getInstance().getPrimaryStage());
            
            SimConfigXML sxml = new SimConfigXML();
            sxml.readXML(selectedFile.getPath());
            
    		MainApp.getInstance().getControlsController().numCarSlider.setValue(sxml.getNoCars());
    		MainApp.getInstance().getControlsController().tickrateSlider.setValue(sxml.getTickRate()); 
    		MainApp.getInstance().getControlsController().spawnrateSlider.setValue(sxml.getSpawnRate()); 
    		MainApp.getInstance().getControlsController().maxcarspeedSlider.setValue(sxml.getMaxSpeedCars()); 
    		MainApp.getInstance().getControlsController().cartruckratioSlider.setValue(sxml.getCarTruckRatio()); 
    		MainApp.getInstance().getControlsController().debugCheckbox.setSelected(sxml.isDebugMode()); 
    		MainApp.getInstance().getControlsController().recklessnormalSlider.setValue(sxml.getRecklessNormRatio()); 
    		
			if(sxml.getCarColourOption().equals("User")) {
				MainApp.getInstance().getControlsController().carcolorComboBox.getSelectionModel().select(VehicleColorOption.USER);
				MainApp.getInstance().getControlsController().carcolorPicker.setValue(Color.web(sxml.getCarColour())); 
				MainApp.getInstance().getControlsController().carcolorPicker.setDisable(false);
			}else if (sxml.getCarColourOption().equals("Speed")){
				MainApp.getInstance().getControlsController().carcolorComboBox.getSelectionModel().select(VehicleColorOption.SPEED);
				MainApp.getInstance().getControlsController().carcolorPicker.setDisable(true);
			}else if (sxml.getCarColourOption().equals("Behavior")){
				MainApp.getInstance().getControlsController().carcolorComboBox.getSelectionModel().select(VehicleColorOption.BEHAVIOR);
				MainApp.getInstance().getControlsController().carcolorPicker.setDisable(true);
			}
			
			if(sxml.getTruckColourOption().equals("User")) {
				MainApp.getInstance().getControlsController().truckcolorComboBox.getSelectionModel().select(VehicleColorOption.USER);
				MainApp.getInstance().getControlsController().truckcolorPicker.setValue(Color.web(sxml.getTruckColourOption())); 
				MainApp.getInstance().getControlsController().truckcolorPicker.setDisable(false);
			}else if (sxml.getTruckColourOption().equals("Speed")){
				MainApp.getInstance().getControlsController().truckcolorComboBox.getSelectionModel().select(VehicleColorOption.SPEED);
				MainApp.getInstance().getControlsController().truckcolorPicker.setDisable(true);
			}else if (sxml.getTruckColourOption().equals("Behavior")){
				MainApp.getInstance().getControlsController().truckcolorComboBox.getSelectionModel().select(VehicleColorOption.BEHAVIOR);
				MainApp.getInstance().getControlsController().truckcolorPicker.setDisable(true);
			}

            
        });
    	
    	/*
    	 * Stores the current simulation configuration in a user-defined file.
    	 */
        saveSimMItem.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Simulation XML...");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Simulus Sim Files", "*.sim"));
            fileChooser.setInitialFileName("CustomSim.sim");
            File selectedFile = fileChooser.showSaveDialog(MainApp.getInstance().getPrimaryStage());
            
            SimConfigXML sxml = new SimConfigXML();
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date date = new Date();
    		
            sxml.writeXML(selectedFile.getPath(), "Simulation File", dateFormat.format(date), "A simulation parameter file", "Team Simulus", 
            		MainApp.getInstance().getControlsController().numCarSlider.getValue(), 
            		MainApp.getInstance().getControlsController().tickrateSlider.getValue(), 
            		MainApp.getInstance().getControlsController().spawnrateSlider.getValue(), 
            		MainApp.getInstance().getControlsController().maxcarspeedSlider.getValue(), 
            		MainApp.getInstance().getControlsController().cartruckratioSlider.getValue(), 
            		MainApp.getInstance().getControlsController().debugCheckbox.isSelected(), 
            		MainApp.getInstance().getControlsController().recklessnormalSlider.getValue(), 
            		MainApp.getInstance().getControlsController().carcolorComboBox.getSelectionModel().getSelectedItem().toString(), 
            		MainApp.getInstance().getControlsController().carcolorPicker.getValue().toString(), 
            		MainApp.getInstance().getControlsController().truckcolorComboBox.getSelectionModel().getSelectedItem().toString(), 
            		MainApp.getInstance().getControlsController().truckcolorPicker.getValue().toString());
            
        });

        aboutMItem.setOnAction((event) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setResizable(false);
            ImageView img = new ImageView(RootLayoutController.class.getResource("/resources/simulus.png").toString());
            img.setPreserveRatio(true);
            img.setFitHeight(75.0d);
            Label infoPanel = new Label("Simulus Traffic Simulation\nVer. 0.1");
            Hyperlink link = new Hyperlink("http://github.com/leorohr/simulus");
            Button okayBtn = new Button("Okay");
            okayBtn.setOnAction((btnEvent) -> dialog.close());

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(5.0d);
            vbox.setPadding(new Insets(0.0d, 0.0d, 5.0d, 0.0d));
            vbox.getChildren().addAll(img, infoPanel, link, okayBtn);

            dialog.setScene(new Scene(vbox));
            dialog.show();
        });
	}
	

}
