package jasmine.jragon.stream.collector.restream.grouping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@RequiredArgsConstructor(staticName = "provide")
public final class KeylessGroup<T, K, V, C extends Collection<V>>
        implements KeyGroup<T, K, V, C, C> {
    @NonNull
    private final Function<T, K> groupKeyMapper;
    @NonNull
    private final Function<T, V> groupValueMapper;
    @NonNull
    private final Supplier<C> collectionSupplier;
    @NonNull
    private final UnaryOperator<C> collectionModifier;

    private final Stream.Builder<C> runningGroupStream = Stream.builder();

    private KeylessGroup(Function<T, K> groupKeyMapper, Function<T, V> groupValueMapper,
                         Supplier<C> collectionSupplier) {
        this(groupKeyMapper, groupValueMapper, collectionSupplier, UnaryOperator.identity());
    }

    @Override
    public BiConsumer<Map<K, C>, T> accumulator() {
        return (map, type) -> {
            K key = groupKeyMapper.apply(type);
            V value = groupValueMapper.apply(type);
            if (map.containsKey(key)) {
                map.get(key).add(value);
            } else {
                C collection = collectionSupplier.get();
                collection.add(value);
                runningGroupStream.accept(collectionModifier.apply(collection));
                map.put(key, collection);
            }
        };
    }

    @Override
    public Function<Map<K, C>, Stream<C>> finisher() {
        return ignored -> runningGroupStream.build()
                .filter(collection -> !collection.isEmpty());
    }

    @Contract("_ -> new")
    public static <T, K> @NotNull KeylessGroup<T, K, T, List<T>> provide(Function<T, K> keyMapper) {
        return new KeylessGroup<>(keyMapper, Function.identity(), ArrayList::new, Collections::unmodifiableList);
    }

    @Contract("_, _ -> new")
    public static <T, K, V> @NotNull KeylessGroup<T, K, V, List<V>> provide(Function<T, K> keyMapper,
                                                                            Function<T, V> valueMapper) {
        return new KeylessGroup<>(keyMapper, valueMapper, ArrayList::new, Collections::unmodifiableList);
    }

    @Contract("_, _ -> new")
    public static <T, K, C extends Collection<T>> @NotNull KeylessGroup<T, K, T, C> provide(Function<T, K> keyMapper,
                                                                                            Supplier<C> collectionSupplier) {
        return new KeylessGroup<>(keyMapper, Function.identity(), collectionSupplier);
    }

    @Contract("_, _ -> new")
    public static <T, K, V> @NotNull KeylessGroup<T, K, V, Set<V>> provideSet(Function<T, K> keyMapper,
                                                                              Function<T, V> valueMapper) {
        return new KeylessGroup<>(keyMapper, valueMapper, HashSet::new, Collections::unmodifiableSet);
    }

    @Contract("_ -> new")
    public static <T, K> @NotNull KeylessGroup<T, K, T, Set<T>> provideSet(Function<T, K> keyMapper) {
        return new KeylessGroup<>(keyMapper, Function.identity(), HashSet::new, Collections::unmodifiableSet);
    }
}
