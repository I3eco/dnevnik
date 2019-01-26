package bg.dnevnik;

import java.util.Collection;

import bg.dnevnik.exceptions.WrongInputException;

public class Article extends Post {
	
	private final String title;
	private String category;
	private String mainPictureUrl;
	private Collection<Comment> comments;
	private Collection<String> keywords;
	private int numberOfViews;
	
	public Article(User poster, String title, String category, String content, Collection<String> keywords) throws WrongInputException {
		super(poster, content);
		
		// TODO add validation; decide on the type of collections that should be used
		this.title = title;
		this.keywords = keywords;
	}
	
	void addComment (Comment comment) {
		this.comments.add(comment);
	}
}
