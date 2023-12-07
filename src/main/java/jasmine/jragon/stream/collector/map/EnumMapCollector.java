package jasmine.jragon.stream.collector.map;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class EnumMapCollector<T, K extends Enum<K>, V> implements MapCollector<T, K, V, EnumMap<K, V>> {
    @NonNull
    private final Class<K> clazz;
    @NonNull
    private final Function<T, K> keyMapper;
    @NonNull
    private final Function<T, V> valueMapper;

    @Contract(pure = true)
    @Override
    public @NotNull Supplier<EnumMap<K, V>> supplier() {
        return () -> new EnumMap<>(clazz);
    }

    @Contract(pure = true)
    @Override
    public @NotNull BiConsumer<EnumMap<K, V>, T> accumulator() {
        return (map, type) -> map.put(
                keyMapper.apply(type),
                valueMapper.apply(type)
        );
    }
}
