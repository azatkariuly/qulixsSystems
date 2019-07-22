package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import classes.Employee;
import projects.SingletonConnection;

public class EmployeeDAO {
	private SingletonConnection conPool;
	
	private static String TABLE_NAME = "employees";
	private static String EMPLOYEE_ID = "employeeId";
	private static String LAST_NAME = "lastName";
	private static String FIRST_NAME = "firstName";
	private static String PATRONYMIC = "patronymic";
	private static String POSITION = "position";

	public EmployeeDAO() throws Exception {
		conPool = SingletonConnection.getInstance();
	}
	
	public List<Employee> getEmployees() throws Exception {
		List<Employee> employees = new ArrayList<>();
		
		Connection con = conPool.myCon.getConnection();
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			
			String sql = "select * from " + TABLE_NAME + " order by " + EMPLOYEE_ID + " asc";
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
		
			while (myRs.next()) {
				
				int id = myRs.getInt(EMPLOYEE_ID);
				String lastName = myRs.getString(LAST_NAME);
				String firstName = myRs.getString(FIRST_NAME);
				String patronymic = myRs.getString(PATRONYMIC);
				String position = myRs.getString(POSITION);
				
				Employee tempEmployee = new Employee(id, lastName, firstName, patronymic, position);
				
				employees.add(tempEmployee);
				
			}
			
			return employees;
		
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

	public void addEmployee(Employee theEmployee) throws Exception {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "insert into " + TABLE_NAME
					+ " (" + EMPLOYEE_ID + ", " + LAST_NAME + ", " + FIRST_NAME + ", " + PATRONYMIC + ", " + POSITION + ") values (?, ?, ?, ?, ?)";
			
			myStmt = con.prepareStatement(sql);

			myStmt.setInt(1, theEmployee.getId());
			myStmt.setString(2, theEmployee.getLastName());
			myStmt.setString(3, theEmployee.getFirstName());
			myStmt.setString(4, theEmployee.getPatronymic());
			myStmt.setString(5, theEmployee.getPosition());
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}

	public Employee getEmployee(String theEmployeeId) throws Exception {

		Employee theEmployee = null;
		
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int employeeId;
		
		try {
			employeeId = Integer.parseInt(theEmployeeId);
			
			String sql = "select * from " + TABLE_NAME + " where " + EMPLOYEE_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
			
			myStmt.setInt(1, employeeId);
			
			myRs = myStmt.executeQuery();
			
			if (myRs.next()) {
				String lastName = myRs.getString(LAST_NAME);
				String firstName = myRs.getString(FIRST_NAME);
				String patronymic = myRs.getString(PATRONYMIC);
				String position = myRs.getString(POSITION);
				
				theEmployee = new Employee(employeeId, lastName, firstName, patronymic, position);
			} else {
				throw new Exception("Couldn't find an employee with id: " + employeeId);
			}
			
			return theEmployee;
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
		
		
	}

	public void updateEmployee(int id, String lastName, String firstName, String patronymic, String position) throws SQLException {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "update " + TABLE_NAME
					+ " set " + LAST_NAME + "=?, " + FIRST_NAME + "=?, " + PATRONYMIC + "=?, " + POSITION + "=? where " + EMPLOYEE_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, lastName);
			myStmt.setString(2, firstName);
			myStmt.setString(3, patronymic);
			myStmt.setString(4, position);
			myStmt.setInt(5, id);
			
			myStmt.execute();
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
	}

	public void deleteEmployee(String theEmployeeId) throws Exception {
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "delete from " + TABLE_NAME + " " 
					+ "where " + EMPLOYEE_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, theEmployeeId);
			
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
			String sql = "select max(" + EMPLOYEE_ID + ") from " + TABLE_NAME;
			myStmt = con.createStatement();
			
			myRs = myStmt.executeQuery(sql);
			
		
			while(myRs.next()) {
				maxID = myRs.getString("max(" + EMPLOYEE_ID + ")");
			}
			
			return maxID;
		
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
	}
	
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public static String getEmployeeId() {
		return EMPLOYEE_ID;
	}
	
	public static String getLastName() {
		return LAST_NAME;
	}
	
	public static String getFirstName() {
		return FIRST_NAME;
	}
	
	public static String getPatronymic() {
		return PATRONYMIC;
	}
	
	public static String getPosition() {
		return POSITION;
	}
}
