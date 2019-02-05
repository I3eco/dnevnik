package bg.dnevnik;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class ConsoleCommandsView {
	
	private Scanner scanner;
	private User currentUser;
	private Article currentArticle;
	
	public ConsoleCommandsView() {
		this.scanner = new Scanner(System.in);
	}
	
	public void start() {
		// Even though it's repetitive,
		// all of the methods have 'command' in their name to avoid confusion
		// with the actual methods that they call, such as signUp()
		
		boolean running = true;
		while(running) {
			try {
				// because system.err does some strange buffer magic, messages appear out of order sometimes
				// and this is so there's a higher chance to have them in the correct order
				Thread.sleep(100); 
			} catch (InterruptedException e) {
				return;
			}
			System.out.print("Enter a command, or write 'commands' for info: ");
			String input = scanner.nextLine().trim().toLowerCase();
			
			switch (input) {
				case "exit": running = false; break;
				case "commands": showCommands(); break;
				case "sign up": signUpCommand(); break;
				case "sign in": signInCommand(); break;
				case "sign out": signOutCommand(); break;
				case "write comment": writeCommentCommand(); break;
				case "write article": writeArticleCommand(); break;
				case "show categories": Site.getInstance().showCategories(); break;
				case "show article": showArticleCommand(); break;
				case "show category": showCategoryCommand(); break;
				case "upvote article": upvoteArticleCommand(); break;
				case "downvote article": downvoteArticleCommand(); break;
				case "show top categories": showTopCategoriesCommand(); break;
				case "show from today": Site.getInstance().showFromToday(); break;
				
				default: 
				
					System.err.println("That command does not exist!"); break;
			}
			System.out.println("\n____________________________________");
		}
	}
	
	private void showTopCategoriesCommand() {
		System.out.print("How many: ");
		int numOfCategories = 0;
		try {
			numOfCategories = Integer.parseInt(this.scanner.nextLine().trim());
		}
		catch (NumberFormatException e) {
			System.err.println("Not a number, try again!");
			return;
		}
		Site.getInstance().showTopCategories(numOfCategories);
		
	}

	private void downvoteArticleCommand() {
		if (!this.isSignedIn() || !this.hasOpenedArticle()) {
			return;
		}
		
		this.currentArticle.downvote();		
	}

	private void upvoteArticleCommand() {
		if (!this.isSignedIn() || !this.hasOpenedArticle()) {
			return;
		}
		
		this.currentArticle.upvote();
	}
	
	private void showArticleCommand() {
		System.out.print("Article ID: ");
		int id = 0;
		
		try {
			id = Integer.parseInt(this.scanner.nextLine().trim());
		}
		catch (NumberFormatException e) {
			System.err.println("Invalid ID, try again!");
			return;
		}
		
		try {
			this.currentArticle = Site.getInstance().getArticleByID(id);
		} 
		catch (NoSuchArticleException e) {
			System.err.println("An article with ID " + id + " does not exist!");
			return;
		}
		System.out.println(this.currentArticle.show());
	}

	private void showCategoryCommand() {
		System.out.print("Category name: ");
		String category = this.scanner.nextLine().trim();
		Site.getInstance().showCategory(category);
	}

	private void writeArticleCommand() {
		// This could have been done with polymorphism, instead of casting,
		// by adding an empty writeArticle() in user.
		// But the problem with that is that you will learn that you don't have the rights
		// only after you write the whole article, which will be annoying.
		// So instead I just put an instanceof check before calling writeArticle()
		if (!this.isSignedIn()) {
			return;
		}
		
		if (!(this.currentUser instanceof User.Author)) {
			System.err.println("Only authors have the right to write articles!");
			return;
		}
		
		System.out.print("Title: ");
		String title = scanner.nextLine();
		System.out.print("Category: ");
		String category = scanner.nextLine();
		System.out.print("Content: ");
		String content = scanner.nextLine();
		
		List<String> keywords = new LinkedList<String>();
		System.out.println("(Enter nothing when you are done with all keywords)");
		
		while (true) {
			System.out.print("Keyword: ");
			String keywordInput = this.scanner.nextLine().trim().toLowerCase();
			if (keywordInput.equals("")) {
				break;
			}
			
			keywords.add(keywordInput);
		}
		
		User.Author author = (User.Author) this.currentUser;
		author.writeArticle(title, category, content, keywords);
	}

	private void signUpCommand() {
		System.out.print("Username: ");
		String username = scanner.nextLine();
		System.out.print("Email: ");
		String email = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();
			
		Site.getInstance().sighUp(username, email, password);
	}
	
	private void signInCommand() {
		System.out.print("Email: ");
		String email = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();
		
		try {
			this.currentUser = Site.getInstance().signIn(email, password);
		} 
		catch (UserDoesNotExistException e) {
			System.err.println("User does not exist with that email or password!");
		}
	}
	
	private void signOutCommand() {
		if (this.isSignedIn()) {
			this.currentUser = null;
			this.currentUser.goOffline();
		}
	}
	
	private void writeCommentCommand() {
		if (!this.isSignedIn() || !this.hasOpenedArticle()) {
			return;
		}
		
		System.out.print("Message: ");
		String content = scanner.nextLine();
		System.out.print("Mood (neutral/cheerful/curious/sad/angry): ");
		Article.CommentMood mood = null;
		
		boolean inputIsIncorrent = true;
		while(inputIsIncorrent) {
			try { 
				String moodInput = scanner.nextLine().trim().toUpperCase();
				mood = Article.CommentMood.valueOf(moodInput); 
				this.currentUser.writeComment(this.currentArticle, content, mood);
				inputIsIncorrent = false;
			}
			catch (IllegalArgumentException e) {
				System.err.println("That mood does not exist!");
			}
		}
		
	}
	
	private void showCommands() {
		/* commands:
		"commands" 
		"exit"
		"sign up" 
		"sign in" 
		"sign out"
		
		"upvote comment" requires comment id
		"downvote comment" requires comment id
		"downvote article"
		"upvote article" 
		"write comment"
		"write article"
		
		"show categories"
		"show top categories" shows the five categories with most articles
		"show category"
		"show article"
		"show comments" requires the last command to have been "show article", 
		and shows them ordered by date, from old to new
		
		These don't require anything, because they use all articles
		"show from today" 
		"sort by new"
		"sort by views"
		"sort by comments"
		"sort by votes"
		*/
		String commandInfo = null;
		System.out.println(commandInfo);
	}
	
	private boolean hasOpenedArticle() {
		if (this.currentArticle == null) {
			System.err.println("There is no opened article!");
			return false;
		}
		return true;
	}

	private boolean isSignedIn() {
		if (this.currentUser == null) {
			System.err.println("You are not signed in!");
			return false;
		}
		return true;
	}
	
}
