package bg.dnevnik;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import bg.dnevnik.User.Admin;
import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;
import bg.dnevnik.utility.ArticleComparatorByID;
import bg.dnevnik.utility.JsonDataHolder;
import bg.dnevnik.utility.UserComparatorByEmail;

public class Site {
	private static Site instance;
	private volatile AtomicInteger articleCount;
	private String name;
	private Set<User> users;
	private Set<Author> authors;
	private Set<Admin> admins;
	private Map<String, Set<Article>> articlesByCategory;
	
	private Site() {
		this.articleCount = new AtomicInteger();
		this.name = "Dnevnik";
		this.users = new TreeSet<User>(new UserComparatorByEmail());
		this.authors = new TreeSet<Author>(new UserComparatorByEmail());
		this.admins = new TreeSet<Admin>(new UserComparatorByEmail());
		this.articlesByCategory = new ConcurrentHashMap<String, Set<Article>>();
		
		//without the next line site cannot load data from json
		instance = this;
	}
	
	public static Site getInstance() {
		if (instance == null) {
			try {
				JsonDataHolder.loadSiteFromJson(Site.instance);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (instance == null) {
				instance = new Site();
			}
		}
		return Site.instance;
	}

	public void createAdmin(String name, String email, String password) {
		User.createUser(name, email, password, "admin");
	}

	public void addUser(User user) {
		this.users.remove(user);
		this.users.add(user);
	}
	
	public void addAuthor(Author author) {
		this.authors.remove(author);
		this.authors.add(author);
	}
	
	public void addAdmin(Admin admin) {
		this.admins.remove(admin);
		this.admins.add(admin);
	}

	public void signUp(String username, String email, String password) {
		User.createUser(username, email, password, "user");
	}

	public User signIn(String email, String password) throws UserDoesNotExistException {
		Collection<User> allUsers = new TreeSet<User>(new UserComparatorByEmail());
		allUsers.addAll(this.users);
		allUsers.addAll(this.authors);
		allUsers.addAll(this.admins);
		
		for (User user : allUsers) {
			if (user.loginInfoMatches(email, password)) {
				user.goOnline();
				return user;
			}
		}
		throw new UserDoesNotExistException("There is no user with that email or password!");
	}
	
	public void addArticle(Article article, String category) {
		category = category.toUpperCase();
		article.setCategory(category);
		if (!this.articlesByCategory.containsKey(category)) {
			this.articlesByCategory.put(category, new TreeSet<Article>(new ArticleComparatorByID()));
		}
		this.articlesByCategory.get(category).add(article);
	}
	
	public void removeArticle(Admin admin, String password, Article article) {
		if (admin.loginInfoMatches(admin.getEmail(), password)) {
			if(article.getAuthor().getTypeOfUser().equals("Author") || article.getAuthor().getTypeOfUser().equals("Admin")) {
				this.articlesByCategory.get(article.getCategory()).remove(article);
			} else {
				System.err.println("Incorret user for article author!");
			}
			
		} else {
			System.err.println("Invalid password!");
		}
	}

	public void showCategories() {
		for (Entry<String, Set<Article>> entry : this.articlesByCategory.entrySet()) {
			System.out.println(entry.getKey().toUpperCase() + " (" + entry.getValue().size() + " articles)");
		}
	}

	public void showCategory(String input) {
		boolean categoryFound = false;
		input = input.toUpperCase();
		for (Entry<String, Set<Article>> entry : this.articlesByCategory.entrySet()) {
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
		for (Entry<String, Set<Article>> entry : this.articlesByCategory.entrySet()) {
			for (Article article : entry.getValue()) {
				if (article.getID() == id) {
					return article;
				}
			}
		}
		throw new NoSuchArticleException();
	}
	
	// TODO test the two methods when there is site data
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

		for (Entry<String, Set<Article>> entry : this.articlesByCategory.entrySet()) {

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

		Comparator<Entry<String, Set<Article>>> comparatorBySize = (a, b) -> a.getValue().size()
				- b.getValue().size();
		Set<Entry<String, Set<Article>>> topCategories = new TreeSet<Entry<String, Set<Article>>>(
				comparatorBySize);

		for (Entry<String, Set<Article>> currentCategory : this.articlesByCategory.entrySet()) {
			if (topCategories.size() < numOfCategories) {
				topCategories.add(currentCategory);
			} else {
				for (Entry<String, Set<Article>> topCategory : topCategories) {
					if (currentCategory.getValue().size() > topCategory.getValue().size()) {
						topCategories.remove(topCategory);
						topCategories.add(currentCategory);
					}
				}
			}
		}

		for (Entry<String, Set<Article>> category : topCategories) {
			System.out.println(category.getKey().toUpperCase() + " (" + category.getValue().size() + ")");
		}

	}

	public void showFromToday() {
		// TODO test this
		Collection<Set<Article>> articles = this.articlesByCategory.values();
		articles.forEach(Collection -> Collection.forEach(article -> {
			if (article.getTimeOfPosting().isAfter(LocalDateTime.now().minusDays(1))) {
				System.out.println(article.getSummary());
			}
		}));
	}
	
	public boolean isUserInSite(String email) {
		
		Iterator<User> userIterator = this.users.iterator();
		
		while(userIterator.hasNext()) {
			if(userIterator.next().getEmail().equals(email)) {
				return true;
			}
		}
		
		return false;
	}
	
	//temp method to see users
	public void showUsersInSite() {
		System.out.println(this.users);
	}
	
	public void showArticlesByFilter(Comparator<Article> comparator, int numArticlesToShow) {
		Set<Article> sortedArticles = new TreeSet<Article>(comparator);
		for (String category : articlesByCategory.keySet()) {
			sortedArticles.addAll(articlesByCategory.get(category));
		}
		
		int count = 0;
		for (Article article : sortedArticles) {
			if (count++ > numArticlesToShow) {
				break;
			}
			System.out.println(article.getSummary());
		}
	}
	
	@Override
	public String toString() {
		return "name=" + name + ", users=" + users + ", articlesByCategory=" + articlesByCategory;
	}
	
	public synchronized int incrementArticleCount() {
		return this.articleCount.incrementAndGet();
	}

	public String getName() {
		return this.name;
	}
}
