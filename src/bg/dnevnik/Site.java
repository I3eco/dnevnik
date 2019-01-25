package bg.dnevnik;

import java.util.Collection;
import java.util.Map;

public class Site {

	private static Site instance;
	private String name;
	private Collection<User> users;
	private Map<String, Collection<Article>> articlesByCategory;

	
	static {
		// static block added for the possibility of adding additional logic later
		
		// Also, this design is preferred right now over lazy initialization, 
		// to avoid problems with multithreaded calls to getInstance() and synchronization
		// The Site object shouldn't be heavy, since all the data gets added to it after anyway
		// Source:
		// https://www.ibm.com/developerworks/library/j-dcl/index.html
		
		Site.instance = new Site();
	}
	
	private Site() {
		
	}

	public static Site getInstance() {
		return Site.instance;
	}

	
}
