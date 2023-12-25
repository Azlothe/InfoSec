package accesscontrol;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HammingIris {

	private static Map<String, String> irisData = new HashMap<>();
	private static double acceptThreshold = 0.32;

	private static int errorCount(long x, long y) {

		long diff = x ^ y;
		int counter = 0;

		while (diff != 0) {
			diff &= (diff - 1);
			++counter;
		}

		return counter;

	}

	private static boolean authenticate(String user, String inputVal) {

		String read = irisData.get(user);
		if (read == null) {
			System.out.println("User or iris data not found");
			return false;
		}

		long realVal = Long.parseUnsignedLong(read, 16);

		long inputValNum = Long.parseUnsignedLong(inputVal, 16);

		double hammingDistance = errorCount(realVal, inputValNum) / (read.length() * 4.0);
		System.out.println("Hamming Distance: " + hammingDistance);

		return (hammingDistance < acceptThreshold);

	}

	private static void help() {
		System.out.println("To enroll a user and their iris code, type enroll <user name> <iris data in hex>");
		System.out.println("To try authentication, type auth <user name> <test iris code in hex>");
		System.out.println("NOTE: Iris data accepted only in hex! The 0x prefix is not needed. User names are case sensitive!");
		System.out.println("Type terminate to terminate");
		System.out.println("Type help to see this again\n");
	}

	private static boolean commandSanitize(String input[], int requiredInput) {
		if (input.length != requiredInput) {
			System.out.println("Command not accepted. Please follow the format");
			return false;
		}

		String hex = input[--requiredInput];
		if (hex.startsWith("0x"))
			input[requiredInput] = hex.substring(2);
		
		return true;
	}

	public static void main(String[] args) {

		help();

		Scanner scanner = new Scanner(System.in);

		boolean terminate = false;

		while (!terminate) {
			String input = scanner.nextLine();

			String parameters[] = input.split(" ");

			if (parameters.length > 3) {
				System.out.println("Too many parameters!");
				continue;
			}

			switch (parameters[0]) {

			case "enroll":
				if (!commandSanitize(parameters, 3)) break;
				
				irisData.put(parameters[1], parameters[2]);
				System.out.println("Registered!");
				break;

			case "auth":
				if (!commandSanitize(parameters, 3)) break;
				
				System.out.println(authenticate(parameters[1], parameters[2]) ? "Authenticated!" : "Not accepted. Please try again");
				break;

			case "help":
				help();
				break;

			case "terminate":
				terminate = true;
				break;

			default:
				System.out.println("Could not understand command");
			}
			System.out.println();
		}

		scanner.close();

	}

}
