package jasmine.jragon.range;

import java.util.Spliterator;

public sealed interface IRange permits DoubleRange, LongRange, Range {
    int RANGE_SPLITERATOR_FLAGS = Spliterator.SIZED | Spliterator.SORTED | Spliterator.DISTINCT | Spliterator.ORDERED;
}
