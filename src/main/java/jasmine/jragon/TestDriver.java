package jasmine.jragon;

import jasmine.jragon.stream.collector.restream.grouping.KeyedGroup;
import jasmine.jragon.tuple.Tuple;
import jasmine.jragon.tuple.type.Duo;

import java.util.stream.Collectors;

public class TestDriver {
    public static void main(String[] args) {
        Tuple t1 = Tuple.of(123, 89.0, 56.6f, 1239829348723492310L);
        Tuple t2 = Tuple.of(123, 67, 45.0, 'c');
        System.out.println(
                t1.union(t2)
                        .enumerate()
                        .collect(KeyedGroup.provide(e -> e.index() % 3))
                        .peek(e -> System.out.println(e.first()))
                        .map(Duo::second)
                        .map(String::valueOf)
                        .collect(Collectors.joining("\n"))
        );
    }
}
