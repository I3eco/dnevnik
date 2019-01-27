package bg.dnevnik;

import java.util.Collection;

import bg.dnevnik.exceptions.WrongInputException;
import bg.dnevnik.utility.Validation;

public class User {
	
	private final String name;
	private String email;
	private String password;
	private Collection<Comment> commentHistory;

	// TODO this is just an idea, but instead of a boolean we could add a state enum ONLINE/OFFLINE/AWAY,
	// and have a thread loop through all users, and if they haven't done anything in a few minutes,
	// it sets their state to AWAY
	private boolean isOnline;
	
	private User(String name, String email, String password) throws WrongInputException {
		Validation.throwIfNull(name, email, password);
		Validation.throwIfEmpty(name, email, password);
		
		if (!email.trim().matches("[\\w-]+@([\\w-]+\\.)+[\\w-]+")) {
			throw new WrongInputException("Email is incorrect!");
		}
		
		this.email = email;
		this.name = name.trim();
		this.password = password.trim();
		
	}
	
	public static void signUp(String username, String email, String password) {
		User user = null;
		try {
			user = new User(username, email, password);
		} catch (WrongInputException e) {
			System.err.println("Could not sign up!");
			e.printStackTrace();
		}
		
		Site.getInstance().addUser(user);
	}
	
	public void addToCommentHistory(Comment comment) {
		this.commentHistory.add(comment);
	}

	public boolean loginInfoMatches(String email, String password) {
		if (this.email.equals(email.trim()) && this.password.equals(password.trim())) {
			return true;
		}
		return false;
	}
	
	public String getName() {
		return this.name;
	}

	public boolean isOnline() {
		return this.isOnline;
	}
	
	public void goOnline() {
		this.isOnline = true;
	}

	public void goOffline() {
		this.isOnline = false;
	}
}
