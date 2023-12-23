package jasmine.jragon.console;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScannerValidation {
    public static int validateInt(Scanner in) {
        while (!in.hasNextInt()) {
            in.nextLine();
        }
        return in.nextInt();
    }

    public static int validateInt(Scanner in, String errorMessage) {
        while (!in.hasNextInt()) {
            System.err.println(errorMessage);
            in.nextLine();
        }
        return in.nextInt();
    }

    public static double validateDouble(Scanner in) {
        while (!in.hasNextDouble()) {
            in.nextLine();
        }
        return in.nextDouble();
    }

    public static double validateDouble(Scanner in, String errorMessage) {
        while (!in.hasNextDouble()) {
            System.err.println(errorMessage);
            in.nextLine();
        }
        return in.nextDouble();
    }
}
