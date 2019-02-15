package bg.dnevnik;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bg.dnevnik.exceptions.UserDoesNotExistException;

public class DemoJSON {
	public static void main(String[] args) throws IOException {
		Site site = Site.getInstance();
		
		//creating two users in Site
		User.createUser("Ivan", "ivan@abv.bg", "123456", "user");
		User.createUser("Georgi", "georgi@abv.bg", "qwerty", "user");
		
		//getting the users from Site
		User ivan = null;
		User georgi = null;
		try {
			ivan = site.signIn("ivan@abv.bg", "123456");
			georgi = site.signIn("georgi@abv.bg", "qwerty");
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
		
		//converting JSONs to Strings
		String jsonIvan = gson.toJson(ivan);
		String jsonGeorgi = gson.toJson(georgi);
		
		//creating file to store JSONs in
		File jsonFile = new File ("jsonFile.txt");
		
		//delete and create the file if exists to clear it
		jsonFile.delete();
		jsonFile.createNewFile();
		
		//creating a writer to the file by enabling the append option, since PrintWriter cannot append by default
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(jsonFile, true)))){
			
			//adding JSONs to the text file
			writer.println(jsonIvan);
			writer.println(jsonGeorgi);
			
		}
		
		File fromJson = new File ("." + File.separator + "ExampleContent" + File.separator + "jsonFile.lajna");
		
		if(fromJson.length() > 0 && fromJson.canRead()) {
			
				try(Scanner readJson = new Scanner(fromJson)){
					StringBuilder json = new StringBuilder();
					while(readJson.hasNext()) {
//						System.out.println(readJson.next());
						json.append(readJson.next() + "\n");
//						json.append("\n");
//						User user = gson.fromJson(readJson.nextLine(), User.class);
//						System.out.println(user);
					}
					System.out.println(json.toString());
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				
		}
		
	}
}
