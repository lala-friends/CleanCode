package main.java.chp4;

import java.util.Arrays;

public class PrimeGenerator {
    private static boolean[] crossedOut;
    private static int[] result;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(generatePrimes(100)));
    }

    public static int[] generatePrimes(int maxValue) {
        if (maxValue < 2) {
            return new int[0];
        } else {
            uncrossIntegersUpTo(maxValue);
            crossOutMultiples();
            putUncrossedIntegersIntoResults();
            return result;
        }
    }

    private static void putUncrossedIntegersIntoResults() {
        result = new int[numberOfUncrossedIntegers()];

        for (int j = 0, i = 2; i < crossedOut.length; i++) {
            if (notCrossed(i)) {
                result[j++] = i;
            }
        }
    }

    private static int numberOfUncrossedIntegers() {
        int count = 0;
        for (int i = 2; i < crossedOut.length; i++) {
            if (notCrossed(i)) {
                count += 1;
            }
        }

        return count;
    }

    private static void crossOutMultiples() {
        int limit = determineIterationLimit();
        for (int i = 2; i <= limit; i++) {
            if (notCrossed(i)) {
                crossOutMultiplesOf(i);
            }
        }
    }

    private static void crossOutMultiplesOf(int i) {
        for (int multiple = 2 * i; multiple < crossedOut.length; multiple += i) {
            crossedOut[multiple] = true;
        }
    }

    private static boolean notCrossed(int i) {
        return !crossedOut[i];
    }

    private static int determineIterationLimit() {
        double iterationLimit = Math.sqrt(crossedOut.length);
        return (int) iterationLimit;
    }

    private static void uncrossIntegersUpTo(int maxValue) {
        crossedOut = new boolean[maxValue + 1];
        for (int i = 2; i < crossedOut.length; i++) {
            crossedOut[i] = false;
        }
    }
}