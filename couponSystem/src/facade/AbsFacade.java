package facade;

import ex.CouponSystemException;
import ex.InvalidLoginException;

public abstract class AbsFacade {

	/**
	 * A helper method for logging a user into the system.
	 * 
	 * @param name     - User name.
	 * @param password - User password.
	 * @param type     - The login type.
	 * @return A matching facade for the login type.
	 * @throws InvalidLoginException
	 */
	protected static AbsFacade login(String name, String password, LoginType type)
			throws InvalidLoginException, CouponSystemException {
		switch (type) {
			case ADMIN:
				return AdminFacade.login(name, password);
			case COMPANY:
				return CompanyFacade.login(name, password);
			case CUSTOMER:
				return CustomerFacade.login(name, password);
			default:
				throw new InvalidLoginException("There is no sutch user!!!!!!");
		}
	}
}
