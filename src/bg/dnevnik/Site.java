package bg.dnevnik;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.UserDoesNotExistException;
import bg.dnevnik.exceptions.WrongInputException;

public class Site {

	private static Site instance;
	private String name;
	private Collection<User> users;
	private Map<String, Collection<Article>> articlesByCategory;

	static {
		// static block added for the possibility of adding additional logic later

		// Also, this design is preferred right now over lazy initialization,
		// to avoid problems with multithreaded calls to getInstance() and
		// synchronization
		// The Site object shouldn't be heavy, since all the data gets added to it after
		// anyway
		// Source:
		// https://www.ibm.com/developerworks/library/j-dcl/index.html

		// The other approach would be to just make all fields and methods static,
		// and have no instances at all

		Site.instance = new Site();
	}

	private Site() {
		// TODO decide on the correct collections
		this.users = new HashSet<User>();
		this.articlesByCategory = new HashMap<String, Collection<Article>>();
	}

	public static Site getInstance() {
		return Site.instance;
	}

	public void addUser(User user) {
		users.add(user);
	}

	public User signIn(String email, String password) throws UserDoesNotExistException {
		for (User user : users) {
			if (user.loginInfoMatches(email, password)) {
				user.goOnline();
				return user;
			}
		}
		throw new UserDoesNotExistException("There is no user with that email or password!");
	}

	public void addArticle(Author author, String title, String category, String content, Collection<String> keywords)
			throws WrongInputException {
		Article article = new Article(author, title, category, content, keywords);
		if (!this.articlesByCategory.containsKey(title)) {
			this.articlesByCategory.put(title, new HashSet<Article>());
		}
		this.articlesByCategory.get(title).add(article);
	}

	public String getName() {
		return this.name;

	}

}
