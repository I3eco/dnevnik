package bg.dnevnik;

import java.io.IOException;

import bg.dnevnik.utility.ContentGenerator;
import bg.dnevnik.utility.JsonDataHolder;
import bg.dnevnik.view.ConsoleCommandsView;

public class Main {

	
	public static void main(String[] args) {
//		new ContentGenerator(10, 500).start();

		new ConsoleCommandsView().start();
		
		
		//IMPORTANT SAVING SITE TO JSON
		try {
			JsonDataHolder.saveSiteToJson(Site.getInstance());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
