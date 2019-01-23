package facade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import dao.CompanyDBDAO;
import dao.CouponDBDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import ex.ObjectAlreadyExistsException;
import javaBeans.Company;
import javaBeans.Coupon;

public class CompanyFacade implements CouponClientFacade {
	Company company;
	CompanyDBDAO companyDBDAO;
	CouponDBDAO couponDBDAO;

	public CompanyFacade(Company company, CompanyDBDAO companyDBDAO, CouponDBDAO couponDBDAO) {
		this.company = company;
		this.couponDBDAO = couponDBDAO;
		this.companyDBDAO = companyDBDAO;

	}// ctor

	public static CouponClientFacade login(String name, String password)
			throws CouponSystemException, InvalidLoginException, SQLException {

		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		Company company = companyDBDAO.login(name, password);
		CouponDBDAO couponDBDAO = new CouponDBDAO();

		return new CompanyFacade(company, companyDBDAO, couponDBDAO);
	}// login

	public void createCoupon(Coupon coupon) throws CouponSystemException, SQLException, ObjectAlreadyExistsException {
		Collection<Coupon> allCoupons = couponDBDAO.getAllCoupons();
		boolean haveCouponWithSameTitle = false;

		if (allCoupons.isEmpty()) { // check if thare is any coupons , if not ignore the title check
			couponDBDAO.createCoupon(coupon);
			companyDBDAO.addCouponToCompany(coupon, company);
		} else {
			for (Coupon coupons : allCoupons) {

				if (coupons.getTitle().equals(coupon.getTitle())) { // if coupon with same title exist.
					haveCouponWithSameTitle = true;
					String msg = "error! , you have coupon with same title!";
					throw new ObjectAlreadyExistsException(msg);
				}
			}
			if (!(haveCouponWithSameTitle)) {
				couponDBDAO.createCoupon(coupon);
				companyDBDAO.addCouponToCompany(coupon, company);
			}
		}
	}// createCoupon

	public void removeCoupon(long couponID) throws CouponSystemException, NoSuchObjectException {
//		couponDBDAO.removeCompmanyCoupon(coupon); // remove from join tabels
		couponDBDAO.removeCoupon(couponID); // remove the coupon
	}// removeCoupon

	public void updateCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException {
		couponDBDAO.updateCoupon(coupon);
	}// removeCoupon

	public Coupon getCoupon(long id) throws CouponSystemException, NoSuchObjectException {
		// TODO:check if it works
		if (couponDBDAO.getCouponById(id).equals(null)) {
			String msg = "There is no such coupons!";
			throw new NoSuchObjectException(msg);
		}
		return couponDBDAO.getCouponById(id);
	}// getCoupon

	public Collection<Coupon> getAllCompanyCoupons(Company company)
			throws CouponSystemException, NoSuchObjectException {
		Collection<Coupon> myCoupons = new ArrayList<>();
		Collection<Long> myCompanyCouponsIds = couponDBDAO.getFromCompanyCoupons(company);
		for (Long coupons : myCompanyCouponsIds) {
			myCoupons.add(couponDBDAO.getCouponById(coupons));
		}
		return myCoupons;
	}// getAllCompanyCoupons

	public Collection<Coupon> getCouponByType(Coupon.CouponType type)
			throws CouponSystemException, NoSuchObjectException {
		if (couponDBDAO.getCouponByType(type).isEmpty()) {
			String msg = "There is no such coupons!";
			throw new NoSuchObjectException(msg);
		}
		return couponDBDAO.getCouponByType(type);
	}// getCouponByType

}
