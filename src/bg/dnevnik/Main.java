package bg.dnevnik;

import java.util.Arrays;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;
import bg.dnevnik.view.ConsoleCommandsView;

public class Main {

	private static final int NUM_ARTICLES = 10;

	public static void main(String[] args) {

		User.createUser("Toshko", "toshanov@gmail.com", "tosharata69", "author");
		Author a = null;
		try {
			a = (Author) Site.getInstance().signIn("toshanov@gmail.com", "tosharata69");
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
			System.err.println("oops");
			return;
		}

		for (int count = 0; count < NUM_ARTICLES; count++) {
			a.writeArticle("tosho e mnogo qk", 
					"Pichovete", 
					"nikoga nqma da povqrvate koi e nai golemiq pich v grada!",
					Arrays.asList("asdhg"));
		}
		
		Article bestArticle = null;
		try {
			bestArticle = Site.getInstance().getArticleByID(1);
		} catch (NoSuchArticleException e) {
			e.printStackTrace();
			System.err.println("oops number 2");
			return;
		}

		a.writeComment(bestArticle, "wow, super qkoto", Article.CommentMood.ANGRY);

		new ConsoleCommandsView().start();
	}
}
