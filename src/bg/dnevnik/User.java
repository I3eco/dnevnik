package bg.dnevnik;

import java.util.ArrayList;
import java.util.Collection;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.WrongInputException;
import bg.dnevnik.utility.Validation;

public class User {
	
	public static class Author extends User {
		private Collection<Article> writtenArticles;
		private Author(String name, String email, String password) throws WrongInputException {
			super(name, email, password);
			this.writtenArticles = new ArrayList<Article>();
		}
	}
	
	public static class Admin extends Author {
		private Collection<Article> writtenArticles;
		private Admin(String name, String email, String password) throws WrongInputException {
			super(name, email, password);
			this.writtenArticles = new ArrayList<Article>();
		}
		
		public void makeUserAuthor (User user) throws WrongInputException {
			Site site = Site.getInstance();
			Author author = new Author(user.name, user.email, user.password);
			
			site.addUser(author);
		}
		
	}
	
	private final String name;
	private String email;
	private String password;
	private Collection<Article.Comment> commentHistory;

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
	
	public static void signUp(String username, String email, String password, boolean isAuthor) {
		User user = null;
		try {
			if (isAuthor) {
				user = new Author(username, email, password);
			} else {
				user = new User(username, email, password);
			}
			Site.getInstance().addUser(user);
		} catch (WrongInputException e) {
			System.err.println("Could not sign up!");
			e.printStackTrace();
		}
	}
	
	public void addToCommentHistory(Article.Comment comment) {
		this.commentHistory.add(comment);
	}

	public boolean loginInfoMatches(String email, String password) {
		if (this.email.equals(email.trim()) && this.password.equals(password.trim())) {
			return true;
		}
		return false;
	}
	


	@Override
	public int hashCode() {
		return this.email.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return this.equals(obj);
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
