package bg.dnevnik.view;

import java.util.Comparator;

import bg.dnevnik.Article;

public class ArticleComparatorByComments implements Comparator<Article> {

	@Override
	public int compare(Article o1, Article o2) {
		return o1.getCommentsCount() - o2.getCommentsCount();
	}

}
