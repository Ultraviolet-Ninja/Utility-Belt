package jasmine.jragon.stream.collector.restream.grouping;

import jasmine.jragon.stream.collector.restream.StreamCheckpoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A.k.a. Fixed windows proposed in JDK 22 - JEP 461
 *
 * @param <T> The incoming type
 * @param <C> The collection used to store the incoming type
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FixedWindow<T, C extends Collection<T>> implements StreamCheckpoint<T, C> {
    private final int groupSize;
    private final Supplier<C> collectionSupplier;

    private C currentCollection;

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

    @Contract("_ -> new")
    public static <T> @NotNull FixedWindow<T, List<T>> group(int groupSize) {
        checkNonPositiveSize(groupSize);
        return new FixedWindow<>(groupSize, ArrayList::new);
    }

    @Contract("_, _ -> new")
    public static <T, C extends List<T>> @NotNull FixedWindow<T, C> groupToLists(int groupSize,
                                                                                 Supplier<C> listSupplier) {
        checkNonPositiveSize(groupSize);
        return new FixedWindow<>(groupSize, listSupplier);
    }

    @Contract("_, _ -> new")
    public static <T, C extends Set<T>> @NotNull FixedWindow<T, C> groupToSets(int groupSize,
                                                                               Supplier<C> setSupplier) {
        checkNonPositiveSize(groupSize);
        return new FixedWindow<>(groupSize, setSupplier);
    }

    @Contract("_, _ -> new")
    public static <T, C extends Collection<T>> @NotNull FixedWindow<T, C> groupToSpecified(int groupSize,
                                                                                           Supplier<C> collectionSupplier) {
        checkNonPositiveSize(groupSize);
        return new FixedWindow<>(groupSize, collectionSupplier);
    }

    private static void checkNonPositiveSize(int windowSize) {
        if (windowSize < 2) {
            throw new IllegalArgumentException("Window size must be at least 2");
        }
    }
}
