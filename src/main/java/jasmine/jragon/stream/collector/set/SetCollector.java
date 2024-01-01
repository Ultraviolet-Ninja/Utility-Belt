package jasmine.jragon.stream.collector.set;

import jasmine.jragon.stream.collector.CustomCollector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public sealed interface SetCollector<K, S extends Set<K>> extends CustomCollector<K, S, S>
        permits EnumSetCollector, LinkedHashSetCollector, TreeSetCollector {
    @Override
    default Function<S, S> finisher() {
        return Function.identity();
    }

    @Override
    default Set<Characteristics> characteristics() {
        return IDENTITY;
    }

    @Override
    default BinaryOperator<S> combiner() {
        return (leftMap, rightMap) -> {
            leftMap.addAll(rightMap);
            return leftMap;
        };
    }

    @Override
    default BiConsumer<S, K> accumulator() {
        return Set::add;
    }

    static <K> @NotNull LinkedHashSetCollector<K> toLinkedHashSet() {
        return new LinkedHashSetCollector<>();
    }

    static <K extends Comparable<K>> @NotNull TreeSetCollector<K> toTreeSet() {
        return new TreeSetCollector<>();
    }

    @Contract("_ -> new")
    static <K extends Enum<K>> @NotNull EnumSetCollector<K> toEnumSet(Class<K> clazz) {
        return new EnumSetCollector<>(clazz);
    }
}
