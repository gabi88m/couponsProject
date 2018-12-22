package Facade;

import couponSystem.ConnectionPool;
import couponSystem.Task;
import ex.CouponSystemException;
import ex.InvalidLoginException;

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

	public AbsFacade login(String name, String password, LoginType type) throws InvalidLoginException {
		return AbsFacade.login(name, password, type);
	}

	protected void start() {
		dailyThread.start();
	}

	protected void shutdown() throws CouponSystemException {
		task.stop();
		ConnectionPool.getInstance().closeAllConnections();
	}

}
