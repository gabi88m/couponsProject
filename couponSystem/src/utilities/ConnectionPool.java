package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

import ex.CouponSystemException;

public class ConnectionPool {
	private static ConnectionPool INSTSNCE;
	private static final int MAX_CONNECTIONS = 10;
	private LinkedBlockingQueue<Connection> connections;

	static {
		try {
			Class.forName(MySQLMetaData.DRIVER_PATH);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private ConnectionPool() throws CouponSystemException {
		connections = new LinkedBlockingQueue<>(MAX_CONNECTIONS);
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			try {
				connections.offer(createConnection());
			} catch (SQLException e) {
				String msg = "Unable to get a connection!" + e.getMessage();
				throw new CouponSystemException(msg);
			}
		}

	}// ctor

	public static Connection createConnection() throws SQLException {
		return DriverManager.getConnection(MySQLMetaData.DATABASE_URL, "root", "123456");
	}// createConnection

	public synchronized Connection getConnection() throws CouponSystemException {
		try {
//			System.out.println("connection taken");
			return connections.take();
		} catch (InterruptedException e) {
			throw new CouponSystemException("Unable to offer a connection!");

		}
	}// getConnection

	public synchronized void closeAllConnections() throws CouponSystemException {
		Connection connection;
		while ((connection = connections.poll()) != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new CouponSystemException("database access error occured while trying to close a connection.");
			}
		}
	}// closeAllConnections

	public synchronized static ConnectionPool getInstance() throws CouponSystemException {
		if (INSTSNCE == null) {
			INSTSNCE = new ConnectionPool();
		}
		return INSTSNCE;
	}// getInstances

	public synchronized void returnConnection(Connection connection) throws CouponSystemException {
		try {
//			System.out.println("connection returned");
			connections.put(connection);
		} catch (InterruptedException e) {
			String msg = "There is not enough space to return the connection!";
			System.out.println(msg);
			throw new CouponSystemException(msg);
		}
	}// returnConnection

}
