package bg.dnevnik;


import bg.dnevnik.exceptions.WrongInputException;

public class Comment extends Post {
	
	enum Mood {
		// Apparently this exists in the website..
		NEUTRAL, CHEERFUL, CURIOUS, SAD, ANGRY
	}
	
	private final Mood mood;

	private Comment(User poster, String content, Mood mood) throws WrongInputException {
		super(poster, content);
		this.mood = mood;
	}
	
	
	public static void postTo(Article article, User poster, String content, Mood mood) {
		// Added wrapper to the constructor, 
		// to divide responsibilities between creating and adding to the article
		// Also it deals with exception handling in one place, 
		// instead of having to try/catch every time a comment is added
		Comment comment = null;
		try {
			comment = new Comment(poster, content, mood);
		} catch (WrongInputException e) {
			System.err.println("Could not create comment!");
			e.printStackTrace();
		}
		article.addComment(comment);
		poster.addToCommentHistory(comment);
	}

}
