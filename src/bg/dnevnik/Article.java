package bg.dnevnik;

import java.time.LocalDateTime;
import java.util.Collection;

public class Article {
	
	private String title;
	private String category;
	private String mainPictureUrl;
	private String content;
	private LocalDateTime timeOfPosting;
	private Collection<Comment> comments;
	private Collection<String> keywords;
	private int numberOfViews;
	
	
	void addComment (Comment comment) {
		this.comments.add(comment);
	}
}
