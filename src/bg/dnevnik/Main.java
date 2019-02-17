package bg.dnevnik;

import java.io.IOException;

import bg.dnevnik.utility.ContentGenerator;
import bg.dnevnik.utility.JsonDataHolder;
import bg.dnevnik.view.ConsoleCommandsView;

public class Main {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		new ContentGenerator(10, 10).start();
		System.out.println(System.currentTimeMillis() - start + "ms spent generating");
		//Site.getInstance().createAdmin("veso", "veso@gmail.com", "nekazvam");
		
		new ConsoleCommandsView().start();
		
		//IMPORTANT SAVING SITE TO JSON
		try {
			JsonDataHolder.saveSiteToJson(Site.getInstance());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
