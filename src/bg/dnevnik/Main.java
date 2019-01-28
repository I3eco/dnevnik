package bg.dnevnik;

import java.util.Scanner;

import bg.dnevnik.exceptions.UserDoesNotExistException;

public class Main {

	public static void main(String[] args) {

		new ConsoleCommandsManager().start();

	}
}
