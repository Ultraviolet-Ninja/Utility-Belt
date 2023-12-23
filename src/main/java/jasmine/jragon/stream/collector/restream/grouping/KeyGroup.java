package jasmine.jragon.stream.collector.restream.grouping;

import jasmine.jragon.stream.collector.CustomCollector;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface KeyGroup<T, K, V, C extends Collection<V>, S>
        extends CustomCollector<T, Map<K, C>, Stream<S>> {
    @Contract(pure = true)
    @Override
    default Supplier<Map<K, C>> supplier() {
        return LinkedHashMap::new;
    }

    @Contract(pure = true)
    @Override
    default BinaryOperator<Map<K, C>> combiner() {
        return (left, right) -> {
            boolean determineCaller = left.size() > right.size();
            var caller = determineCaller ? right : left;
            var callee = determineCaller ? left : right;
            caller.forEach((key, value) -> combine(callee, key, value));
            caller.clear();
            return callee;
        };
    }

    private static <K, V, C extends Collection<V>> void combine(Map<K, C> map, K key, C value) {
        if (map.containsKey(key)) {
            map.get(key).addAll(value);
        } else {
            map.put(key, value);
        }
        value.clear();
    }

    @Override
    default Set<Characteristics> characteristics() {
        return NONE;
    }
}
