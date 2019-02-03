package bg.dnevnik.exceptions;

public class NoSuchArticleException extends Exception {

	private static final long serialVersionUID = -5380291643994296724L;

	public NoSuchArticleException() {
	}

	public NoSuchArticleException(String arg0) {
		super(arg0);
	}

	public NoSuchArticleException(Throwable arg0) {
		super(arg0);
	}

	public NoSuchArticleException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NoSuchArticleException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
