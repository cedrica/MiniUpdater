package com.preag.miniupdater.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.preag.miniupdater.manager.SmbFileServerManager;

public class Helper {
	public static String constructPathToChangeLogFile(String updateUrl) {
		String[] split = updateUrl.split("/");
		if(split != null && split.length > 0){
			String exeFileName = split[split.length-1];
			String replace = exeFileName.replace(".exe",".html");
			return updateUrl.replace(exeFileName, replace);
		}
		return null;
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
	
	public static Properties aquierePropertiesFile(String propName) {
		InputStream input = null;
		Properties properties = new Properties();
		input = SmbFileServerManager.class.getResourceAsStream("/" + propName);
		try {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(input != null)
					input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	
	public static String retrieveServerAddress(String updateUrl) {
		String[] split = updateUrl.split("/");
		if(split != null && split.length > 0){
			return split[0];
		}
		return null;
	}
}
