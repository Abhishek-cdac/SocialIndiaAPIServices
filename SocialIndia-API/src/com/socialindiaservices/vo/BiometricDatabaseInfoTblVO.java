package com.socialindiaservices.vo;

import java.io.Serializable;

public class BiometricDatabaseInfoTblVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ID;
	private String socyteaId;
	private String bioHostname;
	private String bioUsername;
	private String bioPassword;
	private String bioDatabase;
	private String bioPort;
	
	
	public BiometricDatabaseInfoTblVO(){}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getSocyteaId() {
		return socyteaId;
	}

	public void setSocyteaId(String socyteaId) {
		this.socyteaId = socyteaId;
	}

	public String getBioHostname() {
		return bioHostname;
	}

	public void setBioHostname(String bioHostname) {
		this.bioHostname = bioHostname;
	}

	public String getBioUsername() {
		return bioUsername;
	}

	public void setBioUsername(String bioUsername) {
		this.bioUsername = bioUsername;
	}

	public String getBioPassword() {
		return bioPassword;
	}

	public void setBioPassword(String bioPassword) {
		this.bioPassword = bioPassword;
	}

	public String getBioDatabase() {
		return bioDatabase;
	}

	public void setBioDatabase(String bioDatabase) {
		this.bioDatabase = bioDatabase;
	}

	public String getBioPort() {
		return bioPort;
	}

	public void setBioPort(String bioPort) {
		this.bioPort = bioPort;
	}

}