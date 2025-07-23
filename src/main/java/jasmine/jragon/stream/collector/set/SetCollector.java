package jasmine.jragon.stream.collector.set;

import jasmine.jragon.stream.collector.DataStructureCollector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public sealed interface SetCollector<K, S extends Set<K>> extends DataStructureCollector<K, S>
        permits EnumSetCollector, LinkedHashSetCollector, TreeSetCollector {
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
