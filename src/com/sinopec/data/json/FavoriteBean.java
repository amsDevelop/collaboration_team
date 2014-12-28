package com.sinopec.data.json;

public class FavoriteBean {
	public Integer favoriteId;
	public String projectId;
	public String projectName;
	public String userId;

	
	public FavoriteBean(Integer favoriteId, String projectId,
			String projectName, String userId) {
		super();
		this.favoriteId = favoriteId;
		this.projectId = projectId;
		this.projectName = projectName;
		this.userId = userId;
	}
	
	public Integer getFavoriteId() {
		return favoriteId;
	}
	public void setFavoriteId(Integer favoriteId) {
		this.favoriteId = favoriteId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
