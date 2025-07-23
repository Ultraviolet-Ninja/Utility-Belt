package jasmine.jragon.stream.collector;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public interface DataStructureCollector<T, C extends Collection<T>> extends CustomCollector<T, C, C> {
    @Override
    default Function<C, C> finisher() {
        return Function.identity();
    }

    @Override
    default BinaryOperator<C> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    default BiConsumer<C, T> accumulator() {
        return Collection::add;
    }

    @Override
    default Set<Characteristics> characteristics() {
        return IDENTITY;
    }
}
