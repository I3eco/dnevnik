package bg.dnevnik.utility;

import java.io.IOException;
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
		while(!Thread.interrupted()) {
			try {
				this.deleteOldArticles();
			} catch (InterruptedException e) {
				return;
			}
			
		}
	}
	
	public void deleteOldArticles() throws InterruptedException {
		Map<String, Set<Article>> articles = site.getArticlesInSite();		
		for(Entry<String, Set<Article>> entry : articles.entrySet()) {
			Iterator<Article> it = entry.getValue().iterator();
			while(it.hasNext()) {
				Article article = it.next();
				if((LocalDateTime.now().compareTo(article.getTimeOfPosting())) > AGE_OF_ARTICLE_IN_DAYS) {
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
}
