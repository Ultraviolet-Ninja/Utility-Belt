package jasmine.jragon.range;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class DoubleRange implements Iterable<Double>, IRange {
    private final double start;
    private final double stop;
    private final double step;
    private final long size;

    public DoubleRange(double start, double stop, double step) {
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.size = (long) ((Math.abs(stop) - Math.abs(start)) / Math.abs(step));
        if (stop == 0) {
            throw new IllegalArgumentException("Step size amount cannot be 0");
        }
    }

    public DoubleRange(double start, double stop) {
        this(start, stop, 1.0);
    }

    public DoubleRange(double stop) {
        this(0.0, stop, 1.0);
    }

    public static DoubleRange range(double stop) {
        return new DoubleRange(stop);
    }

    public static DoubleRange range(double start, double stop) {
        return new DoubleRange(start, stop);
    }

    public static DoubleRange range(double start, double stop, double step) {
        return new DoubleRange(start, stop, step);
    }

    public DoubleStream stream() {
        return toStream(this, false);
    }

    public DoubleStream parallelStream() {
        return toStream(this, true);
    }

    private static DoubleStream toStream(DoubleRange range, boolean parallel) {
        long size = range.size();
        return StreamSupport.doubleStream(
                Spliterators.spliterator(new DoubleRangeIterator(range), size, RANGE_SPLITERATOR_FLAGS),
                parallel
        );
    }

    public <T> Stream<T> map(DoubleFunction<? extends T> mapper) {
        return stream().mapToObj(mapper);
    }

    @Override
    public @NotNull Iterator<Double> iterator() {
        return new DoubleRangeIterator(this);
    }

    public void forEach(DoubleConsumer action) {
        var iter = new DoubleRangeIterator(this);
        while (iter.hasNext()) {
            action.accept(iter.nextDouble());
        }
    }

    @Override
    public Spliterator<Double> spliterator() {
        return Spliterators.spliterator(new DoubleRangeIterator(this), size, RANGE_SPLITERATOR_FLAGS);
    }

    public long size() {
        return size;
    }

    @SuppressWarnings("DuplicatedCode")
    private static final class DoubleRangeIterator implements PrimitiveIterator.OfDouble {
        private final double stop, step;
        private double currentValue;
        private final boolean stopIsGreater;

        public DoubleRangeIterator(DoubleRange range) {
            this.stop = range.stop;
            this.step = range.step;
            this.currentValue = range.start;
            stopIsGreater = stop > currentValue;

            if (stopIsGreater && step < 0 || !stopIsGreater && step > 0) {
                throw new IllegalArgumentException("Infinite Loop Detected");
            }
        }

        @Override
        public double nextDouble() {
            double temp = currentValue;
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
