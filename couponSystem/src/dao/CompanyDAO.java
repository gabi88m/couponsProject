package dao;

import java.util.Collection;

import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;

public interface CompanyDAO {

	public void createCompany(Company company) throws CouponSystemException;

	public void removeCompany(Company company) throws CouponSystemException, NoSuchObjectException;

	public void updateCompany(Company company) throws CouponSystemException, NoSuchObjectException;

	public Company getCompany(long id) throws CouponSystemException, NoSuchObjectException;

	public Collection<Company> getAllCompamies() throws CouponSystemException, NoSuchObjectException;

	public Collection<Coupon> getCompanyCoupons(Company company) throws CouponSystemException, NoSuchObjectException;

	public Company login(String compName, String password) throws CouponSystemException, InvalidLoginException;
}
