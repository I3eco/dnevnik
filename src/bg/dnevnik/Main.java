package bg.dnevnik;

import java.util.Scanner;

import bg.dnevnik.exceptions.UserDoesNotExistException;

public class Main {

	public static void main(String[] args) {
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
		
		"show all categories" shows all created categories
		"show hot categories" shows the five categories with most articles
		"(category name)" shows all articles in this category 
		(this command should have lower priority over the others, 
		in case a category has the same name as one of the commands)
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
		+ "\n\"show all categories\" shows all created categories"
		+ "\n\"show hot categories\" shows the five categories with most articles"
		+ "\n\"(category name)\" shows all articles in this category "
		+ "\n(this command should have lower priority over the others, "
		+ "\nin case a category has the same name as one of the commands)"
		+ "\n\"show article\" requires an article to show it"
		+ "\n\"show comments\" requires the last command to have been \"show article\", "
		+ "\nand shows them ordered by date, from old to new"
		+ "\n"
		+ "\nThese don't require anything, because they use all articles"
		+ "\n\"show from today\" "
		+ "\n\"sort by new\""
		+ "\n\"sort by views\""
		+ "\n\"sort by comments\""
		+ "\n\"sort by votes\"";
		
		Scanner scanner = new Scanner(System.in);
		User currentUser = null;
		Article currentArticle = null;
		boolean running = true;
		
		while(running) {
			System.out.print("Enter a command, or 'help' for info: ");
			String input = scanner.nextLine();
			
			switch (input) {
			case "exit":
				running = false;
				break;
				
			case "help":
				System.out.println(commandInfo);
				break;
				
			case "sign up": {
				System.out.print("Username: ");
				String username = scanner.nextLine();
				System.out.print("Email: ");
				String email = scanner.nextLine();
				System.out.print("Password: ");
				String password = scanner.nextLine();
				User.signUp(username, email, password);
				} break;
				
			case "sign in":
				System.out.print("Email: ");
				String email = scanner.nextLine();
				System.out.print("Password: ");
				String password = scanner.nextLine();
				try {
					currentUser = Site.getInstance().signIn(email, password);
				} catch (UserDoesNotExistException e) {
					e.printStackTrace();
				}
				break;
				
			case "sign out":
				if (currentUser != null) {
					currentUser.goOffline();
					currentUser = null;
				}
				else System.out.println("You are not signed in");
				
				break;
				
			case "comment":
				if (currentUser == null) {
					System.out.println("You are not signed in!");
					break;
				}
				
				System.out.print("Message: ");
				String content = scanner.nextLine();
				System.out.print("Mood (neutral/cheerful/curious/sad/angry): ");
				Article.CommentMood mood = null;
				
				while(true) {
					boolean caughtException = false;
					try { 
						mood = Article.CommentMood.valueOf(scanner.nextLine().trim().toUpperCase()); 
					}
					
					catch (IllegalArgumentException e) {
						System.out.println("That mood does not exist!");
						caughtException = true;
					}
					
					if (!caughtException) {
						break;
					}
				}
				currentArticle.addComment(currentUser, content, mood);
				break;
				
			default:
				System.out.println("That command does not exist");
				break;
			}
			System.out.println();
		}
		
		scanner.close();
	}

}
