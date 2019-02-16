package bg.dnevnik.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import bg.dnevnik.Site;
import bg.dnevnik.User;

public class JsonDataHolder {
	
	private static final String DATA_FOLDER_NAME = "Data";
	private static final String USERS_DATA_FOLDER_NAME = "UsersData";
	private static Set<User> users = new HashSet<User>();
	private static Set<User> authors = new HashSet<User>();
	private static Set<User> admins = new HashSet<User>();
	private static Set<User> usersToAddInSite = new HashSet<User>();

	public static synchronized void saveUserToJson(User user) throws IOException {
		File usersDir = new File ("." + File.separator + DATA_FOLDER_NAME + File.separator + USERS_DATA_FOLDER_NAME);
		usersDir.mkdirs();
		File usersFile = new File(usersDir, user.getTypeOfUser() + "s.data");
		usersFile.createNewFile();
		
		Gson userToJson = new Gson();
		
		String typeOfUser = user.getTypeOfUser();
		String tempUserJson = "";
		
		switch (typeOfUser) {
		case "User":
			users.add(user);
			tempUserJson = userToJson.toJson(users);
			System.out.println("=================" + users);
			break;
		case "Author":
			authors.add(user);
			tempUserJson = userToJson.toJson(authors);
			System.out.println("=================" + authors);
			break;
		case "Admin":
			admins.add(user);
			tempUserJson = userToJson.toJson(admins);
			System.out.println("=================" + admins);
			break;
		
		}

		try(PrintWriter userWriter = new PrintWriter(new BufferedWriter(new FileWriter(usersFile, false)))){
			userWriter.println(tempUserJson);
		}
		
	}
	
	public static void uploadFile (File file, Set<User> users) throws FileNotFoundException {
		Gson gson = new Gson();
		StringBuilder jsonString = new StringBuilder();
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				jsonString.append(reader.nextLine());
			}
		}
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonString.toString());
		
		//TODO the problem is here!!!
		JsonArray jsonArray = element.getAsJsonArray();
		System.out.println(jsonArray);
		
		users = gson.fromJson(jsonArray, new HashSet<User>(){}.getClass());
	}
	
	public static synchronized void uploadUsersInSite() {
		File usersDir = new File("." + File.separator + DATA_FOLDER_NAME + File.separator + USERS_DATA_FOLDER_NAME);
		usersDir.mkdirs();
		File usersFile = new File(usersDir, "Users.data");
		File authorsFile = new File(usersDir, "Authors.data");
		File adminsDir = new File(usersDir, "Admins.data");
		
		try {
			usersFile.createNewFile();
			authorsFile.createNewFile();
			adminsDir.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			JsonDataHolder.uploadFile(usersFile, users);
			JsonDataHolder.uploadFile(authorsFile, authors);
			JsonDataHolder.uploadFile(adminsDir, admins);
			JsonDataHolder.uploadFile(usersFile, usersToAddInSite);
			JsonDataHolder.uploadFile(authorsFile, usersToAddInSite);
			JsonDataHolder.uploadFile(adminsDir, usersToAddInSite);			
			Site site = Site.getInstance();
			site.uploadUsers(usersToAddInSite);
			site.showUsersInSite();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
