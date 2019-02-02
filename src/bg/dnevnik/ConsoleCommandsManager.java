package bg.dnevnik;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import bg.dnevnik.exceptions.UserDoesNotExistException;

public class ConsoleCommandsManager {
	
	private Scanner scanner;
	private User currentUser;
	private Article currentArticle;
	
	public ConsoleCommandsManager() {
		this.scanner = new Scanner(System.in);
	}
	
	public void start() {
		boolean running = true;
		
		// Even though it's repetitive,
		// all of the methods have 'command' in their name to avoid confusion
		// with the actual methods that they call, such as signUp()
		
		while(running) {
			System.out.print("Enter a command, or 'help' for info: ");
			String input = scanner.nextLine().trim().toLowerCase();
			
			switch (input) {
				case "exit": running = false; break;
				case "help": showHelpCommand(); break;
				case "sign up": signUpCommand(); break;
				case "sign in": signInCommand(); break;
				case "sign out": signOutCommand(); break;
				case "comment": commentCommand(); break;
				case "write article": writeArticleCommand(); break;
				case "show categories": Site.getInstance().showCategories(); break;
				case "show category": showCategoryCommand(); break;
				
				default: System.err.println("That command does not exist"); break;
			}
			System.out.println();
		}
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
		if (this.currentUser == null) {
			System.err.println("You are not signed in!");
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
		System.out.print("Rights (user/author/admin): ");
		String rights = scanner.nextLine();
		
		User.signUp(username, email, password, rights);
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
		if (this.currentUser != null) {
			this.currentUser.goOffline();
			this.currentUser = null;
		}
		else System.out.println("You are not signed in");
	}
	
	private void commentCommand() {
		if (this.currentUser == null) {
			System.out.println("You are not signed in!");
			return;
		}
		
		System.out.print("Message: ");
		String content = scanner.nextLine();
		System.out.print("Mood (neutral/cheerful/curious/sad/angry): ");
		Article.CommentMood mood = null;
		
		while(true) {
			try { 
				String moodInput = scanner.nextLine().trim().toUpperCase();
				mood = Article.CommentMood.valueOf(moodInput); 
				this.currentUser.writeComment(this.currentArticle, content, mood);
			}
			catch (IllegalArgumentException e) {
				System.out.println("That mood does not exist!");
			}
		}
		
	}
	
	private void showHelpCommand() {
		/* These are some of the commands we could add, 
		they should be very helpful as a TODO list.
		It will also be a part of what the simulated users will do.
		They are all according to the website in terms of functionality
		
		The logic for the command management should probably be in the site class,
		it'll likely be here just temporarily
		
		commands:
		"help" shows this info about all commands
		
		"sign up" requires name, email, and password to create a user
		"sign in" requires email and password, allows user to make posts and vote
		"sign out" requires to be signed in, disables ability to make posts and vote
		
		"upvote"/"downvote" requires a post and votes on it
		"comment" requires article and content
		"write article" requires title, content, category, and keywords to create an article
		
		"show categories" shows all created categories
		"show hot categories" shows the five categories with most articles
		"show category" requires category name, to show all articles of that category
		"show article" requires an article to show it
		"show comments" requires the last command to have been "show article", 
		and shows them ordered by date, from old to new
		
		These don't require anything, because they use all articles
		"show from today" 
		"sort by new"
		"sort by views"
		"sort by comments"
		"sort by votes"
		*/
		String commandInfo = ""
			+ "\nCommands: "
			+ "\n\"help\" shows this info about all commands"
			+ "\n"
			+ "\n\"sign up\" requires name, email, and password to create a user"
			+ "\n\"sign in\" requires email and password, allows user to make posts and vote"
			+ "\n\"sign out\" requires to be signed in, disables ability to make posts and vote"
			+ "\n\"exit\" quits the program"
			+ "\n"
			+ "\n\"upvote\"/\"downvote\" requires a post and votes on it"
			+ "\n\"comment\" requires article and content"
			+ "\n\"write article\" requires title, content, category, and keywords to create an article"
			+ "\n"
			+ "\n\"show categories\" shows all created categories"
			+ "\n\"show hot categories\" shows the five categories with most articles"
			+ "\n\"show category\" requires category name, to show all articles of that category"
			+ "\n\"show article\" requires an article number to show it"
			+ "\n\"show comments\" requires the last command to have been \"show article\", "
			+ "\nand shows them ordered by date, from old to new"
			+ "\n"
			+ "\nThese don't require anything, because they use all articles"
			+ "\n\"show from today\" "
			+ "\n\"sort by new\""
			+ "\n\"sort by views\""
			+ "\n\"sort by comments\""
			+ "\n\"sort by votes\"";
		System.out.println(commandInfo);
	}
	

	
}
