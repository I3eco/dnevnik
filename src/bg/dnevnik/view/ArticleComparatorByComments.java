package bg.dnevnik.view;

import java.util.Comparator;

import bg.dnevnik.Article;

public class ArticleComparatorByComments implements Comparator<Article> {

	@Override
	public int compare(Article o1, Article o2) {
		if(o1.getCommentsCount() - o2.getCommentsCount() == 0) {
			return o1.getID() - o2.getID();
		}
		return o2.getCommentsCount() - o1.getCommentsCount();
	}

}
