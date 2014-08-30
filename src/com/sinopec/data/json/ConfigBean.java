package com.sinopec.data.json;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.database.PrimaryKey;
import com.lenovo.nova.util.parse.anntation.database.TableName;
public class ConfigBean extends Bean{

	@PrimaryKey
	private int _id ;
	
	private String IP;

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
