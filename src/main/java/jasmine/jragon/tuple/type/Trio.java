package jasmine.jragon.tuple.type;

import jasmine.jragon.function.TriConsumer;
import jasmine.jragon.function.TriFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record Trio<A, B, C>(A first, B second, C third) {
    @Contract("_ -> new")
    public <D> @NotNull Quad<D, A, B, C> addFirst(D newFirst) {
        return new Quad<>(newFirst, this.first, this.second, third);
    }

    @Contract("_ -> new")
    public <D> @NotNull Quad<A, D, B, C> addSecond(D newSecond) {
        return new Quad<>(this.first, newSecond, this.second, third);
    }

    @Contract("_ -> new")
    public <D> @NotNull Quad<A, B, D, C> addThird(D newThird) {
        return new Quad<>(this.first, this.second, newThird, this.third);
    }

    @Contract("_ -> new")
    public <D> @NotNull Quad<A, B, C, D> add(D fourth) {
        return new Quad<>(this.first, this.second, this.third, fourth);
    }

    @Contract("_ -> new")
    public <D> @NotNull Trio<D, B, C> mapFirst(@NotNull Function<A, D> mapper) {
        return new Trio<>(mapper.apply(first), second, third);
    }

    @Contract("_ -> new")
    public <D> @NotNull Trio<A, D, C> mapSecond(@NotNull Function<B, D> mapper) {
        return new Trio<>(first, mapper.apply(second), third);
    }

    @Contract("_ -> new")
    public <D> @NotNull Trio<A, B, D> mapThird(@NotNull Function<C, D> mapper) {
        return new Trio<>(first, second, mapper.apply(third));
    }

    @Contract("_, _, _ -> new")
    public <D, E, F> @NotNull Trio<D, E, F> map(@NotNull Function<A, D> firstMapper,
                                                @NotNull Function<B, E> secondMapper,
                                                @NotNull Function<C, F> thirdMapper) {
        return new Trio<>(
                firstMapper.apply(first),
                secondMapper.apply(second),
                thirdMapper.apply(third)
        );
    }

    public <T> T reduce(@NotNull TriFunction<A, B, C, T> reduceFunction) {
        return reduceFunction.apply(first, second, third);
    }

    public <T, U> Duo<T, U> pairDown(@NotNull TriFunction<A, B, C, Duo<T, U>> reduceFunction) {
        return reduceFunction.apply(first, second, third);
    }

    public void sendTo(@NotNull TriConsumer<? super A, ? super B, ? super C> action) {
        action.accept(first, second, third);
    }

    public Duo<B, C> removeFirst() {
        return new Duo<>(second, third);
    }

    public Duo<A, C> removeSecond() {
        return new Duo<>(first, third);
    }

    public Duo<A, B> removeThird() {
        return new Duo<>(first, second);
    }

    @Override
    public String toString() {
        return String.format("First Element %s%nSecond Element %s%nThird Element %s%n", first, second, third);
    }

    @Contract("_, _, _ -> new")
    public static <T, U, V> @NotNull Trio<T, U, V> of(T first, U second, V third) {
        return new Trio<>(first, second, third);
    }
}
