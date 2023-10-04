package jasmine.jragon.transcode;

import jasmine.jragon.math.BigInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.IntStream;

public final class CustomBaseTranscoder {
    private final char[] codexString;

    @Contract(pure = true)
    public CustomBaseTranscoder(@NotNull String codexString) {
        Objects.requireNonNull(codexString);

        this.codexString = codexString.toCharArray();
        checkElementUniqueness(this.codexString);
    }

    private static void checkElementUniqueness(char[] codexString) {
        long count = IntStream.range(0, codexString.length)
                .mapToObj(i -> codexString[i])
                .distinct()
                .count();

        if (count != codexString.length) {
            throw new IllegalArgumentException("String must contain all unique characters");
        }
    }

    public @NotNull String encrypt(@NotNull BigInt number) {
        final int size = codexString.length;
        final BigInt bigIntSize = new BigInt(size);
        char encryptedLetter;

        if (number.compareTo(bigIntSize) < 0) {
            encryptedLetter = codexString[extractModulus(number, size)];
            return String.valueOf(encryptedLetter);
        }
        StringBuilder sb = new StringBuilder();
        BigInt numberCopy = number.copy();

        while (numberCopy.compareTo(bigIntSize) >= 0) {
            encryptedLetter = codexString[extractModulus(numberCopy, size)];
            sb.append(encryptedLetter);
            numberCopy.div(bigIntSize);
        }

        encryptedLetter = codexString[numberCopy.intValue()];

        return sb.append(encryptedLetter)
                .reverse()
                .toString();
    }

    public String decrypt(String cipherText) {
        int degree = cipherText.length() - 1;
        BigInt output = new BigInt(0);
        String searchString = String.valueOf(codexString);

        for (char letter : cipherText.toCharArray()) {
            int value = searchString.indexOf(letter);

            if (value < 0) {
                throw new IllegalArgumentException("Detected letter not in the codex");
            }

            int decodedValue = (int) Math.pow(codexString.length, degree--) * value;
            output.add(decodedValue);
        }
        return output.toString();
    }

    public int getBase() {
        return codexString.length;
    }

    private static int extractModulus(BigInt number, int mod) {
        BigInt temp = number.copy();

        temp.mod(new BigInt(mod));
        return temp.intValue();
    }
}
