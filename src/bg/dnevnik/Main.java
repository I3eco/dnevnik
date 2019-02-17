package bg.dnevnik;

import java.io.IOException;
import java.time.LocalDateTime;

import bg.dnevnik.exceptions.NoSuchArticleException;
import bg.dnevnik.utility.ContentGenerator;
import bg.dnevnik.utility.JsonDataHolder;
import bg.dnevnik.view.ConsoleCommandsView;

public class Main {

	public static void main(String[] args) {
//		new ContentGenerator(10, 3).start();
		
//		Site.getInstance().createAdmin("veso", "veso@gmail.com", "nekazvam");
		
		//check search by words
//		Site.getInstance().showArticlesByInputWords("nqkva tam druga statiq");
		
		//

//		System.out.println("Start collector");
//		Thread thread = Site.getInstance().startOldArticleCollector();
//		
//		try {
//			thread.join();
//		} catch (InterruptedException e1) {
//			return;
//		}
		
		new ConsoleCommandsView().start();
		
		//main thread sleeps some time so collector can delete article before main uploads the site data
//		try {
//			Thread.sleep(60000);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		System.out.println("Site downloading");
		
		//IMPORTANT SAVING SITE TO JSON
		try {
			JsonDataHolder.saveSiteToJson(Site.getInstance());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
