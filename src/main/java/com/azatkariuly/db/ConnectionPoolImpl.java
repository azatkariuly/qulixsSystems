package com.azatkariuly.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPoolImpl implements IConnectionPool {
	
	private static ConnectionPoolImpl single_instance = null;
	
	private String url;
	private String user;
	private String password;
    
    private List<Connection> connectionPool = new ArrayList<>();
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 10;
    private static int POOL_SIZE_COUNTER = 0;

    private ConnectionPoolImpl(String url, String user, String password) {
    	this.url = url;
    	this.user = user;
    	this.password = password;
    }
    
    public static ConnectionPoolImpl getInstance(String url, String user, String password) {
    	if (single_instance == null) {
    		single_instance = new ConnectionPoolImpl(url, user, password);
    	}
    	
    	return single_instance;
    }
    
	
    @Override
    public synchronized Connection getConnection() throws SQLException {
    	if (connectionPool.isEmpty()) {
    		if (POOL_SIZE_COUNTER >= INITIAL_POOL_SIZE) {
        		throw new RuntimeException("Maximum pool size reached, no available connections!");	
    		}
    		
    		connectionPool.add(DriverManager.getConnection(url, user, password));
    		POOL_SIZE_COUNTER++;
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

	@Override
	public void shutDown() throws SQLException {
		usedConnections.forEach(this::releaseConnection);
		
		for (Connection c : connectionPool) {
			c.close();
		}
		connectionPool.clear();
		
		POOL_SIZE_COUNTER = 0;
	}
}