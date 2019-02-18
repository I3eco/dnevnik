package bg.dnevnik;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
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
import bg.dnevnik.utility.Logger;
import bg.dnevnik.utility.OldArticleCollector;
import bg.dnevnik.utility.UserComparatorByEmail;

public class Site {
	private static final int AGE_OF_ARTICLE_IN_DAYS = 3;
	private static Site instance;
	private volatile AtomicInteger articleCount;
	private String name;
	private Set<User> users;
	private Set<Author> authors;
	private Set<Admin> admins;
	private Map<String, Set<Article>> articlesByCategory;
	private Thread thread;

	private Site() {
		this.articleCount = new AtomicInteger();
		this.name = "Dnevnik";
		this.users = new TreeSet<User>(new UserComparatorByEmail());
		this.authors = new TreeSet<Author>(new UserComparatorByEmail());
		this.admins = new TreeSet<Admin>(new UserComparatorByEmail());
		this.articlesByCategory = new ConcurrentHashMap<String, Set<Article>>();
		instance = this;
	}

	public static Site getInstance() {
		if (instance == null) {
			try {
				JsonDataHolder.loadSiteFromJson(Site.instance);
			} catch (IOException e) {
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
				try {
					Logger.printUserToFile(user, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
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
			this.articlesByCategory.get(article.getCategory()).remove(article);

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

	public void showHotCategories(int numberOfCategories) {
		class Category {
			String name;
			int numberOfArticles;

			Category(String name, int numberOfArticles) {
				this.name = name;
				this.numberOfArticles = numberOfArticles;
			}
		}

		Set<Category> topCategories = new TreeSet<Category>((c1, c2) -> {
			if (c2.numberOfArticles - c1.numberOfArticles == 0) {
				return c1.name.compareTo(c2.name);
			}
			return c2.numberOfArticles - c1.numberOfArticles;
		});

		for (Entry<String, Set<Article>> entry : this.articlesByCategory.entrySet()) {

			int articlesInCategory = entry.getValue().size();
			Category currentCategory = new Category(entry.getKey(), articlesInCategory);

			if ((topCategories.size() > numberOfCategories)) {
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
		topCategories.forEach(category -> System.out
				.println(category.name.toUpperCase() + " (" + category.numberOfArticles + " articles)"));
	}

	public void showFromToday() {
		Collection<Set<Article>> articles = this.articlesByCategory.values();
		articles.forEach(Collection -> Collection.forEach(article -> {
			if (article.getTimeOfPosting().isAfter(LocalDateTime.now().minusDays(1))) {
				System.out.println(article.getSummary());
			}
		}));
	}

	public boolean isUserInSite(String email) {
		Iterator<User> userIterator = this.users.iterator();

		while (userIterator.hasNext()) {
			if (userIterator.next().getEmail().equals(email)) {
				return true;
			}
		}

		return false;
	}

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

	public void showArticlesByInputWords(String inputWords) {
		String[] words = inputWords.trim().split(" ");

		for (Entry<String, Set<Article>> entry : this.articlesByCategory.entrySet()) {
			entry.getValue().stream().filter(article -> this.isArticleContainWordsInTitle(article, words))
					.sorted((article1, article2) -> {
						if (this.numberOfWordsInArticleTitle(article1, words)
								- this.numberOfWordsInArticleTitle(article2, words) == 0) {
							return article1.getID() - article2.getID();
						}
						return this.numberOfWordsInArticleTitle(article2, words)
								- this.numberOfWordsInArticleTitle(article1, words);
					}).map(article -> article.getSummary()).forEach(System.out::println);
		}
	}

	private boolean isArticleContainWordsInTitle(Article article, String[] words) {
		for (int index = 0; index < words.length; index++) {
			if (article.getTitle().toLowerCase().contains(words[index].toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	private int numberOfWordsInArticleTitle(Article article, String[] words) {
		int count = 0;

		for (int index = 0; index < words.length; index++) {
			if (article.getTitle().toLowerCase().contains(words[index].toLowerCase())) {
				count++;
			}
		}

		return count;
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

	public User getRandomUser() {
		return getRandomUserFrom(users);
	}

	public User getRandomAuthor() {
		return getRandomUserFrom(authors);
	}

	public User getRandomAdmin() {
		return getRandomUserFrom(admins);
	}

	public User getUserOrAuthor(String email, boolean isMakeAdmin) throws UserDoesNotExistException {
		User user = null;

		try {
			user = this.users.stream()
					.filter(siteUser -> siteUser.getEmail().equals(email))
					.findFirst()
					.get();
			return user;
		} catch (NoSuchElementException e) {
			if (!isMakeAdmin)
				throw new UserDoesNotExistException();
		}
		
		User author = null;
		
		try {
			author = this.authors.stream()
					.filter(siteUser -> siteUser.getEmail().equals(email))
					.findFirst()
					.get();
		} catch (NoSuchElementException e) {
			throw new UserDoesNotExistException();
		}

		return author;
	}

	private User getRandomUserFrom(Set<? extends User> users) {
		Iterator<? extends User> i = users.iterator();

		if (users.size() <= 0) {
			return null;
		}

		if (users.size() == 1) {
			return i.next();
		}

		int index = new Random().nextInt(users.size() + 1) - 2;

		for (int count = 0; count < index && i.hasNext(); count++) {
			i.next();
		}

		return i.next();
	}

	public Article getRandomArticle() {

		List<Article> allArticles = new ArrayList<Article>();

		for (String category : articlesByCategory.keySet()) {
			allArticles.addAll(articlesByCategory.get(category));
		}

		return allArticles.get(new Random().nextInt(allArticles.size()));
	}

	public void removeUser(User user) {
		this.users.remove(user);
	}

	public void removeUser(User user, String type) {
		if (type.equals("User")) {
			this.users.remove(user);
			return;
		}
		if (type.equals("Author")) {
			this.authors.remove(user);
		}
	}

	public Map<String, Set<Article>> getArticlesInSite() {
		return new HashMap<String, Set<Article>>(this.articlesByCategory);
	}

	public void deleteOldArticles() throws InterruptedException {
		for (Entry<String, Set<Article>> entry : this.articlesByCategory.entrySet()) {
			Iterator<Article> it = entry.getValue().iterator();
			while (it.hasNext()) {
				Article article = it.next();
				if ((LocalDateTime.now().compareTo(article.getTimeOfPosting())) > AGE_OF_ARTICLE_IN_DAYS) {
					it.remove();
					Thread.sleep(500);
					try {
						Logger.printDeletedArticlesToFile(article);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void startOldArticleCollector() {
		if (this.thread != null && this.thread.isAlive()) {
			System.out.println("You've already started the collector");
			return;
		}
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Login as admin");
		System.out.println("Email:");
		String email = sc.nextLine();
		System.out.println("Password:");
		String password = sc.nextLine();
		try {
			if (!this.signIn(email, password).getTypeOfUser().equals("Admin")) {
				System.out.println("This user is not admin!");
				return;
			}
			this.signIn(email, password);
		} catch (UserDoesNotExistException e) {
			System.out.println("No such user!");
			return;
		}
		this.thread = new Thread(new OldArticleCollector());
		this.thread.setDaemon(true);
		this.thread.start();
	}

	public void stopOldArticleCollector() {
		if (this.thread == null || !this.thread.isAlive()) {
			System.out.println("The collector is not started");
			return;
		}
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Login as admin");
		System.out.println("Email:");
		String email = sc.nextLine();
		System.out.println("Password:");
		String password = sc.nextLine();
		try {
			if (!this.signIn(email, password).getTypeOfUser().equals("Admin")) {
				System.out.println("This user is not admin!");
				return;
			}
			this.signIn(email, password);
		} catch (UserDoesNotExistException e) {
			System.out.println("No such user!");
			return;
		}
		this.thread.interrupt();
	}

}
