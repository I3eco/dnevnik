package bg.dnevnik;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.utility.Validation;

public abstract class Post {

	private final User author;
	private final LocalDateTime timeOfPosting;
	private String content;
	private int upvotesCount;
	private int downvotesCount;
	private Collection<String>votedUsers;
	
	protected Post(User author, String content) throws IncorrectInputException {
		Validation.throwIfNull(author);
		
		if (content == null)
			content = "";
		
		this.author = author;
		this.content = content;
		this.timeOfPosting = LocalDateTime.now();
		this.votedUsers = new HashSet<>();
	}

	public User getAuthor() {
		return this.author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content){
		try {
			Validation.throwIfNullOrEmpty(content);
		} catch (IncorrectInputException e) {
			System.out.println("Wrong input for content");
		}
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
	
	//method which will not allow voted user to vote again
	public void upvote(User user) {
		if(!this.votedUsers.contains(user.getEmail())) {
			this.upvotesCount++;
			this.votedUsers.add(user.getEmail());
		} else {
			System.out.println("You already voted!");
		}

	}
	
	public void downvote() {
		this.downvotesCount++;
	}
	
	//method which will not allow voted user to vote again
	public void downvote(User user) {
		if(!this.votedUsers.contains(user.getEmail())) {
			this.downvotesCount++;
			this.votedUsers.add(user.getEmail());
		} else {
			System.out.println("You already voted!");
		}
	}
	
	
}
