package projects;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInitializer {

	Statement stmt = null;
	
	public DbInitializer(Connection con) throws SQLException {
		stmt = con.createStatement();
	}
	
	public void startInit() throws SQLException {
		createTable(stmt);
//		insertData(stmt);
	}
	
	public void createTable(Statement stmt) throws SQLException {
		String tableProjects = "CREATE TABLE IF NOT EXISTS projects"
				+ "(projectId int(11) NOT NULL AUTO_INCREMENT, "
				+ "title varchar(45) DEFAULT NULL, "
				+ "abbreviation varchar(45) DEFAULT NULL, "
				+ "description varchar(45) DEFAULT NULL, "
				+ "UNIQUE (abbreviation), "
				+ "PRIMARY KEY (projectId)) " 
				+ "ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1";
		stmt.execute(tableProjects);
		
		String tableProblems = "CREATE TABLE IF NOT EXISTS problems"
				+ "(problemId int(11) NOT NULL AUTO_INCREMENT, "
				+ "title varchar(45) DEFAULT NULL, "
				+ "workHour varchar(45) DEFAULT NULL, "
				+ "startDate varchar(45) DEFAULT NULL, "
				+ "endDate varchar(45) DEFAULT NULL, "
				+ "status varchar(45) DEFAULT NULL, "
				+ "projectId int(11) DEFAULT NULL, "
				+ "PRIMARY KEY (problemId), "
				+ "FOREIGN KEY (projectId) REFERENCES projects(projectId) ON DELETE SET NULL) "
				+ "ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1";
		stmt.execute(tableProblems);
		
		String tableEmployees = "CREATE TABLE IF NOT EXISTS employees"
				+ "(employeeId int(11) NOT NULL AUTO_INCREMENT, " 
				+ "lastName varchar(45) DEFAULT NULL, "
				+ "firstName varchar(45) DEFAULT NULL, "
				+ "patronymic varchar(45) DEFAULT NULL, "
				+ "position varchar(45) DEFAULT NULL, "
				+ "PRIMARY KEY (employeeId)) "
				+ "ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1";
		stmt.execute(tableEmployees);
		
		String tableEmployeesProblems = "CREATE TABLE IF NOT EXISTS employees_problems"
				+ "(employeeId INTEGER NOT NULL, "
				+ "problemId INTEGER NOT NULL, "
				+ "FOREIGN KEY (employeeId) REFERENCES employees(employeeId) ON DELETE CASCADE, "
				+ "FOREIGN KEY (problemId) REFERENCES problems(problemId) ON DELETE CASCADE, "
				+ "PRIMARY KEY (employeeId, problemId)) "
				+ "ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1";
		stmt.execute(tableEmployeesProblems);
		
	}
	
	
	public void insertData(Statement stmt) throws SQLException {
		//Insert initial values
		String insertProjects = "INSERT INTO projects(projectId, title, abbreviation, description)"
				+ " VALUES (1, 'Qulix Systems', 'QS', 'Create web application'), "
				+ "(2, 'Netflix Original Series', 'Netflix', 'Publish new films'), "
				+ "(3, 'Fast Project', 'ASAP', 'As soon as possible')";
		stmt.executeUpdate(insertProjects);
		
		String insertProblems = "INSERT INTO problems(problemId, title, workHour, startDate, endDate, status)"
				+ " VALUES (1, 'Mobile App','9','2019-07-01','2019-07-31','in progress'), "
				+ "(2, 'Web App','19','2019-07-09','2019-09-31','completed'), "
				+ "(3, 'JavaEE','3','2018-05-01','2019-12-31','not started')";
		stmt.executeUpdate(insertProblems);
		
		String insertEmployees = "INSERT INTO employees(employeeId, lastName, firstName, patronymic, position)"
				+ " VALUES (1, 'Varushkin','Kirill','Mikhailovich','Web Developer'), "
				+ "(2, 'Azamat','Azat','Kariuly','Intern'), "
				+ "(3, 'Yermakova','Yuliana','Zakharovna','Mobile Developer')";
		stmt.execute(insertEmployees);
		
		String insertEmployeesProblems = "INSERT INTO employees_problems(employeeId, problemId)"
				+ " VALUES (1, 1), "
				+ "(1, 3), "
				+ "(2, 3)";
		stmt.execute(insertEmployeesProblems);
	}
}
