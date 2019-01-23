package dao;

import java.util.Collection;

import ex.CouponAlreadyPurchasedException;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;
import javaBeans.Customer;

public interface CustomerDAO {

	void createTable() throws CouponSystemException;

	void createCustomer(Customer customer) throws CouponSystemException;

	void removeCustomer(long customerId) throws CouponSystemException, NoSuchObjectException;

	void updateCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException;

	Customer getCustomer(long id) throws CouponSystemException, NoSuchObjectException;

	Collection<Customer> getAllCustomers() throws CouponSystemException;

	Collection<Coupon> getCustomerCoupons(Customer customer) throws CouponSystemException, NoSuchObjectException;

	void addCouponToCustomer(long couponId, Customer customer)
			throws CouponSystemException, CouponAlreadyPurchasedException, NoSuchObjectException;

	Customer login(String custName, String Password) throws CouponSystemException, InvalidLoginException;

}
