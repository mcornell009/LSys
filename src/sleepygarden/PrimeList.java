package sleepygarden;

import java.util.ArrayList;
import java.util.List;

public class PrimeList {

	public static void sievePrimes(int N) { // n being amound of ints

		// initially assume all integers are prime
		boolean[] isPrime = new boolean[N + 1];
		for (int i = 2; i <= N; i++) {
			isPrime[i] = true;
		}

		// mark non-primes <= N using Sieve of Eratosthenes
		for (int i = 2; i * i <= N; i++) {

			// if i is prime, then mark multiples of i as nonprime
			// suffices to consider mutiples i, i+1, ..., N/i
			if (isPrime[i]) {
				for (int j = i; i * j <= N; j++) {
					isPrime[i * j] = false;
				}
			}
		}

		// count primes
		int primes = 0;
		for (int i = 2; i <= N; i++) {
			if (isPrime[i])
				primes++;
		}
		System.out.println("The number of primes <= " + N + " is " + primes);
	}

	public static List<Integer> getPrimes() {
		int l = 10000000, n = 2, sqrt = (int) Math.sqrt(l); //
		boolean[] nums = new boolean[l + 1];
		int[] primes = new int[664579];

		while (n <= sqrt) {
			for (int i = 2 * n; i <= l; nums[i] = true, i += n)
				;
			for (n++; nums[n]; n++)
				;
		}

		for (int i = 2, k = 0; i < nums.length; i++)
			if (!nums[i])
				primes[k++] = i;
		System.out.println("finished prime list init");

		List<Integer> ret = new ArrayList<Integer>(primes.length);
		for (int p : primes)
			ret.add(p);
		return ret;
	}

}