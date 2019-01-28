package bg.dnevnik;

import java.util.Collection;

import bg.dnevnik.exceptions.WrongInputException;

public class Article extends Post {

	public enum Mood {
		// Apparently this exists in the website..
		NEUTRAL, CHEERFUL, CURIOUS, SAD, ANGRY;
	}

	public class Comment extends Post {

		private final Mood mood;

		private Comment(User poster, String content, Mood mood) throws WrongInputException {
			super(poster, content);
			this.mood = mood;
		}

		public Mood getMood() {
			return this.mood;
		}

	}

	private final String title;
	private String category;
	private String mainPictureUrl;
	private Collection<Comment> comments;
	private Collection<String> keywords;
	private int numberOfViews;

	public Article(User poster, String title, String category, String content, Collection<String> keywords)
			throws WrongInputException {
		super(poster, content);

		// TODO add validation; decide on the type of collections that should be used
		this.title = title;
		this.keywords = keywords;
	}

	void addComment(User poster, String content, Article.Mood mood) {
		Comment comment = null;
		try {
			comment = new Comment(poster, content, mood);
		} catch (WrongInputException e) {
			System.err.println("Could not create comment!");
			e.printStackTrace();
		}
		this.comments.add(comment);
		poster.addToCommentHistory(comment);

	}
}
