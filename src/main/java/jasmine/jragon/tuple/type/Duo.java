package jasmine.jragon.tuple.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
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

    @Contract(value = " -> new", pure = true)
    public Map.@NotNull Entry<A, B> toEntry() {
        return new AbstractMap.SimpleEntry<>(first, second);
    }

    @Contract(value = " -> new", pure = true)
    public Map.@NotNull Entry<A, B> toImmutableEntry() {
        return new AbstractMap.SimpleImmutableEntry<>(first, second);
    }

    @Contract("_ -> new")
    public <C> @NotNull Trio<C, A, B> addFirst(C newFirst) {
        return new Trio<>(newFirst, this.first, this.second);
    }

    @Contract("_ -> new")
    public <C> @NotNull Trio<A, C, B> addSecond(C newSecond) {
        return new Trio<>(this.first, newSecond, this.second);
    }

    @Contract("_ -> new")
    public <C> @NotNull Trio<C, A, B> addToFirst(C head) {
        return new Trio<>(head, this.first, this.second);
    }

    @Contract("_ -> new")
    public <C> @NotNull Trio<A, C, B> addToSecond(C middle) {
        return new Trio<>(this.first, middle, this.second);
    }

    @Contract("_ -> new")
    public <C> @NotNull Trio<A, B, C> add(C third) {
        return new Trio<>(this.first, this.second, third);
    }

    public <T> T reduce(@NotNull BiFunction<A, B, T> reducer) {
        return reducer.apply(first, second);
    }

    public void sendTo(@NotNull BiConsumer<? super A, ? super B> action) {
        action.accept(first, second);
    }

    @Override
    public String toString() {
        return String.format("First Element %s%nSecond Element %s%n", first, second);
    }

    @Contract("_, _ -> new")
    public static <T, U> @NotNull Duo<T, U> of(T first, U second) {
        return new Duo<>(first, second);
    }

    @Contract("_ -> new")
    public static <T, U> @NotNull Duo<T, U> fromEntry(Map.@NotNull Entry<T, U> entry) {
        return new Duo<>(entry.getKey(), entry.getValue());
    }
}
