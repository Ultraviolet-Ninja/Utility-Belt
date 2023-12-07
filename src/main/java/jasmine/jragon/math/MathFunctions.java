package jasmine.jragon.math;

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
}
