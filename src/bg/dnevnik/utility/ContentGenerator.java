package bg.dnevnik.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import bg.dnevnik.Site;
import bg.dnevnik.User;
import bg.dnevnik.User.Admin;
import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class ContentGenerator {

	private static final List<String> categories = Arrays.asList(""); // TODO add categories
	private final int articleCount;
	private final int userCount;
	private static final Random rnd = new Random();
	private static final List<String> NAMES = Arrays.asList(""); // TODO add names

	public ContentGenerator(int articleCount, int userCount) {
		this.articleCount = articleCount;
		this.userCount = userCount;
	}

	public void start() {
		// DO ONE OF THESE AT RANDOM:
		// create users, who randomly can write comments, view articles, vote on posts
		// have the main admin make someone an author

		// use one of the actions above randomly, depending on type of user

		for (int count = 0; count < userCount; count++) {
			try {
				User user = generateRandomUser();

				if (rnd.nextInt(100) < 30) {
					User admin = Site.getInstance().signIn("veso@gmail.com", "nekazvam");
					if (admin instanceof Admin) {
						((Admin) admin).makeUserAdmin(user);
					}
					else {
						System.err.println("veso@gmail.com IS NOT AN ADMIN?! Aborted!");
						break;
					}
				} 
			}
			// TODO temporary, until tested
			catch (UserDoesNotExistException e) {
				e.printStackTrace(); 
			}
			catch (IncorrectInputException e) {
				e.printStackTrace();
			}
		}

		while(Site.getInstance().getArticleCount() < articleCount) {
			Site.getInstance().getRandomUser().doRandomAction();
		}

	}

	private User generateRandomUser() throws UserDoesNotExistException {
		String username = getRandomName();

		if (rnd.nextInt(100) < 30) {
			username += "." + getRandomName();
		}

		if (rnd.nextInt(100) < 30) {
			username += rnd.nextInt(100);
		}

		String email = username + rnd.nextInt(100) + "@gmail.com";
		String password = username.substring(0, username.length() / 2) + rnd.nextInt(300);
		Site.getInstance().signUp(username, email, password);
		return Site.getInstance().signIn(email, password);
	}

	private String getRandomName() {
		return NAMES.get(rnd.nextInt(NAMES.size()));
	}

	public static String generateCommentContent() {
		// TODO make random text
		return "";
	}

	public static String getRandomCategory() {
		return categories.get(rnd.nextInt(categories.size()));
	}

	public static String getRandomTitle() {
		// TODO make random title
		return "";
	}

	public static String getRandomContent() {
		// TODO get random content
		return "";
	}

	public static Collection<String> getRandomKeywords() {
		// TODO 
		return null;
	}

}
