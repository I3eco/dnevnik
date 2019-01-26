package bg.dnevnik;


import bg.dnevnik.exceptions.WrongInputException;

public class Comment extends Post {

	private Comment(User poster, String content) throws WrongInputException {
		super(poster, content);
	}
	
	
	public static void postTo(Article article, User poster, String content) {
		// Added wrapper to the comment constructor to divide responsibilities between creating and adding to the article
		// Also it deals with exception handling in one place, instead of having to try/catch every time a comment is added
		Comment comment = null;
		try {
			comment = new Comment(poster, content);
		} catch (WrongInputException e) {
			e.printStackTrace();
		}
		article.addComment(comment);
		poster.addToCommentHistory(comment);
	}

}
