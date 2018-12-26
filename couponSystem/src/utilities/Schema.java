package utilities;

public final class Schema {

	/****************************************************************************************************************
	 * Table names
	 */
	private static final String TABLE_NAME_COUPON = "Coupon";
	private static final String TABLE_NAME_CUSTOMER = "Customer";
	private static final String TABLE_NAME_COMPANY = "Company";
	private static final String TABLE_NAME_CUSTOMER_COUPON = "Customer_Coupon";
	private static final String TABLE_NAME_COMPANY_COUPON = "Company_Coupon";

	/****************************************************************************************************************
	 * Common Columns
	 */
	private static final String ID = "id";
	private static final String CUSTOMER_JOIN_ID = "cust_id";
	private static final String COUPON_JOIN_ID = "coupon_id";
	private static final String COMP_JOIN_ID = "comp_id";

	/****************************************************************************************************************
	 * COUPON Columns
	 */
	private static final String COUPON_TITLE = "title";
	private static final String COUPON_START_DATE = "start_date";
	private static final String COUPON_END_DATE = "end_date";
	private static final String COUPON_AMOUNT = "amount";
	private static final String COUPON_TYPE = "type";
	private static final String COUPON_MESSAGE = "message";
	private static final String COUPON_PRICE = "price";
	private static final String COUPON_IMAGE = "image";

	/****************************************************************************************************************
	 * CUSTOMER Columns
	 */
	private static final String CUSTOMER_NAME = "Cust_name";
	private static final String CUSTOMER_PASSWORD = "password";

	/****************************************************************************************************************
	 * COMPANY Columns
	 */
	private static final String COMPANY_NAME = "Comp_name";
	private static final String COMPANY_PASSWORD = "password";
	private static final String COMPANY_EMAIL = "email";

	/****************************************************************************************************************
	 * Crate Tabels
	 */
	private static final String CREATE_TABLE_COUPON = "create table if not exists " + TABLE_NAME_COUPON + "(" + ID
			+ " integer primary key auto_increment," + COUPON_TITLE + " varchar(100), " + COUPON_START_DATE + " date, "
			+ COUPON_END_DATE + " date, " + COUPON_AMOUNT + " integer," + COUPON_TYPE + " integer, " + COUPON_MESSAGE
			+ " varchar(100), " + COUPON_PRICE + " double," + COUPON_IMAGE + " varchar(100));";

	private static final String CREATE_TABLE_CUSTOMER = "create table if not exists " + TABLE_NAME_CUSTOMER + "(" + ID
			+ " integer primary key auto_increment," + CUSTOMER_NAME + " varchar(100), " + CUSTOMER_PASSWORD;

	private static final String CREATE_TABLE_COMPANY = "create table if not exists " + TABLE_NAME_COMPANY + "(" + ID
			+ " integer primary key auto_increment," + COMPANY_NAME + " varchar(100), " + COMPANY_PASSWORD
			+ " varchar(100), " + COMPANY_EMAIL;

	public static String getCreateTableCoupon() {
		return CREATE_TABLE_COUPON;
	}// getCreateTableCoupon

	public static String getCreateTableCustomer() {
		return CREATE_TABLE_CUSTOMER;
	}// getCreateTableCustomer

	public static String getCreateTableCompany() {
		return CREATE_TABLE_COMPANY;
	}// getCreateTableCompany

	/*****************************************************************************************************************
	 * customer
	 */

	public static String getCreateCustomer() {
		return "insert into " + TABLE_NAME_CUSTOMER + "(" + ID + "," + CUSTOMER_NAME + "," + CUSTOMER_PASSWORD
				+ ") values(?,?,?);";
	}// getCreateCustomer

	public static String getDeleteCustomer() {
		return "delete from " + TABLE_NAME_CUSTOMER + " where " + ID + " =?";
	}// getDeleteCustomer

	public static String getUpdateCustomer() {
		return "update " + TABLE_NAME_CUSTOMER + " set " + ID + " =? " + " , " + CUSTOMER_NAME + " =? " + " , "
				+ CUSTOMER_PASSWORD + " =? " + "where " + ID + "=?";
	}// getUpdateCustomer

	public static String getSelectCustomerById() {
		return "select * from " + TABLE_NAME_CUSTOMER + " where " + ID + " = ?;";
	}// getSelectCustomerById

	public static String getSelectCustomerCouponInnerJoinById() {
		return "select * from " + TABLE_NAME_COUPON + " t1 inner join " + TABLE_NAME_CUSTOMER_COUPON + " t2 on t1." + ID
				+ " = t2." + COUPON_JOIN_ID + " where t2." + CUSTOMER_JOIN_ID + " = ?";
	}// getSelectCustomerCouponInnerJoinById

	public static String getSelectIdFromCustomer() {
		return "select " + ID + " from " + TABLE_NAME_CUSTOMER + ";";
	}// getSelectIdFromCustomer

	public static String getSelectCustomerByNameAndPassword() {
		return "select * from " + TABLE_NAME_CUSTOMER + " where " + CUSTOMER_NAME + " = ? and " + CUSTOMER_PASSWORD
				+ " = ?;";
	}// getSelectCustomerByNameAndPassword

	public static String getCustomerId() {
		return ID;
	}

	public static String getCustomerName() {
		return CUSTOMER_NAME;
	}

	public static String getCustomerPassword() {
		return CUSTOMER_PASSWORD;
	}

	/*****************************************************************************************************************
	 * company
	 */

	public static String getCreateCompany() {
		return "insert into " + TABLE_NAME_COMPANY + "(" + ID + "," + COMPANY_NAME + "," + COMPANY_PASSWORD + ","
				+ COMPANY_EMAIL + ") values(?,?,?,?);";
	}// getCreateCompany

	public static String getDeleteCompany() {
		return "delete from " + TABLE_NAME_COMPANY + " where " + ID + " =?";
	}// getDeleteCompany

	public static String getUpdateCompany() {
		return "update " + TABLE_NAME_COMPANY + " set " + ID + " =? " + " , " + COMPANY_NAME + " =? " + " , "
				+ COMPANY_PASSWORD + " =? " + " , " + COMPANY_EMAIL + " =? " + " where " + ID + " =?;";
	}// getUpdateCompany

	public static String getSelectCompanyById() {
		return "select * from " + TABLE_NAME_COMPANY + " where " + ID + " = ?;";
	}// getSelectCompanyById

	public static String getSelectCouponsByCompanyInnerJoinById() {
		return "select * from " + TABLE_NAME_COUPON + " t1 inner join " + TABLE_NAME_COMPANY_COUPON + " t2 on t1. " + ID
				+ " = t2." + COUPON_JOIN_ID + " where t2." + COMP_JOIN_ID + "=?;";
	}// getSelectCouponsByCompanyInnerJoinById

	public static String getSelectIdFromCompany() {
		return "select " + ID + " from " + TABLE_NAME_COMPANY + ";";
	}// getSelectIdFromCustomer

	public static String getSelectCompanyByNameAndPassword() {
		return "select * from " + TABLE_NAME_COMPANY + " where " + COMPANY_NAME + " = ? and " + COMPANY_PASSWORD
				+ " = ?;";
	}// getSelectCompanyByNameAndPassword

	public static String getCompanyId() {
		return ID;
	}

	public static String getCompanyName() {
		return COMPANY_NAME;
	}

	public static String getCompanyPassword() {
		return COMPANY_PASSWORD;
	}

	public static String getCompanyEmail() {
		return COMPANY_EMAIL;
	}

	/*****************************************************************************************************************
	 * coupon
	 */

	public static String getCreateCoupon() {
		return "insert into " + TABLE_NAME_COUPON + "(" + ID + "," + COUPON_TITLE + "," + COUPON_START_DATE + ","
				+ COUPON_END_DATE + "," + COUPON_AMOUNT + "," + COUPON_TYPE + "," + COUPON_MESSAGE + "," + COUPON_PRICE
				+ "," + COUPON_IMAGE + ") values(?,?,?,?,?,?,?,?,?);";
	}// getCreateCompany

	public static String getDeleteCoupon() {
		return "delete from " + TABLE_NAME_COUPON + " where " + ID + " =?;";
	}// getDeleteCoupon

	public static String getUpdateCoupon() {
		return "update " + TABLE_NAME_COUPON + " set " + ID + " =? " + " , " + COUPON_TITLE + " =? " + " , "
				+ COUPON_START_DATE + " =? " + " , " + COUPON_END_DATE + " =? " + ", " + COUPON_AMOUNT + " =? " + " , "
				+ COUPON_TYPE + " =? " + " , " + COUPON_MESSAGE + " =? " + " , " + COUPON_PRICE + " =? " + " , "
				+ COUPON_IMAGE + " =? where " + ID + " =?;";
	}// getUpdateCoupon

	public static String getSelectCouponById() {
		return "select * from " + TABLE_NAME_COUPON + " where " + ID + " =?;";
	}// getSelectCouponById

	public static String getSelectAllCoupons() {
		return "select * from " + TABLE_NAME_COUPON + ";";
	}// getSelectCouponById

	public static String getCouponId() {
		return ID;
	}

	public static String getCouponTitle() {
		return COUPON_TITLE;
	}

	public static String getCouponStartDate() {
		return COUPON_START_DATE;
	}

	public static String getCouponEndDate() {
		return COUPON_END_DATE;
	}

	public static String getCouponAmount() {
		return COUPON_AMOUNT;
	}

	public static String getCouponType() {
		return COUPON_TYPE;
	}

	public static String getCouponMessage() {
		return COUPON_MESSAGE;
	}

	public static String getCouponPrice() {
		return COUPON_PRICE;
	}

	public static String getCouponImage() {
		return COUPON_IMAGE;
	}

	/*****************************************************************************************************************
	 * Join Tabels
	 */

	public static String InsertIntoCustomerCoupon() {
		return "insert into " + TABLE_NAME_CUSTOMER_COUPON + " (" + CUSTOMER_JOIN_ID + "," + COUPON_JOIN_ID
				+ ") values (?,?);";
	}// InsertIntoCustomerCoupon

	public static String InsertIntoCompanyCoupon() {
		return "insert into " + TABLE_NAME_COMPANY_COUPON + " (" + COUPON_JOIN_ID + "," + COMP_JOIN_ID
				+ ") values (?,?);";
	}// InsertIntoCompanyCoupon

	public static String getDeleteFromCompanyCouponInnerJoinById() {
		return "delete from " + TABLE_NAME_COMPANY_COUPON + " where " + COUPON_JOIN_ID + " =?;";
	}// getDeleteFromCustomerCouponInnerJoinById

	public static String getDeleteFromCustomerCouponInnerJoinById() {
		return "delete from " + TABLE_NAME_CUSTOMER_COUPON + " where " + CUSTOMER_JOIN_ID + " =?;";
	}// getDeleteFromCustomerCouponInnerJoinById

	public static String getCustomerJoinId() {
		return CUSTOMER_JOIN_ID;
	}// getCustomerJoinId

	public static String getCouponJoinId() {
		return COUPON_JOIN_ID;
	}// getCouponJoinId

	public static String getCompJoinId() {
		return COMP_JOIN_ID;
	}// getCompJoinId

}
