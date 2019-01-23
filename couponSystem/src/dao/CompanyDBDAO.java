package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;
import utilities.ConnectionPool;
import utilities.StatementUtils;

public class CompanyDBDAO implements CompanyDAO {

	public CompanyDBDAO() {
		try {
			createTable();
		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			// Should never happen.
		}
	}// ctor

	@Override
	public void createTable() throws CouponSystemException {
		// TODO: Check implementation.
		Statement stmtCreateCompanyTable = null;
		Statement stmtCreateJoinTable = null;
		Connection connection = null;

		try {
			connection = ConnectionPool.getInstance().getConnection();

			stmtCreateCompanyTable = connection.createStatement();
			stmtCreateCompanyTable.executeUpdate(Schema.getCreateTableCompany());

			stmtCreateJoinTable = connection.createStatement();
			stmtCreateJoinTable.executeUpdate(Schema.getCreateTableCompanyCoupon());
		} catch (SQLException ex) {
			throw new CouponSystemException("There was a problem creating the company table." + ex.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			StatementUtils.closeAll(stmtCreateCompanyTable, stmtCreateJoinTable);
		}
	}// createTable

	@Override
	public void createCompany(Company company) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;

		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getCreateCompany());
			applyCompanyValuesOnStatement(st, company); // applies the statement values
			st.execute();
		} catch (SQLException e) {
			// if there will be an SQL problem we will throw a massage
			String msg = "There was a problem creating company" + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// createCompany

	@Override
	public void removeCompany(long companyId) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;

		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteCompany());
			st.setLong(1, companyId);
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				// if the "rowAffected" equals to 0 there is not such company , and we will
				// throw a massage
				String msg = "Unable to remove company with id= " + companyId;
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			String msg = "There was a problem deleting company" + e.getMessage();
			throw new CouponSystemException(msg); // if there will be an sql problem we will throw a massage
		} finally {
			// terminate the connection and statement
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
			applyCompanyValuesOnStatement(st, company); // applies the statement values
			st.setLong(5, company.getId());
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) { // if the "rowAffected" equals to 0 there is not such company , and we will
				// throw a massage
				String msg = "Unable to update company with id= " + company.getId();
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			String msg = "There was a problem updating company" + e.getMessage();
			throw new CouponSystemException(msg); // if there will be an sql problem we will throw a massage
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// updateCompany

	@Override
	public Company getCompany(long companyId) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		Company company = null;
		ResultSet res = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCompanyById());
			st.setLong(1, companyId);
			res = st.executeQuery();
			if (res.first()) { // if result set is not empty
				company = resultSetToCompany(res);// turn the result to company
				company.setCoupons(getCompanyCoupons(companyId));// query to get company coupons
			} else {
				// else the result set is empty! , and we will throw a massage
				String msg = "No such commany invalid id = " + companyId;
				throw new NoSuchObjectException(String.format(msg));
			}
		} catch (SQLException e) {
			String msg = "Unable to get commany: " + e.getMessage();
			throw new CouponSystemException(msg); // if there will be an sql problem we will throw a massage
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return company;
	}// getCompany

	@Override
	public Collection<Company> getAllCompanies() throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		Collection<Company> allCompanies = new ArrayList<>(); // will return this list of companies empty/or full
		Company company = null;
		ResultSet res = null;

		// it is possible to check if there is any companies at all-maybe in the future.

		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectIdFromCompany());
			res = st.executeQuery();
			while (res.next()) { // check the result set and while it have "next" will turn it to a company and
									// add the list
				company = getCompany(res.getLong(1));
				allCompanies.add(company);
			}
		} catch (SQLException | NoSuchObjectException e) {
			String msg = "Unable to get all companies: " + e.getMessage();
			throw new CouponSystemException(msg);// if there will be an sql problem we will throw a massage
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return allCompanies;
	}// getAllCompamies

	@Override
	public Collection<Coupon> getCompanyCoupons(long companyId) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Collection<Coupon> companyCoupons = new ArrayList<Coupon>();
		Coupon coupon = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCouponsByCompanyInnerJoinById());
			st.setLong(1, companyId);

			res = st.executeQuery();

			while (res.next()) {
				coupon = (resultSetToCoupon(res));
				companyCoupons.add(coupon);
			}
		} catch (SQLException e) {
			String msg = "Unable to getCompany coupons: " + e.getMessage();
			throw new CouponSystemException(msg); // if there will be an sql problem we will throw a massage
		} finally {
			// terminate the connection and statement
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
				// if the user name and password exist on the DB
				return resultSetToCompany(res);
			} else {
				// if not will throw an exception
				String msg = String.format("Invalid login for name =" + name + "and password =" + password);
				throw new InvalidLoginException(msg);
			}
		} catch (SQLException ex) {
			String msg = "Something went wrong while trying to login.";
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// login

	public void addCouponToCompany(Coupon coupon, Company company) throws SQLException, CouponSystemException {
		// first create here the connection and statement , so we can terminate them at
		// the end
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.InsertIntoCompanyCoupon());
			st.setLong(1, coupon.getId());
			st.setLong(2, company.getId());
			st.execute();

		} catch (CouponSystemException e) {
			String msg = "Something went wrong while trying to add coupon to company.";
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// CreateCoupon

	/**
	 * applies the statement values
	 */
	private void applyCompanyValuesOnStatement(PreparedStatement st, Company company) throws SQLException {
		st.setLong(1, company.getId());
		st.setString(2, company.getCompName());
		st.setString(3, company.getPassword());
		st.setString(4, company.getEmail());
	}// applyCompanyValuesOnStatement

	/**
	 * @return A converted Company from a ResultSet.
	 * @param rs - The ResultSet from which a Coupon is being created.
	 * @throws SQLException
	 */
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

	/**
	 * @return A converted Coupon from a ResultSet.
	 * @param rs - The ResultSet from which a Coupon is being created.
	 * @throws SQLException
	 */
	private static Coupon resultSetToCoupon(ResultSet rs) throws SQLException {
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

		coupon = new Coupon(rs.getLong(couponId), rs.getString(couponTitle), rs.getDate(couponStartDate).toLocalDate(),
				rs.getDate(couponEndDate).toLocalDate(), rs.getInt(couponAmount), rs.getString(couponType),
				rs.getString(couponMessage), rs.getDouble(couponPrice), rs.getString(couponImage));
		return coupon;
	}// resultSetToCoupon

}
