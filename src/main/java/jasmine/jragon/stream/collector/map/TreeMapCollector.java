package jasmine.jragon.stream.collector.map;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class TreeMapCollector<T, K extends Comparable<K>, V> implements MapCollector<T, K, V, TreeMap<K, V>> {
    @NonNull
    private final Function<T, K> keyMapper;
    @NonNull
    private final Function<T, V> valueMapper;

    @Override
    public Supplier<TreeMap<K, V>> supplier() {
        return TreeMap::new;
    }

    @Override
    public BiConsumer<TreeMap<K, V>, T> accumulator() {
        return (map, type) -> map.put(
                keyMapper.apply(type), valueMapper.apply(type)
        );
    }

    @Override
    public Set<Characteristics> characteristics() {
        return UNORDERED_IDENTITY;
    }
}
