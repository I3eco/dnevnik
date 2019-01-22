package bg.dnevnik;

import java.time.LocalDateTime;

public class Comment {
	
	private User poster;
	private LocalDateTime timeOfPosting;
	private String content;
	private int upvotesCnt;
	private int downvotesCnt;
}
