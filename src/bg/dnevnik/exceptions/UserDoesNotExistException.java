package bg.dnevnik.exceptions;

public class UserDoesNotExistException extends Exception {

	private static final long serialVersionUID = 3853338056506406107L;

	public UserDoesNotExistException() {
		super();
	}

	public UserDoesNotExistException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public UserDoesNotExistException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserDoesNotExistException(String arg0) {
		super(arg0);
	}

	public UserDoesNotExistException(Throwable arg0) {
		super(arg0);
	}


}
