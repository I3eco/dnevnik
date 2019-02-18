package bg.dnevnik;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import bg.dnevnik.Article.Comment;
import bg.dnevnik.Article.CommentMood;
import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;
import bg.dnevnik.utility.ContentGenerator;
import bg.dnevnik.utility.Logger;
import bg.dnevnik.utility.Validation;

public class User {

	public static class Author extends User {

		private Author(String name, String email, String password) throws IncorrectInputException {
			super(name, email, password);
		}

		public void writeArticle(String title, String category, String content, Collection<String> keywords) {

			try {
				new Article(this, category, title, content, keywords);
			} catch (IncorrectInputException e) {
				System.err.println("Incorrect input, could not write article!");
			}
		}

		public void editArticle(Article article, String content) {
			article.setContent(content);
		}

		@Override
		public String getTypeOfUser() {
			return "Author";
		}

		@Override
		public void doRandomAction() {
			if (new Random().nextBoolean()) {
				super.doRandomAction();
			} else {
				writeArticle(ContentGenerator.generateContent(50), ContentGenerator.getRandomCategory(),
						ContentGenerator.generateContent(400), ContentGenerator.getRandomKeywords());
			}
		}

	}

	public static class Admin extends Author {

		private Admin(String name, String email, String password) throws IncorrectInputException {
			super(name, email, password);
		}

		public void makeUserAuthor(User user) {
			Site site = Site.getInstance();
			Author author;
			try {
				String name = user.name;
				String email = user.email;
				String password = user.password;
				Site.getInstance().removeUser(user);
				author = new Author(name, email, password);
				site.addAuthor(author);
			} catch (IncorrectInputException e) {
				e.printStackTrace();
			}
		}

		public void makeUserAuthorOrAdmin(String email, boolean isMakeAdmin) throws IncorrectInputException {
			Site site = Site.getInstance();
			User user = null;
			try {
				user = site.getUserOrAuthor(email, isMakeAdmin);
			} catch (UserDoesNotExistException e) {
				System.out.println("No such user!");
				return;
			}
			String name = user.name;
			String password = user.password;

			if (!isMakeAdmin && user.getTypeOfUser().equals("User")) {
				Site.getInstance().removeUser(user, "User");
				Author author = new Author(name, email, password);
				site.addAuthor(author);
			}
			if (isMakeAdmin) {
				if (user.getTypeOfUser().equals("User")) {
					Site.getInstance().removeUser(user, "User");

				} else {
					Site.getInstance().removeUser(user, "Author");
				}
				Admin admin = new Admin(name, email, password);
				site.addAdmin(admin);
			}

		}

		public void makeUserAdmin(User user) throws IncorrectInputException {
			Site site = Site.getInstance();
			String name = user.name;
			String email = user.email;
			String password = user.password;
			Site.getInstance().removeUser(user);
			Admin admin = new Admin(name, email, password);
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

		@Override
		public String getTypeOfUser() {
			return "Admin";
		}

		@Override
		public void doRandomAction() {
			super.doRandomAction();
			makeUserAuthor(Site.getInstance().getRandomUser());
		}
	}

	private String name;
	private final String email;
	private String password;

	private transient boolean isOnline;

	private User(String name, String email, String password) throws IncorrectInputException {
		Validation.throwIfNull(name, email, password);
		Validation.throwIfNullOrEmpty(name, email, password);

		if (Site.getInstance().isUserInSite(email)) {
			throw new IncorrectInputException("User already exists!");
		}

		if (!email.trim().matches("[\\w-]+@([\\w-]+\\.)+[\\w-]+")) {
			throw new IncorrectInputException("Email is incorrect!");
		}

		this.email = email;
		this.name = name.trim();
		this.password = password.trim();

		try {
			Logger.printUserToFile(this, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createUser(String username, String email, String password, String rights) {
		rights = rights.trim().toLowerCase();

		try {
			switch (rights) {
			case "user":
				User user = new User(username, email, password);
				Site.getInstance().addUser(user);
				break;
			case "author":
				Author author = new Author(username, email, password);
				Site.getInstance().addAuthor(author);
				break;
			case "admin":
				Admin admin = new Admin(username, email, password);
				Site.getInstance().addAdmin(admin);
				break;

			default:
				System.err.println("The input for the rights is incorrect!");
				break;
			}
		} catch (IncorrectInputException e) {
			System.err.println(e.getMessage());
			System.err.println("Could not sign up!");
		}
	}

	public void writeComment(Article article, String content, Article.CommentMood mood) {
		try {
			article.new Comment(this, content, mood);
		} catch (IncorrectInputException e) {
			System.err.println("Could not create comment!");
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

	public void doRandomAction() {
		Random r = new Random();

		Article randomArticle = null;
		Comment randomComment = null;
		randomArticle = Site.getInstance().getRandomArticle();
		randomArticle.addView();

		if (randomArticle.getCommentsCount() > 0) {
			randomComment = randomArticle.getComment(r.nextInt(randomArticle.getCommentsCount()));
		}

		int chance = r.nextInt(5);
		switch (chance) {
		case 0:
			randomArticle.upvote(this);
			break;
		case 1:
			randomArticle.downvote(this);
			break;
		case 2:
			if (randomComment != null) {
				randomComment.upvote(this);
				break;
			}
		case 3:
			if (randomComment != null) {
				randomComment.downvote(this);
				break;
			}
		case 4:
			writeComment(randomArticle, ContentGenerator.generateContent(100), CommentMood.randomMood());
			break;
		default:
			break;
		}
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
		return this.getTypeOfUser() + ": name=" + name + ", email=" + email + ", isOnline=" + isOnline;
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
