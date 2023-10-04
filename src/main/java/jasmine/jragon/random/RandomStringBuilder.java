package jasmine.jragon.random;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public final class RandomStringBuilder implements Comparable<RandomStringBuilder>, CharSequence {
    private static final Random RNG = new Random();

    private final StringBuilder stringBuilder;

    public RandomStringBuilder() {
        stringBuilder = new StringBuilder();
    }

    public RandomStringBuilder(String start) {
        stringBuilder = new StringBuilder(start);
    }

    public RandomStringBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }

    public RandomStringBuilder append(String str) {
        stringBuilder.append(str);
        return this;
    }

    public RandomStringBuilder append(boolean b) {
        stringBuilder.append(b);
        return this;
    }


    public RandomStringBuilder append(char c) {
        stringBuilder.append(c);
        return this;
    }


    public RandomStringBuilder append(int i) {
        stringBuilder.append(i);
        return this;
    }


    public RandomStringBuilder append(long lng) {
        stringBuilder.append(lng);
        return this;
    }


    public RandomStringBuilder append(float f) {
        stringBuilder.append(f);
        return this;
    }


    public RandomStringBuilder append(double d) {
        stringBuilder.append(d);
        return this;
    }

    public RandomStringBuilder appendRandomChars(int repeat) {
        if (repeat < 1) return this;
        String generated = RNG.ints(0, Character.MAX_VALUE)
                .limit(repeat)
                .mapToObj(num -> (char) num)
                .map(String::valueOf)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomChars(char start, char end, int repeat) {
        if (repeat < 1) return this;
        StringBuilder generated = RNG.ints(start, end + 1)
                .limit(repeat)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomNumbers(int repeat) {
        if (repeat < 1) return this;
        String generated = RNG.ints()
                .limit(repeat)
                .mapToObj(String::valueOf)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomNumbers(int start, int end, int repeat) {
        if (repeat < 1) return this;
        String generated = RNG.ints(start, end + 1)
                .limit(repeat)
                .mapToObj(String::valueOf)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomBinaryNumbers(int repeat) {
        if (repeat < 1) return this;
        String generated = RNG.ints()
                .limit(repeat)
                .mapToObj(Integer::toBinaryString)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomBinaryNumbers(int start, int end, int repeat) {
        if (repeat < 1) return this;
        String generated = RNG.ints(start, end + 1)
                .limit(repeat)
                .mapToObj(Integer::toBinaryString)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomHexString(int repeat) {
        if (repeat < 1) return this;
        String generated = RNG.ints()
                .limit(repeat)
                .mapToObj(Integer::toHexString)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomHexString(int start, int end, int repeat) {
        if (repeat < 1) return this;
        String generated = RNG.ints(start, end + 1)
                .limit(repeat)
                .mapToObj(Integer::toHexString)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder appendRandomLetter(String str, int repeat) {
        if (str.isEmpty() || repeat < 1)
            return this;
        if (str.length() == 1) {
            stringBuilder.append(str.repeat(repeat));
            return this;
        }

        String generated = RNG.ints(0, str.length())
                .limit(repeat)
                .mapToObj(str::charAt)
                .map(String::valueOf)
                .collect(joining());
        stringBuilder.append(generated);
        return this;
    }

    public RandomStringBuilder reverse() {
        stringBuilder.reverse();
        return this;
    }

    public RandomStringBuilder replace(CharSequence target, CharSequence replacement) {
        String replacedString = stringBuilder.toString().replace(target, replacement);
        clear();
        stringBuilder.append(replacedString);
        return this;
    }

    public RandomStringBuilder replaceAll(@Language("regexp") String regex, String replacement) {
        String replacedString = stringBuilder.toString().replaceAll(regex, replacement);
        clear();
        stringBuilder.append(replacedString);
        return this;
    }

    public RandomStringBuilder[] split(@Language("regexp") String regex) {
        return stream(stringBuilder.toString().split(regex))
                .map(RandomStringBuilder::new)
                .toArray(RandomStringBuilder[]::new);
    }

    public void clear() {
        stringBuilder.setLength(0);
    }

    @Override
    public int length() {
        return stringBuilder.length();
    }

    @Override
    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }

    @Override
    public @NotNull CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }

    @Override
    public @NotNull String toString() {
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(RandomStringBuilder o) {
        return stringBuilder.compareTo(o.stringBuilder);
    }

    @Override
    public int hashCode() {
        return stringBuilder.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!((o instanceof RandomStringBuilder) || (o instanceof StringBuilder) || (o instanceof String)))
            return false;

        if (o instanceof String) return stringBuilder.toString().equals(o);
        return stringBuilder.toString().equals(o.toString());
    }
}
