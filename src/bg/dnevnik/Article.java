package bg.dnevnik;

import java.util.Collection;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.utility.Validation;

public class Article extends Post {

	public enum CommentMood {
		NEUTRAL, CHEERFUL, CURIOUS, SAD, ANGRY;
	}
	
	public class Picture {
		private String title;
		private String url;
		
		private Picture(String url) throws IncorrectInputException {
			Validation.throwIfNull(url);
			Validation.throwIfEmpty(url);

			this.url = url;
		}
		
		private Picture(String title, String url) throws IncorrectInputException {
			this(url);
			Validation.throwIfNull(title);
			Validation.throwIfEmpty(title);
			
			this.title = title;
		}

		public String getTitle() {
			return this.title;
		}

		public String getUrl() {
			return url;
		}
	}

	public class Comment extends Post {

		private final CommentMood mood;

		private Comment(User author, String content, CommentMood mood) throws IncorrectInputException {
			super(author, content);
			this.mood = mood;
		}

		public CommentMood getMood() {
			return this.mood;
		}

	}

	private final String title;
	private Picture mainPicture;
	private Collection<Comment> comments;
	private Collection<String> keywords;
	private int numberOfViews;

	public Article(Author author, String title, String content, Collection<String> keywords) throws IncorrectInputException {
		super(author, content);

		// TODO add validation; decide on the type of collections that should be used
		this.title = title;
		this.keywords = keywords;
	}

	void addComment(User poster, String content, Article.CommentMood mood) {
		Comment comment = null;
		try {
			comment = new Comment(poster, content, mood);
			this.comments.add(comment);
			poster.addToCommentHistory(comment);
		} 
		catch (IncorrectInputException e) {
			System.err.println("Could not create comment!");
			e.printStackTrace();
		}
	}
	
	void addMainPicture (String title, String url) {
		try {
			this.mainPicture = new Article.Picture(title, url);
		} catch (IncorrectInputException e) {
			System.err.println("Picture could not be created!");
		}
	}
	
	void addPictureInContent (int position, String url)  {
		StringBuilder content = new StringBuilder(this.getContent());
		
		content.insert(position, "/n" + url + "/n");
		this.setContent(content.toString());
		
	}
	
	void addPicture (String title, String url, Collection<Article.Picture> pictures) {
		try {
		pictures.add(new Article.Picture(title, url));
		}
		catch (IncorrectInputException e) {
			System.err.println("Picture could not be created!");
		}
	}		
		

	public String getTitle() {
		return this.title;
	}
	
}
