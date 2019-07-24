package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import classes.Employee;
import jdbcConnections.SingletonConnection;

public class EmployeeDAO implements DAOInterface {
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
	public List<Employee> getList() throws Exception {
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
	
	@Override
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

	@Override
	public void add(Object obj) throws Exception {
		Employee theEmployee = (Employee) obj;
		
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

	@Override
	public void update(Object obj) throws Exception {
		Employee theEmployee = (Employee) obj;
		
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		
		try {
			String sql = "update " + TABLE_NAME
					+ " set " + LAST_NAME + "=?, " + FIRST_NAME + "=?, " + PATRONYMIC + "=?, " + POSITION + "=? where " + EMPLOYEE_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, theEmployee.getLastName());
			myStmt.setString(2, theEmployee.getFirstName());
			myStmt.setString(3, theEmployee.getPatronymic());
			myStmt.setString(4, theEmployee.getPosition());
			myStmt.setInt(5, theEmployee.getId());
			
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
					+ "where " + EMPLOYEE_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
	
			myStmt.setString(1, id);
			
			myStmt.execute();
			
		} finally {
			close(myStmt, null);
			conPool.myCon.releaseConnection(con);
		}
		
	}

	@Override
	public Object get(int id) throws Exception{
		Employee theEmployee = null;
		
		Connection con = conPool.myCon.getConnection();
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			String sql = "select * from " + TABLE_NAME + " where " + EMPLOYEE_ID + "=?";
			
			myStmt = con.prepareStatement(sql);
			
			myStmt.setInt(1, id);
			
			myRs = myStmt.executeQuery();
			
			if (myRs.next()) {
				String lastName = myRs.getString(LAST_NAME);
				String firstName = myRs.getString(FIRST_NAME);
				String patronymic = myRs.getString(PATRONYMIC);
				String position = myRs.getString(POSITION);
				
				theEmployee = new Employee(id, lastName, firstName, patronymic, position);
			} else {
				throw new Exception("Couldn't find an employee with id: " + id);
			}
			
			
		} finally {
			close(myStmt, myRs);
			conPool.myCon.releaseConnection(con);
		}
		
		return theEmployee;
	}

	@Override
	public int getIdByAbbr(String abbreviation) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
