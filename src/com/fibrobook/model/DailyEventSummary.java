package com.fibrobook.model;

public class DailyEventSummary {
	
	private User user;
	private DailyEvent dailyEvent;
	private String date;
	private float intensity;
	private String local;

	public DailyEventSummary(User user, DailyEvent dailyEvent, String date, float intensity, String local) {
		this.setUser(user);
		this.setDailyEvent(dailyEvent);
		this.setDate(date);
		this.setIntensity(intensity);
		this.setLocal(local);
	}

	public DailyEventSummary(User user, DailyEvent dailyEvent, String date, float intensity) {
		this.setUser(user);
		this.setDailyEvent(dailyEvent);
		this.setDate(date);
		this.setIntensity(intensity);
	}

	public DailyEventSummary(User user, DailyEvent dailyEvent, String date) {
		this.setUser(user);
		this.setDailyEvent(dailyEvent);
		this.setDate(date);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DailyEvent getDailyEvent() {
		return dailyEvent;
	}

	public void setDailyEvent(DailyEvent dailyEvent) {
		this.dailyEvent = dailyEvent;
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
