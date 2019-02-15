package bg.dnevnik;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;
import bg.dnevnik.utility.Validation;

public class ConsoleCommandsView {

	private Scanner scanner;
	private User currentUser;

	public ConsoleCommandsView() {
		scanner = new Scanner(System.in);
	}

	public void start() {
		boolean running = true;
		while (running) {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				return;
			}
			System.out.print("Enter a command, or write 'commands' for info: ");
			String input = scanner.nextLine().trim().toLowerCase();

			switch(input) {
				case "exit":
					running = false;
					break;
				case "commands":
					showCommands();
					break;
				case "sign up":
					signUpCommand();
					break;
				case "sign in":
					signInCommand();
					break;
				case "sign out":
					signOutCommand();
					break;
				case "write comment":
					writeCommentCommand();
					break;
				case "write article":
					writeArticleCommand();
					break;
				case "show categories":
					Site.getInstance().showCategories();
					break;
				case "show article":
					findArticleAndThen(Article::show);
					break;
				case "show category":
					showCategoryCommand();
					break;
				case "upvote article":
					upvoteArticleCommand();
					break;
				case "downvote article":
					downvoteArticleCommand();
					break;
				case "show top categories":
					showTopCategoriesCommand();
					break;
				case "show from today":
					Site.getInstance().showFromToday();
					break;
				case "show comments":
					showCommentsCommand();
					break;

				default:

					System.err.println("That command does not exist!");
					break;
			}
			System.out.println("\n____________________________________");
		}
	}

	private void showCommentsCommand() {
		findArticleAndThen(Article::showComments);
	}

	private void showTopCategoriesCommand() {
		System.out.print("How many: ");

		try {
			Site.getInstance().showTopCategories(Validation.readInt());
		}
		catch (IncorrectInputException e) {
			System.err.println("Invalid number, try again!");
		}
	}

	private void downvoteArticleCommand() {
		if (!this.isSignedIn()) {
			return;
		}
		findArticleAndThen(Article::downvote);
	}

	private void upvoteArticleCommand() {
		if (!this.isSignedIn()) {
			return;
		}
		findArticleAndThen(Article::upvote);
	}

	private void showCategoryCommand() {
		System.out.print("Category name: ");
		String category = this.scanner.nextLine().trim();
		Site.getInstance().showCategory(category);
	}

	private void writeArticleCommand() {
		if (!this.isSignedIn()) {
			return;
		}

		// This could have been done with polymorphism, instead of casting,
		// by adding an empty writeArticle() in user.
		// But the problem with that is that you will learn that you don't have the rights
		// only after you write the whole article, which will be annoying.
		// So instead I just put an instanceof check before calling writeArticle()
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

		Site.getInstance().signUp(username, email, password);
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
		if (!this.isSignedIn()) {
			return;
		}

		findArticleAndThen((article) -> {
			System.out.print("Message: ");
			String content = scanner.nextLine();
			System.out.print("Mood (neutral/cheerful/curious/sad/angry): ");
			Article.CommentMood mood = null;

			boolean inputIsIncorrent = true;
			while (inputIsIncorrent) {
				try {
					String moodInput = scanner.nextLine().trim().toUpperCase();
					mood = Article.CommentMood.valueOf(moodInput);
					this.currentUser.writeComment(article, content, mood);
					inputIsIncorrent = false;
				}
				catch (IllegalArgumentException e) {
					System.err.println("That mood does not exist!");
				}
			}
		});
	}

	private void showCommands() {
		/*
		 * commands: 'exit' 'sign up' 'sign in' 'sign out'
		 * 
		 * 'upvote comment' requires comment id 'downvote comment' requires comment id
		 * 'downvote article' 'upvote article' 'write comment' 'write article'
		 * 
		 * 'show categories' 'show top categories' shows the five categories with most
		 * articles 'show category' 'show article' 'show comments' requires the last
		 * command to have been 'show article', and shows them ordered by date, from old
		 * to new
		 * 
		 * These don't require anything, because they use all articles 'show from today'
		 * 'sort by new' 'sort by views' 'sort by comments' 'sort by votes'
		 */

		String commandInfo = "'exit'\r\n" + "'sign up' \r\n" + "'sign in' \r\n" + "'sign out'\r\n" + "\r\n"
				+ "'upvote comment' requires comment id\r\n" + "'downvote comment' requires comment id\r\n"
				+ "'downvote article'\r\n" + "'upvote article' \r\n" + "'write comment'\r\n" + "'write article'\r\n"
				+ "\r\n" + "'show categories'\r\n"
				+ "'show top categories' shows the five categories with most articles\r\n" + "'show category'\r\n"
				+ "'show article'\r\n" + "'show comments' requires the last command to have been 'show article', \r\n"
				+ "and shows them ordered by date, from old to new\r\n" + "\r\n"
				+ "These don't require anything, because they use all articles\r\n" + "'show from today' \r\n"
				+ "'sort by new'\r\n" + "'sort by views'\r\n" + "'sort by comments'\r\n" + "'sort by votes'";
		System.out.println(commandInfo);
	}
	
	private void findArticleAndThen(Consumer<Article> action) {
		System.out.print("Choose an article by ID: ");

		try {
			action.accept(Site.getInstance().getArticleByID(Validation.readInt()));
		}
		catch (NoSuchArticleException e) {
			System.err.println("An article with that ID does not exist!");
			return;
		}
		catch (IncorrectInputException e) {
			System.err.println("Invalid input, try again!");
			return;		
		}
	}

	private boolean isSignedIn() {
		if (this.currentUser == null) {
			System.err.println("You are not signed in!");
			return false;
		}
		return true;
	}

}
