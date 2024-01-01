package jasmine.jragon.tuple.type;

import jasmine.jragon.function.TriFunction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public final class Trio<A, B, C> {
    private final A first;
    private final B second;
    private final C third;

    @Contract("_ -> new")
    public <D> @NotNull Trio<D, B, C> mapFirst(@NotNull Function<A, D> mapper) {
        return new Trio<>(mapper.apply(first), second, third);
    }

    @Contract("_ -> new")
    public <D> @NotNull Trio<A, D, C> mapSecond(@NotNull Function<B, D> mapper) {
        return new Trio<>(first, mapper.apply(second), third);
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
