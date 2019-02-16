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
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

import bg.dnevnik.User;
import bg.dnevnik.User.Admin;
import bg.dnevnik.User.Author;

public class JsonDataHolder {
	
	private static final String DATA_FOLDER_NAME = "Data";
	private static final String USERS_DATA_FOLDER_NAME = "UsersData";
	private static Set<User> users = new HashSet<User>();
	private static Set<User> authors = new HashSet<User>();
	private static Set<User> admins = new HashSet<User>();

	public static synchronized void saveUserToJson(User user) throws IOException {
		File usersDir = new File ("." + File.separator + DATA_FOLDER_NAME + File.separator + USERS_DATA_FOLDER_NAME);
		usersDir.mkdirs();
		File usersFile = new File(usersDir, user.getTypeOfUser() + "s.data");
		usersFile.createNewFile();
		
		Gson gson = new Gson();
		
		String typeOfUser = user.getTypeOfUser();
		System.out.println(typeOfUser);
		String tempUserJson = "";
		
		switch (typeOfUser) {
		case "User":
			users.add(user);
			tempUserJson = gson.toJson(users);
			System.out.println("=================" + users);
			break;
		case "Author":
			authors.add((Author) user);
			
			//to make json save authors instead of users
			Set<Author>tempAuthors = new HashSet<>();
			admins.forEach(author -> tempAuthors.add((Author) author));
			
			tempUserJson = gson.toJson(tempAuthors);
			System.out.println("=================" + authors);
			break;
		case "Admin":
			admins.add(user);
			
			//to make json save admins instead of users
			Set<Admin>tempAdmins = new HashSet<>();
			admins.forEach(admin -> tempAdmins.add((Admin) admin));
			
			tempUserJson = gson.toJson(tempAdmins);
			System.out.println("=================" + tempAdmins);
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
		
		JsonElement element = gson.fromJson(jsonString.toString(), JsonElement.class);
		JsonArray jsonArray = null;

		if(element != null) {
			jsonArray = element.getAsJsonArray();
		}
		
		if(jsonArray != null) {
			String fileName = file.getName();
			switch (fileName) {
			case "Users.data":
				for (int index = 0; index < jsonArray.size(); index++) {
					User user = gson.fromJson(jsonArray.get(index), User.class);
					if(users != null) {
						users.add(user);
					}

				}
				break;
			case "Authors.data":
				for (int index = 0; index < jsonArray.size(); index++) {
					Author user = gson.fromJson(jsonArray.get(index), Author.class);
					if(users != null) {
						users.add(user);
					}

				}
				break;
			case "Admins.data":
				for (int index = 0; index < jsonArray.size(); index++) {
					Admin user = gson.fromJson(jsonArray.get(index), Admin.class);
					if(users != null) {
						users.add(user);
					}

				}
				break;
			}
		}
	}
	
	public static synchronized void uploadUsersInSite(Set<User>usersInSite) {
		File usersDir = new File("." + File.separator + DATA_FOLDER_NAME + File.separator + USERS_DATA_FOLDER_NAME);
		usersDir.mkdirs();
		File usersFile = new File(usersDir, "Users.data");
		File authorsFile = new File(usersDir, "Authors.data");
		File adminsFile = new File(usersDir, "Admins.data");
		
		try {
			usersFile.createNewFile();
			authorsFile.createNewFile();
			adminsFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			JsonDataHolder.uploadFile(usersFile, users);
			JsonDataHolder.uploadFile(authorsFile, authors);
			JsonDataHolder.uploadFile(adminsFile, admins);
			JsonDataHolder.uploadFile(usersFile, usersInSite);
			JsonDataHolder.uploadFile(authorsFile, usersInSite);
			JsonDataHolder.uploadFile(adminsFile, usersInSite);			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
