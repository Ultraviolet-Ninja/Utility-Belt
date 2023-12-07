package jasmine.jragon.stream.collector;

import jasmine.jragon.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TupleCollector implements CustomCollector<Object, List<Object>, Tuple> {
    @Override
    public Supplier<List<Object>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Object>, Object> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<Object>> combiner() {
        return (l, r) -> {
            l.addAll(r);
            return l;
        };
    }

    @Override
    public Function<List<Object>, Tuple> finisher() {
        return Tuple::of;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return NONE;
    }
}
