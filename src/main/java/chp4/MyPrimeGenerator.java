package main.java.chp4;

import java.util.Arrays;

public class MyPrimeGenerator {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(generatePrimes(100)));
    }
    public static int[] generatePrimes(int maxValue) {
        if (maxValue < 2) return new int[0];

        boolean[] isPrime = initializeArray(maxValue);

        boolean[] calculatedPrime = calculate(maxValue, isPrime);

        int primeCount = getPrimeCount(calculatedPrime);

        return getPrimes(primeCount, calculatedPrime);
    }

    private static int getPrimeCount(boolean[] calculatedPrime) {
        int count = 0;
        for (int index = 0; index < calculatedPrime.length; index++) {
            if (calculatedPrime[index]) count++;
        }
        return count;
    }

    private static int[] getPrimes(int primeCount, boolean[] calculatedPrime) {
        int[] primes = new int[primeCount];
        for (int index = 0, j = 0; index < calculatedPrime.length; index++) {
            if (calculatedPrime[index]) {
                primes[j++] = index;
            }
        }
        return primes;
    }

    private static boolean[] calculate(int maxValue, boolean[] isPrime) {
        for (int index = 2; index < Math.sqrt(maxValue) + 1; index++) {
            if (!isPrime[index]) continue;
            for (int targetIndex = index * 2; targetIndex <= maxValue; targetIndex += index) {
                isPrime[targetIndex] = false;
            }
        }

        return isPrime;
    }

    private static boolean[] initializeArray(int maxValue) {
        int arraySize = maxValue + 1;
        boolean[] isPrime = new boolean[arraySize];

        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;

        return isPrime;
    }
}
