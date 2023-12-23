package jasmine.jragon.heap.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public final class PermutationGenerator<E> {
    public List<E[]> heapPermutation(E[] initialArray) {
        List<E[]> permutationList = new ArrayList<>();
        heapPermutation(initialArray, permutationList);
        return permutationList;
    }

    public void heapPermutation(E[] initialArray, Collection<E[]> c) {
//        if (initialArray.length <= PermutationTask.SEQUENTIAL_WORK_THRESHOLD) {
            heapPermutation(initialArray, initialArray.length, c);
            return;
//        }
//        ForkJoinPool pool = new ForkJoinPool(2);
//        PermutationTask<E> thread = new PermutationTask<>(initialArray, -1);
//        c.addAll(pool.invoke(thread));
    }

    private void heapPermutation(E[] array, int size, Collection<E[]> permutationCollection) {
        if (size == 1) {
            permutationCollection.add(array.clone());
            return;
        }

        for (int i = 0; i < size; i++) {
            heapPermutation(array, size - 1, permutationCollection);
            // If size is odd, swap first and last element
            // If size is even, swap ith and last element
            int indexToSwap = size % 2 == 1 ? 0 : i;

            E temp = array[indexToSwap];
            array[indexToSwap] = array[size - 1];
            array[size - 1] = temp;
        }
    }
}

final class PermutationTask<E> extends RecursiveTask<Collection<E[]>> {
    public static final int SEQUENTIAL_WORK_THRESHOLD = 10;

    private final int guaranteedIndex;
    private E[] array;

    public PermutationTask(E[] array, int guaranteedIndex) {
        this.guaranteedIndex = guaranteedIndex;
        this.array = array;
    }

    @Override
    protected Collection<E[]> compute() {
        Collection<E[]> output = new ArrayList<>();

        if (guaranteedIndex == -1 || array.length - guaranteedIndex > SEQUENTIAL_WORK_THRESHOLD) {
            int newIndex = guaranteedIndex + 1;
            List<PermutationTask<E>> threadList = new ArrayList<>();
            for (int i = 0; i < array.length - newIndex; i++) {
                threadList.add(new PermutationTask<>(array.clone(), newIndex));
                rotateArray(newIndex);
            }
            array = null;

            threadList.forEach(thread -> {
                thread.fork();
                output.addAll(thread.join());
            });
            return output;
        }

        sequentialHeapGeneration(array, array.length, guaranteedIndex, output);
        return output;
    }

    private void rotateArray(int start) {
        E temp = array[start];
        if (array.length - 1 - start >= 0)
            System.arraycopy(array, start + 1, array, start, array.length - 1 - start);
        array[array.length - 1] = temp;
    }

    private void sequentialHeapGeneration(E[] array, int size, int guaranteedIndex, Collection<E[]> permutationCollection) {
        int startingIndex = 1 + guaranteedIndex;
        if (size - startingIndex == 1) {
            permutationCollection.add(array.clone());
            return;
        }

        for (int i = 0; i < size - startingIndex; i++) {
            sequentialHeapGeneration(array, size - 1, guaranteedIndex, permutationCollection);
            int indexToSwap = size % 2 == 1 ? startingIndex : i;

            E temp = array[indexToSwap];
            array[indexToSwap] = array[size - 1];
            array[size - 1] = temp;
        }
    }
}