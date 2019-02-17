package bg.dnevnik.utility;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import bg.dnevnik.Article;
import bg.dnevnik.Site;
import bg.dnevnik.User.Admin;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class OldArticleCollector implements Runnable{
	private static final int AGE_OF_ARTICLE_IN_DAYS = 3;
	private Site site;	
	
	public OldArticleCollector() {
		this.site = Site.getInstance();
	}

	@Override
	public void run() {
		try {
			this.deleteOldArticles();
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
		
	}
	
	public void deleteOldArticles() throws UserDoesNotExistException {
		Admin admin = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your email!");
		String email = sc.nextLine();
		System.out.println("Enter your password!");
		String password = sc.nextLine();
		if (this.site.signIn(email, password).getTypeOfUser().equals("Admin")) {
			admin = (Admin) this.site.signIn(email, password);
		} else {
			throw new UserDoesNotExistException("Incorrect admin.");
		}
		Map<String, Set<Article>> articles = site.getArticlesInSite();		
		for(Entry<String, Set<Article>> entry : articles.entrySet()) {
			Iterator<Article> it = entry.getValue().iterator();
			while(it.hasNext()) {
				Article article = it.next();
				if((LocalDateTime.now().compareTo(article.getTimeOfPosting())) > AGE_OF_ARTICLE_IN_DAYS) {
					admin.deleteArticle(article, password);
					System.out.println("Old Article: " + article.getID() + " " + article.getTitle() + " deleted");
				} else {
					System.out.println("New Article: " + article.getID() + " " + article.getTitle());
				}
			}
		}
	}
}
