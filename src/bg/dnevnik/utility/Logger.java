package bg.dnevnik.utility;

public class Logger {
	private static boolean isEnabled = true;
	
	public static void pritnToConsole(String log) {
		if(isEnabled) {
			System.out.println(log);
		}
	}

	public static boolean isEnabled() {
		return isEnabled;
	}

	public static void setEnabled(boolean isEnabled) {
		Logger.isEnabled = isEnabled;
	}
}
