package bg.dnevnik.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bg.dnevnik.User;

public class Logger {
	private static final String LOGS_FOLDER_NAME = "Logs";
	private static final String USERS_LOGS_FOLDER_NAME = "UsersLogs";
	private static boolean isEnabled = true;
	
	public static synchronized void printUserToFile(User user) throws IOException {
		if(isEnabled) {
			File usersDir = new File ("." + File.separator + LOGS_FOLDER_NAME + File.separator + USERS_LOGS_FOLDER_NAME);
			usersDir.mkdirs();
			File usersFile = new File(usersDir, user.getTypeOfUser() + "s.log");
			usersFile.createNewFile();
			LocalDateTime currentDataTime = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM uuuu - hh:mm");
			
			try(PrintWriter userWriter = new PrintWriter(new BufferedWriter(new FileWriter(usersFile, true)))){
				userWriter.println(user.getTypeOfUser() + " with name: " + user.getName() + " and email: " + user.getEmail() + " was created on: " + currentDataTime.format(dateTimeFormatter));
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
