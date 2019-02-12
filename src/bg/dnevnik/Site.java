package bg.dnevnik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class Site {
	private static Site instance;
	private String name;
	private Collection<User> users;
	private Map<String, Collection<Article>> articlesByCategory;

	static {
		File fromJson = new File ("." + File.separator + "ExampleContent" + File.separator + "SiteContent.json");
		if(fromJson.length() > 0 && fromJson.canRead()) {
			
			try {
				FileInputStream readJson = new FileInputStream(fromJson);
				String json = "";
				
				int b = readJson.read();
				while (b != -1) {
					json += b;
					b = readJson.read();
				}
				System.out.println(json);
				readJson.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Site() {
		this.name = "Dnevnik";
		this.users = new HashSet<User>();
		this.articlesByCategory = new ConcurrentHashMap<String, Collection<Article>>();
	}
	
	public static Site getInstance() {
		if (instance == null) {
			Site.instance = new Site();
			return Site.instance;
		}
		return Site.instance;
	}

	public static void createAdmin(String name, String email, String password) {
		User.createUser(name, email, password, "admin");
	}

	public void addUser(User user) {
		this.users.remove(user);
		this.users.add(user);
	}
	
	public User getUser (String name, String password) {
		User user = null;
		
		for (User person : this.users) {
			if (person.getName().equals(name) && person.validatePassword(password)) {
				user = person;
			}
		}
		
		return user;
	}

	public void sighUp(String username, String email, String password) {
		User.createUser(username, email, password, "user");
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

	public void showHotCategories(int numberOfCategories) {
		class Category {
			String name;
			int numberOfArticles;

			Category(String name, int numberOfArticles) {
				this.name = name;
				this.numberOfArticles = numberOfArticles;
			}
		}

		Set<Category> topCategories = new TreeSet<Category>((c1, c2) -> c2.numberOfArticles - c1.numberOfArticles);

		for (Entry<String, Collection<Article>> entry : this.articlesByCategory.entrySet()) {

			int articlesInCategory = entry.getValue().size();
			Category currentCategory = new Category(entry.getKey(), articlesInCategory);

			if ((topCategories.size() >= numberOfCategories)) {
				ArrayList<Category> temp = new ArrayList<Category>(topCategories);
				Category category = temp.get(numberOfCategories);

				if (category.numberOfArticles < articlesInCategory) {
					topCategories.remove(category);
					topCategories.add(currentCategory);
				}
			} else {
				topCategories.add(currentCategory);
			}
			
		}
		System.out.println(topCategories);
	}
	
	public void showTopCategories(int numOfCategories) {
		if (numOfCategories <= 0) {
			return;
		}

		Comparator<Entry<String, Collection<Article>>> comparatorBySize = (a, b) -> a.getValue().size()
				- b.getValue().size();
		Set<Entry<String, Collection<Article>>> topCategories = new TreeSet<Entry<String, Collection<Article>>>(
				comparatorBySize);

		for (Entry<String, Collection<Article>> currentCategory : this.articlesByCategory.entrySet()) {
			if (topCategories.size() < numOfCategories) {
				topCategories.add(currentCategory);
			} else {
				for (Entry<String, Collection<Article>> topCategory : topCategories) {
					if (currentCategory.getValue().size() > topCategory.getValue().size()) {
						topCategories.remove(topCategory);
						topCategories.add(currentCategory);
					}
				}
			}
		}

		for (Entry<String, Collection<Article>> category : topCategories) {
			System.out.println(category.getKey().toUpperCase() + " (" + category.getValue().size() + ")");
		}

	}

	public void showFromToday() {
		// TODO test this
		Collection<Collection<Article>> articles = this.articlesByCategory.values();
		articles.forEach(Collection -> Collection.forEach(article -> {
			if (article.getTimeOfPosting().isAfter(LocalDateTime.now().minusDays(1))) {
				System.out.println(article.getSummary());
			}
		}));
	}
	
	public String getName() {
		return this.name;
	}


}
