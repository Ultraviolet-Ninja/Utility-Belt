package jasmine.jragon.stream.collector.queue;

import jasmine.jragon.stream.collector.DataStructureCollector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Queue;

public sealed interface QueueCollector<T, Q extends Queue<T>>
        extends DataStructureCollector<T, Q> permits ArrayDequeCollector, PriorityQueueCollector {
    @Contract(" -> new")
    static <T extends Comparable<T>> @NotNull PriorityQueueCollector<T> toComparablePriorityQueue() {
        return PriorityQueueCollector.ofComparable();
    }

    @Contract("_ -> new")
    static <T> @NotNull PriorityQueueCollector<T> toPriorityQueue(Comparator<T> comparator) {
        return new PriorityQueueCollector<>(comparator);
    }

    @Contract(" -> new")
    static <T> @NotNull ArrayDequeCollector<T> toArrayDeque() {
        return new ArrayDequeCollector<>();
    }
}
