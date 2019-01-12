package utilities;

import java.time.LocalDate;
import java.util.Collection;

import dbdao.CouponDBDAO;
import ex.CouponSystemException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;

public class Task implements Runnable {

	private CouponDBDAO dao;
	private boolean isAlive;

	public Task(CouponDBDAO dao) {
		this.dao = dao;

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
					if (coupon.getEndDate().isBefore(LocalDate.now())) {
						System.out.println("from Task/run()");
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
