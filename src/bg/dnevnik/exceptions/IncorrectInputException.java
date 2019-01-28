package bg.dnevnik.exceptions;

public class IncorrectInputException extends Exception {

	private static final long serialVersionUID = -11575062265803171L;

	public IncorrectInputException() {
		super();
	}

	public IncorrectInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IncorrectInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectInputException(String message) {
		super(message);
	}

	public IncorrectInputException(Throwable cause) {
		super(cause);
	}
}
