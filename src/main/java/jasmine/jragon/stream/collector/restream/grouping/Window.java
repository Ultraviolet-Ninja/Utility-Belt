package jasmine.jragon.stream.collector.restream.grouping;

import jasmine.jragon.stream.collector.CustomCollector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Window<T, C extends Collection<T>> extends CustomCollector<T, Stream.Builder<C>, Stream<C>> {
    @Contract(pure = true)
    @Override
    default @NotNull Supplier<Stream.Builder<C>> supplier() {
        return Stream::builder;
    }

    @Contract(pure = true)
    @Override
    default @NotNull BinaryOperator<Stream.Builder<C>> combiner() {
        return (left, right) -> {
            right.build().forEach(left::add);
            return left;
        };
    }

    @Contract(pure = true)
    @Override
    default @NotNull Function<Stream.Builder<C>, Stream<C>> finisher() {
        return Stream.Builder::build;
    }

    @Override
    default Set<Characteristics> characteristics() {
        return NONE;
    }
}
