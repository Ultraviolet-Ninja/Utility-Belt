package jasmine.jragon.range;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Range implements Iterable<Integer>, IRange {
    private final int start;
    private final int stop;
    private final int step;
    private final int size;

    public Range(int start, int stop, int step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.size = (Math.abs(stop) - Math.abs(start)) / Math.abs(step);
        if (stop == 0) {
            throw new IllegalArgumentException("Step size amount cannot be 0");
        }
    }

    public Range(int start, int stop) {
        this(start, stop, 1);
    }

    public Range(int stop) {
        this(0, stop, 1);
    }

    public static Range range(int stop) {
        return new Range(stop);
    }

    public static Range range(int start, int stop) {
        return new Range(start, stop);
    }

    public static Range range(int start, int stop, int step) {
        return new Range(start, stop, step);
    }

    public IntStream stream() {
        return toStream(this, false);
    }

    public IntStream parallelStream() {
        return toStream(this, true);
    }

    private static IntStream toStream(Range range, boolean parallel) {
        int size = range.size();
        return StreamSupport.intStream(
                Spliterators.spliterator(new RangeIterator(range), size, RANGE_SPLITERATOR_FLAGS),
                parallel
        );
    }

    public <T> Stream<T> map(IntFunction<? extends T> mapper) {
        return stream().mapToObj(mapper);
    }

    @Override
    public @NotNull Iterator<Integer> iterator() {
        return new RangeIterator(this);
    }

    public void forEach(IntConsumer action) {
        var iter = new RangeIterator(this);
        while (iter.hasNext()) {
            action.accept(iter.nextInt());
        }
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(new RangeIterator(this), size, RANGE_SPLITERATOR_FLAGS);
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("DuplicatedCode")
    private static final class RangeIterator implements PrimitiveIterator.OfInt {
        private final int stop, step;
        private int currentValue;
        private final boolean stopIsGreater;

        public RangeIterator(Range range) {
            this.stop = range.stop;
            this.step = range.step;
            this.currentValue = range.start;
            stopIsGreater = stop > currentValue;

            if (stopIsGreater && step < 0 || !stopIsGreater && step > 0) {
                throw new IllegalArgumentException("Infinite Loop Detected");
            }
        }

        @Override
        public int nextInt() {
            int temp = currentValue;
            currentValue += step;
            return temp;
        }

        @Override
        public boolean hasNext() {
            return stopIsGreater ?
                    currentValue < stop :
                    currentValue > stop;
        }
    }
}
