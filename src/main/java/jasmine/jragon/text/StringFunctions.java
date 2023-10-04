package jasmine.jragon.text;

import org.jetbrains.annotations.NotNull;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public final class StringFunctions {
    public static String convertFirstLetterCapital(String sample) {
        return sample.length() > 1 ?
                sample.substring(0, 1).toUpperCase() + sample.substring(1).toLowerCase() :
                sample.toUpperCase();
    }

    public static String toTitleCase(String text) {
        return stream(text.split("[-_ ]"))
                .map(StringFunctions::convertFirstLetterCapital)
                .collect(joining(" "));
    }

    public static String toScreamSnakeCase(String text) {
        return stream(text.split("[- ]"))
                .map(String::toUpperCase)
                .collect(joining("_"));
    }
    public static @NotNull String createOrdinalNumber(long number) {
        if (number < 0)
            throw new IllegalArgumentException("Number cannot be negative");
        long mod = number % 100;

        if (mod == 1) return number + "st";
        if (mod == 2) return number + "nd";
        if (mod == 3) return number + "rd";
        return number + "th";
    }
}
