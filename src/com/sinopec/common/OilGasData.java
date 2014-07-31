package com.sinopec.common;

/**
 * 油气专题对象
 */
public class OilGasData {
	private String url;
	private boolean isChecked = false;
	private boolean isVisible = false;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
