package jasmine.jragon.stream.collector;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collector;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public interface CustomCollector<T, A, R> extends Collector<T, A, R> {
    Set<Collector.Characteristics> NONE = Collections.emptySet();
    Set<Collector.Characteristics> IDENTITY = unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
    Set<Collector.Characteristics> UNORDERED_CH = unmodifiableSet(EnumSet.of(UNORDERED));
    Set<Collector.Characteristics> CONCURRENT_IDENTITY = unmodifiableSet(EnumSet.of(CONCURRENT, IDENTITY_FINISH));
    Set<Collector.Characteristics> UNORDERED_IDENTITY = unmodifiableSet(EnumSet.of(UNORDERED, IDENTITY_FINISH));
}
