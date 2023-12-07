package jasmine.jragon.stream.collector.map;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class LinkedHashMapCollector<T, K, V> implements MapCollector<T, K, V, LinkedHashMap<K, V>> {
    @NonNull
    private final Function<T, K> keyMapper;
    @NonNull
    private final Function<T, V> valueMapper;

    @Override
    public Supplier<LinkedHashMap<K, V>> supplier() {
        return LinkedHashMap::new;
    }

    @Override
    public BiConsumer<LinkedHashMap<K, V>, T> accumulator() {
        return (map, type) -> map.put(
                keyMapper.apply(type), valueMapper.apply(type)
        );
    }
}
