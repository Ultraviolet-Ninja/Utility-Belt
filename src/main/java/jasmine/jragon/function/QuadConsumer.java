package jasmine.jragon.function;

import java.util.Objects;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D> {
    void accept(A a, B b, C c, D d);

    default QuadConsumer<A, B, C, D> andThen(QuadConsumer<? super A, ? super B, ? super C, ? super D> after) {
        Objects.requireNonNull(after);

        return (f, s, t, ft) -> {
            accept(f, s, t, ft);
            after.accept(f, s, t, ft);
        };
    }
}
