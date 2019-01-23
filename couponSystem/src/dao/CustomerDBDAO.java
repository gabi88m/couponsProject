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
import javaBeans.Coupon;
import javaBeans.Customer;
import utilities.ConnectionPool;
import utilities.StatementUtils;

public class CustomerDBDAO implements CustomerDAO {

	public CustomerDBDAO() {
		try {
			createTable();
		} catch (CouponSystemException e) {
			System.out.println(e.getMessage());
			// Should never happen.
		}
	}// ctor

	@Override
	public void createTable() throws CouponSystemException {
		Statement stmtCreateCustomerTable = null;
		Statement stmtCreateJoinTable = null;
		Connection connection = null;
		try {
			connection = ConnectionPool.getInstance().getConnection();

			stmtCreateCustomerTable = connection.createStatement();
			stmtCreateCustomerTable.executeUpdate(Schema.getCreateTableCustomer());

			stmtCreateJoinTable = connection.createStatement();
			stmtCreateJoinTable.executeUpdate(Schema.getCreateTableCustomerCoupon());

		} catch (SQLException ex) {
			throw new CouponSystemException("There was a problem creating a customer table." + ex.getMessage());
			// finally run just with try block and finally always run after
			// catch
		} finally {
			ConnectionPool.getInstance().returnConnection(connection);
			StatementUtils.closeAll(stmtCreateCustomerTable, stmtCreateJoinTable);
		}

	}// createTable

	@Override
	public void createCustomer(Customer customer) throws CouponSystemException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getCreateCustomer());
			applyCustomerValuesOnStatement(st, customer); // applies the statement values
			st.execute();
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem creating customer" + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// createCustomer

	@Override
	public void removeCustomer(long customerId) throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.getDeleteCustomer());
			st.setLong(1, customerId);
			int rowAffected = st.executeUpdate();
			if (rowAffected == 0) {
				// if the "rowAffected" equals to 0 there is not such Customer , and we will
				// throw a massage
				String msg = "Unable to remove customer with id= " + customerId;
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem deleting customer customer" + e.getMessage();
			throw new NoSuchObjectException(msg);
		} finally {
			// terminate the connection and statement
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
				// if the "rowAffected" equals to 0 there is not such Customer , and we will
				// throw a massage
				String msg = "Unable to update customer with id= " + customer.getId();
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "There was a problem updating customer" + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
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
				// if the res is not empty we will get the Customer
				customer = resultSetToCustomer(res);
				customer.setCoupons(getCustomerCoupons(customer));// add customer coupons
			} else {
				// if res is empty we will throw exception
				String msg = "No such customer " + id;
				throw new NoSuchObjectException(msg);
			}
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "Unable to getCustomer: " + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
		return customer;
	}// getCustomer

	@Override
	public Collection<Customer> getAllCustomers() throws CouponSystemException {
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
				// while the res is not empty we will add customers to the ArrayList
				long currCustomerid = res.getLong("id");
				customer = getCustomer(currCustomerid);
				allCustomers.add(customer);
			}
		} catch (SQLException | NoSuchObjectException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "Failed getting all customers: " + e.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
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
				// while the res is not empty we will add customers coupons to the ArrayList
				coupon = resultSetToCoupon(res);
				coupons.add(coupon);
			}
		} catch (SQLException ex) {
			// if there will be an sql problem we will throw a massage
			String msg = "Unable to get customer coupon" + ex.getMessage();
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
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
				// if the user name and password exist on the DB
				return resultSetToCustomer(res);
			} else {
				// if not will throw an exception
				String msg = String.format("Invalid login for name =" + name + "and password =" + password);
				throw new InvalidLoginException(msg);
			}
		} catch (SQLException ex) {
			// if there will be an sql problem we will throw a massage
			String msg = "Something went wrong while trying to login.";
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// login

	public void addCouponToCustomer(long couponId, Customer customer)
			throws CouponSystemException, NoSuchObjectException {
		Connection c1 = null;
		PreparedStatement st = null;
		try {
			c1 = ConnectionPool.getInstance().getConnection();
			st = c1.prepareStatement(Schema.InsertIntoCustomerCoupon());
			st.setLong(1, customer.getId());
			st.setLong(2, couponId);
			st.execute();
		} catch (SQLException e) {
			// if there will be an sql problem we will throw a massage
			String msg = "there was a problem adding coupon to this customer!!";
			throw new CouponSystemException(msg);
		} finally {
			// terminate the connection and statement
			ConnectionPool.getInstance().returnConnection(c1);
			StatementUtils.closeAll(st);
		}
	}// addCouponToCustomer

	/**
	 * applies the statement values
	 */
	private void applyCustomerValuesOnStatement(PreparedStatement st, Customer customer) throws SQLException {
		st.setLong(1, customer.getId());
		st.setString(2, customer.getCustName());
		st.setString(3, customer.getPassword());
	}// applyCustomerValuesOnStatement

	/**
	 * @return A converted Customer from a ResultSet.
	 * @param rs - The ResultSet from which a Company is being created.
	 * @throws SQLException
	 */
	private Customer resultSetToCustomer(ResultSet rs) throws SQLException {
		Customer customer = null;

		String customerId = Schema.getCustomerId();
		String customerName = Schema.getCustomerName();
		String customerPassword = Schema.getCustomerPassword();

		customer = new Customer(rs.getLong(customerId), rs.getString(customerName), rs.getString(customerPassword));

		return customer;
	}// resultSetToCustomer

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
	}// resultSetToCoupon
}
