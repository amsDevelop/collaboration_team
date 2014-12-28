package com.sinopec.data.json;


public class UserBean {
	public String userId;
	public String userName;
	public String password;
	public UserBean(){};
	public UserBean(String userId,String userName, String password){
		this.userId = userId;
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	
	

}
