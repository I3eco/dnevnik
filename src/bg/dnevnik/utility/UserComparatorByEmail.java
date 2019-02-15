package bg.dnevnik.utility;

import java.util.Comparator;

import bg.dnevnik.User;

public class UserComparatorByEmail implements Comparator<User>{

	@Override
	public int compare(User user1, User user2) {
		
		return user1.getEmail().compareTo(user2.getEmail());
	}

}
