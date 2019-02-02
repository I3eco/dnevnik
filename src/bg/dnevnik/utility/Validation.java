package bg.dnevnik.utility;

import bg.dnevnik.exceptions.IncorrectInputException;

public class Validation {

	private Validation() {
		// This class is just for helper static methods, so no instances will be allowed 
	}

	public static void throwIfNull(Object ...objects) throws IncorrectInputException {
		if (objects == null) {
			throw new IncorrectInputException("Input is null!");
		}
		
		for (int index = 0; index < objects.length; index++) {
			if (objects[index] == null) {
				throw new IncorrectInputException("An element is null!");
			}
		}
	}

	public static void throwIfNullOrEmpty(String ...strings) throws IncorrectInputException {
		if (strings == null) {
			throw new IncorrectInputException("Input is null!");
		}
		
		for (int index = 0; index < strings.length; index++) {
			if (strings[index] == null) {
				throw new IncorrectInputException("An element is null!");
			}
			if (strings[index].equals("")) {
				throw new IncorrectInputException("An element is empty!");
			}
		}
	}
}
