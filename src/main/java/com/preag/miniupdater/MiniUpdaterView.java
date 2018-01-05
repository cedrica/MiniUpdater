package com.preag.miniupdater;

import java.io.IOException;

import com.preag.miniupdater.data.DummySoftareUpdate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class MiniUpdaterView extends BorderPane {
	private ObjectProperty<Double> downloadProgress = new SimpleObjectProperty<>();
	private StringProperty propertyFilePath = new SimpleStringProperty();
	private ObjectProperty<Boolean> startUpdate = new SimpleObjectProperty<>(false);
	private ObjectProperty<DummySoftareUpdate> softwareUpdate = new SimpleObjectProperty<>();
	
	public MiniUpdaterView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/preag/miniupdater/MiniUpdaterView.fxml"));
			loader.setRoot(this);
			loader.load();
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}

	}

	public final ObjectProperty<Double> downloadProgressProperty() {
		return this.downloadProgress;
	}

	public final Double getDownloadProgress() {
		return this.downloadProgressProperty().get();
	}

	public final void setDownloadProgress(final Double downloadProgress) {
		this.downloadProgressProperty().set(downloadProgress);
	}

	public final StringProperty propertyFilePathProperty() {
		return this.propertyFilePath;
	}

	public final String getPropertyFilePath() {
		return this.propertyFilePathProperty().get();
	}

	public final void setPropertyFilePath(final String propertyFilePath) {
		this.propertyFilePathProperty().set(propertyFilePath);
	}

	public final ObjectProperty<Boolean> startUpdateProperty() {
		return this.startUpdate;
	}

	public final Boolean getStartUpdate() {
		return this.startUpdateProperty().get();
	}

	/**
	 * this function must be call at least, when all configuration is done
	 */
	public final void startUpdate() {
		this.startUpdateProperty().set(true);
	}

	public final ObjectProperty<DummySoftareUpdate> softwareUpdateProperty() {
		return this.softwareUpdate;
	}
	

	public final DummySoftareUpdate getSoftwareUpdate() {
		return this.softwareUpdateProperty().get();
	}
	

	public final void setSoftwareUpdate(final DummySoftareUpdate softwareUpdate) {
		this.softwareUpdateProperty().set(softwareUpdate);
	}
}
