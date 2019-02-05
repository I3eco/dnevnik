package bg.dnevnik;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class Main {

	public static void main(String[] args) {
		
		//new ConsoleCommandsView().start();
	
		User.createUser("Toshko", "toshanov@gmail.com", "tosharata69", "author");
		Author a = null;
		try {
			a = (Author) Site.getInstance().signIn("toshanov@gmail.com", "tosharata69");
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
			System.err.println("oops");
			return;
		}
		
		a.writeArticle("tosho e mnogo qk", "Pichovete", "nikoga nqma da povqrvate koi e nai golemiq pich v grada!", Arrays.asList("asdhg"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!", Arrays.asList("asdgf"));
		
		Article bestArticle = null;
		try {
			bestArticle = Site.getInstance().getArticleByID(1);
		} catch (NoSuchArticleException e) {
			e.printStackTrace();
			System.err.println("oops number 2");
			return;
		}
		
		
		a.writeComment(bestArticle, "wow, super qkoto", Article.CommentMood.ANGRY);
		
		
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(Site.getInstance());  
		
		System.out.println(json);
		
		

	}
}
