package bg.dnevnik;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import bg.dnevnik.User.Admin;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class Site {
	private static Site instance;
	private String name;
	private Collection<User> users;
	private Map<String, Collection<Article>> articlesByCategory;

	static {
		Site.instance = new Site();
	}

	private Site() {
		this.name = "Dnevnik";
		this.users = new HashSet<User>();
		this.articlesByCategory = new ConcurrentHashMap<String, Collection<Article>>();
	}

	public static Site getInstance() {
		return Site.instance;
	}
	
	public static void createAdmin(String name, String email, String password) {
		User.createUser(name, email, password, "admin");
	}

	public void addUser(User user) {
		this.users.remove(user);
		this.users.add(user);
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

	public void addArticle(Article article, String category) {
		category = category.toUpperCase();
		if (!this.articlesByCategory.containsKey(category)) {
			this.articlesByCategory.put(category, new HashSet<Article>());
		}
		this.articlesByCategory.get(category).add(article);		
	}

	public void showCategories() {
		for (Entry<String, Collection<Article>> entry : this.articlesByCategory.entrySet()) {
			System.out.println(entry.getKey().toUpperCase() + " (" + entry.getValue().size() + " articles)");
		}
	}

	public void showCategory(String input) {
		boolean categoryFound = false;
		input = input.toUpperCase();
		for (Entry<String, Collection<Article>> entry : this.articlesByCategory.entrySet()) {
			if (entry.getKey().equals(input)) {
				categoryFound = true;
				for (Article article : entry.getValue()) {
					System.out.println(article.getSummary());
				}
				break;
			}
		}
		
		if (!categoryFound) {
			System.err.println("There is no category called " + input + "!");
		}
	}

	public Article getArticleByID(int id) throws NoSuchArticleException {
		for (Entry<String, Collection<Article>> entry : this.articlesByCategory.entrySet()) {
			for (Article article : entry.getValue()) {
				if (article.getID() == id) {
					return article;
				}
			}
		}
		throw new NoSuchArticleException();
	}
	
	public void showTopCategories(int numOfCategories) {
		List<Entry<String, Collection<Article>>> topCategories = new ArrayList<Entry<String, Collection<Article>>>();
		
		for (Entry<String, Collection<Article>> currentCategory : this.articlesByCategory.entrySet()) {
			if (topCategories.size() < numOfCategories) {
				topCategories.add(currentCategory);
			}
			else {
				for (Entry<String, Collection<Article>> topCategory : topCategories) {
					if (currentCategory.getValue().size() > topCategory.getValue().size()) {
						topCategories.remove(topCategory);
						topCategories.add(currentCategory);
					}
				}
			}
		}
		
		Collections.sort(topCategories, (a, b) -> a.getValue().size() - b.getValue().size());
		
		for (Entry<String, Collection<Article>> category : topCategories) {
			System.out.println(category.getKey().toUpperCase() + " (" + category.getValue().size()+ ")");
		}
	}

	public String getName() {
		return this.name;
	}
}
