package jdbcConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyConnectionPool implements ConnectionPool {
	private String url;
	private String user;
	private String password;
    
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;

    private MyConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
    	this.url = url;
    	this.user = user;
    	this.password = password;
    	this.connectionPool = connectionPool;
    }
	
    public static MyConnectionPool create(String url, String user, String password) throws SQLException {
    	List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
    	
    	for(int i=0; i<INITIAL_POOL_SIZE; i++) {
    		pool.add(DriverManager.getConnection(url, user, password));
    	}
    	
    	return new MyConnectionPool(url, user, password, pool);
    }
    
    @Override
    public synchronized Connection getConnection() {
    	if (connectionPool.isEmpty()) {
    		throw new RuntimeException("Maximum pool size reached, no available connections!");
    	}
    	
    	Connection con = connectionPool.remove(connectionPool.size() - 1);
    	usedConnections.add(con);
    	
    	return con;
    }
    
    @Override
    public void releaseConnection(Connection con) {
    	connectionPool.add(con);
    	usedConnections.remove(con);
    }

	
	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return password;
	}
}