package utilities;

import java.sql.SQLException;
import java.util.Date;

import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CouponSystem;
import facade.CustomerFacade;
import facade.LoginType;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.Customer;

public class Test {

	public static void main(String[] args)
			throws CouponSystemException, NoSuchObjectException, SQLException, InvalidLoginException {

		/*****************************************************************************************************************
		 * for help
		 */
		CouponDBDAO couponDAO = new CouponDBDAO();
		CustomerDBDAO customerDAO = new CustomerDBDAO();
		CompanyDBDAO companyDAO = new CompanyDBDAO();

		/*****************************************************************************************************************
		 * Coupons
		 */
		Coupon coupon1 = new Coupon(111, "title1", new Date(12 / 12 / 2012), new Date(12 / 12 / 2013), 5, "SPORTS",
				"hello", 22, "image");
		Coupon coupon2 = new Coupon(222, "title2", new Date(12 / 12 / 2012), new Date(12 / 12 / 2013), 5, "RESTURANS",
				"hello", 22, "image");

		Coupon coupon3 = new Coupon(333, "title3", new Date(12 / 12 / 2012), new Date(12 / 12 / 2013), 5, "HEALTH",
				"hello", 22, "image");

		/*****************************************************************************************************************
		 * Customers
		 */
		Customer customer1 = new Customer(11, "customer1", "123456");
		Customer customer2 = new Customer(22, "customer2", "123456");
		Customer customer3 = new Customer(33, "customer3", "123456");

		/*****************************************************************************************************************
		 * Companies
		 */
		Company company1 = new Company(1, "compname1", "123456", "comp1@gmail.com");
		Company company2 = new Company(2, "compname2", "123456", "comp2@gmail.com");
		Company company3 = new Company(3, "compname3", "123456", "comp3@gmail.com");

		/*****************************************************************************************************************
		 * Facades
		 */
		AdminFacade adminFacade = (AdminFacade) CouponSystem.getInstance().login("admin", "1234", LoginType.ADMIN);
		CompanyFacade companyFacade = (CompanyFacade) CouponSystem.getInstance().login("compname1", "123456",
				LoginType.COMPANY);
		CustomerFacade customerFacade = (CustomerFacade) CouponSystem.getInstance().login("customer1", "123456",
				LoginType.CUSTOMER);

		/*****************************************************************************************************************
		 * All Tests
		 */
//		adminFacade.createCompany(company1);
//		adminFacade.createCustomer(customer1);
//		System.out.println(adminFacade.getAllCompanies());
//		System.out.println(adminFacade.getAllCustomers());
//		System.out.println(adminFacade.getCompany(company2.getId()));
//		System.out.println(adminFacade.getCustomer(customer2.getId()));
//		adminFacade.removeCompany(company1);
//		adminFacade.removeCustomer(customer1);
//		company1.setCompName("comp1");
//		customer1.setCustName("cust1");
//		adminFacade.updateCompany(company1);
//		adminFacade.updateCustomer(customer1);

//		companyFacade.createCoupon(coupon1, company1);
//		companyFacade.removeCoupon(coupon1);
//		System.out.println(companyFacade.getAllCompanyCoupons(company1));
//		System.out.println(companyFacade.getCoupon(coupon1.getId()));
//		System.out.println(companyFacade.getCouponByType(CouponType.SPORTS));
//		coupon1.setTitle("title1");
//		companyFacade.updateCoupon(coupon1);

//		customerFacade.purchaseCoupon(coupon1);
//		System.out.println(customerFacade.getAllPurchasedCoupons(customer2));
//		System.out.println(customerFacade.getAllPurchasedCouponsByPrice(customer2, 22));
//		System.out.println(customerFacade.getAllPurchasedCouponsByType(customer2, CouponType.SPORTS));

	}
}