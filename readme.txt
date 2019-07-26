1. Run "create-user.sql" script file on your MySQL Software. (MySQLScript/create-user.sql)
2. Run command "mvn clean install tomcat7:run -DdbUrl=(your db URL)"
	Example: 
		mvn clean install tomcat7:run -DdbUrl=jdbc:mysql://localhost:3306
3. Go to your server