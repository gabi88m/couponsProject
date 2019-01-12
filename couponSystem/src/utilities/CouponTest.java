package utilities;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import ex.ObjectAlreadyExistsException;
import facade.AdminFacade;
import facade.CompanyFacade;
import facade.CouponSystem;
import facade.CustomerFacade;
import facade.LoginType;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.Customer;

public class CouponTest {

	public static void main(String[] args) throws CouponSystemException, NoSuchObjectException, SQLException,
			InvalidLoginException, ObjectAlreadyExistsException {

		/*****************************************************************************************************************
		 * for help
		 */
		CouponDBDAO couponDAO = new CouponDBDAO();
		CustomerDBDAO customerDAO = new CustomerDBDAO();
		CompanyDBDAO companyDAO = new CompanyDBDAO();

		/*****************************************************************************************************************
		 * Coupons
		 */
		Coupon coupon1 = new Coupon(111, "title1", LocalDate.now(), LocalDate.of(2016, Month.DECEMBER, 25), 5, "SPORTS",
				"hello", 22, "image");
		Coupon coupon2 = new Coupon(222, "title2", LocalDate.now(), LocalDate.of(2022, Month.DECEMBER, 25), 5,
				"RESTURANS", "hello", 22, "image");

		Coupon coupon3 = new Coupon(333, "title3", LocalDate.now(), LocalDate.of(2016, Month.DECEMBER, 25), 5, "HEALTH",
				"hello", 22, "image");

		/*****************************************************************************************************************
		 * Customers
		 */
		Customer customer1 = new Customer(11, "customer1", "123456");
		Customer customer2 = new Customer(22, "customer2", "123456");
		Customer customer3 = new Customer(33, "customer3", "123456");
		Customer customer4 = new Customer(44, "customer4", "123456");

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
		 * All Tests - all works!!
		 */

//		adminFacade.createCompany(company3);
//		adminFacade.createCustomer(customer3);
//		System.out.println(adminFacade.getAllCompanies());
//		System.out.println(adminFacade.getAllCustomers());
//		System.out.println(adminFacade.getCompany(company2.getId()));
//		System.out.println(adminFacade.getCustomer(customer2.getId()));
//		adminFacade.removeCompany(company3);
//		adminFacade.removeCustomer(customer3);
//		company1.setEmail("comp1@gmail.com");
//		customer1.setPassword("123456");
//		adminFacade.updateCompany(company1);
//		adminFacade.updateCustomer(customer1);

//		companyFacade.createCoupon(coupon1);
//		companyFacade.removeCoupon(coupon1);
//		System.out.println(companyFacade.getAllCompanyCoupons(company1));
//		System.out.println(companyFacade.getCoupon(coupon1.getId()));
//		System.out.println(companyFacade.getCouponByType(CouponType.SPORTS));
//		coupon1.setTitle("title1");
//		companyFacade.updateCoupon(coupon1);

//		customerFacade.purchaseCoupon(coupon1.getId());
//		System.out.println(customerFacade.getAllPurchasedCoupons(customer1));
//		System.out.println(customerFacade.getAllPurchasedCouponsByPrice(customer1, 22));
//		System.out.println(customerFacade.getAllPurchasedCouponsByType(customer1, CouponType.SPORTS));

	}
}