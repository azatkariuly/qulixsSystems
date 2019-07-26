package com.azatkariuly.domain;

public class Employee {

	private int id;
	private String lastName;
	private String firstName;
	private String patronymic;
	private String position;
	
	public Employee(String lastName, String firstName, String patronymic, String position) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronymic = patronymic;
		this.position = position;
	}
	
	public Employee(int id, String lastName, String firstName, String patronymic, String position) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronymic = patronymic;
		this.position = position;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getId() {
		return id;
	}
	
	
}
