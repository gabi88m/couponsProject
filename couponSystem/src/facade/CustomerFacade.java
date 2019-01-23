package facade;

import java.util.Collection;
import java.util.Iterator;

import dao.CouponDBDAO;
import dao.CustomerDBDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import ex.ObjectAlreadyExistsException;
import javaBeans.Coupon;
import javaBeans.Customer;

public class CustomerFacade implements CouponClientFacade {
	private Customer customer;
	private CustomerDBDAO customerDBDAO;
	private CouponDBDAO couponDBDAO;

	public CustomerFacade(Customer customer, CustomerDBDAO customerDBDAO, CouponDBDAO couponDBDAO) {
		this.customer = customer;
		this.customerDBDAO = customerDBDAO;
		this.couponDBDAO = couponDBDAO;
	}// ctor

	public static CouponClientFacade login(String name, String password)
			throws CouponSystemException, InvalidLoginException {
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		Customer customer = customerDBDAO.login(name, password);

		CouponDBDAO couponDBDAO = new CouponDBDAO();
		return new CustomerFacade(customer, customerDBDAO, couponDBDAO);
	}

	public void purchaseCoupon(long cpouponId)//
			throws CouponSystemException, NoSuchObjectException, ObjectAlreadyExistsException {

		Coupon coupon = couponDBDAO.getCouponById(cpouponId);

		if (coupon == null || coupon.getAmount() == 0) {
			String msg = "No coupons left of this kind.";
			throw new ObjectAlreadyExistsException(msg);
		}

		Collection<Coupon> allCoupons = getAllPurchasedCoupons();
//		if (allCoupons.isEmpty()) {
//			// buyCoupon
//			customerDBDAO.addCouponToCustomer(cpouponId, customer);
//			// Decrement the coupon amount and update the DB
//			couponDBDAO.getCouponById(cpouponId).setAmount(couponDBDAO.getCouponById(cpouponId).getAmount() - 1);
//			couponDBDAO.updateCoupon(couponDBDAO.getCouponById(cpouponId));
//		}//no need to check this
		for (Coupon currcoupon : allCoupons) {
			if (currcoupon.getId() == cpouponId) {
				String msg = "error! , you already have this coupon!";
				throw new ObjectAlreadyExistsException(msg);
			}
		}

		// buyCoupon
		customerDBDAO.addCouponToCustomer(cpouponId, customer);
		// Decrement the coupon amount and update the DB
		coupon.setAmount(coupon.getAmount() - 1);
		couponDBDAO.updateCoupon(coupon);// TODO:fix decrement coupon amount

	}// purchaseCoupon

	public Collection<Coupon> getAllPurchasedCoupons() throws CouponSystemException {
		return customerDBDAO.getCustomerCoupons(customer);
	}// getAllPurchasedCoupons

	public Collection<Coupon> getAllPurchasedCouponsByType(Coupon.CouponType type) throws CouponSystemException {
		Collection<Coupon> myCoupons = getAllPurchasedCoupons();
		for (Iterator<Coupon> iterator = myCoupons.iterator(); iterator.hasNext();) {
			Coupon coupon = iterator.next();
			if (coupon.getCouponType() != type) {
				iterator.remove();
			}
		}
		return myCoupons;
	}// getAllPurchasedCouponsByType

	public Collection<Coupon> getAllPurchasedCouponsByPrice(long price) throws CouponSystemException {
		Collection<Coupon> myCoupons = getAllPurchasedCoupons();
		for (Iterator<Coupon> iterator = myCoupons.iterator(); iterator.hasNext();) {
			Coupon coupon = iterator.next();
			if (coupon.getPrice() != price) {
				iterator.remove();
			}
		}
		return myCoupons;
	}// getAllPurchasedCouponsByPrice

}
