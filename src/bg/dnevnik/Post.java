package bg.dnevnik;

import java.time.LocalDateTime;

import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.utility.Validation;

public abstract class Post {

	private final User author;
	private final LocalDateTime timeOfPosting;
	private String content;
	private int upvotesCount;
	private int downvotesCount;
	
	protected Post(User author, String content) throws IncorrectInputException {
		Validation.throwIfNull(author);
		
		if (content == null)
			content = "";
		
		this.author = author;
		this.content = content;
		this.timeOfPosting = LocalDateTime.now();
	}

	public User getAuthor() {
		return this.author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public LocalDateTime getTimeOfPosting() {
		return this.timeOfPosting;
	}

	public int getUpvotesCount() {
		return this.upvotesCount;
	}

	public int getDownvotesCount() {
		return this.downvotesCount;
	}
	
	public void upvote() {
		this.upvotesCount++;
	}
	
	public void downvote() {
		this.downvotesCount++;
	}
}
