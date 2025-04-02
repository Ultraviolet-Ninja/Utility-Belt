package jasmine.jragon.trigger;

import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

public final class ConcurrentClock<E> extends ActionClock<E> {
    private final LongAdder counter = new LongAdder();

    public ConcurrentClock(int threshold, E target, Consumer<E> action) {
        super(threshold, target, action);
    }

    @Override
    public void tick() {
        counter.increment();
        if (counter.sum() >= threshold) {
            synchronized (this) {
                if (counter.sum() >= threshold) {
                    action.accept(target);
                    counter.reset();
                }
            }
        }
    }

    @Override
    public void triggerAndReset() {
        synchronized (this) {
            action.accept(target);
            counter.reset();
        }
    }
}
