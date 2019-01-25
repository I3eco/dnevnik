package bg.dnevnik;

import java.time.LocalDateTime;
import java.util.Collection;

public class Article {
	
	private final String title;
	private final LocalDateTime timeOfPosting;
	private String category;
	private String mainPictureUrl;
	private String content;
	private Collection<Comment> comments;
	private Collection<String> keywords;
	private int numberOfViews;
	
	public Article(String title, String category, String content, Collection<String> keywords) {
		this.title = title;
		this.timeOfPosting = LocalDateTime.now();
		this.keywords = keywords;
	}
	
	void addComment (Comment comment) {
		this.comments.add(comment);
	}
}
