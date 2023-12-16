package jasmine.jragon.math;

import java.util.stream.LongStream;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public final class MathFunctions {
    public static boolean isPerfectSquare(long num) {
        if (num <= 0) {
            return false;
        }
        double root = Math.sqrt(num);
        return (root - Math.floor(root)) == 0.0;
    }

    public static int digitalRoot(long number) {
        long temp = Math.abs(number);
        long root = 0;

        while (temp > 0 || root > 9) {
            if (temp == 0) {
                temp = root;
                root = 0;
            }

            root += temp % 10;
            temp /= 10;
        }
        return (int) root;
    }

    public static int digitalRoot(double number) {
        long temp = Long.parseLong(String.valueOf(number).replace(".", ""));
        return digitalRoot(temp);
    }

    public static boolean isAnInteger(double number) {
        return number % 1 == 0.0;
    }

    public static double roundToNPlaces(double number, int places) {
        double factor = pow(10.0, places);
        return round(number * factor) / factor;
    }

    public static int negativeSafeModulo(int number, final int modulus) {
        while(number < 0) number += modulus;
        return number % modulus;
    }

    public static long reverseNumber(long number) {
        long output = 0;
        while (number > 0) {
            output *= 10;
            int temp = (int) (number % 10);
            output += temp;
            number /= 10;
        }
        return output;
    }

    //Calculating combinations that involve 2 elements from a given set
    public static long calculateCombinatorialSize(int n) {
        return Math.abs(factorial(n) / (factorial(n - 2) << 1));
    }

    public static long calculateCombinatorialSize(int n, int r) {
        if (r == 2)
            return calculateCombinatorialSize(n);
        return Math.abs(factorial(n) / (factorial(n - r) * factorial(r)));
    }

    public static long calculatePermutationSize(int n, int r) {
        if (r == 2)
            return calculateCombinatorialSize(n);
        return Math.abs(factorial(n) / (factorial(n - r)));
    }

    public static long factorial(int n) {
        return LongStream.range(2, n)
                .reduce((a, b) -> a * b)
                .orElse(1);
    }

    public static BigInt factorialBigInt(int n) {
        return LongStream.range(2, n)
                .mapToObj(BigInt::of)
                .reduce((l, r) -> {
                    l.mul(r);
                    return l;
                })
                .orElse(BigInt.ONE);
    }
}
