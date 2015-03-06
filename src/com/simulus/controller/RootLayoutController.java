package com.simulus.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    MenuItem closeMItem;
    @FXML
    MenuItem aboutMItem;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        menuBar.setUseSystemMenuBar(true);

        closeMItem.setOnAction((event) -> Platform.exit());

        newMapMItem.setOnAction((event) ->{
            //TODO launch map editor
        });

        editMapMItem.setOnAction((event) -> {
            Stage editorStage = new Stage();
            EditorApp editor = new EditorApp();
            editor.loadMap(SimulationController.getInstance().getLastLoadedMap().toPath().toString());
            editor.start(editorStage);
        });

        openMapMItem.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Map...");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            fileChooser.setInitialFileName("CustomMap.xml");
            File selectedFile = fileChooser.showOpenDialog(MainApp.getInstance().getPrimaryStage());
            SimulationController.getInstance().getMap().loadMap(selectedFile);            
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
