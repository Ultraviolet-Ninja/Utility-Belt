package jasmine.jragon.enumerated;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public record EnumeratedElement<T>(int index, @NonNull T element) implements Comparable<EnumeratedElement<T>> {
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
