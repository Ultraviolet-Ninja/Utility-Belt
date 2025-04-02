package jasmine.jragon.tuple.type;

import jasmine.jragon.function.QuadConsumer;
import jasmine.jragon.function.QuadFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public record Quad<A, B, C, D>(A first, B second, C third, D fourth) {
    @Contract("_ -> new")
    public <E> @NotNull Quad<E, B, C, D> mapFirst(@NotNull Function<A, E> mapper) {
        return new Quad<>(mapper.apply(first), second, third, fourth);
    }

    @Contract("_ -> new")
    public <E> @NotNull Quad<A, E, C, D> mapSecond(@NotNull Function<B, E> mapper) {
        return new Quad<>(first, mapper.apply(second), third, fourth);
    }

    @Contract("_ -> new")
    public <E> @NotNull Quad<A, B, E, D> mapThird(@NotNull Function<C, E> mapper) {
        return new Quad<>(first, second, mapper.apply(third), fourth);
    }

    @Contract("_ -> new")
    public <E> @NotNull Quad<A, B, C, E> mapFourth(@NotNull Function<D, E> mapper) {
        return new Quad<>(first, second, third, mapper.apply(fourth));
    }

    @Contract("_, _, _, _ -> new")
    public <E, F, G, H> @NotNull Quad<E, F, G, H> map(@NotNull Function<A, E> firstMapper,
                                                @NotNull Function<B, F> secondMapper,
                                                @NotNull Function<C, G> thirdMapper,
                                                @NotNull Function<D, H> fourthMapper) {
        return new Quad<>(
                firstMapper.apply(first),
                secondMapper.apply(second),
                thirdMapper.apply(third),
                fourthMapper.apply(fourth)
        );
    }

    public <T> T reduce(@NotNull QuadFunction<A, B, C, D, T> reduceFunction) {
        return reduceFunction.apply(first, second, third, fourth);
    }

    public void sendTo(@NotNull QuadConsumer<? super A, ? super B, ? super C, ? super D> action) {
        action.accept(first, second, third, fourth);
    }

    @Contract(" -> new")
    public @NotNull Trio<B, C, D> removeFirst() {
        return new Trio<>(second, third, fourth);
    }

    @Contract(" -> new")
    public @NotNull Trio<A, C, D> removeSecond() {
        return new Trio<>(first, third, fourth);
    }

    @Contract(" -> new")
    public @NotNull Trio<A, B, D> removeThird() {
        return new Trio<>(first, second, fourth);
    }

    @Contract(" -> new")
    public @NotNull Trio<A, B, C> removeFourth() {
        return new Trio<>(first, second, third);
    }

    @Contract("_, _, _, _ -> new")
    public static <T, U, V, X> @NotNull Quad<T, U, V, X> of(T first, U second, V third, X fourth) {
        return new Quad<>(first, second, third, fourth);
    }
}
