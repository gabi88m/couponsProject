package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import ex.CouponSystemException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.Customer;
import utilities.ConnectionPool;
import utilities.StatementUtils;

public class CouponDBDAO implements CouponDAO {

	public CouponDBDAO() {
		try {
			createTable();
		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void createTable() throws CouponSystemException {
		Statement st = null;
		Connection connection = null;

		try {
			connection = ConnectionPool.getInstance().getConnection();
			st = connection.createStatement();
			st.executeUpdate(Schema.getCreateTableCoupon());
		} catch (SQLException ex) {
			throw new CouponSystemException("There was a problem creating the company table." + ex.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			StatementUtils.closeAll(st);
		}
	}

	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getCreateCoupon());
			applyCouponValuesOnStatement(st, coupon); // applies the statement values
			st.execute();
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem creating coupon, " + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// createCoupon

	@Override
	public void removeCoupon(long couponId) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteCoupon());
			st.setLong(1, couponId);
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				// if the "rowAffected" equals to 0 there is not such Coupon , and we will // if
				// the "rowAffected" equals to 0 there is not such Coupon , and we will
				String msg = "Unable to remove Coupon with id= " + couponId;
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem deleting coupon" + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// removeCoupon

	@Override
	public void updateCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getUpdateCoupon());
			applyCouponValuesOnStatement(st, coupon);
			st.setLong(10, coupon.getId());
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				// if the "rowAffected" equals to 0 there is not such Customer , and we will
				// throw a massage
				String msg = "Unable to update coupon with id= " + coupon.getId();
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem updating coupon" + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// updateCoupon

	@Override
	public Coupon getCouponById(long id) throws CouponSystemException, NoSuchObjectException {
		Coupon coupon = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Connection c1 = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCouponById());
			st.setLong(1, id);
			res = st.executeQuery();
			if (res.next()) {
				coupon = resultSetToCoupon(res);
			} else {
				// if the result set is empty we will throw an exception because that mean we
				// have not this Coupon
				String msg = "No such coupon " + id;
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if the "rowAffected" equals to 0 there is not such Customer , and we will
			// throw a massage
			String msg = "There was a problem geting coupon" + id + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return coupon;
	}// getCoupon

	@Override
	public Collection<Coupon> getAllCoupons() throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Coupon coupon = null;
		Collection<Coupon> allCoupons = new ArrayList<>();
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectAllCoupons());
			res = st.executeQuery();
			while (res.next()) {
				// while we have "next" there is more Coupons and we will add them to the
				// ArrayList
				coupon = resultSetToCoupon(res);
				allCoupons.add(coupon);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem geting all coupons:" + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return allCoupons;
	}// getAllCoupon

	@Override
	public Collection<Coupon> getCouponByType(Coupon.CouponType type)
			throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Coupon coupon = null;
		Collection<Coupon> allCoupons = new ArrayList<>();
		// TODO:NoSuchObjectExeption(type)
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCouponByType());
			st.setString(1, type.toString());
			res = st.executeQuery();
			while (res.next()) {
				// while we have "next" there is more Coupons and we will add them to the
				// ArrayList
				coupon = resultSetToCoupon(res);
				allCoupons.add(coupon);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem geting coupons by type" + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return allCoupons;
	}

	public void removeCompmanyCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteFromCompanyCouponInnerJoinById());
			st.setLong(1, coupon.getId());
			int rowAffected = st.executeUpdate();
			System.out.println("test");

			if (rowAffected == 0) {
				// if the "rowAffected" equals to 0 there is not such Customer , and we will
				// throw a massage
				System.out.println(rowAffected);
				System.out.println("test");
				String msg = "Unable to remove coupon from company with id= " + coupon.getId();
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem deleting coupon from company " + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);

		}

	}// removeCompmanyCoupon

	public void removeCustomerCoupon(Customer customer) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteFromCustomerCouponInnerJoinById());
			st.setLong(1, customer.getId());

			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				// if the "rowAffected" equals to 0 there is not such Customer , and we will
				// throw a massage
				String msg = "Unable to remove coupon from customer with id= " + customer.getId();
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem deleting coupon from customer " + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// removeCustomerCoupon

	/*****************************************************************************************************************
	 * returns the customer and coupons join tabel
	 */
	public Collection<Long> getFromCustomerCoupons() throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Collection<Long> myCouponsIds = new ArrayList<>();
		// TODO:NoSuchObjectExeption(type)////////////
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectAllFromCustomerCoupon());
			res = st.executeQuery();
			while (res.next()) {
				myCouponsIds.add(res.getLong(Schema.getCompJoinId()));
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem geting Customer coupons " + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return myCouponsIds;
	}// getFromCustomerCoupons

	public Collection<Long> getFromCompanyCoupons(Company company) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Collection<Long> myCouponsIds = new ArrayList<>();
		//
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectFromCompanyCouponById());
			st.setLong(1, company.getId());
			res = st.executeQuery();
			while (res.next()) {
				myCouponsIds.add(res.getLong(Schema.getCouponJoinId()));
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem geting company coupons " + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return myCouponsIds;
	}// getFromCustomerCoupons

	/**
	 * applies the statement values
	 */
	private void applyCouponValuesOnStatement(PreparedStatement st, Coupon coupon) throws SQLException {
		st.setLong(1, coupon.getId());
		st.setString(2, coupon.getTitle());
		st.setDate(3, java.sql.Date.valueOf(coupon.getStartDate()));
		st.setDate(4, java.sql.Date.valueOf(coupon.getEndDate()));
		st.setLong(5, coupon.getAmount());
		st.setString(6, coupon.getCouponType().toString());
		st.setString(7, coupon.getImage());
		st.setFloat(8, (float) coupon.getPrice());
		st.setString(9, coupon.getImage());
	}// applyCouponValuesOnStatement

	/**
	 * @return A converted Coupon from a ResultSet.
	 * @param rs - The ResultSet from which a Coupon is being created.
	 * @throws SQLException
	 */
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

		coupon = new Coupon(rs.getLong(couponId), rs.getString(couponTitle), rs.getDate(couponStartDate).toLocalDate(),
				rs.getDate(couponEndDate).toLocalDate(), rs.getInt(couponAmount), rs.getString(couponType),
				rs.getString(couponMessage), rs.getDouble(couponPrice), rs.getString(couponImage));
		return coupon;
	}
}
