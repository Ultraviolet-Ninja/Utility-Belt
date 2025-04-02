package jasmine.jragon.trigger;

import java.util.function.Consumer;

public sealed abstract class ActionClock<E> permits ConcurrentClock, SimpleClock {
    protected final int threshold;
    protected final E target;
    protected final Consumer<E> action;

    ActionClock(int threshold, E target, Consumer<E> action) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }

        this.threshold = threshold;
        this.target = target;
        this.action = action;
    }

    public abstract void tick();
    public abstract void triggerAndReset();
}
