package jasmine.jragon.stream.collector.restream;

import jasmine.jragon.enumerated.EnumeratedElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementEnumerator<E> implements StreamCheckpoint<E, EnumeratedElement<E>> {
    private int indexCounter = 0;

    @Contract(pure = true)
    @Override
    public @NotNull BiConsumer<Stream.Builder<EnumeratedElement<E>>, E> accumulator() {
        return (builder, element) -> builder.accept(create(element));
    }

    @Contract("_ -> new")
    private <T> @NotNull EnumeratedElement<T> create(T element) {
        return new EnumeratedElement<>(indexCounter++, element);
    }

    @Contract(" -> new")
    public static <T> @NotNull ElementEnumerator<T> enumerate() {
        return new ElementEnumerator<>();
    }

    @Contract("_ -> new")
    public static <T> @NotNull ElementEnumerator<T> enumerate(int startingValue) {
        return new ElementEnumerator<>(startingValue);
    }
}
