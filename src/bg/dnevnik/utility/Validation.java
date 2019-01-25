package bg.dnevnik.utility;

import bg.dnevnik.exceptions.WrongInputException;

public class Validation {

	private Validation() {
		// This class is just for helper static methods, so no instances will be allowed 
	}

	public static void checkIfNull(Object ...objects) throws WrongInputException {
		for (int index = 0; index < objects.length; index++) {
			if (objects[index] == null) {
				throw new WrongInputException("Elements are null!");
			}
		}
	}
	
	
	
}
