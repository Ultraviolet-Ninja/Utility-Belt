package jasmine.jragon.stream.collector.set;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class LinkedHashSetCollector<K> implements SetCollector<K, LinkedHashSet<K>> {
    @Contract(pure = true)
    @Override
    public @NotNull Supplier<LinkedHashSet<K>> supplier() {
        return LinkedHashSet::new;
    }
}
