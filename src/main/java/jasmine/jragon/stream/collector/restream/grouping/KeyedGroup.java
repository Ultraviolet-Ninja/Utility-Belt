package jasmine.jragon.stream.collector.restream.grouping;

import jasmine.jragon.tuple.type.Duo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor(staticName = "provide")
public final class KeyedGroup<T, K, V, C extends Collection<V>>
        implements KeyGroup<T, K, V, C, Duo<K, C>> {
    @NonNull
    private final Function<T, K> groupKeyMapper;
    @NonNull
    private final Function<T, V> groupValueMapper;
    @NonNull
    private final Supplier<C> collectionSupplier;
    private final Stream.Builder<Duo<K, C>> runningGroupStream = Stream.builder();

    @Contract(pure = true)
    @Override
    public @NotNull BiConsumer<Map<K, C>, T> accumulator() {
        return (map, type) -> {
            K key = groupKeyMapper.apply(type);
            V value = groupValueMapper.apply(type);
            if (map.containsKey(key)) {
                map.get(key).add(value);
            } else {
                C collection = collectionSupplier.get();
                collection.add(value);
                runningGroupStream.accept(new Duo<>(key, collection));
                map.put(key, collection);
            }
        };
    }

    @Contract(pure = true)
    @Override
    public @NotNull Function<Map<K, C>, Stream<Duo<K, C>>> finisher() {
        return ignored -> runningGroupStream.build()
                .filter(KeyedGroup::hasElements);
    }

    private static <K, C extends Collection<?>> boolean hasElements(Duo<K, C> duo) {
        return !duo.getSecond().isEmpty();
    }

    @Contract("_ -> new")
    public static <T, K> @NotNull KeyedGroup<T, K, T, List<T>> provide(Function<T, K> keyMapper) {
        return new KeyedGroup<>(keyMapper, Function.identity(), ArrayList::new);
    }

    @Contract("_, _ -> new")
    public static <T, K, V> @NotNull KeyedGroup<T, K, V, List<V>> provide(Function<T, K> keyMapper,
                                                                          Function<T, V> valueMapper) {
        return new KeyedGroup<>(keyMapper, valueMapper, ArrayList::new);
    }

    @Contract("_, _ -> new")
    public static <T, K, C extends Collection<T>> @NotNull KeyedGroup<T, K, T, C> provide(Function<T, K> keyMapper,
                                                                                          Supplier<C> collectionSupplier) {
        return new KeyedGroup<>(keyMapper, Function.identity(), collectionSupplier);
    }

    @Contract("_, _, _ -> new")
    public static <T, K, V, S extends Set<V>> @NotNull KeyedGroup<T, K, V, S> provideSet(Function<T, K> keyMapper,
                                                                                         Function<T, V> valueMapper,
                                                                                         Supplier<S> collectionSupplier) {
        return new KeyedGroup<>(keyMapper, valueMapper, collectionSupplier);
    }

    @Contract("_, _ -> new")
    public static <T, K, V> @NotNull KeyedGroup<T, K, V, Set<V>> provideSet(Function<T, K> keyMapper,
                                                                            Function<T, V> valueMapper) {
        return new KeyedGroup<>(keyMapper, valueMapper, HashSet::new);
    }

    @Contract("_ -> new")
    public static <T, K> @NotNull KeyedGroup<T, K, T, Set<T>> provideSet(Function<T, K> keyMapper) {
        return new KeyedGroup<>(keyMapper, Function.identity(), HashSet::new);
    }
}
