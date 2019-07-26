package com.azatkariuly.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.azatkariuly.domain.Employee;
import com.azatkariuly.repository.IDAO;
import com.azatkariuly.repository.EmployeeDAO;

/**
 * Employee List
 * 
 * @author AAzamat
 *
 */
public class EmployeeController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IDAO<Employee> employeeDAO;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {	
			employeeDAO = new EmployeeDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			String theCommand = request.getParameter("command");
			
			if (theCommand == null) {
				theCommand = "LIST";
			}
			
			switch (theCommand) {
			case "LIST":
				listEmployees(request, response);
				break;
			case "ADD":
				addEmployee(request, response);
				break;
			case "LOAD":
				loadEmployee(request, response);
				break;
			case "UPDATE":
				updateEmployee(request, response);
				break;
			case "DELETE":
				deleteEmployee(request, response);
				break;
			case "ADD-EMPLOYEE-FORM":
				addEmployeeForm(request, response);
				break;
			default:
				listEmployees(request, response);
//				break;
			}			
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void addEmployeeForm(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
		String tempID = employeeDAO.getMaxID();
		int maxID = 0;
		
		if (tempID != null) {
			maxID = Integer.parseInt(employeeDAO.getMaxID());
		}
		
		maxID++;
		
		request.setAttribute("MAX_ID", maxID);	

		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-employee-form.jsp");
		dispatcher.forward(request, response);
	}

	private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theEmployeeId = request.getParameter("employeeId");
		
//		employeeDAO.deleteEmployee(theEmployeeId);
		
		employeeDAO.delete(theEmployeeId);
		
		listEmployees(request, response);
		
	}

	private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("employeeId"));
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String patronymic = request.getParameter("patronymic");
		String position = request.getParameter("position");
		
		Employee theEmployee = new Employee(id, lastName, firstName, patronymic, position);
		
		employeeDAO.update(theEmployee);
		
		listEmployees(request, response);
	}

	private void loadEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theEmployeeId = request.getParameter("employeeId");
		Employee theEmployee = employeeDAO.get(Integer.parseInt(theEmployeeId));
		
		request.setAttribute("THE_ID", theEmployee.getId());
		request.setAttribute("THE_LASTNAME", theEmployee.getLastName());
		request.setAttribute("THE_FIRSTNAME", theEmployee.getFirstName());
		request.setAttribute("THE_PATRONYMIC", theEmployee.getPatronymic());
		request.setAttribute("THE_POSITION", theEmployee.getPosition());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-employee-form.jsp");
		dispatcher.forward(request, response);
		
	}

	private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tempID = request.getParameter("maxID");
		
		int id = Integer.parseInt(tempID);
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		String patronymic = request.getParameter("patronymic");
		String position = request.getParameter("position");
		
		Employee theEmployee = new Employee(id, lastName, firstName, patronymic, position);
		
		employeeDAO.add(theEmployee);
		
		listEmployees(request, response);
	}

	private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Employee> employees = (List<Employee>) employeeDAO.getList();
		
		request.setAttribute("EMPLOYEE_LIST", employees);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/employees.jsp");
		dispatcher.forward(request, response);
	}

}
