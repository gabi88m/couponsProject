package dao;

import java.util.Collection;

import ex.CouponSystemException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;

public interface CouponDAO {

	public void createCoupon(Coupon coupon) throws CouponSystemException;

	public void removeCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException;

	public void updateCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException;

	public Coupon getCoupon(long id) throws CouponSystemException, NoSuchObjectException;

	public Collection<Coupon> getAllCoupons() throws CouponSystemException;

	public Collection<Coupon> getCouponByType(Coupon.CouponType type)
			throws CouponSystemException, NoSuchObjectException;
}
