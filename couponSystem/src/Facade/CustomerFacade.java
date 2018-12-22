package Facade;

import java.util.Collection;
import java.util.Iterator;

import dbdao.CustomerDBDAO;
import ex.CouponSystemException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;
import javaBeans.Customer;

public class CustomerFacade implements CouponClientFacade {
	private CustomerDBDAO customerDAO = new CustomerDBDAO();

	public CustomerFacade() {
	}// ctor

	public void purchaseCoupon(Coupon coupon, Customer customer) throws CouponSystemException, NoSuchObjectException {
		Collection<Coupon> AllCustomerCoupons = customerDAO.getCustomerCoupons(customer);
		boolean haveCouponWithSameArgs = false;

		for (Coupon currCoupon : AllCustomerCoupons) {
			if (currCoupon.equals(coupon)) {
				haveCouponWithSameArgs = true;
				System.out.println("allrady have this coupon!");
			} else if (currCoupon.getAmount() < 1) {
				haveCouponWithSameArgs = true;
				System.out.println("amount have expired!");
			} // TODO:add date check(not expired???)...
				// buyCoupon
		}
		if (!haveCouponWithSameArgs)
			customerDAO.addCouponToCustomer(coupon, customer);
	}// purchaseCoupon

	public Collection<Coupon> getAllPurchasedCoupons(Customer customer) throws CouponSystemException {
		return customerDAO.getCustomerCoupons(customer);
	}// getAllPurchasedCoupons

	public Collection<Coupon> getAllPurchasedCouponsByType(Customer customer, Coupon.CouponType type)
			throws CouponSystemException {
		Collection<Coupon> myCoupons = getAllPurchasedCoupons(customer);
		for (Iterator<Coupon> iterator = myCoupons.iterator(); iterator.hasNext();) {
			Coupon coupon = iterator.next();
			if (coupon.getCouponType() != type) {
				iterator.remove();
			}
		}
		return myCoupons;
	}// getAllPurchasedCouponsByType

	public Collection<Coupon> getAllPurchasedCouponsByPrice(Customer customer, long price)
			throws CouponSystemException {
		Collection<Coupon> myCoupons = getAllPurchasedCoupons(customer);
		for (Iterator<Coupon> iterator = myCoupons.iterator(); iterator.hasNext();) {
			Coupon coupon = iterator.next();
			if (coupon.getPrice() != price) {
				iterator.remove();
			}
		}
		return myCoupons;
	}// getAllPurchasedCouponsByPrice

	@Override
	public CouponClientFacade login(String name, String password, String clientType) {
		// TODO Auto-generated method stub
		return null;
	}

}
