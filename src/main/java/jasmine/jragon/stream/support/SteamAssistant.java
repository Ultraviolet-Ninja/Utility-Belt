package jasmine.jragon.stream.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterator.SIZED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SteamAssistant {
    public static final int ORDERED_SIZED = SIZED | ORDERED;

    public static final int SORTED = Spliterator.SORTED | ORDERED;

    public static <T> Stream<T> fromIterator(Iterable<T> typedIterable, int characteristics) {
        var spliterator = Spliterators.spliteratorUnknownSize(typedIterable.iterator(), characteristics);
        return StreamSupport.stream(spliterator, false);
    }

    public static <T> Stream<T> fromIterator(Iterable<T> typedIterable, int size, int characteristics) {
        var spliterator = Spliterators.spliterator(typedIterable.iterator(), size, characteristics | SIZED);
        return StreamSupport.stream(spliterator, false);
    }

    public static <T> Stream<T> fromIteratorParallel(Iterable<T> typedIterable, int characteristics) {
        var spliterator = Spliterators.spliteratorUnknownSize(typedIterable.iterator(), characteristics);
        return StreamSupport.stream(spliterator, true);
    }

    public static <T> Stream<T> fromIteratorParallel(Iterable<T> typedIterable, int size, int characteristics) {
        var spliterator = Spliterators.spliterator(typedIterable.iterator(), size, characteristics | SIZED);
        return StreamSupport.stream(spliterator, true);
    }
}
