package crypto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimpleSubstitution {

	private static String ciphertext;
	private static Map<Character, Character> keyMap = new HashMap<>(); // plain to cipher
	private static Map<Character, Integer> frequencyMap = new HashMap<>();

	private final static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	private static Scanner scanner = new Scanner(System.in);

	private static String guess(String key) {
		// remove spaces
		key = key.replaceAll("\\s", "");

		// edge case check
		if (key.length() != alphabet.length) {
			System.out.println("Use " + alphabet.length + " letter permutation. You have " + key.length());
			return null;
		}

		String temporary = ciphertext;
		char[] arr = key.toCharArray();

		for (int i = 0; i < alphabet.length; i++)
			keyMap.put(alphabet[i], arr[i]);

		for (Character i : keyMap.keySet())
			temporary = temporary.replace(keyMap.get(i), i);

		return temporary;

	}

	private static void control() {
		
		System.out.print("> ");
		String command = scanner.nextLine();

		if (command.length() >= 4) {

			switch (command.substring(0, 4)) {

			case "key ":
				System.out.println("Test result: " + guess(command.substring(4)));
				control();
				break;

			case "map ":
				command = command.replaceAll("\\s", "");
				System.out.println(map(command.charAt(3), command.charAt(4)));
				control();
				break;

			case "done":
				System.out.println("\n" + keyMap.keySet());
				System.out.println(keyMap.values());
				System.out.println("Top is plaintext and bottom is corresponding ciphertext");
				
				System.out.println("Is it correct? Terminate or continue?");
				
				if (terminate())
					return;
				
				control();
				break;

			case "help":
				help();
				control();
				break;

			default:
				System.out.println("Command not understood\n");
				control();
			}
		} else {
			System.out.println("Not valid input");
			control();
		}
	}
	
	private static boolean terminate() {
		
		System.out.println("Type 'y' to terminate. Type 'n' to continue.");
		System.out.print("> ");
		String choice = scanner.nextLine();
		
		if (choice.equals("y"))
			return true;
		
		System.out.println();
		
		return false;
		
	}

	private static void help() {
		System.out.println("To try a full key, type key <the 26 letter permutation>");
		System.out.println("To try a one to one map, type map <uppercase ciphertext letter to replace> <what to replace with>");
		System.out.println("Type \"done\" to output key if plaintext makes sense");
		System.out.println("Type help to see this again");
	}

	private static String map(char toReplace, char sub) {
		keyMap.put(sub, toReplace);
		ciphertext = ciphertext.replace(toReplace, sub);
		return ciphertext;
	}

	private static void charFrequency() {
		String temporary = ciphertext.replaceAll("\\s", "");

		for (Character i : alphabet)
			frequencyMap.put(Character.toUpperCase(i), 0);

		for (Character j : temporary.toCharArray())
			frequencyMap.replace(j, frequencyMap.get(j) + 1);

		System.out.println("\nCiphertext character frequency:");

		print(frequencyMap.keySet());
		print(frequencyMap.values());
	}
	
	private static <E> void print(Collection<E> col) {
		for (E e : col)
			System.out.print(e + "\t");
		
		System.out.println();

	}
	
	public static void main(String[] args) {
		System.out.println("Enter ciphertext:");
		ciphertext = scanner.nextLine();

		ciphertext = ciphertext.toUpperCase();

		charFrequency();

		help();

		control();

		scanner.close();

	}

}
