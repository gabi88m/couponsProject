package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import couponSystem.ConnectionPool;
import couponSystem.Schema;
import couponSystem.StatementUtils;
import dao.CouponDAO;
import ex.CouponSystemException;
import ex.NoSuchObjectException;
import javaBeans.Company;
import javaBeans.Coupon;
import javaBeans.Customer;

public class CouponDBDAO implements CouponDAO {

	@Override
	public void createCoupon(Coupon coupon) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getCreateCoupon());
			applyCouponValuesOnStatement(st, coupon);
			st.execute();
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem creating coupon" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// createCoupon

	@Override
	public void removeCoupon(Coupon coupon) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteCoupon());
			st.setLong(1, coupon.getId());
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				throw new NoSuchObjectException("Unable to remove Coupon with id= " + coupon.getId());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem deleting coupon" + e.getMessage());
		} finally {
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
				throw new NoSuchObjectException("Unable to update coupon with id= " + coupon.getId());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem updating coupon" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// updateCoupon

	@Override
	public Coupon getCoupon(long id) throws CouponSystemException, NoSuchObjectException {
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
				throw new NoSuchObjectException("No such coupon " + id);
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem geting coupon" + id + e.getMessage());
		} finally {
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
				coupon = resultSetToCoupon(res);
				allCoupons.add(coupon);
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem geting all coupons:" + e.getMessage());
		} finally {
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
		// TODO:NoSuchObjectExeption(type)////////////
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement("select * from coupon where TYPE=?");
			st.setString(1, type.toString());
			res = st.executeQuery();
			while (res.next()) {
				coupon = resultSetToCoupon(res);
				allCoupons.add(coupon);
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem geting coupons by type" + e.getMessage());
		} finally {
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
			if (rowAffected == 0) {
				throw new NoSuchObjectException("Unable to remove coupon from company with id= " + coupon.getId());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem deleting coupon from company " + e.getMessage());
		} finally {
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
				throw new NoSuchObjectException("Unable to remove coupon from customer with id= " + customer.getId());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem deleting coupon from customer " + e.getMessage());
		} finally {
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
			st = c1.prepareStatement("select * from customer_coupon");
			res = st.executeQuery();
			while (res.next()) {
				myCouponsIds.add(res.getLong(Schema.getCompJoinId()));
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem geting Customer coupons " + e.getMessage());
		} finally {
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
		// TODO:NoSuchObjectExeption(type)////////////
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement("select * from company_coupon where comp_id=?");
			st.setLong(1, company.getId());
			res = st.executeQuery();
			while (res.next()) {
				myCouponsIds.add(res.getLong(Schema.getCouponJoinId()));
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem geting company coupons " + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return myCouponsIds;
	}// getFromCustomerCoupons

	private void applyCouponValuesOnStatement(PreparedStatement st, Coupon coupon) throws SQLException {
		st.setLong(1, coupon.getId());
		st.setString(2, coupon.getTitle());
		st.setDate(3, new java.sql.Date(coupon.getEndDate().getTime()));
		st.setDate(4, new java.sql.Date(coupon.getEndDate().getTime()));
		st.setLong(5, coupon.getAmount());
		st.setString(6, coupon.getCouponType().toString());
		st.setString(7, coupon.getImage());
		st.setFloat(8, (float) coupon.getPrice());
		st.setString(9, coupon.getImage());
	}// applyCouponValuesOnStatement

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
	}
}
