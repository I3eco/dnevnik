package bg.dnevnik.utility;

import bg.dnevnik.exceptions.IncorrectInputException;

public class Validation {

	private Validation() {
		// This class is just for helper static methods, so no instances will be allowed 
	}

	public static void throwIfNull(Object ...objects) throws IncorrectInputException {
		for (int index = 0; index < objects.length; index++) {
			if (objects[index] == null) {
				throw new IncorrectInputException("Elements are null!");
			}
		}
	}

	public static void throwIfEmpty(String ...strings) throws IncorrectInputException {
		for (int index = 0; index < strings.length; index++) {
			if (strings[index].equals("")) {
				throw new IncorrectInputException("Input is empty!");
			}
		}
	}
}
