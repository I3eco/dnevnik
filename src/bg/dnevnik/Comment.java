package bg.dnevnik;

import java.time.LocalDateTime;

import bg.dnevnik.exceptions.WrongInputException;
import bg.dnevnik.utility.Validation;

public class Comment {

	private final User poster;
	private final LocalDateTime timeOfPosting;
	private String content;
	private int upvotesCount;
	private int downvotesCount;

	private Comment(User poster, String content) throws WrongInputException {
		Validation.checkIfNull(poster);
		
		if (content == null)
			content = "";
		
		this.content = content;
		this.poster = poster;
		this.timeOfPosting = LocalDateTime.now();
	}
	
	
	public static void postTo(Article article, User poster, String content) {
		// Added wrapper to the comment constructor to divide responsibilities between creating and adding to the article
		// Also it deals with exception handling in one place, instead of having to try/catch every time a comment is added
		try {
			article.addComment(new Comment(poster, content));
		} catch (WrongInputException e) {
			e.printStackTrace();
		}
	}

}
