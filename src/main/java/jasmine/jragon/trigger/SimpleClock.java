package jasmine.jragon.trigger;

import java.util.function.Consumer;

public final class SimpleClock<E> extends ActionClock<E> {
    private int counter = 0;

    public SimpleClock(int threshold, E target, Consumer<E> action) {
        super(threshold, target, action);
    }

    @Override
    public void tick() {
        if (counter++ >= threshold) {
            action.accept(target);
            counter = 0;
        }
    }

    @Override
    public void triggerAndReset() {
        action.accept(target);
        counter = 0;
    }
}
