package jdbcConnections;

import java.io.InputStream;
import java.util.Properties;

public class SingletonConnection {
	
	private String className = "com.mysql.cj.jdbc.Driver";
	
	private static SingletonConnection single_instance = null;
	
	public ConnectionPool myCon;
	
	private SingletonConnection() throws Exception {
		Class.forName(className);
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("config.properties");
		Properties prop = new Properties();
		prop.load(input);
		
		myCon = MyConnectionPool.create(prop.getProperty("db.url") + "/" + prop.getProperty("db.name"), prop.getProperty("db.user"), prop.getProperty("db.password"));
	
	}
	
	public static SingletonConnection getInstance() throws Exception {
		if(single_instance == null) {
			single_instance = new SingletonConnection();
		}
		
		return single_instance;
	}
	
}
