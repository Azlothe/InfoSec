package crypto;

import java.util.Scanner;

public class A5_1 {
	// format as x, y, z
	private static final int[] majorityBit = { 8, 10, 10 };

	private static final int[] xStepBit = { 13, 16, 17, 18 };
	private static final int[] yStepBit = { 20, 21 };
	private static final int[] zStepBit = { 7, 20, 21, 22 };

	private static final int[][] stepBit = { xStepBit, yStepBit, zStepBit };

	// format as x, y, z
	private static final int[] sizes = { 19, 22, 23 };

	private static int[] key = new int[3];

	private static int majority(int[] bits) {
		int count = 0;

		for (Integer i : bits)
			count += (i > 0) ? 1 : -1;

		return (count > 0) ? 1 : 0;
	}

	private static int getBitAtPos(int pos, int num) {
		num >>= pos;
		return num & 1;
	}

	/**
	 * assumptions: posBits are ordered from least to greatest
	 */
	private static int[] getBitAtPos(int[] posBits, int num, int size) {
		int[] ret = new int[posBits.length];
		
		int finalIndex = posBits.length - 1;

		num >>= (size - posBits[finalIndex] - 1);
		ret[finalIndex] = num & 1;

		for (int i = finalIndex - 1; i >= 0; i--) {
			num >>= (posBits[i + 1] - posBits[i]);
			ret[i] = num & 1;
		}

		return ret;

	}

	private static int step(int[] stepBits, int register, int size) {
		
		int[] bits = getBitAtPos(stepBits, register, size);

		int p = xor(bits);
		if (p == 0)
			register >>>= 1;
		else
			register = insertAtHead(1, register, size);

		return register;
	}

	private static int insertAtHead(int bitAdd, int number, int size) {
		number >>= 1;
		bitAdd <<= (size - 1);
		return number | bitAdd;
	}

	private static int xor(int[] bits) {
		int count = 0;
		for (Integer i : bits)
			count += i;

		return count % 2;
	}

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.println("Input X:");
		key[0] = Integer.parseInt(scanner.nextLine(), 2);

		System.out.println("Input Y:");
		key[1] = Integer.parseInt(scanner.nextLine(), 2);

		System.out.println("Input Z:");
		key[2] = Integer.parseInt(scanner.nextLine(), 2);

		System.out.println("Input desired length:");
		int rounds = scanner.nextInt();
		int[] keystream = new int[rounds];

		scanner.close();

		int maj[] = new int[3];

		for (int i = 0; i < rounds; i++) {

			for (int j = 0; j < maj.length; j++)
				maj[j] = getBitAtPos(sizes[j] - majorityBit[j] - 1, key[j]);

			int majBit = majority(maj);

			for (int j = 0; j < maj.length; j++)
				if (maj[j] == majBit)
					key[j] = step(stepBit[j], key[j], sizes[j]);

			int[] keystreamBit = new int[3];
			for (int j = 0; j < keystreamBit.length; j++)
				keystreamBit[j] = key[j] & 1;

			keystream[i] = xor(keystreamBit);
		}

		StringBuilder build = new StringBuilder();
		for (Integer i : keystream)
			build.append(i);

		System.out.println("\nKeystream: " + build);
		System.out.println("New X: " + String.format("%19s", Integer.toBinaryString(key[0])).replace(' ', '0'));
		System.out.println("New Y: " + String.format("%22s", Integer.toBinaryString(key[1])).replace(' ', '0'));
		System.out.println("New Z: " + String.format("%23s", Integer.toBinaryString(key[2])).replace(' ', '0'));
	}
}
