package jasmine.jragon.heap.algo;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public final class PermutationGeneratorMk3<E> {
    public static final int ARRAY_SIZE_LIMIT = 12;

    private final E[] array;
    private Predicate<E[]> condition;

    public PermutationGeneratorMk3(E[] array) {
        this.array = array;
        condition = null;
    }

    public LinkedList<E[]> generateAllPermutations() {
        if (array.length > ARRAY_SIZE_LIMIT)
            throw new IllegalArgumentException("Array is too large to compute");
        LinkedList<E[]> permutations = new LinkedList<>();
        heapPermutation(permutations, array.length);
        return permutations;
    }

    public LinkedList<E[]> generateConditionalPermutations(Predicate<E[]> condition) {
        LinkedList<E[]> permutations = new LinkedList<>();
        this.condition = condition;
        heapPermutation(permutations, array.length);
        this.condition = null;
        return permutations;
    }

    private void heapPermutation(List<E[]> permutationCollection, int size) {
        if (areConditionsMet(size)) {
            permutationCollection.add(array.clone());
            return;
        }

        for (int i = 0; i < size; i++) {
            heapPermutation(permutationCollection, size - 1);
            int indexToSwap = size % 2 == 1 ? 0 : i;

            E temp = array[indexToSwap];
            array[indexToSwap] = array[size - 1];
            array[size - 1] = temp;
        }
    }

    private boolean areConditionsMet(int size) {
        if (condition != null) {
            return size == 1 && condition.test(array);
        }
        return size == 1;
    }
}
