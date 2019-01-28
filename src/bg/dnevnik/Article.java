package bg.dnevnik;

import java.util.Collection;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.WrongInputException;
import bg.dnevnik.utility.Validation;

public class Article extends Post {

	public enum CommentMood {
		// Apparently this exists in the website..
		NEUTRAL, CHEERFUL, CURIOUS, SAD, ANGRY;
	}
	
	public class Picture {
		private String title;
		private String url;
		
		private Picture(String url) throws WrongInputException {
			Validation.throwIfNull(url);
			Validation.throwIfEmpty(url);

			this.url = url;
		}
		
		private Picture(String title, String url) throws WrongInputException {
			this(url);
			
			Validation.throwIfNull(title);
			Validation.throwIfEmpty(title);
			
			this.title = title;
		}
	}

	public class Comment extends Post {

		private final CommentMood mood;

		private Comment(User author, String content, CommentMood mood) throws WrongInputException {
			super(author, content);
			this.mood = mood;
		}

		public CommentMood getMood() {
			return this.mood;
		}

	}

	private final String title;
	private String category;
	private Picture mainPicture;
	private Collection<Comment> comments;
	private Collection<String> keywords;
	private int numberOfViews;

	public Article(Author author, String title, String category, String content, Collection<String> keywords)
			throws WrongInputException {
		super(author, content);

		// TODO add validation; decide on the type of collections that should be used
		this.title = title;
		this.keywords = keywords;
	}

	void addComment(User poster, String content, Article.CommentMood mood) {
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
	
	void addMainPicture (String title, String url) throws WrongInputException {
		this.mainPicture = new Article.Picture(title, url);
	}
	
	void addPictureInContent (int position, String url) throws WrongInputException {
		StringBuilder content = new StringBuilder(this.getContent());
		
		content.insert(position, "/n" + url + "/n");
		
		this.setContent(content.toString());
		
	}
	
	void addPicture (String title, String url, Collection<Article.Picture> pictures) throws WrongInputException {
		pictures.add(new Article.Picture(title, url));
	}
	
}
