package jasmine.jragon.enumerated;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true, cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public final class EnumeratedElement<T> implements Comparable<EnumeratedElement<T>> {
    private final int index;
    @NonNull
    private final T element;

    @Override
    public String toString() {
        return String.format("{Index: %d - Element: %s}", index, element);
    }

    @Override
    public int compareTo(@NotNull EnumeratedElement o) {
        return Integer.compare(this.index, o.index);
    }

    @Contract(pure = true)
    public int compareElements(@NotNull EnumeratedElement<T> other,
                               @NotNull Comparator<T> comparator) {
        return comparator.compare(other.element, this.element);
    }
}
