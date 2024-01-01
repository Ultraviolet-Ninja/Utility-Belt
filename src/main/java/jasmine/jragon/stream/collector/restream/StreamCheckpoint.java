package jasmine.jragon.stream.collector.restream;

import jasmine.jragon.stream.collector.CustomCollector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * My way of bringing the ideas of the Gatherer functionality proposed in JDK 22 back into older flavors of Java (Before discovering they were going to be a thing).
 * Instead of performing downstream operations, I'm proposing a procedure of collecting to a {@link Stream.Builder}, accumulating elements of type {@link T}, mapping them to some output type {@link R}, and "restarting" the stream again.
 * <p>
 * This allows for operations like grouping by windowing or by key similar to how {@link java.util.stream.Collectors#groupingBy(Function)} works, although much more seamlessly if you're trying to group by a key and need to re-stream.
 * <p>
 *
 * I have no understanding of the performance or memory implications of this way of doing things, but for my needs, it gets the job done.
 * @param <T> The incoming element type
 * @param <R> The outgoing type of the new Stream
 */
public interface StreamCheckpoint<T, R> extends CustomCollector<T, Stream.Builder<R>, Stream<R>> {
    @Contract(pure = true)
    @Override
    default @NotNull Supplier<Stream.Builder<R>> supplier() {
        return Stream::builder;
    }

    @Contract(pure = true)
    @Override
    default @NotNull BinaryOperator<Stream.Builder<R>> combiner() {
        return (left, right) -> {
            right.build().forEach(left::add);
            return left;
        };
    }

    @Contract(pure = true)
    @Override
    default @NotNull Function<Stream.Builder<R>, Stream<R>> finisher() {
        return Stream.Builder::build;
    }

    @Override
    default Set<Characteristics> characteristics() {
        return NONE;
    }
}