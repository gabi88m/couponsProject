package facade;

import java.util.Collection;
import java.util.Iterator;

import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.Customer;

public class AdminFacade implements CouponClientFacade {
	CompanyDBDAO companyDBDAO;
	CustomerDBDAO customerDBDAO;
	CouponDBDAO couponDBDAO;

	public AdminFacade() {
		companyDBDAO = new CompanyDBDAO();
		customerDBDAO = new CustomerDBDAO();
		couponDBDAO = new CouponDBDAO();
	}// ctor

	public void createCompany(Company company) throws CouponSystemException, NoSuchObjectException {
		Collection<Company> myCompanies = companyDBDAO.getAllCompamies();
		boolean haveCompany = false;
		for (Iterator<Company> iterator = myCompanies.iterator(); iterator.hasNext();) {
			Company cuurcompany = iterator.next();
			if (cuurcompany.getCompName().equals(company.getCompName())) {
				haveCompany = true;
			}
		}
		if (haveCompany) {
			System.out.println("there is company with the same name!!! cant create!!!!");

		} else {
			companyDBDAO.createCompany(company);
		}
	}// createCompany

	public void removeCompany(Company company) throws CouponSystemException, NoSuchObjectException {
		Collection<Coupon> myCompanyCoupons = companyDBDAO.getCompanyCoupons(company);
		if (myCompanyCoupons.isEmpty())
			for (Coupon currCoupon : myCompanyCoupons) {// will remove all coupons from Join Tables id exist
				couponDBDAO.removeCompmanyCoupon(currCoupon);
				couponDBDAO.removeCoupon(currCoupon);
			}
		companyDBDAO.removeCompany(company); // remove the company
	}// removeCompany

	public void updateCompany(Company company) throws CouponSystemException, NoSuchObjectException {
		companyDBDAO.updateCompany(company);
		// TODO: check what params to update
	}// updateCompany

	public Company getCompany(long id) throws CouponSystemException, NoSuchObjectException {
		return companyDBDAO.getCompany(id);
	}// getCompany

	public Collection<Company> getAllCompanies() throws CouponSystemException, NoSuchObjectException {
		return companyDBDAO.getAllCompamies();
	}// getAllCompanies

	public void createCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException {
		Collection<Customer> myCustomers = customerDBDAO.getAllCustomers();
		boolean haveCompany = false;
		for (Iterator<Customer> iterator = myCustomers.iterator(); iterator.hasNext();) {
			Customer currCustomer = iterator.next();
			if (currCustomer.getCustName().equals(customer.getCustName())) {
				haveCompany = true;
			}
		}
		if (haveCompany) {
			System.out.println("there is company with the same name!!! cant create!!!!");

		} else {
			customerDBDAO.createCustomer(customer);
		}
	}// createCustomer

	public void removeCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException {
		Collection<Long> customerCoupontable = couponDBDAO.getFromCustomerCoupons();
		if (customerCoupontable.isEmpty()) {// if customer have not coupons yet
			customerDBDAO.removeCustomer(customer);
		} else {
			couponDBDAO.removeCustomerCoupon(customer);
			customerDBDAO.removeCustomer(customer);
		}

	}// removeCustomer

	public void updateCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException {
		customerDBDAO.updateCustomer(customer);
		// TODO: check what params to update
	}// updateCustomer

	public Customer getCustomer(long id) throws CouponSystemException, NoSuchObjectException {
		return customerDBDAO.getCustomer(id);
	}// getCustomer

	public Collection<Customer> getAllCustomers() throws CouponSystemException, NoSuchObjectException {
		return customerDBDAO.getAllCustomers();
	}// getAllCustomers

	public static CouponClientFacade login(String name, String password) throws InvalidLoginException {
		if ("admin".equals(name) && "1234".equals(password)) {// admin can be only this user
			return new AdminFacade();
		}
		throw new InvalidLoginException("Can not login as admin the provided credentials!");
	}// login

}
