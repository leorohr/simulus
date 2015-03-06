package com.simulus.controller;

import java.io.File;
import java.net.URL;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.simulus.EditorApp;
import com.simulus.MainApp;

public class EditorRootLayoutController implements Initializable {

    @FXML
    MenuBar menuBar;
    @FXML
    MenuItem newMapMItem;
    @FXML
    MenuItem openMapMItem;
    @FXML
    MenuItem saveMapMItem;
    @FXML
    MenuItem simulateMapMItem;
    @FXML
    MenuItem closeMItem;
    @FXML
    MenuItem aboutMItem;
    
    private File lastLoadedMap;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        menuBar.setUseSystemMenuBar(true);

        closeMItem.setOnAction((event) -> Platform.exit());

        newMapMItem.setOnAction((event) ->{
            //TODO launch map editor
        });

        saveMapMItem.setOnAction((event) -> {
           EditorApp.getInstance().saveMapDialog();
        });

        openMapMItem.setOnAction((event) -> {
        	EditorApp.getInstance().openMapDialog();

        });
        
        simulateMapMItem.setOnAction((event) -> {
        	
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.initOwner(EditorApp.getInstance().getPrimaryStage());
        	alert.setTitle("Opening Simulator");
        	alert.setHeaderText("Opening the Editor will close the current map.");
        	alert.setContentText("Continue?");
        	Optional<ButtonType> result = alert.showAndWait();
        	if(result.get() != ButtonType.OK)
        	return;
        	Stage mainStage = new Stage();
        	MainApp mainApp = null;
        	try {
        		mainApp = MainApp.class.newInstance();
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	//MainApp.start(mainStage);
        	//MainApp.loadMap(SimulationController.getInstance().getLastLoadedMap().toPath().toString());
        	EditorApp.getInstance().getPrimaryStage().close();
        	});
	       

        aboutMItem.setOnAction((event) -> {
        	
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setResizable(false);
            ImageView img = new ImageView(EditorRootLayoutController.class.getResource("/resources/simulus.png").toString());
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
