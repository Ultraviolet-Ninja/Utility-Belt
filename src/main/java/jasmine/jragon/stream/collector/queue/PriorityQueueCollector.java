package jasmine.jragon.stream.collector.queue;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class PriorityQueueCollector<T> implements QueueCollector<T, PriorityQueue<T>> {
    @NonNull
    private final Comparator<T> comparator;

    @Contract(pure = true)
    @Override
    public @NotNull Supplier<PriorityQueue<T>> supplier() {
        return () -> new PriorityQueue<>(comparator);
    }

    static <C extends Comparable<C>> PriorityQueueCollector<C> ofComparable() {
        return new PriorityQueueCollector<>(Comparable::compareTo);
    }
}
