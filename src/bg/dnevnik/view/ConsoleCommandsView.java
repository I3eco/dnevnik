package bg.dnevnik.view;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import bg.dnevnik.Article;
import bg.dnevnik.Post;
import bg.dnevnik.Site;
import bg.dnevnik.User;
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
				System.err.println("Console view interrupted!");
				return;
			}
			System.out.print("Enter a command, or write 'commands' for info: ");
			String input = scanner.nextLine().trim().toLowerCase();

			switch(input) {
				case "commands": showCommands(); break;
				
				case "sign up": signUpCommand(); break;
				case "sign in": signInCommand(); break;
				case "sign out": signOutCommand(); break;
				
				case "show categories": Site.getInstance().showCategories(); break;
				case "show top categories": showTopCategoriesCommand(); break;
				case "show category": showCategoryCommand(); break;

				case "write article": writeArticleCommand(); break;
				case "show article": findArticleAndThen(Article::show); break;
				case "upvote article": upvoteArticleCommand(); break;
				case "downvote article": downvoteArticleCommand(); break;

				case "write comment": writeCommentCommand(); break;
				case "show comments": showCommentsCommand(); break;
				case "downvote comment": downvoteCommentCommand(); break;
				case "upvote comment": upvoteCommentCommand(); break;

				case "show from today": Site.getInstance().showFromToday(); break;
				case "sort by date": sortArticlesByFilter(new ArticleComparatorByDate()); break;
				case "sort by views": sortArticlesByFilter(new ArticleComparatorByViews()); break;
				case "sort by comments": sortArticlesByFilter(new ArticleComparatorByComments()); break;
				case "sort by rating": sortArticlesByFilter(new ArticleComparatorByRating()); break;

				case "exit": running = false; break;

				default: System.err.println("That command does not exist!"); break;
			}
			System.out.println("\n____________________________________");
		}
	}
	
	private static void sortArticlesByFilter(Comparator<Article> comparator) {
		System.out.print("How many articles: ");
		try {
			Site.getInstance().showArticlesByFilter(comparator, Validation.readInt());
		} 
		catch (IncorrectInputException e) {
			System.err.println("Not a number, try again!");
			return;
		}
		
	}

	private void upvoteCommentCommand() {
		// TODO
	}

	private void downvoteCommentCommand() {
		// TODO
	}

	private static void showCommentsCommand() {
		findArticleAndThen(Article::showComments);
	}

	private static void showTopCategoriesCommand() {
		System.out.print("How many: ");

		try {
			Site.getInstance().showHotCategories(Validation.readInt());
		}
		catch (IncorrectInputException e) {
			System.err.println("Invalid number, try again!");
		}
	}

	private void downvoteArticleCommand() {
		if (!isSignedIn()) {
			return;
		}
		findArticleAndThen(Post::downvote, currentUser);
	}

	private void upvoteArticleCommand() {
		if (!isSignedIn()) {
			return;
		}
		findArticleAndThen(Post::upvote, currentUser);
	}

	private void showCategoryCommand() {
		System.out.print("Category name: ");
		String category = scanner.nextLine().trim();
		Site.getInstance().showCategory(category);
	}

	private void writeArticleCommand() {
		if (!isSignedIn()) {
			return;
		}

		if (!(currentUser instanceof User.Author)) {
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
			String keywordInput = scanner.nextLine().trim().toLowerCase();
			if (keywordInput.equals("")) {
				break;
			}

			keywords.add(keywordInput);
		}

		User.Author author = (User.Author) currentUser;
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
		signOutCommand();
		System.out.print("Email: ");
		String email = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();

		try {
			currentUser = Site.getInstance().signIn(email, password);
		}
		catch (UserDoesNotExistException e) {
			System.err.println("User does not exist with that email or password!");
		}
	}

	private void signOutCommand() {
		if (currentUser != null) {
			currentUser.goOffline();
			currentUser = null;
		}
	}

	private void writeCommentCommand() {
		if (!isSignedIn()) {
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
					currentUser.writeComment(article, content, mood);
					inputIsIncorrent = false;
				}
				catch (IllegalArgumentException e) {
					System.err.println("That mood does not exist!");
				}
			}
		});
	}

	private static void showCommands() {
		//commands: 
		//'sign up' 
		//'sign in' 
		//'sign out'
		//
		//'show categories'
		//'show top categories'
		//'show category'
		//
		//'write article'
		//'show article'
		//'upvote/downvote article'
		//
		//'write comment'
		//'show comments'
		//'upvote/downvote comment'
		//
		//'sort by new' 
		//'sort by views' 
		//'sort by comments' 
		//'sort by votes'
		//
		// 'show from today'
		//
		//'exit' 

		String commandInfo = 
			"commands: \r\n" + 
			"'sign up' \r\n" + 
			"'sign in' \r\n" + 
			"'sign out'\r\n" + 
			"\r\n" + 
			"'show categories'\r\n" + 
			"'show top categories'\r\n" + 
			"'show category'\r\n" + 
			"\r\n" + 
			"'write article'\r\n" + 
			"'show article'\r\n" + 
			"'upvote/downvote article'\r\n" + 
			"\r\n" + 
			"'write comment'\r\n" + 
			"'show comments'\r\n" + 
			"'upvote/downvote comment'\r\n" + 
			"\r\n" + 
			"'sort by new' \r\n" + 
			"'sort by views' \r\n" + 
			"'sort by comments' \r\n" + 
			"'sort by votes'\r\n" + 
			"\r\n" + 
			"'exit' ";
		System.out.println(commandInfo);
	}
	
	private static void findArticleAndThen(Consumer<Article> action) {
		System.out.print("Choose an article by ID: ");

		try {
			action.accept(Site.getInstance().getArticleByID(Validation.readInt()));
		}
		catch (NoSuchArticleException e) {
			System.err.println("An article with that ID does not exist!");
			return;
		}
		catch (IncorrectInputException e) {
			System.err.println("Not a number, try again!");
			return;		
		}
	}
	
	private static void findArticleAndThen(BiConsumer<Post, User> action, User user) {
		System.out.print("Choose an article by ID: ");

		try {
			action.accept(Site.getInstance().getArticleByID(Validation.readInt()), user);
		}
		catch (NoSuchArticleException e) {
			System.err.println("An article with that ID does not exist!");
			return;
		}
		catch (IncorrectInputException e) {
			System.err.println("Not a number, try again!");
			return;		
		}
	}
	
	

	private boolean isSignedIn() {
		if (currentUser == null) {
			System.err.println("You are not signed in!");
			return false;
		}
		return true;
	}

}
