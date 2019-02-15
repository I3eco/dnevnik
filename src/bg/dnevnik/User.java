package bg.dnevnik;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import bg.dnevnik.Article.Comment;
import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.utility.Logger;
import bg.dnevnik.utility.Validation;

public class User {
	
	public static class Author extends User {
		private Collection<Article> writtenArticles;
		
		private Author(String name, String email, String password) throws IncorrectInputException {
			super(name, email, password);
			this.writtenArticles = new ArrayList<Article>();
		}
		
		public void writeArticle(String title, String category, String content, Collection<String> keywords) {

			Article article;
			try {
				article = new Article(this, category, title, content, keywords);
				this.writtenArticles.add(article);
			} 
			catch (IncorrectInputException e) {
				System.err.println("Incorrect input, could not write article!");
			}
			
		}
		
		public void editArticle(Article article, String content) {
			try {
				article = Site.getInstance().getArticleByID(article.getID());
			} catch (NoSuchArticleException e) {
				System.out.println("No such article");
			}
			article.setContent(content);
		}
		
			@Override
		public String getTypeOfUser() {
				return "Author";
			}
	}
	
	public static class Admin extends Author {
		private Admin(String name, String email, String password) throws IncorrectInputException {
			super(name, email, password);
		}
		
		public void makeUserAuthor (User user) throws IncorrectInputException {
			Site site = Site.getInstance();
			Author author = new Author(user.name, user.email, user.password);			
			site.addUser(author);
		}
		
		public void makeUserAdmin (User user) throws IncorrectInputException {
			Site site = Site.getInstance();
			Admin admin = new Admin(user.name, user.email, user.password);			
			site.addUser(admin);
		}
		
		public void editArticle(Article article, String content) {
			try {
				article = Site.getInstance().getArticleByID(article.getID());
			} catch (NoSuchArticleException e) {
				System.out.println("No such article");
			}
			article.setContent(content);
		}
		
		public void deleteArticle() {
			
		}
		
		@Override
		public String getTypeOfUser() {
			return "Admin";
		}
		
	}
	
	private String name;
	private final String email;
	private String password;
	private Collection<Article.Comment> commentHistory;

	// TODO this is just an idea, but instead of a boolean we could add a state enum ONLINE/OFFLINE/AWAY,
	// and have a thread loop through all users, and if they haven't done anything in a few minutes,
	// it sets their state to AWAY
	private boolean isOnline;
	
	private User(String name, String email, String password) throws IncorrectInputException {
		Validation.throwIfNull(name, email, password);
		Validation.throwIfNullOrEmpty(name, email, password);
		
		if(Site.getInstance().isUserInSite(email)) {
			IncorrectInputException e = new IncorrectInputException ("User already exist!");
			System.out.println(e.getMessage());
			throw e;
		}
		
		if (!email.trim().matches("[\\w-]+@([\\w-]+\\.)+[\\w-]+")) {
			throw new IncorrectInputException("Email is incorrect!");
		}
		
		this.email = email;
		this.name = name.trim();
		this.password = password.trim();
		this.commentHistory = new LinkedList<Comment>();
		Logger.pritnToConsole(this.getTypeOfUser() + " with name: " + this.getName() + " and email: " + this.getEmail() + " was created.");
	}
	
	public static void createUser(String username, String email, String password, String rights) {
		rights = rights.trim().toLowerCase();
		User user = null;
		
		try {
			switch (rights) {
				case "user": user = new User(username, email, password); break;
				case "author": user = new Author(username, email, password); break;
				case "admin": user = new Admin(username, email, password); break;
				
				default: System.err.println("The input for the rights is incorrect!"); break;
			}
			Site.getInstance().addUser(user);
		} 
		catch (IncorrectInputException e) {
			System.err.println("Could not sign up!");
		}
	}
	
	public void writeComment(Article article, String content, Article.CommentMood mood) {
		// just like writeArticle() is in Author, it would make sense for writeComment to be in User too
		Comment comment = null;
		try {
			comment = article.new Comment(this, content, mood);
			this.addToCommentHistory(comment);
		} 
		catch (IncorrectInputException e) {
			System.err.println("Could not create comment!");
		}
	}
	
	public void addToCommentHistory(Article.Comment comment) {
		if (comment != null) {
			this.commentHistory.add(comment);
		}
	}

	public boolean loginInfoMatches(String email, String password) {
		if (email == null || password == null) {
			return false;
		}
		if (this.email.equals(email.trim()) && this.password.equals(password.trim())) {
			return true;
		}
		return false;
	}
	
	public boolean validatePassword (String password) {
		if (this.password.equals(password)) {
			return true;
		}
		return false;
	}
	
	public String getTypeOfUser() {
		return "User";
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
		User other = (User) obj;
		return this.email.equals(other.email);
	}

	@Override
	public String toString() {
		return "USER: name=" + name + ", email=" + email + ", isOnline=" + isOnline;
	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return email;
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
