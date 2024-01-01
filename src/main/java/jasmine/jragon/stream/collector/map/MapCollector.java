package jasmine.jragon.stream.collector.map;

import jasmine.jragon.stream.collector.CustomCollector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public sealed interface MapCollector<T, K, V, M extends Map<K, V>> extends CustomCollector<T, M, M>
        permits EnumMapCollector, LinkedHashMapCollector, TreeMapCollector {
    @Override
    default Function<M, M> finisher() {
        return Function.identity();
    }

    @Override
    default Set<Characteristics> characteristics() {
        return IDENTITY;
    }

    @Override
    default BinaryOperator<M> combiner() {
        return (leftMap, rightMap) -> {
            leftMap.putAll(rightMap);
            return leftMap;
        };
    }

    @Contract("_, _ -> new")
    static <T, K, V> @NotNull LinkedHashMapCollector<T, K, V> toLinkedHashMap(Function<T, K> keyMapper,
                                                                              Function<T, V> valueMapper) {
        return new LinkedHashMapCollector<>(keyMapper, valueMapper);
    }

    @Contract("_ -> new")
    static <T, V> @NotNull LinkedHashMapCollector<T, T, V> toLinkedHashMap(Function<T, V> valueMapper) {
        return new LinkedHashMapCollector<>(Function.identity(), valueMapper);
    }

    @Contract("_, _ -> new")
    static <T, K extends Comparable<K>, V> @NotNull TreeMapCollector<T, K, V> toTreeMap(Function<T, K> keyMapper,
                                                                                        Function<T, V> valueMapper) {
        return new TreeMapCollector<>(keyMapper, valueMapper);
    }

    @Contract("_ -> new")
    static <T extends Comparable<T>, V> @NotNull TreeMapCollector<T, T, V> toTreeMap(Function<T, V> valueMapper) {
        return new TreeMapCollector<>(Function.identity(), valueMapper);
    }

    @Contract("_, _, _ -> new")
    static <T, K extends Enum<K>, V> @NotNull EnumMapCollector<T, K, V> toEnumMap(Class<K> clazz,
                                                                                  Function<T, K> keyMapper,
                                                                                  Function<T, V> valueMapper) {
        return new EnumMapCollector<>(clazz, keyMapper, valueMapper);
    }

    @Contract("_, _ -> new")
    static <T extends Enum<T>, V> @NotNull EnumMapCollector<T, T, V> toEnumMap(Class<T> clazz,
                                                                               Function<T, V> valueMapper) {
        return new EnumMapCollector<>(clazz, Function.identity(), valueMapper);
    }
}
