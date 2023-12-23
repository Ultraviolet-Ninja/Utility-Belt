package jasmine.jragon.range;

import java.util.Spliterator;

public interface IRange {
    int RANGE_SPLITERATOR_FLAGS = Spliterator.SIZED | Spliterator.SORTED | Spliterator.DISTINCT | Spliterator.ORDERED;
}
