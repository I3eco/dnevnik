package bg.dnevnik.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import bg.dnevnik.Site;
import bg.dnevnik.User;
import bg.dnevnik.User.Admin;
import bg.dnevnik.exceptions.IncorrectInputException;
import bg.dnevnik.exceptions.UserDoesNotExistException;

public class ContentGenerator {

	private static final List<String> categories = Arrays.asList(
			"news", "politics", "sport", "europe", "travel", "entertainment", "kids", 
			"agriculture", "nature", "world", "culture", "business");
	
	private static final List<String> NAMES = Arrays.asList("baikiro", "domatite", "krastavica", "magdalenka", 
			"kiler", "djordjano", "todorka", "silviq", "hlebche");
	
	private static final String content = "Sigh view am high neat half to what. Sent late held than set why wife our. If an blessing building steepest. Agreement distrusts mrs six affection satisfied. Day blushes visitor end company old prevent chapter. Consider declared out expenses her concerns. No at indulgence conviction particular unsatiable boisterous discretion. Direct enough off others say eldest may exeter she. Possible all ignorant supplied get settling marriage recurred. " + 
			 "Admiration we surrounded possession frequently he. Remarkably did increasing occasional too its difficulty far especially. Known tiled but sorry joy balls. Bed sudden manner indeed fat now feebly. Face do with in need of wife paid that be. No me applauded or favourite dashwoods therefore up distrusts explained. " + 
			 "Started earnest brother believe an exposed so. Me he believing daughters if forfeited at furniture. Age again and stuff downs spoke. Late hour new nay able fat each sell. Nor themselves age introduced frequently use unsatiable devonshire get. They why quit gay cold rose deal park. One same they four did ask busy. Reserved opinions fat him nay position. Breakfast as zealously incommode do agreeable furniture. One too nay led fanny allow plate. " + 
			 "Feet evil to hold long he open knew an no. Apartments occasional boisterous as solicitude to introduced. Or fifteen covered we enjoyed demesne is in prepare. In stimulated my everything it literature. Greatly explain attempt perhaps in feeling he. House men taste bed not drawn joy. Through enquire however do equally herself at. Greatly way old may you present improve. Wishing the feeling village him musical. " + 
			 "Yourself off its pleasant ecstatic now law. Ye their mirth seems of songs. Prospect out bed contempt separate. Her inquietude our shy yet sentiments collecting. Cottage fat beloved himself arrived old. Grave widow hours among him no you led. Power had these met least nor young. Yet match drift wrong his our. " + 
			 "Folly was these three and songs arose whose. Of in vicinity contempt together in possible branched. Assured company hastily looking garrets in oh. Most have love my gone to this so. Discovered interested prosperous the our affronting insipidity day. Missed lovers way one vanity wishes nay but. Use shy seemed within twenty wished old few regret passed. Absolute one hastened mrs any sensible. " + 
			 "Improve him believe opinion offered met and end cheered forbade. Friendly as stronger speedily by recurred. Son interest wandered sir addition end say. Manners beloved affixed picture men ask. Explain few led parties attacks picture company. On sure fine kept walk am in it. Resolved to in believed desirous unpacked weddings together. Nor off for enjoyed cousins herself. Little our played lively she adieus far sussex. Do theirs others merely at temper it nearer. " + 
			 "Ask especially collecting terminated may son expression. Extremely eagerness principle estimable own was man. Men received far his dashwood subjects new. My sufficient surrounded an companions dispatched in on. Connection too unaffected expression led son possession. New smiling friends and her another. Leaf she does none love high yet. Snug love will up bore as be. Pursuit man son musical general pointed. It surprise informed mr advanced do outweigh. " + 
			 "Acceptance middletons me if discretion boisterous travelling an. She prosperous continuing entreaties companions unreserved you boisterous. Middleton sportsmen sir now cordially ask additions for. You ten occasional saw everything but conviction. Daughter returned quitting few are day advanced branched. Do enjoyment defective objection or we if favourite. At wonder afford so danger cannot former seeing. Power visit charm money add heard new other put. Attended no indulged marriage is to judgment offering landlord. " + 
			 "Ladyship it daughter securing procured or am moreover mr. Put sir she exercise vicinity cheerful wondered. Continual say suspicion provision you neglected sir curiosity unwilling. Simplicity end themselves increasing led day sympathize yet. General windows effects not are drawing man garrets. Common indeed garden you his ladies out yet. Preference imprudence contrasted to remarkably in on. Taken now you him trees tears any. Her object giving end sister except oppose.";

	private final int articleCount;
	private final int userCount;
	private static final Random rnd = new Random();

	public ContentGenerator(int articleCount, int userCount) {
		this.articleCount = articleCount;
		this.userCount = userCount;
	}

	public void start() {
		for (int count = 0; count < userCount; count++) {
			try {
				User user = generateRandomUser();

//				if (rnd.nextInt(100) < 100) {
//					User admin = Site.getInstance().signIn("veso@gmail.com", "nekazvam");
//					if (admin instanceof Admin) {
//						((Admin) admin).makeUserAuthor(user);
//						System.out.println(user.getName() + " is now an author");
//					}
//					else {
//						System.err.println("veso@gmail.com IS NOT AN ADMIN?! Aborted!");
//						break;
//					}
//				} 
			}
			// TODO temporary, until tested
			catch (UserDoesNotExistException e) {
				e.printStackTrace(); 
			}
		}

		for (int i = 0; i < 100; i++) {
			Site.getInstance().getRandomAuthor().doRandomAction();
			Site.getInstance().getRandomUser().doRandomAction();
			Site.getInstance().getRandomUser().doRandomAction();
			Site.getInstance().getRandomUser().doRandomAction();
		}

	}

	private User generateRandomUser() throws UserDoesNotExistException {
		String username = getRandomName();

		if (rnd.nextInt(100) < 60) {
			username += getRandomName();
		}

		username += rnd.nextInt(100);

		String email = username + rnd.nextInt(100) + "@gmail.com";
		String password = username.substring(0, username.length() / 2) + rnd.nextInt(300);
		Site.getInstance().signUp(username, email, password);
		return Site.getInstance().signIn(email, password);
	}

	private String getRandomName() {
		return NAMES.get(rnd.nextInt(NAMES.size()));
	}

	public static String generateContent(int contentLength) {
		int startIndex = rnd.nextInt(content.length() - contentLength);
		return content.substring(startIndex, startIndex + contentLength);
	}

	public static String getRandomCategory() {
		return categories.get(rnd.nextInt(categories.size()));
	}

	public static Collection<String> getRandomKeywords() {
		List<String> keywords = new LinkedList<String>();
		int keywordsCount = rnd.nextInt(5);
		for (int count = 0; count < keywordsCount; count++) {
			keywords.add(generateContent(10));
		}
		return keywords;
	}

}
