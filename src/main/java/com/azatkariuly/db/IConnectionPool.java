package com.azatkariuly.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
	public Connection getConnection() throws SQLException;
	public void releaseConnection(Connection con);
	public void shutDown() throws SQLException;
	public String getUrl();
	public String getUser();
	public String getPassword();
}
