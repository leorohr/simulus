package com.simulus.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RootLayoutController implements Initializable {

    @FXML
    MenuBar menuBar;
    @FXML
    MenuItem newMapMItem;
    @FXML
    MenuItem openMapMItem;
    @FXML
    MenuItem saveMapMItem;
    @FXML
    MenuItem closeMItem;
    @FXML
    MenuItem aboutMItem;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        menuBar.setUseSystemMenuBar(true);

        closeMItem.setOnAction((event) -> {
            Platform.exit();
        });


        aboutMItem.setOnAction((event) -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setResizable(false);
            ImageView img = new ImageView(RootLayoutController.class.getResource("/resources/simulus.png").toString());
            img.setPreserveRatio(true);
            img.setFitHeight(75.0d);
            Label infoPanel = new Label("Copyright... SimulusSimulusSimulus\nVer. 0.1");
            Button okayBtn = new Button("Okay");
            okayBtn.setOnAction((btnEvent) -> dialog.close());

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(5.0d);
            vbox.getChildren().addAll(img, infoPanel, okayBtn);

            dialog.setScene(new Scene(vbox));
            dialog.show();
        });
	}

}
