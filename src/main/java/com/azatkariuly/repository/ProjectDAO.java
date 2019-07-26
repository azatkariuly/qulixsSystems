package com.azatkariuly.repository;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.azatkariuly.domain.Project;
import com.azatkariuly.db.ConnectionPoolImpl;
import com.azatkariuly.db.IConnectionPool;

public class ProjectDAO implements IDAO<Project> {
	private IConnectionPool conPool;
	
	private static String TABLE_NAME = "projects";
	private static String PROJECT_ID = "projectId";
	private static String TITLE = "title";
	private static String ABBREVIATION = "abbreviation";
	private static String DESCRIPTION = "description";
	
	public ProjectDAO() throws Exception {
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
	}

	private void close(Statement myStmt, ResultSet myRs) {
		try {
			
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Project> getList() throws Exception {
		List<Project> projects = new ArrayList<Project>();
		
		Connection con = conPool.getConnection();
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			String sql = "select * from " + TABLE_NAME + " order by " + PROJECT_ID + " asc";
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
		
			while (myRs.next()) {
				
				int id = myRs.getInt(PROJECT_ID);
				String title = myRs.getString(TITLE);
				String abbreviation = myRs.getString(ABBREVIATION);
				String description = myRs.getString(DESCRIPTION);
				
				Project tempProject = new Project(id, title, abbreviation, description);
				
				projects.add(tempProject);
				
			}
			
			return projects;
		
		} finally {
			close(myStmt, myRs);
			conPool.releaseConnection(con);
		}
	}

	public String getMaxID() throws Exception {
		String maxID = null;
		
		Connection con = conPool.getConnection();
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {			
			String sql = "select max(" + PROJECT_ID + ") from " + TABLE_NAME;
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
			
		
			while(myRs.next()) {
				maxID = myRs.getString("max(" + PROJECT_ID + ")");
			}
			
			return maxID;
		
		} finally {
			close(myStmt, myRs);
			conPool.releaseConnection(con);
		}
	}

	@Override
	public int getIdByAbbr(String projectAbbreviation) throws Exception {
		int tempID = 0;
		
		Connection con = conPool.getConnection();
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			String sql = "select " + PROJECT_ID + " from " + TABLE_NAME + " where " + ABBREVIATION + "='" + projectAbbreviation + "'";
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
			
			while(myRs.next()) {
				tempID = myRs.getInt(PROJECT_ID);
			}
			
			return tempID;
		
		} finally {
			close(myStmt, myRs);
			conPool.releaseConnection(con);
		}
	}

	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public static String getProjectId() {
		return PROJECT_ID;
	}
	
	public static String getTitle() {
		return TITLE;
	}
	
	public static String getAbbreviation() {
		return ABBREVIATION;
	}

	@Override
	public void add(Project theProject) throws Exception {
		Connection con = conPool.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			
			String sql = "insert into " + TABLE_NAME + " "
					+ "(" + PROJECT_ID + ", " + TITLE + ", " + ABBREVIATION + ", " + DESCRIPTION+ ") "
					+ "values (?, ?, ?, ?)";
			
			myStmt = con.prepareStatement(sql);

			myStmt.setInt(1, theProject.getId());
			myStmt.setString(2, theProject.getTitle());
			myStmt.setString(3, theProject.getAbbreviation());
			myStmt.setString(4, theProject.getDescription());
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.releaseConnection(con);
		}
	}	

	@Override
	public void update(Project theProject) throws Exception {
		Connection con = conPool.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "update " + TABLE_NAME + " " 
					+ "set " + TITLE + "=?, " + ABBREVIATION + "=?, " + DESCRIPTION + "=? "
					+ "where " + PROJECT_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, theProject.getTitle());
			myStmt.setString(2, theProject.getAbbreviation());
			myStmt.setString(3, theProject.getDescription());
			myStmt.setInt(4, theProject.getId());
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.releaseConnection(con);
		}
	}
	
	@Override
	public void delete(String id) throws Exception {
		Connection con = conPool.getConnection();
		PreparedStatement myStmt = null;
		
		try {		
			
			String sql = "delete from " + TABLE_NAME + " " 
					+ "where " + PROJECT_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, id);
			
			myStmt.execute();
			
		} finally {
			close(myStmt, null);
			conPool.releaseConnection(con);
		}
	}
	
	@Override
	public Project get(int id) throws Exception {
		Project theProject = null;
		
		Connection con = conPool.getConnection();
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			String sql = "select * from " + TABLE_NAME + " where " + PROJECT_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
			myStmt.setInt(1, id);
			
			myRs = myStmt.executeQuery();
			
			if (myRs.next()) {
				String title = myRs.getString(TITLE);
				String abbreviation = myRs.getString(ABBREVIATION);
				String description = myRs.getString(DESCRIPTION);
				
				theProject = new Project(id, title, abbreviation, description);
			} else {
				throw new Exception("Couldn't find a project with id: " + id);
			}
			
			return theProject;
		} finally {
			close(myStmt, myRs);
			conPool.releaseConnection(con);
		}
	}
}
