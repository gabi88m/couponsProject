package couponSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingDeque;

import ex.CouponSystemException;

public class ConnectionPool {
	private static ConnectionPool INSTSNCE;
	private static final int MAX_CONNECTIONS = 10;
	private LinkedBlockingDeque<Connection> connections;

	static {
		try {
			Class.forName(MySQLMetaData.DRIVER_PATH);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ConnectionPool() throws CouponSystemException {
		connections = new LinkedBlockingDeque<Connection>();
		try {
			for (int i = 0; i < 9; i++) {
				connections.offer(createConnection());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get a connection!");

		}
	}// ctor

	public static ConnectionPool getInstance() throws CouponSystemException {
		if (INSTSNCE == null) {
			INSTSNCE = new ConnectionPool();
		}
		return INSTSNCE;
	}// getInstances

	public static Connection createConnection() throws SQLException {
		return DriverManager.getConnection(MySQLMetaData.DATABASE_URL, "root", "123456");
	}// createConnection

	public synchronized Connection getConnection() throws CouponSystemException {
		try {
			return connections.take();
		} catch (InterruptedException e) {
			throw new CouponSystemException("Unable to offer a connection!");

		}
	}// getConnection

	public void returnConnection(Connection connection) throws CouponSystemException {
		try {
			connections.put(connection);
		} catch (InterruptedException e) {
			throw new CouponSystemException("There is not enough space to return the connection!");
		}
	}// returnConnection

	public void closeAllConnections() throws CouponSystemException {
		Connection connection;
		while ((connection = connections.poll()) != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponSystemException("database access error occured while trying to close a connection.");
			}
		}
	}// closeAllConnections

}
