package bg.dnevnik;

import java.time.LocalDateTime;

import bg.dnevnik.exceptions.WrongInputException;

public class Comment {

	private User poster;
	private LocalDateTime timeOfPosting;
	private String content;
	private int upvotesCnt;
	private int downvotesCnt;

	public Comment(User poster, String content, Article article) throws WrongInputException {
		if (poster == null || article == null)
			throw new WrongInputException();
		this.poster = poster;
		this.timeOfPosting = LocalDateTime.now();
		if (content == null)
			content = "";
		this.content = content;
		this.upvotesCnt = 0;
		this.downvotesCnt = 0;			
		article.addComment(this);
	}

}
