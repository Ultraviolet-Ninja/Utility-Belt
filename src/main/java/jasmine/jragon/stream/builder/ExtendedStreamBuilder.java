package jasmine.jragon.stream.builder;

import lombok.NonNull;

import java.util.Collection;
import java.util.stream.Stream;

public final class ExtendedStreamBuilder<E> implements Stream.Builder<E> {
    private final Stream.Builder<E> internalBuilder = Stream.builder();

    @Override
    public void accept(E e) {
        internalBuilder.accept(e);
    }

    @Override
    public Stream<E> build() {
        return internalBuilder.build();
    }

    public ExtendedStreamBuilder<E> addAll(@NonNull Collection<? extends E> c) {
        c.forEach(internalBuilder);
        return this;
    }

    @Override
    public ExtendedStreamBuilder<E> add(E e) {
        internalBuilder.accept(e);
        return this;
    }
}
