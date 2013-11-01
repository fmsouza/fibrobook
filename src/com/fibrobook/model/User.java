package com.fibrobook.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User {
	
	private String name;
	private String birthday;
	private String password;
	private int id;

	public User(String name) {
		this.name = name;
		this.id = 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public int getAge(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		String[] bday = birthday.split("-");
		String[] today = f.format(new Date()).split("-");

		int years = Integer.parseInt(today[0]) - Integer.parseInt(bday[0]);
		int months = Integer.parseInt(today[1]) - Integer.parseInt(bday[1]);
		int days = Integer.parseInt(today[2]) - Integer.parseInt(bday[2]);
		
		int age = (years*365) + (months*30) + days; // In days 
		
		return (int) age/365;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
