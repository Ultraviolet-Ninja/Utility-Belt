package jasmine.jragon.heap.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

public final class PermutationGeneratorMk4<E> {
    public static final byte FULL_PERMUTATION_ARRAY_SIZE_LIMIT = 11;

    private final E[] elements;
    private Predicate<E[]> condition;

    public PermutationGeneratorMk4(E[] elements) {
        this.elements = elements;
        condition = null;
    }

    public Set<List<E>> generateAllPermutations() {
        if (elements.length > FULL_PERMUTATION_ARRAY_SIZE_LIMIT)
            throw new IllegalArgumentException("Array is too large to compute");
        Set<List<E>> output = new HashSet<>();
        heapPermutation(output, elements.length);
        return output;
    }

    public Set<List<E>> generateConditionalPermutations(Predicate<E[]> condition) {
        if (checkPredicate(condition))
            throw new IllegalArgumentException();

        if (elements.length > FULL_PERMUTATION_ARRAY_SIZE_LIMIT) {
            ForkJoinPool pool = new ForkJoinPool(4);
            ConditionalPermutationTask<E> thread = new ConditionalPermutationTask<>(elements, -1, condition);
            return pool.invoke(thread);
        }

        Set<List<E>> output = new HashSet<>();
        this.condition = condition;
        heapPermutation(output, elements.length);
        this.condition = null;
        return output;
    }

    private void heapPermutation(Set<List<E>> permutationSet, int size) {
        if (areConditionsMet(size)) {
            permutationSet.add(Arrays.asList(elements.clone()));
            return;
        }

        for (int i = 0; i < size; i++) {
            heapPermutation(permutationSet, size - 1);
            int indexToSwap = size % 2 == 1 ? 0 : i;

            E temp = elements[indexToSwap];
            elements[indexToSwap] = elements[size - 1];
            elements[size - 1] = temp;
        }
    }

    private boolean areConditionsMet(int size) {
        if (condition != null) return size == 1 && condition.test(elements);
        return size == 1;
    }

    private boolean checkPredicate(Predicate<E[]> condition) {
        try {
            condition.test(null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

final class ConditionalPermutationTask<E> extends RecursiveTask<Set<List<E>>> {
    private static final int MAX_GUARANTEED_INDEX = 2;

    private final Predicate<E[]> condition;
    private final int guaranteedIndex;
    private E[] array;

    public ConditionalPermutationTask(E[] array, int guaranteedIndex, Predicate<E[]> condition) {
        this.condition = condition;
        this.guaranteedIndex = guaranteedIndex;
        this.array = array;
    }

    @Override
    protected Set<List<E>> compute() {
        Set<List<E>> output = new HashSet<>();

        int nextIndex = guaranteedIndex + 1;
        if (nextIndex < MAX_GUARANTEED_INDEX) {
            List<ConditionalPermutationTask<E>> taskList = new ArrayList<>();

            for (int i = 0; i < array.length - nextIndex; i++) {
                taskList.add(new ConditionalPermutationTask<>(array.clone(), nextIndex, condition));
                rotateArray(nextIndex);
            }
            array = null;

            taskList.forEach(ForkJoinTask::fork);
            taskList.forEach(task -> output.addAll(task.join()));
            return output;
        }

        sequentialHeapGeneration(array, array.length, output);
        return output;
    }

    private void rotateArray(int start) {
        E temp = array[start];
        if (array.length - 1 - start >= 0)
            System.arraycopy(array, start + 1, array, start, array.length - 1 - start);
        array[array.length - 1] = temp;
    }

    private void sequentialHeapGeneration(E[] array, int size, Set<List<E>> set) {
        int startingIndex = 1 + guaranteedIndex;
        if (areConditionsMet(size, startingIndex)) {
            set.add(Arrays.asList(array.clone()));
            return;
        }

        for (int i = 0; i < size - startingIndex; i++) {
            sequentialHeapGeneration(array, size - 1, set);
            int indexToSwap = size % 2 == 1 ? startingIndex : i;

            E temp = array[indexToSwap];
            array[indexToSwap] = array[size - 1];
            array[size - 1] = temp;
        }
    }

    private boolean areConditionsMet(int size, int startingIndex) {
        boolean isAPermutation = size - startingIndex == 1;

        if (condition != null) return isAPermutation && condition.test(array);
        return isAPermutation;
    }
}
