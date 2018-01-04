package com.preag.miniupdater;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
protected MiniUpdaterView updateChecker;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			MiniUpdaterView miniUpdaterView = new MiniUpdaterView();
			miniUpdaterView.setPropertyFilePath("fileServer.properties");
			miniUpdaterView.checkUpdate();
			Scene scene = new Scene(miniUpdaterView,700,600);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
