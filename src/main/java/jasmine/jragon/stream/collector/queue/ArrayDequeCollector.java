package jasmine.jragon.stream.collector.queue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayDeque;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class ArrayDequeCollector<T> implements QueueCollector<T, ArrayDeque<T>> {
    @Override
    public Supplier<ArrayDeque<T>> supplier() {
        return ArrayDeque::new;
    }
}
