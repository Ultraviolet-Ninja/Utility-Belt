package jasmine.jragon.stream.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public abstract class ListAccumulator<E, R> implements CustomCollector<E, List<E>, R> {
    @Override
    public Supplier<List<E>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<E>, E> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<E>> combiner() {
        return (l, r) -> {
            l.addAll(r);
            return l;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return NONE;
    }
}
