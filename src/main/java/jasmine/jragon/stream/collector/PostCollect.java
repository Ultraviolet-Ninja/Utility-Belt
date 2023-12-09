package jasmine.jragon.stream.collector;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PostCollect<T, C extends Collection<T>, R> implements CustomCollector<T, C, R> {
    @NonNull
    private final Supplier<C> collectionSupplier;

    @NonNull
    private final Function<C, R> fromCollectionFunction;

    @NonNull
    private final Set<Characteristics> collectionCharacteristics;

    @Override
    public Supplier<C> supplier() {
        return collectionSupplier;
    }

    @Override
    public BiConsumer<C, T> accumulator() {
        return Collection::add;
    }

    @Override
    public BinaryOperator<C> combiner() {
        return (left, right) -> {left.addAll(right); return left;};
    }

    @Override
    public Function<C, R> finisher() {
        return fromCollectionFunction;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return collectionCharacteristics;
    }

    public static <T, R> PostCollect<T, List<T>, R> toListAndThen(Function<List<T>, R> mapper) {
        return new PostCollect<>(ArrayList::new, mapper, NONE);
    }

    public static <T, R> PostCollect<T, List<T>, R> toListAndThen(Supplier<List<T>> listSupplier,
                                                                  Function<List<T>, R> mapper) {
        return new PostCollect<>(listSupplier, mapper, NONE);
    }

    public static <T, R> PostCollect<T, Set<T>, R> toSetAndThen(Function<Set<T>, R> mapper) {
        return new PostCollect<>(HashSet::new, mapper, UNORDERED_CH);
    }

    public static <T, R> PostCollect<T, Set<T>, R> toSetAndThen(Supplier<Set<T>> setSupplier,
                                                                Function<Set<T>, R> mapper) {
        return new PostCollect<>(setSupplier, mapper, UNORDERED_CH);
    }
}
