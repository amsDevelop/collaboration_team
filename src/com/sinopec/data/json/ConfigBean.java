package com.sinopec.data.json;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.database.ID;
public class ConfigBean extends Bean{

	@ID
	private int _id ;
	
	private String IP;

	public ConfigBean() {
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int get_id() {
		return _id;
	}
	
	
	
}
