package bg.dnevnik;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import bg.dnevnik.exceptions.UserDoesNotExistException;

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
		
		// The other approach would be to just make all fields and methods static, 
		// and have no instances at all
		
		Site.instance = new Site();
	}
	
	private Site() {
		// TODO decide on the correct collections
		users = new LinkedList<User>();
	}

	public static Site getInstance() {
		return Site.instance;
	}

	public void addUser(User user) {
		users.add(user);
	}

	public User signIn(String email, String password) throws UserDoesNotExistException {
		for (User user : users) {
			if(user.loginInfoMatches(email, password)) {
				user.goOnline();
				return user;
			}
		}
		throw new UserDoesNotExistException("There is no user with that email or password!");
	}
	
	public String getName() {
		return this.name;
		
	}
	
}
