package jasmine.jragon.stream.collector.set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class TreeSetCollector<K extends Comparable<K>> implements SetCollector<K, TreeSet<K>> {
    @Contract(pure = true)
    @Override
    public @NotNull Supplier<TreeSet<K>> supplier() {
        return TreeSet::new;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return NONE;
    }
}
