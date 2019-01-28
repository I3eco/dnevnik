package bg.dnevnik;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.UserDoesNotExistException;
import bg.dnevnik.exceptions.WrongInputException;

public class Site {
	private static Site instance;
	private String name;
	private Collection<User> users;
	private Map<String, Collection<Article>> articlesByCategory;

	static {
		Site.instance = new Site();
	}

	private Site() {
		this.users = new HashSet<User>();
		this.articlesByCategory = new ConcurrentHashMap<String, Collection<Article>>();
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
