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
import jdbcConnections.SingletonConnection;

public class ProblemDAO implements DAOInterface {
	
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

	public List<Employee> getProblemEmployees(int theProblemId) throws SQLException {

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

	public List<Employee> getFreeProblemEmployees(int theProblemId) throws SQLException {
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

	@Override
	public List<Problem> getList() throws Exception {
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
			    
			    String abbreviation = "";
			    
			    while (myRs2.next()) {
			    	abbreviation = myRs2.getString(ProjectDAO.getAbbreviation());
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
				
				
				Problem tempProblem = new Problem(id, title, workHour, startDate, endDate, status, projectId, abbreviation, employees);
				problems.add(tempProblem);
			}
			
			return problems;
		
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
	}	

	@Override
	public void add(Object obj) throws Exception {
		Problem theProblem = (Problem) obj;
		
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
			
			if(theProblem.getProjectId() != 0) {
				myStmt.setInt(7, theProblem.getProjectId());
			} else {
				myStmt.setString(7, null);
			}
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}	
	
	@Override
	public void update(Object obj) throws Exception {
		Problem theProblem = (Problem) obj;
		
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			
			String sql = "update " + TABLE_NAME + " " 
					+ "set " + TITLE + "=?, " + WORK_HOUR + "=?, " + START_DATE + "=?, " + END_DATE + "=?, " + STATUS + "=? "
					+ "where " + PROBLEM_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, theProblem.getTitle());
			myStmt.setString(2, theProblem.getWorkHour());
			myStmt.setString(3, theProblem.getStartDate());
			myStmt.setString(4, theProblem.getEndDate());
			myStmt.setString(5, theProblem.getStatus());
			myStmt.setInt(6, theProblem.getId());
			
			System.out.println(myStmt);
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}
	
	@Override
	public void delete(String id) throws Exception {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "delete from " + TABLE_NAME + " " 
					+ "where " + PROBLEM_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, id);
			
			myStmt.execute();
			
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}
	
	@Override
	public Object get(int id) throws Exception {
		Problem theProblem = null;
		
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			String sql = "select * from " + TABLE_NAME + " where " + PROBLEM_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
			
			myStmt.setInt(1, id);
			
			myRs = myStmt.executeQuery();
			
			if (myRs.next()) {
				String title = myRs.getString(TITLE);
				String workHour = myRs.getString(WORK_HOUR);
				String startDate = myRs.getString(START_DATE);
				String endDate = myRs.getString(END_DATE);
				String status = myRs.getString(STATUS);
				int projectId = myRs.getInt(PROJECT_ID);
				
				theProblem = new Problem(id, title, workHour, startDate, endDate, status, projectId, null, null);
			} else {
				throw new Exception("Couldn't find a problem with id: " + id);
			}
			
			return theProblem;
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
	}	
	
	@Override
	public int getIdByAbbr(String abbreviation) throws Exception {
		return 0;
	}
}
