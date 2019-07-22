package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.Employee;
import classes.Problem;
import classes.Project;
import daos.ProblemDAO;
import daos.ProjectDAO;
import projects.DbInitializer;
import projects.SingletonConnection;

public class ProblemControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProblemDAO problemDAO;
	private SingletonConnection conPool;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		Connection con = null;
		
		try {
			conPool = SingletonConnection.getInstance();
			
			con = conPool.myCon.getConnection();
			new DbInitializer(con).startInit();
			
			problemDAO = new ProblemDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			conPool.myCon.releaseConnection(con);
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
				listProblems(request, response);
				break;
			case "ADD":
				addProblem(request, response);
				break;
			case "LOAD":
				loadProblem(request, response);
				break;
			case "UPDATE":
				updateProblem(request, response);
				break;
			case "DELETE":
				deleteProblem(request, response);
				break;
			case "ADD-PROBLEM-FORM":
				addProblemForm(request, response);
				break;
			case "ADDEMPLOYEE":
				addProblemEmployee(request, response);
				break;
			case "REMOVEEMPLOYEE":
				removeProblemEmployee(request, response);
				break;
			default:
				listProblems(request, response);
//				break;
			}			
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	private void removeProblemEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String problemId = request.getParameter("problemId");
		String employeeId = request.getParameter("employeeId");

		problemDAO.removeProblemEmployee(problemId, employeeId);
		
		loadProblem(request, response);
	}

	private void addProblemEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String problemId = request.getParameter("problemId");
		String employeeId = request.getParameter("employeeId");

		problemDAO.addProblemEmployee(problemId, employeeId);
		
		loadProblem(request, response);
	}

	private void addProblemForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Project> projects = new ProjectDAO().getProjects();
		
		request.setAttribute("PROJECT_LIST", projects);
		
		String tempID = problemDAO.getMaxID();
		int maxID = 0;
		
		if (tempID != null) {
			maxID = Integer.parseInt(problemDAO.getMaxID());
		}
		
		maxID++;
		
		request.setAttribute("MAX_ID", maxID);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-problem-form.jsp");
		dispatcher.forward(request, response);
	}

	private void deleteProblem(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		String theProblemId = request.getParameter("problemId");
		
		problemDAO.deleteProblem(theProblemId);
		
		listProblems(request, response);
	}

	private void updateProblem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int id = Integer.parseInt(request.getParameter("problemId"));
		
		String title= request.getParameter("title");
		String workHour = request.getParameter("workHour");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String status = request.getParameter("status");
		
		
		System.out.println("TITLE: " + title);
		
		problemDAO.updateProblem(id, title, workHour, startDate, endDate, status);
		
		listProblems(request, response);
	}

	private void loadProblem(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		
		String theProblemId = request.getParameter("problemId");
		
		Problem theProblem = problemDAO.getProblem(theProblemId);
		
		String projectName = problemDAO.getProjectName(theProblemId);
		
		List<Employee> employees = problemDAO.getProblemEmployees(theProblemId);
		
		List<Employee> freeEmployees = problemDAO.getFreeProblemEmployees(theProblemId);
		
		request.setAttribute("THE_PROJECT_ID", projectName);
		request.setAttribute("THE_ID", theProblem.getId());
		request.setAttribute("THE_TITLE", theProblem.getTitle());
		request.setAttribute("THE_WORKHOUR", theProblem.getWorkHour());
		request.setAttribute("THE_STARTDATE", theProblem.getStartDate());
		request.setAttribute("THE_ENDDATE", theProblem.getEndDate());
		request.setAttribute("THE_STATUS", theProblem.getStatus());
		
		request.setAttribute("THE_PROBLEM_EMPLOYEES", employees);
		request.setAttribute("THE_FREE_PROBLEM_EMPLOYEES", freeEmployees);
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-problem-form.jsp");
		dispatcher.forward(request, response);
		
	}

	private void addProblem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String projectAbbreviation = request.getParameter("projectAbbreviation");
		
		String tempID = request.getParameter("maxID");
		
		int maxID = Integer.parseInt(tempID);
		String title = request.getParameter("title");
		String workHour = request.getParameter("workHour");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String status = request.getParameter("status");
		
		if (projectAbbreviation.equals("")) {
			Problem theProblem = new Problem(maxID, title, workHour, startDate, endDate, status);
			problemDAO.addProblem(theProblem);
		} else {
			Problem theProblem = new Problem(maxID, title, workHour, startDate, endDate, status, projectAbbreviation);
			problemDAO.addProblemWithAbbr(theProblem);
		}
		
		listProblems(request, response);
	}

	private void listProblems(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Problem> problems = problemDAO.getProblems();
		
		request.setAttribute("PROBLEM_LIST", problems);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/problems.jsp");
		dispatcher.forward(request, response);
	}

}
