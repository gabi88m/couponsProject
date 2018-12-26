package facade;

import ex.CouponSystemException;
import ex.InvalidLoginException;
import utilities.ConnectionPool;
import utilities.Task;

public class CouponSystem {

	private static CouponSystem instance;
	private Thread dailyThread;
	private Task task;

	private CouponSystem() {
		task = Task.create();
	}// ctor

	public static CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}// getInstance

	public CouponClientFacade login(String name, String password, LoginType type)
			throws InvalidLoginException, CouponSystemException {
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
