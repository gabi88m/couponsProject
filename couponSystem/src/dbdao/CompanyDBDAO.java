package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import dao.CompanyDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;
import utilities.ConnectionPool;
import utilities.Schema;
import utilities.StatementUtils;

public class CompanyDBDAO implements CompanyDAO {

	@Override
	public void createCompany(Company company) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getCreateCompany());
			applyCompanyValuesOnStatement(st, company);
			st.execute();
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem creating company" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// createCompany

	@Override
	public void removeCompany(Company company) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteCompany());
			st.setLong(1, company.getId());
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				throw new NoSuchObjectException("Unable to remove company with id= " + company.getId());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem deleting company" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// removeCompany

	@Override
	public void updateCompany(Company company) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getUpdateCompany());
			applyCompanyValuesOnStatement(st, company);
			st.setLong(5, company.getId());
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				throw new NoSuchObjectException("Unable to update company with id= " + company.getId());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem updating company" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// updateCompany

	@Override
	public Company getCompany(long id) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		Company company = null;
		ResultSet res = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCompanyById());
			st.setLong(1, id);
			res = st.executeQuery();
			if (res.first()) {
				company = resultSetToCompany(res);
				company.setCoupons(getCompanyCoupons(company));
			} else {
				throw new NoSuchObjectException(String.format("No such commany invalid id = " + id));
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get commany: " + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return company;
	}// getCompany

	@Override
	public Collection<Company> getAllCompamies() throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		Collection<Company> allCompanies = new ArrayList<>();
		Company company = null;
		ResultSet res = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectIdFromCompany());
			res = st.executeQuery();
			while (res.next()) {
				company = getCompany(res.getLong(1));
				allCompanies.add(company);
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Unable to get all companies: " + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return allCompanies;
	}// getAllCompamies

	@Override
	public Collection<Coupon> getCompanyCoupons(Company company) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Collection<Coupon> companyCoupons = new ArrayList<Coupon>();
		Coupon coupon = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCouponsByCompanyInnerJoinById());
			st.setLong(1, company.getId());
			res = st.executeQuery();
			// TODO:add NoSuchObjectexeption
			while (res.next()) {
				coupon = (resultSetToCoupon(res));
				companyCoupons.add(coupon);
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Unable to getCompany coupons: " + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return companyCoupons;
	}// getCoupons

	@Override
	public Company login(String name, String password) throws CouponSystemException, InvalidLoginException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCompanyByNameAndPassword());
			st.setString(1, name);
			st.setString(2, password);
			ResultSet res = st.executeQuery();
			if (res.first()) {
				return resultSetToCompany(res);
			} else {
				String msg = String.format("Invalid login for name =" + name + "and password =" + password);
				throw new InvalidLoginException(msg);
			}
		} catch (SQLException ex) {
			throw new CouponSystemException("Something went wrong while trying to login.");
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// login

	public void addCouponToCompany(Coupon coupon, Company company) throws SQLException {
		Connection c1 = null;
		PreparedStatement st = null;
		// TODO:fix the sql
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.InsertIntoCompanyCoupon());
			st.setLong(1, coupon.getId());
			st.setLong(2, company.getId());
			st.execute();

		} catch (CouponSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// CreateCoupon

	private void applyCompanyValuesOnStatement(PreparedStatement st, Company company) throws SQLException {
		st.setLong(1, company.getId());
		st.setString(2, company.getCompName());
		st.setString(3, company.getPassword());
		st.setString(4, company.getEmail());
	}// applyCompanyValuesOnStatement

	private Company resultSetToCompany(ResultSet rs) throws SQLException {
		Company company = null;
		String companyId = Schema.getCompanyId();
		String compayName = Schema.getCompanyName();
		String companyPassword = Schema.getCompanyPassword();
		String companyEmail = Schema.getCompanyEmail();

		company = new Company(rs.getLong(companyId), rs.getString(compayName), rs.getString(companyPassword),
				rs.getString(companyEmail));
		return company;
	}// resultSetToCompany

	private Coupon resultSetToCoupon(ResultSet rs) throws SQLException {
		Coupon coupon = null;

		String couponId = Schema.getCouponId();
		String couponTitle = Schema.getCouponTitle();
		String couponStartDate = Schema.getCouponStartDate();
		String couponEndDate = Schema.getCouponEndDate();
		String couponAmount = Schema.getCouponAmount();
		String couponType = Schema.getCouponType().toString();
		String couponMessage = Schema.getCouponMessage();
		String couponPrice = Schema.getCouponPrice();
		String couponImage = Schema.getCouponImage();

		coupon = new Coupon(rs.getLong(couponId), rs.getString(couponTitle), rs.getDate(couponStartDate),
				rs.getDate(couponEndDate), rs.getInt(couponAmount), rs.getString(couponType),
				rs.getString(couponMessage), rs.getDouble(couponPrice), rs.getString(couponImage));
		return coupon;
	}// resultSetToCoupon

}
