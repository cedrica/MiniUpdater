package com.preag.miniupdater.manager;

import java.net.InetAddress;
import java.util.Properties;

import com.preag.miniupdater.MiniUpdaterView;
import com.preag.miniupdater.common.Helper;
import com.preag.miniupdater.data.DummySoftareUpdate;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

public class SmbFileServerConnectionManager {
	
	private String serverAddress = null;
	private String userName = null;
	private String password = null;
	private String domain = null;
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

	/**
	 * Load all Properties
	 * @param prop
	 */
	public void loadProperty(MiniUpdaterView miniUpdaterView) {
		DummySoftareUpdate softwareUpdate = miniUpdaterView.getSoftwareUpdate();
		this.serverAddress = Helper.retrieveServerAddress(softwareUpdate.getUpdateUrl());
		Properties prop = Helper.aquierePropertiesFile(miniUpdaterView.getPropertyFilePath());
		this.serverAddress = prop.getProperty("serveraddress");
		this.userName = prop.getProperty("username");
		this.password = prop.getProperty("password");
		this.domain = prop.getProperty("domain");
	}



	public NtlmPasswordAuthentication getAuthentification() {
		return authentification;
	}

	public void setAuthentification(NtlmPasswordAuthentication authentification) {
		this.authentification = authentification;
	}
}
