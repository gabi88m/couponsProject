package dao;

import java.util.Collection;

import ex.CouponSystemException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;

public interface CouponDAO {

	void createTable() throws CouponSystemException;

	void createCoupon(Coupon coupon) throws CouponSystemException;

	void removeCoupon(long couponId) throws CouponSystemException, NoSuchObjectException;

	void updateCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException;

	Coupon getCouponById(long id) throws CouponSystemException, NoSuchObjectException;

	Collection<Coupon> getAllCoupons() throws CouponSystemException;

	Collection<Coupon> getCouponByType(Coupon.CouponType type) throws CouponSystemException, NoSuchObjectException;

}
