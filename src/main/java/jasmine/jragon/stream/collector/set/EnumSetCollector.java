package jasmine.jragon.stream.collector.set;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class EnumSetCollector<K extends Enum<K>> implements SetCollector<K, EnumSet<K>> {
    @NonNull
    private final Class<K> clazz;

    @Contract(pure = true)
    @Override
    public @NotNull Supplier<EnumSet<K>> supplier() {
        return () -> EnumSet.noneOf(clazz);
    }
}
