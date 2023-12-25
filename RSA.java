package crypto;

import java.util.Scanner;

public class RSA {
	private static int extendEuclid(int x, int y) {
		if (x == 0 && y == 0)
			return 0;

		int[] g = ((y == 0) ? new int[]  {0, y, x} : new int[] { 0, x, y });
		int[] a = { 0, 1, 0 };
		int[] b = { 0, 0, 1 };
		int q;

		do {
			shiftArr(g, g[1] % g[2]);
			q = g[0] / g[1];
			shiftArr(a, a[1] - q * a[2]);
			shiftArr(b, b[1] - q * b[2]);
		} while (g[2] > 0);
		
		// only want a
		int ret = a[1];
		
		if (a[2] > a[1])
			ret += a[2];
		
		return ret;
	}

	private static void shiftArr(int[] arr, int finalVal) {
		for (int i = 0; i < arr.length - 1; i++)
			arr[i] = arr[i + 1];

		arr[arr.length - 1] = finalVal;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input p: ");
		int p = scanner.nextInt();

		System.out.println("Input q: ");
		int q = scanner.nextInt();

		System.out.println("Input e: ");
		int e = scanner.nextInt();

		scanner.close();

		// e^-1 mod (totient n = p-1 * q-1)
		System.out.println("Smallest d > 0: " + extendEuclid(e, (p - 1) * (q - 1)));
	}
}
