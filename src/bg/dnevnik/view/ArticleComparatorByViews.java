package bg.dnevnik.view;

import java.util.Comparator;

import bg.dnevnik.Article;

public class ArticleComparatorByViews implements Comparator<Article> {

	@Override
	public int compare(Article o1, Article o2) {
		if(o1.getNumberOfViews() - o2.getNumberOfViews() == 0) {
			return o1.getID() - o2.getID();
		}
		return o1.getNumberOfViews() - o2.getNumberOfViews();
	}

}
