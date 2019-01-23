package dao;

import java.util.Collection;

import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;

public interface CompanyDAO {

	void createTable() throws CouponSystemException;

	public void createCompany(Company company) throws CouponSystemException;

	public void removeCompany(long companyId) throws CouponSystemException, NoSuchObjectException;

	public void updateCompany(Company company) throws CouponSystemException, NoSuchObjectException;

	public Company getCompany(long id) throws CouponSystemException, NoSuchObjectException;

	public Collection<Company> getAllCompanies() throws CouponSystemException;

	public Collection<Coupon> getCompanyCoupons(long companyId) throws CouponSystemException, NoSuchObjectException;

	public Company login(String compName, String password) throws CouponSystemException, InvalidLoginException;

}
