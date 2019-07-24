package jdbcConnections;

import java.sql.Connection;

public interface ConnectionPool {
	Connection getConnection();
	void releaseConnection(Connection con);
	String getUrl();
	String getUser();
	String getPassword();
}
