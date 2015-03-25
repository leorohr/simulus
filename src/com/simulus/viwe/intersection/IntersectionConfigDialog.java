package com.simulus.viwe.intersection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;

import com.simulus.MainApp;
import com.simulus.controller.IntersectionDialogController;

public class IntersectionConfigDialog extends Dialog<Map<String, Long>> { 
		
	public IntersectionConfigDialog(String is, long nsPhaseTime, long wePhaseTime) {
		setTitle("Intersection " + is);
		setHeaderText("Adjust the traffic light times.");

		AnchorPane sliderPane = null;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class
				.getResource("view/ui/IntersectionDialog.fxml"));
		try {
			sliderPane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		IntersectionDialogController controller = loader.getController();
		controller.setNsPhase(nsPhaseTime);
		controller.setWePhase(wePhaseTime);
		
		getDialogPane().setContent(sliderPane);
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		setResultConverter(dialogButton -> {
			if(dialogButton == ButtonType.OK) {
				Map<String, Long> res = new HashMap<String, Long>();  
				res.put("nsphase", controller.getNsPhase());
				res.put("wephase", controller.getWePhase());
				return res;
			}
			return null;
		});
	}
}
