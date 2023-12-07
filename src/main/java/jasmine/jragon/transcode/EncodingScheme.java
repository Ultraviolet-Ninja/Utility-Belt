package jasmine.jragon.transcode;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class EncodingScheme {
    public static final String EXTENDED_ASCII_CODEX_STRING;

    static {
        var codexPrefix =
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ:;<>=?{}()[]`|~\"'!@#$%&*+,-./\\^_";

        var codexArray = codexPrefix.toCharArray();

        var firstPart = IntStream.range(0, codexPrefix.length())
                .mapToObj(i -> codexArray[i]);

        var string = IntStream.range(0, 256)
                .mapToObj(i -> (char)i);

        EXTENDED_ASCII_CODEX_STRING = Stream.concat(firstPart, string)
                .distinct()
                .map(String::valueOf)
                .collect(joining());
    }

    private EncodingScheme() {}
}