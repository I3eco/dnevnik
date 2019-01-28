package bg.dnevnik;

import java.util.ArrayList;
import java.util.Collection;

import bg.dnevnik.exceptions.WrongInputException;

public class GalleryArticle extends Article {
	
	private Collection<Picture>pictures;

	public GalleryArticle(User author, String title, String category, Collection<String> keywords)
			throws WrongInputException {
		super(author, title, category, "", keywords);
		this.pictures = new ArrayList<Picture>();
	}
	
	void addPicture (String title, String url) throws WrongInputException {
		this.addPicture(title, url, this.pictures);
	}
}
