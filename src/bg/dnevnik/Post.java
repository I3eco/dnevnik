package bg.dnevnik;

import java.time.LocalDateTime;

import bg.dnevnik.exceptions.WrongInputException;
import bg.dnevnik.utility.Validation;

public class Post {

	private final User poster;
	private final LocalDateTime timeOfPosting;
	private String content;
	private int upvotesCount;
	private int downvotesCount;
	
	protected Post(User poster, String content) throws WrongInputException {
		Validation.checkIfNull(poster);
		
		if (content == null)
			content = "";
		
		this.content = content;
		this.poster = poster;
		this.timeOfPosting = LocalDateTime.now();
	}
}
