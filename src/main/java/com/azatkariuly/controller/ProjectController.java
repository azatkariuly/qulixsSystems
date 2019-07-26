package com.azatkariuly.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.azatkariuly.domain.Problem;
import com.azatkariuly.domain.Project;
import com.azatkariuly.repository.IDAO;
import com.azatkariuly.repository.ProblemDAO;
import com.azatkariuly.repository.ProjectDAO;
import com.azatkariuly.db.ConnectionPoolImpl;
import com.azatkariuly.db.DbInitializer;
import com.azatkariuly.db.IConnectionPool;

/**
 * Project List
 * 
 * @author AAzamat
 *
 */
public class ProjectController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private IDAO<Project> projectDAO;
	private IConnectionPool conPool;

	@Override
	public void init() throws ServletException {
		super.init();			
		
		Connection con = null;
		
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(input);
			
			Class.forName(prop.getProperty("className"));
			
			String url = null;
			
			if(System.getProperty("dbUrl") == null) {
				url = prop.getProperty("db.url");
			} else {
				url = System.getProperty("dbUrl");
			}
			
			conPool = ConnectionPoolImpl.getInstance(url + "/" + prop.getProperty("db.name"),
					prop.getProperty("db.user"), prop.getProperty("db.password"));
			
			con = conPool.getConnection();
			new DbInitializer(con).startInit();
			
			projectDAO = new ProjectDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			conPool.releaseConnection(con);
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
				listProjects(request, response);
				break;
			case "ADD":
				addProject(request, response);
				break;
			case "LOAD":
				loadProject(request, response);
				break;
			case "UPDATE":
				updateProject(request, response);
				break;
			case "DELETE":
				deleteProject(request, response);
				break;
			case "ADDPROBLEM":
				addProblem(request, response);
				break;
			case "ADD-PROJECT-FORM":
				addProjectForm(request, response);
				break;
			case "REMOVEPROBLEM":
				removeProblem(request, response);
				break;
			default:
				listProjects(request, response);
//				break;
			}			
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void removeProblem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		new ProblemDAO().setProjectId(request.getParameter("problemId"), null);

		loadProject(request, response);
	}


	private void addProblem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		new ProblemDAO().setProjectId(request.getParameter("problemId"), request.getParameter("projectId"));		

		loadProject(request, response);
	}


	private void loadProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String theProjectId = request.getParameter("projectId");
		Project theProject = projectDAO.get(Integer.parseInt(theProjectId));
		
		request.setAttribute("THE_ID", theProject.getId());
		request.setAttribute("THE_TITLE", theProject.getTitle());
		request.setAttribute("THE_ABBREVIATION", theProject.getAbbreviation());
		request.setAttribute("THE_DESCRIPTION", theProject.getDescription());
		
		List<Problem> projectProblems = new ProblemDAO().getProjectProblems(theProject.getId());
		List<Problem> freeProblems = new ProblemDAO().getFreeProblems();
		
		request.setAttribute("PROJECT_PROBLEMS", projectProblems);
		request.setAttribute("FREE_PROBLEMS", freeProblems);
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-project-form.jsp");
		dispatcher.forward(request, response);
		
	}


	
	private void addProjectForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tempID = projectDAO.getMaxID();
		int maxID = 0;
		
		if (tempID != null) {
			maxID = Integer.parseInt(projectDAO.getMaxID());
		}
		
		maxID++;
		
		request.setAttribute("MAX_ID", maxID);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/add-project-form.jsp");
		dispatcher.forward(request, response);
	}


	private void deleteProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String theProjectId = request.getParameter("projectId");
		
		projectDAO.delete(theProjectId);
		listProjects(request, response);
		
	}


	private void updateProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("projectId"));
		String title = request.getParameter("title");
		String abbreviation = request.getParameter("abbreviation");
		String description = request.getParameter("description");
		
		Project theProject = new Project(id, title, abbreviation, description);
		
		projectDAO.update(theProject);
		
		listProjects(request, response);
	}


	private void addProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tempID = request.getParameter("maxID");
		
		int maxID = Integer.parseInt(tempID);
		
		String title = request.getParameter("title");
		String abbreviation = request.getParameter("abbreviation");
		String description = request.getParameter("description");
		
		Project theProject = new Project(maxID, title, abbreviation, description);		
		
		
		try {
			projectDAO.add(theProject);
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println("DUPLICATED");
		}
		
		listProjects(request, response);
	}


	private void listProjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Project> projects = (List<Project>) projectDAO.getList();
		
		request.setAttribute("PROJECT_LIST", projects);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/projects.jsp");
		
		dispatcher.forward(request, response);
	}


}
