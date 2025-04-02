package jasmine.jragon.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface QuadFunction<A, B, C, D, T> {
    T apply(A first, B second, C third, D fourth);

    default <V> QuadFunction<A, B, C, D, V> andThen(Function<? super T, ? extends V> after) {
        Objects.requireNonNull(after);
        return (a, b, c, d) -> after.apply(apply(a, b, c, d));
    }
}
