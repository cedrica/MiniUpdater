package com.preag.miniupdater;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class SmbFileServerManager {

	private MiniUpdaterView miniUpdaterView;
	private URL filesFeed;
	private String version;
	private String link;
	private ObjectProperty<Long> originalSize = new SimpleObjectProperty<>();
	private ObjectProperty<Long> downloadedSize = new SimpleObjectProperty<>();

	private static String SMB_PATH = "smb:";
	private SmbFileServerConnectionManager smbFileServerConnectionManager;

	public SmbFileServerManager(MiniUpdaterView miniUpdaterView, WebEngine webEngine) {
		this.miniUpdaterView = miniUpdaterView;
		smbFileServerConnectionManager = new SmbFileServerConnectionManager(miniUpdaterView);

		try {
			File file = convertSMBToTempFile(smbFileServerConnectionManager.pathToChangeLogFile());
			if(file != null)
			webEngine.load(file.toURI().toURL().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.miniUpdaterView.addEventHandler(MiniUpdaterEvent.START_OR_PAUSE_DOWNLOAD, evt -> {
			updateDownload(smbFileServerConnectionManager.pathToExeFile());
		});
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 * Call this function when update button is started
	 * 
	 * @param updaterURL
	 * @param preUpdateView
	 */
	public void updateDownload(String updaterURL) {
		if (this.miniUpdaterView.isDownload()) {
			startDownload(updaterURL);
			downloadedSizeProperty()
					.addListener(evt -> this.miniUpdaterView.setDownloadProgress(updateDownloadProgress()));
		}
	}

	void startDownload(String updaterUrl) {
		Task<File> downloadTask = createDownloadTask(updaterUrl);
		new Thread(downloadTask).start();
		downloadTask.valueProperty().addListener(evt -> finishDownload(downloadTask.getValue()));
	}

	private void finishDownload(File newVal) {

		if (newVal != null && newVal.exists()) {
			try {
				Desktop.getDesktop().open(newVal);
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// ALERT SOMETHING
		}
	}

	private Task<File> createDownloadTask(String updaterURL) {
		return new Task<File>() {

			@Override
			protected File call() throws Exception {
				File newVersion = convertSMBToTempFileAndDownload(updaterURL);
				if (newVersion == null) {
					newVersion = new File("");
				}
				return newVersion;
			}
		};
	}

	double updateDownloadProgress() {
		Long originalSize = getOriginalSize();
		Long downloadedSize = getDownloadedSize();
		return downloadedSize.doubleValue() / originalSize.doubleValue();
	}

	/**
	 * <item> < </item>
	 * 
	 * @return
	 */
	public boolean updateNeeded() {
		try {
			this.filesFeed = new URL(smbFileServerConnectionManager.pathToChangeLogFile());
			InputStream inputStream = this.filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			Node latesFile = document.getElementsByTagName("div").item(0);
			NodeList childNodes = latesFile.getChildNodes();
			this.version = childNodes.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
			this.link = childNodes.item(3).getTextContent();
			System.out.println("Verion: " + this.version + " link: " + this.link);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static String getExtensionFromFilename(String filename) {
		if (filename == null) {
			return null;
		}
		if (filename.contains(".")) {
			String[] split = filename.split("\\.");
			return split[split.length - 1];
		}
		return null;
	}

	public File convertSMBToTempFile(String source) {
		SmbFile smbFile = getSmbFile(source);
		try {
			if (smbFile.exists()) {
				InputStream in = smbFile.getInputStream();
				File temp = File.createTempFile(source, ".html");
				FileOutputStream out = new FileOutputStream(temp);
				int len;
				final byte[] buf = new byte[1024 * 1024];
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				return temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public File convertSMBToTempFileAndDownload(String source) {
		SmbFile smbFile = getSmbFile(source);
		try {
			if (smbFile.exists()) {
				try {
					InputStream in = smbFile.getInputStream();
					String extension = getExtensionFromFilename(smbFile.getName());
					File temp = File.createTempFile(source, "." + extension);
					FileOutputStream out = new FileOutputStream(temp);

					setOriginalSize(smbFile.length());
					int len;
					final byte[] buf = new byte[1024 * 1024];
					setDownloadedSize(0l);
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
						setDownloadedSize(getDownloadedSize() + len);
					}
					in.close();
					out.close();

					return temp;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public SmbFile getSmbFile(String source) {
		try {
			SmbFile originalFile = new SmbFile(SMB_PATH + "//" + source,
					smbFileServerConnectionManager.getAuthentification());
			return originalFile;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final ObjectProperty<Long> originalSizeProperty() {
		return this.originalSize;
	}

	public final Long getOriginalSize() {
		return this.originalSizeProperty().get();
	}

	public final void setOriginalSize(final Long originalSize) {
		this.originalSizeProperty().set(originalSize);
	}

	public final ObjectProperty<Long> downloadedSizeProperty() {
		return this.downloadedSize;
	}

	public final Long getDownloadedSize() {
		return this.downloadedSizeProperty().get();
	}

	public final void setDownloadedSize(final Long downloadedSize) {
		this.downloadedSizeProperty().set(downloadedSize);
	}
}
