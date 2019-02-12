package bg.dnevnik;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DemoJSON {
	public static void main(String[] args) throws IOException {
		Site site = Site.getInstance();
		
		//creating two users in Site
		User.createUser("Ivan", "ivan@abv.bg", "123456", "user");
		User.createUser("Georgi", "georgi@abv.bg", "qwerty", "user");
		
		//getting the users from Site
		User ivan = site.getUser("Ivan", "123456");
		User georgi = site.getUser("Georgi", "qwerty");
		
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
		
	}
}
