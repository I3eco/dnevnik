package bg.dnevnik;

import java.util.Collection;

public class User {
	
	private String name;
	private Collection<Comment> commentHistory;
	private String email;
	private String pasword;
	private boolean isOnline;
	
	public void addToCommentHistory(Comment comment) {
		this.commentHistory.add(comment);
	}
	
	
}
