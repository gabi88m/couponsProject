package utilities;

import java.sql.SQLException;
import java.sql.Statement;

import ex.CouponSystemException;

public final class StatementUtils {
	public static void closeAll(Statement... statements) throws CouponSystemException {
		for (Statement statement : statements) {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new CouponSystemException("Unable to close statement " + statement);
				}
			}
		}
	}
}
