package bg.dnevnik;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

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

		public String toString() {
			StringBuilder comment = new StringBuilder();
			
			comment.append("By " + getAuthor().getName() + " ");
			comment.append(getTimeOfPosting().format(DateTimeFormatter.ofPattern("HH:mm dd MM yy")) + " ");
			comment.append(mood);
			comment.append("\n   "+ getContent());
			
			return comment.toString();
		}
	}
	
	public static class GalleryArticle extends Article {
		
		private Collection<Picture> pictures;

		public GalleryArticle(Author author, String category, String title, Collection<String> keywords)
				throws IncorrectInputException {
			super(author, title, "", category, keywords);
			this.pictures = new ArrayList<Picture>();
		}
		
		void addPicture(String title, String url) {
			try {
				this.pictures.add(new Article.Picture(title, url));
			} catch (IncorrectInputException e) {
				System.err.println("Picture could not be created!");
			}
		}

	}

	private static int count;
	private final int ID;
	private final String title;
	private Collection<String> keywords;
	private Picture mainPicture;
	private int numberOfViews;
	private Collection<Comment> comments;

	public Article(Author author, String category, String title, String content, Collection<String> keywords) throws IncorrectInputException {
		super(author, content);
		Validation.throwIfNull(keywords);
		Validation.throwIfNullOrEmpty(title);
		
		this.title = title;
		this.keywords = keywords;
		this.comments = new LinkedList<Comment>();
		this.ID = ++Article.count;
		Site.getInstance().addArticle(this, category);
	}
	
	void addMainPicture (String title, String url) {
		try {
			if(title == null || title.trim().length() <= 0) {
				this.mainPicture = new Article.Picture(url);
			} else {
				this.mainPicture = new Article.Picture(title, url);
			}
		} catch (IncorrectInputException e) {
			System.err.println("Picture could not be created!");
		}
	}
	
	void addPictureInContent (int position, String url)  {
		StringBuilder content = new StringBuilder(this.getContent());
		
		content.insert(position, "/n" + url + "/n");
		this.setContent(content.toString());
		
	}
	
	public String getSummary() {
		StringBuilder summary = new StringBuilder();
		
		summary.append("\n(ID " + this.getID() + ") ");
		summary.append(this.title + "\n");
		summary.append(this.getTimeOfPosting().format(DateTimeFormatter.ofPattern("hh:mm, dd MMM uu")));
		summary.append(" / " + this.getAuthor().getName());
		summary.append(" / " + this.numberOfViews + " Views, ");
		summary.append(this.comments.size() + " Comments");
		
		return summary.toString();
	}

	public void show() {
		// TODO find a good way to synchronize this
		this.numberOfViews++;
		
		StringBuilder info = new StringBuilder();
		info.append(this.getSummary());
		info.append("\n\n   " + this.getContent());
		info.append("\n\n ^" + this.getUpvotesCount() + " v" + this.getDownvotesCount());
		info.append(" / Keywords: ");
		info.append(this.keywords.stream().collect(Collectors.joining(", ")));
		System.out.println(info.toString());
	}

	public int getID() {
		return this.ID;
	}
	
	public String getTitle() {
		return this.title;
	}

	
	public void showComments() {
		if (comments.size() == 0) {
			System.out.println("No comments to show!");
			return;
		}
		for (Comment comment : comments) {
			System.out.println(comment);
			System.out.println();
		}
	}
	
}
