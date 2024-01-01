package jasmine.jragon.tuple.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public final class Duo<A, B> {
    private final A first;
    private final B second;

    @Contract("_ -> new")
    public <C> @NotNull Duo<C, B> mapFirst(@NotNull Function<A, C> mapper) {
        return new Duo<>(mapper.apply(first), second);
    }

    @Contract("_ -> new")
    public <C> @NotNull Duo<A, C> mapSecond(@NotNull Function<B, C> mapper) {
        return new Duo<>(first, mapper.apply(second));
    }

    @Contract("_, _ -> new")
    public <C, D> @NotNull Duo<C, D> map(@NotNull Function<A, C> firstMapper, @NotNull Function<B, D> secondMapper) {
        return new Duo<>(
                firstMapper.apply(first),
                secondMapper.apply(second)
        );
    }

    @Contract("_ -> new")
    public <C> @NotNull Trio<A, B, C> add(C third) {
        return new Trio<>(this.first, this.second, third);
    }

    public <T> T reduce(@NotNull BiFunction<A, B, T> reducer) {
        return reducer.apply(first, second);
    }

    @Override
    public String toString() {
        return String.format("First Element %s%nSecond Element %s%n", first, second);
    }

    @Contract("_, _ -> new")
    public static <T, U> @NotNull Duo<T, U> of(T first, U second) {
        return new Duo<>(first, second);
    }
}
