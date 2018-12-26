package utilities;

import java.util.Collection;
import java.util.Date;

import dao.CouponDAO;
import dbdao.CouponDBDAO;
import ex.CouponSystemException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;

public class Task implements Runnable {

	private CouponDBDAO dao;
	private boolean isAlive;

	public Task(CouponDAO dao) {
		this.dao = (CouponDBDAO) dao;
	}// ctor

	public static Task create() {
		return new Task(new CouponDBDAO());
	}// create

	@Override
	public void run() {
		isAlive = true;
		try {
			while (isAlive) {
				Collection<Coupon> allCoupons = dao.getAllCoupons();
				for (Coupon coupon : allCoupons) {
					if (coupon.getEndDate().before(new Date())) {
						dao.removeCoupon(coupon);
					}
				}
				Thread.sleep(1000 * 60 * 60 * 24); // sleep every 24 hours
			}
		} catch (CouponSystemException | NoSuchObjectException | InterruptedException e) {
			// ignore.
		}

	}// run

	public void stop() {
		isAlive = false;
	}// stop

}
