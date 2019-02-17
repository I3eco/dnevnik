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

import bg.dnevnik.Site;
import bg.dnevnik.User;
import bg.dnevnik.User.Admin;
import bg.dnevnik.User.Author;

public class JsonDataHolder {

	private static final String DATA_FOLDER_NAME = "Data";
	private static final String SITE_DATA_FOLDER_NAME = "SiteData";

	public static void saveSiteToJson(Site site) throws IOException {
		File siteDir = new File("." + File.separator + DATA_FOLDER_NAME + File.separator + SITE_DATA_FOLDER_NAME);
		siteDir.mkdirs();
		File siteFile = new File(siteDir, "Site.json");
		siteFile.createNewFile();
		Gson gson = new Gson();
		String siteJson = gson.toJson(site, Site.class);

		try (PrintWriter userWriter = new PrintWriter(new BufferedWriter(new FileWriter(siteFile, false)))) {
			userWriter.println(siteJson);
		}

	}
	
	public static void loadSiteFromJson(Site site) throws IOException {
		File siteDir = new File("." + File.separator + DATA_FOLDER_NAME + File.separator + SITE_DATA_FOLDER_NAME);
		siteDir.mkdirs();
		File siteFile = new File(siteDir, "Site.json");
		siteFile.createNewFile();
		Gson gson = new Gson();
		StringBuilder siteJson = new StringBuilder();

		try(Scanner reader = new Scanner(siteFile)){
			while(reader.hasNext()) {
				siteJson.append(reader.nextLine());
			}
		}
		site = gson.fromJson(siteJson.toString(), Site.class);		
	}

}
