package com.preag.miniupdater;
	
import java.util.Properties;

import com.preag.miniupdater.common.Helper;
import com.preag.miniupdater.data.DummySoftareUpdate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
protected MiniUpdaterView updateChecker;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//GET NEW SOFTWARE UPATE INFO FROM DATABASE
			DummySoftareUpdate dummySoftareUpdate = new DummySoftareUpdate();
			dummySoftareUpdate.setUpdateUrl("213.95.137.180/SW_Update/Pep/update/PepExcel-1.3.0.exe");
			dummySoftareUpdate.setVersion("1.3.0");
			//----------------------------------
			if(updateNeeded(dummySoftareUpdate, "fileServer.properties")){
				MiniUpdaterView miniUpdaterView = new MiniUpdaterView();
				miniUpdaterView.setPropertyFilePath("fileServer.properties");
				miniUpdaterView.setSoftwareUpdate(dummySoftareUpdate);
				miniUpdaterView.startUpdate();
				Scene scene = new Scene(miniUpdaterView,700,600);
				primaryStage.setScene(scene);
			}
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean updateNeeded(DummySoftareUpdate dummySoftareUpdate, String fileServerPropFile) {
		Properties prop = Helper.aquierePropertiesFile(fileServerPropFile);
		String currentVersion = prop.getProperty("currentVersion");
		return !dummySoftareUpdate.getVersion().equals(currentVersion);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
