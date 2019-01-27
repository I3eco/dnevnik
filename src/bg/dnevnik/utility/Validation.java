package bg.dnevnik.utility;

import bg.dnevnik.exceptions.WrongInputException;

public class Validation {

	private Validation() {
		// This class is just for helper static methods, so no instances will be allowed 
	}

	public static void throwIfNull(Object ...objects) throws WrongInputException {
		for (int index = 0; index < objects.length; index++) {
			if (objects[index] == null) {
				throw new WrongInputException("Elements are null!");
			}
		}
	}

	public static void throwIfEmpty(String ...strings) throws WrongInputException {
		for (int index = 0; index < strings.length; index++) {
			if (strings[index].equals("")) {
				throw new WrongInputException("Input is empty!");
			}
		}
	}
}
