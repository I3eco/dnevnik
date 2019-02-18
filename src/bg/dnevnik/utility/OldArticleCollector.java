package bg.dnevnik.utility;

import bg.dnevnik.Site;

public class OldArticleCollector implements Runnable{
	private Site site;
	
	public OldArticleCollector() {
		this.site = Site.getInstance();
	}

	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				site.deleteOldArticles();
			} catch (InterruptedException e) {
				return;
			}
			
		}
	}
}

