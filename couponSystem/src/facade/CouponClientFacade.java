package facade;

import java.sql.SQLException;

import ex.CouponSystemException;
import ex.InvalidLoginException;

public interface CouponClientFacade {

	public static CouponClientFacade login(String name, String password, LoginType clientType)
			throws InvalidLoginException, CouponSystemException, SQLException {
		switch (clientType) {
			case ADMIN:
				return AdminFacade.login(name, password);
			case COMPANY:
				return CompanyFacade.login(name, password);
			case CUSTOMER:
				return CustomerFacade.login(name, password);
			default:
				throw new InvalidLoginException("What are you? :-0");
		}
	}

}
