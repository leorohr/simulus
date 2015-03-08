package com.simulus.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.simulus.EditorApp;
import com.simulus.MainApp;
import com.simulus.io.FileIO;
import com.simulus.io.MapXML;
import com.simulus.util.Configuration;

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
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            fileChooser.setInitialFileName("CustomMap.xml");
            File selectedFile = fileChooser.showOpenDialog(MainApp.getInstance().getPrimaryStage());
            SimulationController.getInstance().getMap().loadMap(selectedFile);            
        });
        
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
