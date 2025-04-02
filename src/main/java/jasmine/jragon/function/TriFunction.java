package jasmine.jragon.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<A, B, C, T> {
    T apply(A first, B second, C third);

    default <V> TriFunction<A, B, C, V> andThen(Function<? super T, ? extends V> after) {
        Objects.requireNonNull(after);
        return (a, b, c) -> after.apply(apply(a, b, c));
    }
}
