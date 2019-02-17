package bg.dnevnik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import bg.dnevnik.Article.Comment;
import bg.dnevnik.Article.CommentMood;
import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.utility.ContentGenerator;
import bg.dnevnik.utility.Logger;
import bg.dnevnik.utility.Validation;

public class User {
	
	public static class Author extends User {
		private transient Collection<Article> writtenArticles;
		
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
//			try {
//				JsonDataHolder.saveUserToJson(this);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}

		public void editArticle(Article article, String content) {
			try {
				article = Site.getInstance().getArticleByID(article.getID());
			} catch (NoSuchArticleException e) {
				System.out.println("No such article");
			}
			article.setContent(content);
		}
		
		public void removeArticle(Article article) {
			this.writtenArticles.remove(article);
		}
		
			@Override
		public String getTypeOfUser() {
				return "Author";
		}
			
		@Override
		public String toString() {
			return super.toString() + ", articles: " + this.writtenArticles;
		}
	
		public void doRandomAction() {
			if (new Random().nextBoolean()) {
				super.doRandomAction();
			}
			else {
				writeArticle(ContentGenerator.getRandomTitle(), 
						ContentGenerator.getRandomCategory(), 
						ContentGenerator.getRandomContent(), 
						ContentGenerator.getRandomKeywords());
			}
		}
	
	}
	
	public static class Admin extends Author {
		private Admin(String name, String email, String password) throws IncorrectInputException {
			super(name, email, password);
		}
		
		public void makeUserAuthor (User user) {
			Site site = Site.getInstance();
			Author author;
			try {
				author = new Author(user.name, user.email, user.password);
				site.addUser(author);
			}
			catch (IncorrectInputException e) {
			}			
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
		
		public void deleteArticle(Article article) {
			Site site = Site.getInstance();
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter your password:");
			String password = sc.nextLine().trim();
			site.removeArticle(this, password, article);
			sc.close();
		}
		
		@Override
		public String getTypeOfUser() {
			return "Admin";
		}
		
		public void doRandomAction() {
			if (new Random().nextBoolean()) {
				super.doRandomAction();
			}
			else {
				makeUserAuthor(Site.getInstance().getRandomUser());
			}
		}
	}
	
	private String name;
	private final String email;
	private String password;
	private transient Collection<Article.Comment> commentHistory;

	private transient boolean isOnline;
	
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
		
		try {
			Logger.printUserToFile(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		try {
//			Logger.printUserToFile(this);
//			JsonDataHolder.saveUserToJson(this);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
	
	public static void createUser(String username, String email, String password, String rights) {
		rights = rights.trim().toLowerCase();
//		User user = null;
		
		try {
			switch (rights) {
				case "user": User user = new User(username, email, password); Site.getInstance().addUser(user); break;
				case "author":Author author = new Author(username, email, password); Site.getInstance().addAuthor(author); break;
				case "admin": Admin admin = new Admin(username, email, password); Site.getInstance().addAdmin(admin); break;
				
				default: System.err.println("The input for the rights is incorrect!"); break;
			}
		} 
		catch (IncorrectInputException e) {
			System.err.println(e.getMessage());
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
//		try {
//			JsonDataHolder.saveUserToJson(this);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	
	public void doRandomAction() {
		// TODO probably temporary, just for generation
		Random r = new Random();
		
		Article randomArticle = null;
		Comment randomComment = null;
		try {
			randomArticle = Site.getInstance().getArticleByID(r.nextInt(Site.getInstance().getArticleCount()));
			randomComment = randomArticle.getComment(r.nextInt(randomArticle.getCommentsCount()));
		}
		catch (NoSuchArticleException e) {
			e.printStackTrace();
			return;
		}
		
		int chance = r.nextInt(4);
		switch(chance) {
			case 1: case 7: case 8: case 9: randomArticle.addView();
			case 2: randomArticle.upvote(); break;
			case 3: randomArticle.downvote(); break;
			case 4: randomComment.upvote(); break;
			case 5: randomComment.downvote(); break;
			case 6: writeComment(randomArticle, ContentGenerator.generateCommentContent(), CommentMood.randomMood()); break;
			default: break;
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
