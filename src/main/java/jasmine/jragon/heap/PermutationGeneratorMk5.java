package jasmine.jragon.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class PermutationGeneratorMk5<E> {
    private static final int FULL_PERMUTATION_ARRAY_SIZE_LIMIT = 11;
    private static final int PARALLELIZATION_LIMIT = 9;

    private final E[] elements;

    public PermutationGeneratorMk5(E[] elements) {
        this.elements = elements;
    }

    public Set<List<E>> generateAllPermutations() {
        if (elements.length > FULL_PERMUTATION_ARRAY_SIZE_LIMIT)
            throw new IllegalArgumentException("Array is too large to compute all combinations");

        Stream<E[]> firstLetterPermutations = generateAllFirstIndexes();

        if (elements.length >= PARALLELIZATION_LIMIT)
            firstLetterPermutations.parallel();

        return firstLetterPermutations
                .map(array -> generatePermutationSet(array, arr -> true))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<List<E>> generateConditionalPermutations(Predicate<E[]> condition)
            throws IllegalArgumentException, NullPointerException {
        if (checkPredicate(condition))
            throw new IllegalArgumentException();

        Stream<E[]> firstLetterPermutations = generateAllFirstIndexes();

        if (elements.length > PARALLELIZATION_LIMIT)
            firstLetterPermutations.parallel();

        return firstLetterPermutations
                .map(array -> generatePermutationSet(array, condition))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Stream<E[]> generateAllFirstIndexes() {
        List<E[]> output = new ArrayList<>(elements.length);

        for (int i = 0; i < elements.length; i++) {
            output.add(elements.clone());
            rotateArray();
        }

        return output.stream();
    }

    private Set<List<E>> generatePermutationSet(E[] arrayInstance, Predicate<E[]> condition) {
        Set<List<E>> results = new HashSet<>();
        heapPermutation(arrayInstance, results, arrayInstance.length - 1, condition);
        return results;
    }

    private void heapPermutation(E[] arrayInstance, Set<List<E>> permutationSet,
                                 int size, Predicate<E[]> condition) {
        if (size == 1) {
            if (!condition.test(arrayInstance)) return;

            permutationSet.add(Arrays.asList(arrayInstance.clone()));
            return;
        }

        for (int i = 0; i < size; i++) {
            heapPermutation(arrayInstance, permutationSet, size - 1, condition);
            int indexToSwap = size % 2 == 1 ? 1 : i;

            E temp = arrayInstance[indexToSwap];
            arrayInstance[indexToSwap] = arrayInstance[size - 1];
            arrayInstance[size - 1] = temp;
        }
    }

    private void rotateArray() {
        E temp = elements[0];
        System.arraycopy(elements, 1, elements, 0, elements.length - 1);
        elements[elements.length - 1] = temp;
    }

    private boolean checkPredicate(Predicate<E[]> condition) {
        Objects.requireNonNull(condition);
        try {
            condition.test(null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
