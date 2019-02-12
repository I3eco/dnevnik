package bg.dnevnik;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class Main {

	public static void main(String[] args) {

		// new ConsoleCommandsView().start();

		User.createUser("Toshko", "toshanov@gmail.com", "tosharata69", "author");
		Author a = null;
		try {
			a = (Author) Site.getInstance().signIn("toshanov@gmail.com", "tosharata69");
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
			System.err.println("oops");
			return;
		}

		a.writeArticle("tosho e mnogo qk", "Pichovete", "nikoga nqma da povqrvate koi e nai golemiq pich v grada!",
				Arrays.asList("asdhg"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));
		a.writeArticle("pesho e tup!", "Pichovete", "nikoga nqma da povqrvate koi e nai prekrasniq chovek v grada!",
				Arrays.asList("asdgf"));

		Article bestArticle = null;
		try {
			bestArticle = Site.getInstance().getArticleByID(1);
		} catch (NoSuchArticleException e) {
			e.printStackTrace();
			System.err.println("oops number 2");
			return;
		}

		a.writeComment(bestArticle, "wow, super qkoto", Article.CommentMood.ANGRY);

		Gson gson = new Gson();
		String json = gson.toJson(Site.getInstance());

		try (FileWriter jsonToFile = new FileWriter(
				new File("." + File.separator + "ExampleContent" + File.separator + "SiteContent.json"));){
			
			jsonToFile.write(json);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

			Gson gson2 = new GsonBuilder().create();
			String json2 = gson.toJson(Site.getInstance());
			File f = new File("src" + File.separator + "bg" + File.separator + "siteContent.json");
			FileWriter writer = null;
			try {
				writer = new FileWriter(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				writer.write(json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
