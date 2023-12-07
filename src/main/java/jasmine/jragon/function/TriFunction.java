package jasmine.jragon.function;

@FunctionalInterface
public interface TriFunction<A, B, C, T> {
    T apply(A first, B second, C third);
}
