package bg.dnevnik;

import java.util.ArrayList;
import java.util.Collection;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.IncorrectInputException;

public class GalleryArticle extends Article {
	
	private Collection<Picture> pictures;

	public GalleryArticle(Author author, String title, Collection<String> keywords)
			throws IncorrectInputException {
		super(author, title, "", keywords);
		this.pictures = new ArrayList<Picture>();
	}
	
	void addPicture (String title, String url) {
		this.addPicture(title, url, this.pictures);
	}
}
