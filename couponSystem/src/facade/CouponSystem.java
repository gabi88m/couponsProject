package facade;

import java.sql.SQLException;

import ex.CouponSystemException;
import ex.InvalidLoginException;
import utilities.ConnectionPool;
import utilities.Task;

public class CouponSystem {

	private static CouponSystem INSTANCE;
	private Thread dailyThread;
	private Task task;

	private CouponSystem() {
		dailyThread = new Thread(Task.create());
//		dailyThread.setDaemon(true);
	}// ctor

	public static CouponSystem getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CouponSystem();
		}
		return INSTANCE;
	}// getInstance

	public CouponClientFacade login(String name, String password, LoginType type)
			throws InvalidLoginException, CouponSystemException, SQLException {
		return CouponClientFacade.login(name, password, type);
	}// login

	protected void start() {
		dailyThread.start();
	}// start

	protected void shutdown() throws CouponSystemException {
		task.stop();
		ConnectionPool.getInstance().closeAllConnections();
	}// shutdown

}
