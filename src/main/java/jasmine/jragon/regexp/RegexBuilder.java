package jasmine.jragon.regexp;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public final class RegexBuilder {
    private static final long MAX_VALUE = 0x7ce66c50e283ffffL;

    private final StringBuilder patternBuilder;

    public RegexBuilder() {
        patternBuilder = new StringBuilder();
    }

    public RegexBuilder(@Language("regexp") @NotNull String start) {
        patternBuilder = new StringBuilder(start);
    }

    public RegexBuilder append(@Language("regexp") @NotNull String expression) {
        patternBuilder.append(expression);
        return this;
    }

    public RegexBuilder appendCharRange(char start, char end) {
        patternBuilder.append(String.format("[%c-%c]", start, end));
        return this;
    }

    public RegexBuilder appendNumberRange(long start, long end) {
        patternBuilder.append(generateNumberRange(start, end));
        return this;
    }

    public RegexBuilder appendCaptureGroup(@Language("regexp") @NotNull String expression) {
        patternBuilder.append(String.format("(%s)", expression));
        return this;
    }

    public RegexBuilder appendNonCaptureGroup(@Language("regexp") @NotNull String expression) {
        patternBuilder.append(String.format("(?:%s)", expression));
        return this;
    }

    public RegexBuilder or(@Language("regexp") @NotNull String expression) {
        patternBuilder.append("|").append(expression);
        return this;
    }

    public RegexBuilder oneOrMany(@Language("regexp") @NotNull String expression) {
        patternBuilder.append(expression).append("+");
        return this;
    }

    public RegexBuilder zeroOrMany(@Language("regexp") @NotNull String expression) {
        patternBuilder.append(expression).append("*");
        return this;
    }

    public RegexBuilder optional(@Language("regexp") @NotNull String expression) {
        patternBuilder.append(expression).append("?");
        return this;
    }

    public RegexBuilder lowerRange(int lower) {
        patternBuilder.append(String.format("{%d}", lower));
        return this;
    }

    public RegexBuilder lowerRangeToMany(int lower) {
        patternBuilder.append(String.format("{%d,}", lower));
        return this;
    }

    public RegexBuilder lowerToUpperRange(int lower, int upper) {
        patternBuilder.append(String.format("{%d,%d}", lower, upper));
        return this;
    }

    public Regex build() {
        return new Regex(patternBuilder.toString());
    }

    public Regex build(String text) {
        return new Regex(patternBuilder.toString(), text);
    }

    public Regex anchoredBuild() {
        patternBuilder.insert(0, "^").append("$");
        return new Regex(patternBuilder.toString());
    }

    public Regex anchoredBuild(String text) {
        patternBuilder.insert(0, "^").append("$");
        return new Regex(patternBuilder.toString(), text);
    }

    public Regex boundedBuild() {
        patternBuilder.insert(0, "\\b").append("\\b");
        return new Regex(patternBuilder.toString());
    }

    public Regex boundedBuild(String text) {
        patternBuilder.insert(0, "\\b").append("\\b");
        return new Regex(patternBuilder.toString(), text);
    }

    @NotNull
    @Language("regexp")
    public static String generateNegToPosRange(long bound)
            throws IllegalArgumentException {
        return String.format("0|-?(?:%s)", generateNumberRange(1, bound));
    }

    @NotNull
    @Language("regexp")
    public static String generateNumberRange(long start, long end)
            throws IllegalArgumentException {
        if (start < 0 || end < 0) throw new IllegalArgumentException("Bounds cannot be negative");
        if (start > end) throw new IllegalArgumentException("Start must be smaller than the end");
        if (start > MAX_VALUE || end > MAX_VALUE) throw new IllegalArgumentException("Value is too large");

        List<Long> ranges = addRanges(start, end);
        StringBuilder builder = new StringBuilder();

        int stop = ranges.size() - 1;
        String from, to;
        for(int i = 0; i < stop; i++) {
            from = ranges.get(i).toString();
            to = String.valueOf(ranges.get(i + 1) - 1);
            createSingleRange(builder, from, to);
        }
        builder.setLength(builder.length() - 1);
        List<String> partitions = asList(
                builder.toString()
                        .replaceAll("\\[0-9]", "\\\\d")
                        .split("\\|")
        );
        Collections.reverse(partitions);
        return String.join("|", partitions);
    }

    private static List<Long> addRanges(long start, long end) {
        long increment = 1L;
        long next = start;
        boolean isHigher = true;
        List<Long> ranges = new ArrayList<>();
        ranges.add(start);

        do {
            next += increment;
            if (next + increment > end) {
                if (next <= end)
                    ranges.add(next);

                increment /= 10;
                isHigher = false;
            } else if (next % (increment*10) == 0) {
                ranges.add(next);
                increment = isHigher ? increment*10 : increment/10;
            }
        } while (isHigher || increment >= 10);
        ranges.add(end + 1);
        return ranges;
    }

    private static void createSingleRange(StringBuilder builder, String from, String to) {
        int size = from.length();
        for (int j = 0; j < size; j++) {
            char fromChar = from.charAt(j);
            char toChar = to.charAt(j);
            if (fromChar == toChar) {
                builder.append(fromChar);
            } else {
                builder.append("[").append(fromChar)
                        .append("-").append(toChar).append("]");
            }
        }
        builder.append("|");
    }
}