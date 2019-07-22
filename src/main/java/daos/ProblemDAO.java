package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import classes.Employee;
import classes.Problem;
import projects.SingletonConnection;

public class ProblemDAO {
	
	private SingletonConnection conPool;
	
	private static String TABLE_NAME = "problems";
	private static String PROBLEM_ID = "problemId";
	private static String TITLE = "title";
	private static String WORK_HOUR = "workHour";
	private static String START_DATE = "startDate";
	private static String END_DATE = "endDate";
	private static String STATUS = "status";
	private static String PROJECT_ID = "projectId";
	
	private static String EP_TABLE_NAME = "employees_problems";
	private static String EP_EMPLOYEE_ID = "employeeId";
	private static String EP_PROBLEM_ID = "problemId";
	
	public ProblemDAO() throws Exception {
		conPool = SingletonConnection.getInstance();
	}
	
	public List<Problem> getProblems() throws Exception {
		List<Problem> problems = new ArrayList<Problem>();
		
		Connection con = conPool.myCon.getConnection();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		Statement myStmt2 = null;
		ResultSet myRs2 = null;
		
		try {
			String sql = "select * from " + TABLE_NAME + " order by " + PROBLEM_ID + " asc";
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
		
			while (myRs.next()) {
				
				int id = myRs.getInt(PROBLEM_ID);
				String title = myRs.getString(TITLE);
				String workHour = myRs.getString(WORK_HOUR);
				String startDate = myRs.getString(START_DATE);
				String endDate = myRs.getString(END_DATE);
				String status = myRs.getString(STATUS);
				
				int projectId = myRs.getInt(PROJECT_ID);
				
				
				//get project abbreviation for problem
				String sql2 = "select " + ProjectDAO.getAbbreviation() + " from " + ProjectDAO.getTableName()
						+ " where  " + ProjectDAO.getProjectId() + "=" + projectId;
				
				myStmt2 = con.createStatement();
				myRs2 = myStmt2.executeQuery(sql2);
				
				String projectAbbr = "";
				
				while (myRs2.next()) {
					projectAbbr = myRs2.getString(ProjectDAO.getAbbreviation());
				}

				close(myStmt2, myRs2);
				
				//get problem executors
				sql2 = "select " + EmployeeDAO.getTableName() + "." + EmployeeDAO.getFirstName() + ", "
						+ EmployeeDAO.getTableName() + "." + EmployeeDAO.getLastName() + ", "
						+ EmployeeDAO.getTableName() + "." + EmployeeDAO.getPatronymic() + " "
						+ "from " + EmployeeDAO.getTableName() + ", " + TABLE_NAME + ", " + EP_TABLE_NAME + " "
						+ "where " + EmployeeDAO.getTableName() + "." + EmployeeDAO.getEmployeeId() + " = "
						+ EP_TABLE_NAME + "." + EP_EMPLOYEE_ID + " and " + TABLE_NAME + "." + PROBLEM_ID + " = "
						+ EP_TABLE_NAME + "." + EP_PROBLEM_ID + " and " + TABLE_NAME + "." + TITLE + "='" + title + "'";
				
				myStmt2 = con.createStatement();
				myRs2 = myStmt2.executeQuery(sql2);
				
				List<Employee> employees = new ArrayList<Employee>();
				
				while (myRs2.next()) {
					String firstName = myRs2.getString(EmployeeDAO.getFirstName());
					String lastName = myRs2.getString(EmployeeDAO.getLastName());
					String patronymic = myRs2.getString(EmployeeDAO.getPatronymic());
					
					employees.add(new Employee(lastName, firstName, patronymic, null));		
				}
				
				
				Problem tempProblem = new Problem(id, title, workHour, startDate, endDate, status, projectAbbr, employees);
				problems.add(tempProblem);
			}
			
			return problems;
		
		} finally {
			close(myStmt, myRs);
			close(myStmt2, myRs2);
			conPool.myCon.releaseConnection(con);
		}
		
		
	}
	
	public List<Problem> getFreeProblems() throws Exception {
		
		List<Problem> problems = new ArrayList<Problem>();
		
		Connection con = conPool.myCon.getConnection();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			String sql = "select * from " + TABLE_NAME + " where " + PROJECT_ID + " is null order by " + PROBLEM_ID + " asc";
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
		
			while (myRs.next()) {
				
				int id = myRs.getInt(PROBLEM_ID);
				String title = myRs.getString(TITLE);
				String workHour = myRs.getString(WORK_HOUR);
				String startDate = myRs.getString(START_DATE);
				String endDate = myRs.getString(END_DATE);
				String status = myRs.getString(STATUS);
				
				Problem tempProblem = new Problem(id, title, workHour, startDate, endDate, status);
				
				problems.add(tempProblem);
				
			}
			
			return problems;
		
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
		
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

	public void addProblem(Problem theProblem) throws Exception {
		Connection con = conPool.myCon.getConnection();
		
		PreparedStatement myStmt = null;
		
		try {	
			String sql = "insert into " + TABLE_NAME + " "
					+ "(" + PROBLEM_ID +", " + TITLE + ", " + WORK_HOUR + ", " + START_DATE + ", " + END_DATE + ", " + STATUS + ") "
					+ "values (?, ?, ?, ?, ?, ?)";
			
			myStmt = con.prepareStatement(sql);

			myStmt.setInt(1, theProblem.getId());
			myStmt.setString(2, theProblem.getTitle());
			myStmt.setString(3, theProblem.getWorkHour());
			myStmt.setString(4, theProblem.getStartDate());
			myStmt.setString(5, theProblem.getEndDate());
			myStmt.setString(6, theProblem.getStatus());
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}
	
	public void addProblemWithAbbr(Problem theProblem) throws Exception {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {	
			String sql = "insert into " + TABLE_NAME + " "
					+ "(" + PROBLEM_ID +", " + TITLE + ", " + WORK_HOUR + ", " + START_DATE + ", " + END_DATE + ", " + STATUS + ", " + PROJECT_ID + ") "
					+ "values (?, ?, ?, ?, ?, ?, ?)";
			
			myStmt = con.prepareStatement(sql);

			myStmt.setInt(1, theProblem.getId());
			myStmt.setString(2, theProblem.getTitle());
			myStmt.setString(3, theProblem.getWorkHour());
			myStmt.setString(4, theProblem.getStartDate());
			myStmt.setString(5, theProblem.getEndDate());
			myStmt.setString(6, theProblem.getStatus());
			
			String tempID;
			
			tempID = new ProjectDAO().getIdByAbbr(theProblem.getProjectAbbreviation());
			myStmt.setString(7, tempID);
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}

	public Problem getProblem(String theProblemId) throws Exception {

		Problem theProblem = null;
		
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int problemId;
		
		try {
			
			problemId = Integer.parseInt(theProblemId);
			
			String sql = "select * from " + TABLE_NAME + " where " + PROBLEM_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
			
			myStmt.setInt(1, problemId);
			
			myRs = myStmt.executeQuery();
			
			if (myRs.next()) {
				String title = myRs.getString(TITLE);
				String workHour = myRs.getString(WORK_HOUR);
				String startDate = myRs.getString(START_DATE);
				String endDate = myRs.getString(END_DATE);
				String status = myRs.getString(STATUS);
				
				theProblem = new Problem(problemId, title, workHour, startDate, endDate, status);
			} else {
				throw new Exception("Couldn't find a problem with id: " + problemId);
			}
			
			return theProblem;
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
		
		
	}

	public void updateProblem(int id, String title, String workHour, String startDate, String endDate, String status) throws SQLException {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			
			String sql = "update " + TABLE_NAME + " " 
					+ "set " + TITLE + "=?, " + WORK_HOUR + "=?, " + START_DATE + "=?, " + END_DATE + "=?, " + STATUS + "=? "
					+ "where " + PROBLEM_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, title);
			myStmt.setString(2, workHour);
			myStmt.setString(3, startDate);
			myStmt.setString(4, endDate);
			myStmt.setString(5, status);
			myStmt.setInt(6, id);
			
			System.out.println(myStmt);
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}

	public void deleteProblem(String theProblemId) throws Exception {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "delete from " + TABLE_NAME + " " 
					+ "where " + PROBLEM_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, theProblemId);
			
			myStmt.execute();
			
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}

	public List<Problem> getProjectProblems(int projectID) throws SQLException {
		List<Problem> problems = new ArrayList<Problem>();
		
		Connection con = conPool.myCon.getConnection();
		
		Statement myStmt = null;
		ResultSet myRs = null;
		
		Statement myStmt2 = null;
		ResultSet myRs2 = null;
		
		try {
			
			String sql = "select * from " + TABLE_NAME + " where " + PROJECT_ID + "=" + projectID + " order by " + PROBLEM_ID + " asc";
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
		
			while (myRs.next()) {
				
				int id = myRs.getInt(PROBLEM_ID);
				String title = myRs.getString(TITLE);
				String workHour = myRs.getString(WORK_HOUR);
				String startDate = myRs.getString(START_DATE);
				String endDate = myRs.getString(END_DATE);
				String status = myRs.getString(STATUS);			
				
				Problem tempProblem = new Problem(id, title, workHour, startDate, endDate, status);
				
				problems.add(tempProblem);
				
			}
			
			return problems;
		
		} finally {
			close(myStmt, myRs);
			close(myStmt2, myRs2);
			conPool.myCon.releaseConnection(con);
		}
	
	}

	public void setProjectId(String theProblemId, String theProjectId) throws SQLException {
		
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;

		try {
			
			String sql = "update " + TABLE_NAME + " " 
					+ "set " + PROJECT_ID + "=" + theProjectId
					+ " where " + PROBLEM_ID + "="+ theProblemId;
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.execute();

		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
		
	}

	public String getProjectName(String theProblemId) throws Exception {
		
		String projectName;
		String projectId = null;
		
		Connection con = conPool.myCon.getConnection();
		
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		PreparedStatement myStmt2 = null;
		ResultSet myRs2 = null;
		try {
			//get projectId
			String sql2 = "select * from " + TABLE_NAME + " where " + PROBLEM_ID + "=?";
			
			myStmt2 = con.prepareStatement(sql2);
			
			myStmt2.setString(1, theProblemId);
			
			myRs2 = myStmt2.executeQuery();
			
			if (myRs2.next()) {		
				projectId = myRs2.getString(PROJECT_ID);			
			} 
			
			//get projectTitle
			String sql = "select * from " + ProjectDAO.getTableName() + " where " + ProjectDAO.getProjectId() +"=?";
			
			myStmt = con.prepareStatement(sql);
			
			myStmt.setString(1, projectId);
			
			myRs = myStmt.executeQuery();
			
			if (myRs.next()) {		
				projectName = myRs.getString(ProjectDAO.getTitle());
				projectName += " (" + myRs.getString(ProjectDAO.getAbbreviation()) + ")";
			} else {
				return null; 
			}
			
			return projectName;
		} finally {
			close(myStmt, myRs);
			close(myStmt2, myRs2);
			conPool.myCon.releaseConnection(con);
		}
	}

	public List<Employee> getProblemEmployees(String theProblemId) throws SQLException {

		List<Employee> employees = new ArrayList<Employee>();
		
		Connection con = conPool.myCon.getConnection();
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			String sql = "select " + EmployeeDAO.getTableName() + ".*  from " + EmployeeDAO.getTableName() + ", "
					+ TABLE_NAME + ", " + EP_TABLE_NAME + " "
					+ "where " + EmployeeDAO.getTableName() + "." + EmployeeDAO.getEmployeeId() + " = " + EP_TABLE_NAME + "." + EP_EMPLOYEE_ID + " "
					+ "and " + TABLE_NAME + "." + PROBLEM_ID + " = " + EP_TABLE_NAME + "." + EP_PROBLEM_ID + " and "
					+ TABLE_NAME + "." + PROBLEM_ID + "='" + theProblemId + "'";
			
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
		
			while (myRs.next()) {
				int id = myRs.getInt(EmployeeDAO.getEmployeeId());
				String lastName = myRs.getString(EmployeeDAO.getLastName());
				String firstName = myRs.getString(EmployeeDAO.getFirstName());
				String patronymic = myRs.getString(EmployeeDAO.getPatronymic());
				String position = myRs.getString(EmployeeDAO.getPosition());
				
				Employee tempEmployee = new Employee(id, lastName, firstName, patronymic, position);
				
				employees.add(tempEmployee);
				
			}
			
			return employees;
		
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
		
	}

	public List<Employee> getFreeProblemEmployees(String theProblemId) throws SQLException {
		List<Employee> employees = new ArrayList<Employee>();
		
		Connection con = conPool.myCon.getConnection();
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			String sql = "select * from " + EmployeeDAO.getTableName() + " where " + EmployeeDAO.getEmployeeId() + " not in "
					+ "(select " + EP_EMPLOYEE_ID + " from " + EP_TABLE_NAME + " WHERE " + EP_PROBLEM_ID + "='"
					+ theProblemId + "')";
			
			myStmt = con.createStatement();
			myRs = myStmt.executeQuery(sql);
		
			while (myRs.next()) {
				int id = myRs.getInt(EmployeeDAO.getEmployeeId());
				String lastName = myRs.getString(EmployeeDAO.getLastName());
				String firstName = myRs.getString(EmployeeDAO.getFirstName());
				String patronymic = myRs.getString(EmployeeDAO.getPatronymic());
				String position = myRs.getString(EmployeeDAO.getPosition());
				
				Employee tempEmployee = new Employee(id, lastName, firstName, patronymic, position);
				
				employees.add(tempEmployee);
			}
			
			return employees;
		
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
	}

	public void addProblemEmployee(String problemId, String employeeId) throws SQLException {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "insert into " + EP_TABLE_NAME + " (" + EP_EMPLOYEE_ID + ", " + EP_PROBLEM_ID +") values (?, ?)";
			
			myStmt = con.prepareStatement(sql);

			myStmt.setString(1, employeeId);
			myStmt.setString(2, problemId);
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}

	public void removeProblemEmployee(String problemId, String employeeId) throws SQLException {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "delete from " + EP_TABLE_NAME + " " 
					+ "where " + EP_EMPLOYEE_ID + "=? and " + EP_PROBLEM_ID + "=?";
			
			myStmt = con.prepareStatement(sql);

			myStmt.setString(1, employeeId);
			myStmt.setString(2, problemId);
			
			myStmt.execute();
			
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
		
	}
	
	public String getMaxID() throws Exception {
		String maxID = null;
		
		Connection con = conPool.myCon.getConnection();
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {			
			String sql = "select max(" + PROBLEM_ID + ") from " + TABLE_NAME;
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
			
		
			while(myRs.next()) {
				maxID = myRs.getString("max(" + PROBLEM_ID + ")");
			}
			
			return maxID;
		
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
	}
}
