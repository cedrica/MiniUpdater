package com.preag.miniupdater;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.MaskerPane;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.web.WebView;

public class MiniUpdaterController implements Initializable {

	@FXML
	MiniUpdaterView rootNode;
	@FXML
	ProgressBar progressBar;
	@FXML
	ToggleButton tgbDownload;
	@FXML
	WebView webView;
	@FXML
	Label lblIcon;
	@FXML
	Button btnCancel;
	@FXML
	MaskerPane progressIndicator;
	private Worker<Void> worker;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		worker = webView.getEngine().getLoadWorker();
		worker.stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == Worker.State.SUCCEEDED) {
					progressIndicator.setVisible(false);
					webView.setVisible(true);
					webView.toFront();
				}
			}
		});
		registerBindings();
		registerListener();

	}

	private void registerListener() {
		tgbDownload.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				lblIcon.getStyleClass().clear();
				lblIcon.getStyleClass().add("arrow-right-icon");
				tgbDownload.setText("Download starten");
			} else {
				lblIcon.getStyleClass().clear();
				lblIcon.getStyleClass().add("download-icon");
				tgbDownload.setText("Download stopen");
			}
		});
		rootNode.checkUpdateProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				new SmbFileServerManager(rootNode, webView.getEngine());
			}
		});
	}

	private void registerBindings() {
		progressIndicator.progressProperty().bind(worker.progressProperty());
		rootNode.downloadProperty().bind(tgbDownload.selectedProperty());
		progressBar.progressProperty().bind(rootNode.downloadProgressProperty());
		progressBar.visibleProperty().bind(tgbDownload.selectedProperty());
	}

	@FXML
	public void onStartDownload() {
		rootNode.fireEvent(new MiniUpdaterEvent(MiniUpdaterEvent.START_OR_PAUSE_DOWNLOAD));
	}

	@FXML
	public void onCancel() {
		Platform.exit();
	}

}
