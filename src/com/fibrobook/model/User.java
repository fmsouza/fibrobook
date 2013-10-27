package com.fibrobook.model;

public class User {
	
	private String name;
	private int age;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
