package jasmine.jragon.optionality;

import jasmine.jragon.optionality.function.BooleanConsumer;

import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class OptionalBool {
    private static final OptionalBool EMPTY = new OptionalBool();

    private final int bits;

    private OptionalBool() {
        bits = 0;
    }

    private OptionalBool(boolean value) {
        int bits = 1 << 1;
        if (value)
            bits |= 1;
        this.bits = bits;
    }

    public static OptionalBool empty() {
        return EMPTY;
    }

    public static OptionalBool of(boolean value) {
        return new OptionalBool(value);
    }

    public boolean isPresent() {
        return (bits & 2) != 0;
    }

    public boolean isEmpty() {
        return (bits & 2) == 0;
    }

    public boolean test() {
        if (isPresent()) {
            return (bits & 1) == 1;
        }
        throw new NoSuchElementException("No value present");
    }

    public void ifPresent(BooleanConsumer action) {
        if (isPresent()) {
            action.accept(test());
        }
    }

    public void ifPresentOrElse(BooleanConsumer action, Runnable emptyAction) {
        if (isPresent()) {
            action.accept(test());
        } else {
            emptyAction.run();
        }
    }

    public Stream<Boolean> stream() {
        if (isPresent()) {
            return Stream.of(test());
        } else {
            return Stream.empty();
        }
    }

    public boolean orElse(boolean other) {
        return isPresent() ? test() : other;
    }

    public boolean orElseGet(BooleanSupplier supplier) {
        return isPresent() ? (bits & 1) == 1 : supplier.getAsBoolean();
    }

    public boolean orElseThrow() {
        if (isPresent()) {
            return (bits & 1) == 1;
        }
        throw new NoSuchElementException("No value present");
    }

    public<X extends Throwable> boolean orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return (bits & 1) == 1;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof OptionalBool) {
            var other = (OptionalBool) obj;
            return this.bits == other.bits;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return isPresent() ? Boolean.hashCode((bits & 1) == 1) : 0;
    }

    @Override
    public String toString() {
        return isPresent()
                ? ("OptionalBool[" + ((bits & 1) == 1) + "]")
                : "OptionalBool.empty";
    }
}
