package jasmine.jragon.range;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class LongRange implements Iterable<Long>, IRange {
    private final long start;
    private final long stop;
    private final long step;
    private final long size;

    public LongRange(long start, long stop, long step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.size = (Math.abs(stop) - Math.abs(start)) / Math.abs(step);
        if (stop == 0) {
            throw new IllegalArgumentException("Step size amount cannot be 0");
        }
    }

    public LongRange(long start, long stop) {
        this(start, stop, 1);
    }

    public LongRange(long stop) {
        this(0, stop, 1);
    }

    public static LongRange range(long stop) {
        return new LongRange(stop);
    }

    public static LongRange range(long start, long stop) {
        return new LongRange(start, stop);
    }

    public static LongRange range(long start, long stop, long step) {
        return new LongRange(start, stop, step);
    }

    public LongStream stream() {
        return toStream(this, false);
    }

    public LongStream parallelStream() {
        return toStream(this, true);
    }

    private static LongStream toStream(LongRange range, boolean parallel) {
        long size = range.size();
        return StreamSupport.longStream(
                Spliterators.spliterator(new LongRangeIterator(range), size, RANGE_SPLITERATOR_FLAGS),
                parallel
        );
    }

    public <T> Stream<T> map(LongFunction<? extends T> mapper) {
        return stream().mapToObj(mapper);
    }

    @Override
    public @NotNull Iterator<Long> iterator() {
        return new LongRangeIterator(this);
    }

    public void forEach(LongConsumer action) {
        var iter = new LongRangeIterator(this);
        while (iter.hasNext()) {
            action.accept(iter.nextLong());
        }
    }

    @Override
    public Spliterator<Long> spliterator() {
        return Spliterators.spliterator(new LongRangeIterator(this), size, RANGE_SPLITERATOR_FLAGS);
    }

    public long size() {
        return size;
    }

    @SuppressWarnings("DuplicatedCode")
    private static final class LongRangeIterator implements PrimitiveIterator.OfLong {
        private final long stop, step;
        private long currentValue;
        private final boolean stopIsGreater;

        public LongRangeIterator(LongRange range) {
            this.stop = range.stop;
            this.step = range.step;
            this.currentValue = range.start;
            stopIsGreater = stop > currentValue;

            if (stopIsGreater && step < 0 || !stopIsGreater && step > 0) {
                throw new IllegalArgumentException("Infinite Loop Detected");
            }
        }

        @Override
        public long nextLong() {
            long temp = currentValue;
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
