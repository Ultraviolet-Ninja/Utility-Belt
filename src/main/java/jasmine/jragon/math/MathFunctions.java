package jasmine.jragon.math;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public final class MathFunctions {
    public static boolean isPerfectSquare(long num) {
        double root = Math.sqrt(num);
        return (root - Math.floor(root)) == 0;
    }

    public static int digitalRoot(long number) {
        long root = 0;

        while (number > 0 || root > 9) {
            if (number == 0) {
                number = root;
                root = 0;
            }

            root += number % 10;
            number /= 10;
        }
        return (int) root;
    }

    public static int digitalRoot(double number) {
        number = Double.parseDouble(String.valueOf(number).replace(".", ""));
        return digitalRoot((long) number);
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
}
