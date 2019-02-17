package bg.dnevnik;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;

import bg.dnevnik.User.Author;
import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.exceptions.UserDoesNotExistException;
import bg.dnevnik.utility.JsonDataHolder;
import bg.dnevnik.view.ConsoleCommandsView;

public class Main {

	private static final int NUM_ARTICLES = 10;

	public static void main(String[] args) {
		
//		Site.getInstance().createAdmin("veso", "veso@gmail.com", "nekazvam");
		
		new ConsoleCommandsView().start();
		
		//IMPORTANT SAVING SITE TO JSON
		try {
			JsonDataHolder.saveSiteToJson(Site.getInstance());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
