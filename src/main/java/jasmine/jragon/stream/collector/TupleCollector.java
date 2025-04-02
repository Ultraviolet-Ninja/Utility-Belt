package jasmine.jragon.stream.collector;

import jasmine.jragon.tuple.Tuple;

import java.util.List;
import java.util.function.Function;

public final class TupleCollector extends ListAccumulator<Object, Tuple> {
    @Override
    public Function<List<Object>, Tuple> finisher() {
        return Tuple::of;
    }
}
