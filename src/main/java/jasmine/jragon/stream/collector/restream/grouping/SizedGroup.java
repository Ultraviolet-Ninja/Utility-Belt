package jasmine.jragon.stream.collector.restream.grouping;

import jasmine.jragon.stream.collector.CustomCollector;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SizedGroup<T, C extends Collection<T>>
        implements CustomCollector<T, Stream.Builder<C>, Stream<C>> {
    private final int groupSize;
    private final Supplier<C> collectionSupplier;

    private C currentCollection;

    @Contract(pure = true)
    @Override
    public @NotNull Supplier<Stream.Builder<C>> supplier() {
        return Stream::builder;
    }

    @Contract(pure = true)
    @Override
    public @NotNull BiConsumer<Stream.Builder<C>, T> accumulator() {
        return (cBuilder, type) -> {
            if (currentCollection == null || currentCollection.size() % groupSize == 0) {
                currentCollection = collectionSupplier.get();
                cBuilder.add(currentCollection);
            }

            currentCollection.add(type);
        };
    }

    @Contract(pure = true)
    @Override
    public @NotNull BinaryOperator<Stream.Builder<C>> combiner() {
        return (left, right) -> {
            right.build().forEach(left::add);
            return left;
        };
    }

    @Contract(pure = true)
    @Override
    public @NotNull Function<Stream.Builder<C>, Stream<C>> finisher() {
        return Stream.Builder::build;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return NONE;
    }

    @Contract("_ -> new")
    public static <T> @NotNull SizedGroup<T, List<T>> group(int groupSize) {
        return new SizedGroup<>(groupSize, ArrayList::new);
    }

    @Contract("_, _ -> new")
    public static <T, C extends List<T>> @NotNull SizedGroup<T, C> groupToLists(int groupSize,
                                                                                Supplier<C> listSupplier) {
        return new SizedGroup<>(groupSize, listSupplier);
    }

    @Contract("_, _ -> new")
    public static <T, C extends Set<T>> @NotNull SizedGroup<T, C> groupToSets(int groupSize,
                                                                              Supplier<C> setSupplier) {
        return new SizedGroup<>(groupSize, setSupplier);
    }

    @Contract("_, _ -> new")
    public static <T, C extends Collection<T>> @NotNull SizedGroup<T, C> groupToSpecified(int groupSize,
                                                                                          Supplier<C> collectionSupplier) {
        return new SizedGroup<>(groupSize, collectionSupplier);
    }
}
