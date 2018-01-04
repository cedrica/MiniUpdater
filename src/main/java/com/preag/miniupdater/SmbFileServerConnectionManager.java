package com.preag.miniupdater;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

public class SmbFileServerConnectionManager {
	
	private String serverAddress = null;
	private String userName = null;
	private String password = null;
	private String domain = null;
	private String folder = null;
	private String programbasename = null;
	private NtlmPasswordAuthentication authentification ;
	
	public  SmbFileServerConnectionManager (MiniUpdaterView miniUpdaterView){
		loadProperty(miniUpdaterView);
		connectToSmb();
	}
	
	/**
	 * Try to authentificate with the smb server
	 * 
	 * @return
	 */
	public boolean connectToSmb() {
		try {
			setAuthentification(login());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public NtlmPasswordAuthentication login() throws Exception {
		InetAddress ipAddress = InetAddress.getByName(this.serverAddress);
		UniAddress myDomain = new UniAddress(ipAddress);
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(domain, userName, password);
		try {
			SmbSession.logon(myDomain, auth);
		} catch (SmbException e) {
			e.printStackTrace();
		}
		return auth;
	}
	public  String pathToExeFile(){
		return this.serverAddress.concat(this.folder).concat(this.programbasename).concat(".exe");
	}
	public  String pathToChangeLogFile(){
		return this.serverAddress.concat(this.folder).concat(this.programbasename).concat(".html");
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
	/**
	 * Load all Properties
	 * @param prop
	 */
	public void loadProperty(MiniUpdaterView miniUpdaterView) {
		Properties prop = aquierePropertiesFile(miniUpdaterView.getPropertyFilePath());
		this.serverAddress = prop.getProperty("serveraddress");
		this.userName = prop.getProperty("username");
		this.password = prop.getProperty("password");
		this.domain = prop.getProperty("domain");
		this.folder = prop.getProperty("folder");
		this.programbasename = prop.getProperty("programbasename");
	}

	public NtlmPasswordAuthentication getAuthentification() {
		return authentification;
	}

	public void setAuthentification(NtlmPasswordAuthentication authentification) {
		this.authentification = authentification;
	}
}
