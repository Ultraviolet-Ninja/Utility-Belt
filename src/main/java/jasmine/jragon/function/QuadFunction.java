package jasmine.jragon.function;

@FunctionalInterface
public interface QuadFunction<A, B, C, D, T> {
    T apply(A first, B second, C third, D fourth);
}
