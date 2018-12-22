package dao;

import java.util.Collection;

import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;
import javaBeans.Customer;

public interface CustomerDAO {

	public void createCustomer(Customer customer) throws CouponSystemException;

	public void removeCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException;

	public void updateCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException;

	public Customer getCustomer(long id) throws CouponSystemException, NoSuchObjectException;

	public Collection<Customer> getAllCustomers() throws CouponSystemException, NoSuchObjectException;

	public Collection<Coupon> getCustomerCoupons(Customer customer) throws CouponSystemException, NoSuchObjectException;

	public Customer login(String custName, String Password) throws CouponSystemException, InvalidLoginException;

//	public void addCouponToCustomer(Coupon coupon, Customer customer) throws ConnectionExeption, InvalidLoginException;
}
