package bg.dnevnik;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.utility.Validation;

public class Article extends Post {

	public class Picture {
		private String title;
		private String url;
		
		private Picture(String url) throws IncorrectInputException {
			Validation.throwIfNullOrEmpty(url);

			this.url = url;
			this.title = "";
		}
		
		private Picture(String title, String url) throws IncorrectInputException {
			this(url);
			Validation.throwIfNullOrEmpty(title);
			
			this.title = title;
		}

		public String getTitle() {
			return this.title;
		}

		public String getUrl() {
			return this.url;
		}
	}

	public enum CommentMood {
		NEUTRAL, CHEERFUL, CURIOUS, SAD, ANGRY;
	}
	
	public class Comment extends Post {

		private final CommentMood mood;

		Comment(User author, String content, CommentMood mood) throws IncorrectInputException {
			super(author, content);
			this.mood = mood;
			
			comments.add(this);
		}

		public CommentMood getMood() {
			return this.mood;
		}
	}

	private final String title;
	private Collection<String> keywords;
	private Picture mainPicture;
	private int numberOfViews;
	private Collection<Comment> comments;

	public Article(Author author, String content, String title, Collection<String> keywords) throws IncorrectInputException {
		super(author, content);
		Validation.throwIfNull(keywords);
		Validation.throwIfNullOrEmpty(title);
		
		// TODO decide on the type of collections that should be used
		
		this.title = title;
		this.keywords = keywords;
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
	
	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		
		summary.append(this.title + "\n");
		summary.append(this.getTimeOfPosting().format(DateTimeFormatter.ofPattern("hh:mm, dd MMM uu")));
		summary.append(" / " + this.getAuthor().getName());
		summary.append(" (" + this.comments.size() + " Comments)");
		
		return summary.toString();
	}
	
}
