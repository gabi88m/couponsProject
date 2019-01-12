package facade;

import java.util.Collection;

import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import ex.ObjectAlreadyExistsException;
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

	public static CouponClientFacade login(String name, String password) throws InvalidLoginException {
		if ("admin".equals(name) && "1234".equals(password)) {// admin can be only this user
			return new AdminFacade();
		}
		String msg = "Can not login as admin the provided credentials!";
		throw new InvalidLoginException(msg);
	}// login

	public void createCompany(Company company)
			throws CouponSystemException, NoSuchObjectException, ObjectAlreadyExistsException {
		Collection<Company> allCompanies = companyDBDAO.getAllCompanies();
		for (Company c : allCompanies) {
			if (c.getCompName().equals(company.getCompName())) {
				String msg = String.format("Company with name \"%s\" already exists!", c.getCompName());
				throw new CouponSystemException(msg);
			}
		}

		companyDBDAO.createCompany(company);
	}// createCompany

	public void removeCompany(Company company) throws CouponSystemException, NoSuchObjectException {
		Collection<Coupon> coupons = companyDBDAO.getCompanyCoupons(company);

		for (Coupon coupon : coupons) {
			try {
				couponDBDAO.removeCoupon(coupon);
			} catch (CouponSystemException | NoSuchObjectException e) {
				// We ignore such case.
			}
		}
		companyDBDAO.removeCompany(company);
	}// removeCompany

	public void updateCompany(Company company) throws CouponSystemException, NoSuchObjectException {
		Company c = companyDBDAO.getCompany(company.getId());

		if (!c.getCompName().equals(company.getCompName())) {
			throw new CouponSystemException("Changing company's name is not allowed!");
		}
		companyDBDAO.updateCompany(company);
	}// updateCompany

	public Company getCompany(long id) throws CouponSystemException, NoSuchObjectException {
		return companyDBDAO.getCompany(id);
	}// getCompany

	public Collection<Company> getAllCompanies() throws CouponSystemException, NoSuchObjectException {
		return companyDBDAO.getAllCompanies();
	}// getAllCompanies

	public void createCustomer(Customer customer)
			throws CouponSystemException, NoSuchObjectException, ObjectAlreadyExistsException {
		Collection<Customer> allCustomers = customerDBDAO.getAllCustomers();

		for (Customer c : allCustomers) {
			if (c.getCustName().equals(customer.getCustName())) {
				String msg = String.format("Customer with name %s already exists.", c.getCustName());
				throw new ObjectAlreadyExistsException(msg);
			}
		}
		customerDBDAO.createCustomer(customer);
	}// createCustomer

	public void removeCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException {
		customerDBDAO.removeCustomer(customer);
	}// removeCustomer

	public void updateCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException {
		Customer c = customerDBDAO.getCustomer(customer.getId());
		if (!c.getCustName().equals(customer.getCustName())) {
			throw new CouponSystemException("Changing customer's name is not allowed!");
		}
		customerDBDAO.updateCustomer(customer);
	}// updateCustomer

	public Customer getCustomer(long id) throws CouponSystemException, NoSuchObjectException {
		return customerDBDAO.getCustomer(id);
	}// getCustomer

	public Collection<Customer> getAllCustomers() throws CouponSystemException {
		return customerDBDAO.getAllCustomers();
	}// getAllCustomers

}
