package bg.dnevnik.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bg.dnevnik.Article;
import bg.dnevnik.User;

public class Logger {
	private static final String LOGS_FOLDER_NAME = "Logs";
	private static final String USERS_LOGS_FOLDER_NAME = "UsersLogs";
	private static final String DELETED_ARTICLES_LOGS_FOLDER_NAME = "DeletedArticles";
	private static boolean isEnabled = true;
	
	public static void printUserToFile(User user, boolean isCreated) throws IOException {
		if(isEnabled) {
			File usersDir = new File ("." + File.separator + LOGS_FOLDER_NAME + File.separator + USERS_LOGS_FOLDER_NAME);
			usersDir.mkdirs();
			File usersFile = new File(usersDir, user.getTypeOfUser() + "s.logger");
			usersFile.createNewFile();
			LocalDateTime currentDataTime = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM uuuu - hh:mm");
			
			try(PrintWriter userWriter = new PrintWriter(new BufferedWriter(new FileWriter(usersFile, true)))){
				if(isCreated) {
					userWriter.println(user.getTypeOfUser() + " with name: " + user.getName() + " and email: " + user.getEmail() + " was created on: " + currentDataTime.format(dateTimeFormatter));
				} else {
					userWriter.println(user.getTypeOfUser() + " with name: " + user.getName() + " and email: " + user.getEmail() + " logged on: " + currentDataTime.format(dateTimeFormatter));
				}
			}
		}
	}
	
	public static void printDeletedArticlesToFile(Article article) throws IOException {
		if(isEnabled) {
			File deletedArticlesDir = new File ("." + File.separator + LOGS_FOLDER_NAME + File.separator + DELETED_ARTICLES_LOGS_FOLDER_NAME);
			deletedArticlesDir.mkdirs();
			File deletedArticlesFile = new File(deletedArticlesDir, "DeletedArticles.logger");
			deletedArticlesFile.createNewFile();
			LocalDateTime currentDataTime = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM uuuu - hh:mm");
			
			try(PrintWriter articleWriter = new PrintWriter(new BufferedWriter(new FileWriter(deletedArticlesFile, true)))){
				articleWriter.println("Article with ID: " + article.getID() + " and name: " + article.getTitle() + " was deleted on: " + currentDataTime.format(dateTimeFormatter));
			}
		}
	}
	
		

	public static boolean isEnabled() {
		return isEnabled;
	}

	public static void setEnabled(boolean isEnabled) {
		Logger.isEnabled = isEnabled;
	}
}
