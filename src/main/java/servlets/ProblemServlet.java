package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.Employee;
import classes.Problem;
import classes.Project;
import daos.DAOInterface;
import daos.ProblemDAO;
import daos.ProjectDAO;

/**
 * Problem List
 * 
 * @author AAzamat
 *
 */
public class ProblemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DAOInterface problemDAO;

	@Override
	public void init() throws ServletException {
		super.init();
				
		try {
			problemDAO = new ProblemDAO();
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

		new ProblemDAO().removeProblemEmployee(problemId, employeeId);
		
		loadProblem(request, response);
	}

	private void addProblemEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String problemId = request.getParameter("problemId");
		String employeeId = request.getParameter("employeeId");

		new ProblemDAO().addProblemEmployee(problemId, employeeId);
		
		loadProblem(request, response);
	}

	private void addProblemForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Project> projects = new ProjectDAO().getList();
		
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
		
		problemDAO.delete(theProblemId);
		
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
		
		Problem theProblem = new Problem(id, title, workHour, startDate, endDate, status);
		problemDAO.update(theProblem);
		
		listProblems(request, response);
	}

	private void loadProblem(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		int theProblemId = Integer.parseInt(request.getParameter("problemId"));
		
		Problem theProblem = (Problem) problemDAO.get(theProblemId);
		String projectName = null;
		
		if(theProblem.getProjectId() != 0) {
			DAOInterface projectDAO = new ProjectDAO();
			Project theProject = (Project) projectDAO.get(theProblem.getProjectId());
			
			projectName = theProject.getTitle();
		}
		
		List<Employee> employees = new ProblemDAO().getProblemEmployees(theProblemId);
		List<Employee> freeEmployees = new ProblemDAO().getFreeProblemEmployees(theProblemId);
		
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
		
		int projectId = 0;
		
		if (projectAbbreviation.equals("")) {
			projectAbbreviation = null;
		} else {
			DAOInterface projectDAO = new ProjectDAO();
			projectId = projectDAO.getIdByAbbr(projectAbbreviation);
		}

		
		Problem theProblem = new Problem(maxID, title, workHour, startDate, endDate, status, projectId, projectAbbreviation, null);
		problemDAO.add(theProblem);
		
		listProblems(request, response);
	}

	private void listProblems(HttpServletRequest request, HttpServletResponse response) throws Exception {
		@SuppressWarnings("unchecked")
		List<Problem> problems = (List<Problem>) problemDAO.getList();
		
		request.setAttribute("PROBLEM_LIST", problems);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/problems.jsp");
		dispatcher.forward(request, response);
		
	}

}
