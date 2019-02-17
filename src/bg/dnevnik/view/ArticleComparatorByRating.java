package bg.dnevnik.view;

import java.util.Comparator;

import bg.dnevnik.Article;

public class ArticleComparatorByRating implements Comparator<Article> {

	@Override
	public int compare(Article o1, Article o2) {
		if(o1.calculateRating() - o2.calculateRating() == 0) {
			return o1.getID() - o2.getID();
		}
		return o2.calculateRating() - o1.calculateRating();
	}

}
