package facade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;

public class CompanyFacade implements CouponClientFacade {
	CouponDBDAO couponDBDAO = new CouponDBDAO();
	CompanyDBDAO companyDBDAO = new CompanyDBDAO();

	public CompanyFacade() {

	}// ctor

	public void createCoupon(Coupon coupon, Company company) throws CouponSystemException, SQLException {
		Collection<Coupon> allCoupons = couponDBDAO.getAllCoupons();
		boolean haveCouponWithSameTitle = false;

		if (allCoupons.isEmpty()) {// check if thare is any coupons , if not ignore the title check
			couponDBDAO.createCoupon(coupon);
			companyDBDAO.addCouponToCompany(coupon, company);
		} else {
			for (Coupon coupons : allCoupons) {

				if (coupons.getTitle().equals(coupon.getTitle())) {
					System.out.println(coupons.getTitle());
					System.out.println(coupon.getTitle());
					haveCouponWithSameTitle = true;
					System.out.println("error! , you have coupon with same title!");
				}
			}
			if (!(haveCouponWithSameTitle)) {
				couponDBDAO.createCoupon(coupon);
				companyDBDAO.addCouponToCompany(coupon, company);
			}

		}
	}// createCoupon

	public void removeCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException {
		couponDBDAO.removeCompmanyCoupon(coupon); // remove from join tabels
//		couponDAO.removeCustomerCoupon(coupon); // no need 
		couponDBDAO.removeCoupon(coupon); // remove the coupon
	}// removeCoupon

	public void updateCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException {
		couponDBDAO.updateCoupon(coupon);
	}// removeCoupon

	public Coupon getCoupon(long id) throws CouponSystemException, NoSuchObjectException {
		return couponDBDAO.getCoupon(id);
	}// getCoupon

	public Collection<Coupon> getAllCompanyCoupons(Company company)
			throws CouponSystemException, NoSuchObjectException {
		Collection<Coupon> myCoupons = new ArrayList<>();
		Collection<Long> myCompanyCouponsIds = couponDBDAO.getFromCompanyCoupons(company);
		for (Long coupons : myCompanyCouponsIds) {

			myCoupons.add(couponDBDAO.getCoupon(coupons));
		}
		return myCoupons;
	}// getAllCompanyCoupons

	public Collection<Coupon> getCouponByType(Coupon.CouponType type)
			throws CouponSystemException, NoSuchObjectException {
		return couponDBDAO.getCouponByType(type);
	}// getCouponByType

	public static CouponClientFacade login(String name, String password)
			throws CouponSystemException, InvalidLoginException {
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		Company company = companyDBDAO.login(name, password);
		return new CompanyFacade();
	}// login

}
