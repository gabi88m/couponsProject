package dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import couponSystem.ConnectionPool;
import couponSystem.Schema;
import couponSystem.StatementUtils;
import dao.CustomerDAO;
import ex.CouponSystemException;
import ex.InvalidLoginException;
import ex.NoSuchObjectException;
import javaBeans.Coupon;
import javaBeans.Customer;

public class CustomerDBDAO implements CustomerDAO {

	@Override
	public void createCustomer(Customer customer) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getCreateCustomer());
			applyCustomerValuesOnStatement(st, customer);
			st.execute();
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem creating customer" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// createCustomer

	@Override
	public void removeCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteCustomer());
			st.setLong(1, customer.getId());
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				throw new NoSuchObjectException("Unable to remove customer with id= " + customer.getId());
			}
		} catch (SQLException e) {
			throw new NoSuchObjectException("There was a problem deleting customer customer" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// removeCustomer

	@Override
	public void updateCustomer(Customer customer) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getUpdateCustomer());
			applyCustomerValuesOnStatement(st, customer);
			st.setLong(4, customer.getId());
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				throw new NoSuchObjectException("Unable to update customer with id= " + customer.getId());
			}
		} catch (SQLException e) {
			throw new CouponSystemException("There was a problem updating customer" + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// updateCustomer

	@Override
	public Customer getCustomer(long id) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		Customer customer = null;
		ResultSet res = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCustomerById());
			st.setLong(1, id);
			res = st.executeQuery();
			if (res.first()) {
				customer = resultSetToCustomer(res);
				customer.setCoupons(getCustomerCoupons(customer));// add customer coupons
			} else {
				throw new NoSuchObjectException("No such customer " + id);
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Unable to getCustomer: " + e.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return customer;
	}// getCustomer

	@Override
	public Collection<Customer> getAllCustomers() throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		Statement st = null;
		Customer customer = null;
		ResultSet res = null;
		Collection<Customer> allCustomers = new ArrayList<>();

		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.createStatement();
			res = st.executeQuery(Schema.getSelectIdFromCustomer());
			while (res.next()) {
				long currCustomerid = res.getLong(1);
//				customer = resultSetToCustomer(res);
				customer = getCustomer(currCustomerid);
//				customer.setCoupons(getCustomertCoupons(getCustomer(currCustomerid))); // add customer coupons
				allCustomers.add(customer);
			}
		} catch (SQLException e) {
			throw new CouponSystemException("Failed getting all customers: " + e.getMessage());

		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return allCustomers;
	}// getAllCustomer

	@Override
	public Collection<Coupon> getCustomerCoupons(Customer customer) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		ResultSet res = null;
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Coupon coupon = null;

		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCustomerCouponInnerJoinById());
			st.setLong(1, customer.getId());
			res = st.executeQuery();
			while (res.next()) {
				coupon = resultSetToCoupon(res);
				coupons.add(coupon);
			}
		} catch (SQLException ex) {
			throw new CouponSystemException("Unable to get customer coupon" + ex.getMessage());
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return coupons;
	}
	// getCoupons

	@Override
	public Customer login(String name, String password) throws CouponSystemException, InvalidLoginException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getSelectCustomerByNameAndPassword());
			st.setString(1, name);
			st.setString(2, password);
			ResultSet res = st.executeQuery();
			if (res.first()) {
				return resultSetToCustomer(res);
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

	public void addCouponToCustomer(Coupon coupon, Customer customer)
			throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.InsertIntoCustomerCoupon());
			st.setLong(1, customer.getId());
			st.setLong(2, coupon.getId());
			st.execute();
			coupon.setAmount(coupon.getAmount() - 1);
			CouponDBDAO couponDBDAO = new CouponDBDAO();
			couponDBDAO.updateCoupon(coupon);
		} catch (SQLException e) {
			throw new CouponSystemException("there was a problem adding coupon to this customer!!");
		} finally {
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// addCouponToCustomer

	private void applyCustomerValuesOnStatement(PreparedStatement st, Customer customer) throws SQLException {
		st.setLong(1, customer.getId());
		st.setString(2, customer.getCustName());
		st.setString(3, customer.getPassword());
	}// applyCustomerValuesOnStatement

	private Customer resultSetToCustomer(ResultSet rs) throws SQLException {
		Customer customer = null;

		String customerId = Schema.getCustomerId();
		String customerName = Schema.getCustomerName();
		String customerPassword = Schema.getCustomerPassword();

		customer = new Customer(rs.getLong(customerId), rs.getString(customerName), rs.getString(customerPassword));

		return customer;
	}// resultSetToCustomer

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
