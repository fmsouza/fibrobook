package com.fibrobook.model;

public class SymphtomSummary {
	
	private User user;
	private Disease disease;
	private String date;
	private float intensity;
	private String local;

	public SymphtomSummary(User user, Disease disease, String date, float intensity, String local) {
		this.setUser(user);
		this.setDisease(disease);
		this.setDate(date);
		this.setIntensity(intensity);
		this.setLocal(local);
	}

	public SymphtomSummary(User user, Disease disease, String date, float intensity) {
		this.setUser(user);
		this.setDisease(disease);
		this.setDate(date);
		this.setIntensity(intensity);
	}

	public SymphtomSummary(User user, Disease disease, String date) {
		this.setUser(user);
		this.setDisease(disease);
		this.setDate(date);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Disease getDisease() {
		return disease;
	}

	public void setDisease(Disease disease) {
		this.disease = disease;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

}
