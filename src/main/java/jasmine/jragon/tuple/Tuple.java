package jasmine.jragon.tuple;

import jasmine.jragon.enumerated.EnumeratedElement;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@EqualsAndHashCode(cacheStrategy = EqualsAndHashCode.CacheStrategy.LAZY)
public final class Tuple implements Iterable<Object>, RandomAccess {
    public static final Tuple EMPTY = new Tuple(new Object[0]);

    @NonNull
    private final Object[] data;

    public static Tuple of(Object... objects) {
        if (objects.length == 0) {
            return EMPTY;
        }
        return new Tuple(objects);
    }

    public static Tuple of(Collection<?> objects) {
        if (objects.isEmpty()) {
            return EMPTY;
        }
        return new Tuple(objects);
    }

    private Tuple(Collection<?> objects) {
        data = objects.toArray();
    }

    public Class<?> getCommonAncestor() {
        var ancestor = Arrays.stream(data)
                .map(Object::getClass)
                .map(Tuple::getAncestry)
                .flatMap(Collection::stream)
                .collect(groupingBy(identity(), LinkedHashMap::new, counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == data.length)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(a -> getAncestry(a).size()));

        return ancestor.isEmpty() ?
                Object.class :
                ancestor.get();
    }

    private static List<Class<?>> getAncestry(Class<?> clazz) {
        List<Class<?>> ancestry = new ArrayList<>();
        while (clazz != null) {
            ancestry.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return ancestry;
    }

    public boolean isEmpty() {
        return data.length == 0;
    }

    public int size() {
        return data.length;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int index) throws ClassCastException {
        return (T) data[index];
    }

    public boolean getBool(int index) {
        return (boolean) data[index];
    }

    public char getChar(int index) {
        return (char) data[index];
    }

    public int getInt(int index) {
        return (int) data[index];
    }

    public double getDouble(int index) {
        return (double) data[index];
    }

    public float getFloat(int index) {
        return (int) data[index];
    }

    public long getLong(int index) {
        return (long) data[index];
    }

    public short getShort(int index) {
        return (short) data[index];
    }

    public byte getByte(int index) {
        return (byte) data[index];
    }

    public Tuple prepend(Tuple before) {
        if (before.isEmpty()) {
            return this;
        } else if (data.length == 0) {
            return before;
        }
        return new Tuple(mergeArrays(before.data, this.data));
    }

    public Tuple append(Tuple after) {
        if (after.isEmpty()) {
            return this;
        } else if (data.length == 0) {
            return after;
        }
        return new Tuple(mergeArrays(this.data, after.data));
    }

    public Tuple union(Tuple other) {
        if (this == other || this.equals(other)) {
            return this;
        }
        var firstSet = new HashSet<>(List.of(this.data));
        var secondSet = new HashSet<>(List.of(other.data));
        firstSet.addAll(secondSet);
        return new Tuple(firstSet.toArray());
    }

    public Tuple intersection(Tuple other) {
        if (this == other || this.equals(other)) {
            return this;
        }
        var set = new HashSet<>(List.of(other.data));
        var array = Arrays.stream(this.data)
                .filter(set::contains)
                .distinct()
                .toArray();
        return new Tuple(array);
    }

    public Tuple subtract(Tuple minus) {
        if (this == minus || this.equals(minus)) {
            return EMPTY;
        }
        var set = new HashSet<>(List.of(minus.data));
        var array = Arrays.stream(this.data)
                .filter(item -> !set.contains(item))
                .toArray();

        if (array.length == 0) {
            return EMPTY;
        }
        return new Tuple(array);
    }

    public List<Object> toList() {
        return List.of(data);
    }

    public List<Object> toMutableList() {
        return new ArrayList<>(List.of(data));
    }

    public Set<Object> toSet() {
        return Set.of(data);
    }

    public Set<Object> toMutableSet() {
        return new HashSet<>(List.of(data));
    }

    public Stream<Object> stream() {
        return Arrays.stream(data);
    }

    public Stream<Object> parallelStream() {
        return stream().parallel();
    }

    public Map<Class<?>, List<Object>> toClassMap() {
        return Arrays.stream(data)
                .collect(groupingBy(Object::getClass));
    }

    public Tuple applyToElements(UnaryOperator<Object> function) {
        var array = Arrays.stream(data)
                .map(function)
                .toArray();
        return new Tuple(array);
    }

    public List<EnumeratedElement<Object>> enumerateToList() {
        return enumerate().collect(Collectors.toList());
    }

    public Stream<EnumeratedElement<Object>> enumerate() {
        return IntStream.range(0, data.length)
                .mapToObj(i -> new EnumeratedElement<>(i, data[i]));
    }

    private static Object[] mergeArrays(Object[] first, Object[] second) {
        var arr = new Object[first.length + second.length];
        System.arraycopy(first, 0, arr, 0, first.length);
        System.arraycopy(second, 0, arr, first.length, second.length);
        return arr;
    }

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return Arrays.asList(data).iterator();
    }

    @NotNull
    public Iterator<EnumeratedElement<Object>> enumeratedIterator() {
        return enumerate().iterator();
    }

    @NotNull
    @Override
    public String toString() {
        return data.length != 0 ? "{\n" + IntStream.range(0, data.length)
                .mapToObj(i -> "\t" + i + ": " + data[i] + "\n")
                .collect(Collectors.joining()) + '}' :
                "{}";
    }

    public <T> T convert(Function<Object[], T> conversionFunction) {
        return conversionFunction.apply(data);
    }

    public Object[] toArray() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> T @NotNull [] toArray(T[] a) {
        if (a.length < data.length) {
            return (T[]) Arrays.copyOfRange(
                    this.data, 0, data.length, a.getClass());
        }

        System.arraycopy(data, 0, a, 0, data.length);
        if (a.length > data.length)
            a[data.length] = null;
        return a;
    }
}
