package com.fibrobook.model;

public class DailyEvent {
	
	private int id;
	private String title;
	private int isActive;

	public DailyEvent(String title, int id, int isActive) {
		this.title = title;
		this.id = id;
		this.isActive = isActive;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int isActive() {
		return isActive;
	}

	public void setActive(int isActive) {
		this.isActive = isActive;
	}
	
	public String toString(){
		return getTitle();
	}

}
