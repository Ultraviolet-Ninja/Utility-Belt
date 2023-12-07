package jasmine.jragon.stream.collector.restream;

import jasmine.jragon.enumerated.EnumeratedElement;
import jasmine.jragon.stream.collector.CustomCollector;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
public final class ElementEnumerator<E> implements CustomCollector<E,
        Stream.Builder<EnumeratedElement<E>>, Stream<EnumeratedElement<E>>> {
    private int indexCounter = 0;

    @Contract(pure = true)
    @Override
    public @NotNull Supplier<Stream.Builder<EnumeratedElement<E>>> supplier() {
        return Stream::builder;
    }

    @Contract(pure = true)
    @Override
    public @NotNull BiConsumer<Stream.Builder<EnumeratedElement<E>>, E> accumulator() {
        return (builder, element) -> builder.accept(create(element));
    }

    @Override
    public BinaryOperator<Stream.Builder<EnumeratedElement<E>>> combiner() {
        return (left, right) -> {
            left.build().forEach(right::add);
            return right;
        };
    }

    @Contract(pure = true)
    @Override
    public @NotNull Function<Stream.Builder<EnumeratedElement<E>>,
            Stream<EnumeratedElement<E>>> finisher() {
        return Stream.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return NONE;
    }

    @Contract("_ -> new")
    private <T> @NotNull EnumeratedElement<T> create(T element) {
        return new EnumeratedElement<>(indexCounter++, element);
    }
}
