package jasmine.jragon.optionality;

import jasmine.jragon.optionality.function.CharConsumer;
import jasmine.jragon.optionality.function.CharSupplier;

import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class OptionalChar {
    private static final OptionalChar EMPTY = new OptionalChar();

    private final boolean isPresent;
    private final char value;

    private OptionalChar() {
        isPresent = false;
        value = 0;
    }

    private OptionalChar(char value) {
        isPresent = true;
        this.value = value;
    }

    public static OptionalChar empty() {
        return EMPTY;
    }

    public static OptionalChar of(char value) {
        return new OptionalChar(value);
    }

    public static OptionalChar of(int value) {
        return new OptionalChar((char) value);
    }

    public boolean isPresent() {
        return isPresent;
    }

    public boolean isEmpty() {
        return !isPresent;
    }

    public char get(){
        if (isPresent) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public void ifPresent(CharConsumer action) {
        if (isPresent) {
            action.accept(value);
        }
    }

    public void ifPresentOrElse(CharConsumer action, Runnable emptyAction) {
        if (isPresent) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public Stream<Character> stream() {
        if (isPresent()) {
            return Stream.of(value);
        } else {
            return Stream.empty();
        }
    }

    public IntStream streamCodePoint() {
        if (isPresent()) {
            return IntStream.of(value);
        } else {
            return IntStream.empty();
        }
    }

    public char orElse(char other) {
        return isPresent ? value : other;
    }

    public char orElseGet(CharSupplier supplier) {
        return isPresent ? value : supplier.get();
    }

    public char orElseThrow() {
        if (isPresent) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public<X extends Throwable> char orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof OptionalChar) {
            var other = (OptionalChar) obj;
            return this.isPresent == other.isPresent
                    && this.value == other.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return isPresent ? Character.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent()
                ? ("OptionalChar[" + value + "]")
                : "OptionalChar.empty";
    }
}
