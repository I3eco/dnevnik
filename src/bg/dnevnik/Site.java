package bg.dnevnik;

import java.util.Collection;
import java.util.Map;

public class Site {
	
	private String name;
	private Collection<User> users;
	private Map<String, Collection<Article>> articlesByCategory;
}
