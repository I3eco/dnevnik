package bg.dnevnik.utility;

import java.util.Comparator;

import bg.dnevnik.Article;

public class ArticleComparatorByID implements Comparator<Article>{

	@Override
	public int compare(Article article1, Article article2) {
		return article1.getID() - article2.getID();
	}

}
