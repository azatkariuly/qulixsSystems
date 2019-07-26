package com.azatkariuly.domain;

import java.util.List;

public class Problem {
	
	private int id;
	private String title;
	private String workHour;
	private String startDate;
	private String endDate;
	private String status;
	private int projectId;
	
	private String abbreviation;
	private List<Employee> employees;
	
	
	
	public Problem(String title, String workHour, String startDate, String endDate, String status) {
		this.title = title;
		this.workHour = workHour;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}
	
	public Problem(int id, String title, String workHour, String startDate, String endDate, String status) {
		this.id = id;
		this.title = title;
		this.workHour = workHour;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}
	
	public Problem(int id, String title, String workHour, String startDate, String endDate, String status, int projectId) {
		this.id = id;
		this.title = title;
		this.workHour = workHour;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.projectId = projectId;
	}
	
	public Problem(int id, String title, String workHour, String startDate, String endDate, String status, int projectId, String abbreviation, 
			List<Employee> employees) {
		this.id = id;
		this.title = title;
		this.workHour = workHour;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.projectId = projectId;
		this.setAbbreviation(abbreviation);
		this.employees = employees;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWorkHour() {
		return workHour;
	}

	public void setWorkHour(String workHour) {
		this.workHour = workHour;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}
	
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

}
