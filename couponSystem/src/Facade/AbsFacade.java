package Facade;

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
	protected static AbsFacade login(String name, String password, LoginType type) throws InvalidLoginException {
		switch (type) {
			case ADMIN:
				// return AdminFacade.performLogin(name, password);
			case COMPANY:

				break;
			case CUSTOMER:

				break;
			default:
				break;

		}
		return null;
	}
}
