package com.preag.miniupdater;

import com.preag.core.ui.service.FXMLService;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;
public class MiniUpdaterView extends BorderPane {
	private ObjectProperty<Boolean> download = new SimpleObjectProperty<>(false);
	private ObjectProperty<Double> downloadProgress = new SimpleObjectProperty<>();
	private StringProperty propertyFilePath = new SimpleStringProperty();
	private ObjectProperty<Boolean> checkUpdate = new SimpleObjectProperty<>(false);
	
	public MiniUpdaterView() {
		FXMLService.INSTANCE.loadView(this);
	}
	
	public final ObjectProperty<Boolean> downloadProperty() {
		return this.download;
	}
	
	public final Boolean isDownload() {
		return this.downloadProperty().get();
	}
	
	public final void setDownload(final Boolean download) {
		this.downloadProperty().set(download);
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

	public final ObjectProperty<Boolean> checkUpdateProperty() {
		return this.checkUpdate;
	}
	

	public final Boolean getCheckUpdate() {
		return this.checkUpdateProperty().get();
	}
	

	public final void checkUpdate() {
		this.checkUpdateProperty().set(true);
	}
	
	
	
}
