package jasmine.jragon.heap.algo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class PermutationGeneratorMK2<E> {
    public List<E[]> heapPermutation(E[] initialArray) {
        List<E[]> permutationList = new LinkedList<>();
        LinkedList<byte[]> indexPermutationList = new LinkedList<>();
        byte[] indexArray = createByteArray(initialArray.length);

        if (initialArray.length <= PermutationTask.SEQUENTIAL_WORK_THRESHOLD) {
            heapPermutation(indexArray, initialArray.length, indexPermutationList);
            convertPermutations(initialArray, indexPermutationList, permutationList);
            return permutationList;
        }

        ForkJoinPool pool = new ForkJoinPool(2);
        PermutationTaskMK2 thread = new PermutationTaskMK2(indexArray, -1);
        indexPermutationList.addAll(pool.invoke(thread));
        convertPermutations(initialArray, indexPermutationList, permutationList);
        return permutationList;
    }

    private byte[] createByteArray(int arrayLength) {
        byte[] array = new byte[arrayLength];

        for (byte b = 0; b < array.length; b++)
            array[b] = b;

        return array;
    }

    private void heapPermutation(byte[] array, int size, List<byte[]> permutationList) {
        if (size == 1) {
            permutationList.add(array.clone());
            return;
        }

        for (int i = 0; i < size; i++) {
            heapPermutation(array, size - 1, permutationList);
            int indexToSwap = size % 2 == 1 ? 0 : i;

            byte temp = array[indexToSwap];
            array[indexToSwap] = array[size - 1];
            array[size - 1] = temp;
        }
    }

    private void convertPermutations(final E[] initialArray, LinkedList<byte[]> indexList, List<E[]> permutationList) {
        E[] temp = (E[]) new Object[initialArray.length];
        while (indexList.size() > 0) {
            byte[] permutation = indexList.pollLast();

            for (int i = 0; i < permutation.length; i++) {
                temp[i] = initialArray[permutation[i]];
            }

            permutationList.add(temp.clone());
        }
    }
}

final class PermutationTaskMK2 extends RecursiveTask<LinkedList<byte[]>> {
    public static final int SEQUENTIAL_WORK_THRESHOLD = 10;

    private final int guaranteedIndex;
    private byte[] array;

    public PermutationTaskMK2(byte[] array, int guaranteedIndex) {
        this.guaranteedIndex = guaranteedIndex;
        this.array = array;
    }

    @Override
    protected LinkedList<byte[]> compute() {
        LinkedList<byte[]> output = new LinkedList<>();

        if (guaranteedIndex == -1 || array.length - guaranteedIndex > SEQUENTIAL_WORK_THRESHOLD) {
            int newIndex = guaranteedIndex + 1;
            List<PermutationTaskMK2> threadList = new ArrayList<>();

            for (int i = 0; i < array.length - newIndex; i++) {
                threadList.add(new PermutationTaskMK2(array.clone(), newIndex));
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
        byte temp = array[start];
        if (array.length - 1 - start >= 0)
            System.arraycopy(array, start + 1, array, start, array.length - 1 - start);
        array[array.length - 1] = temp;
    }

    private void sequentialHeapGeneration(byte[] array, int size, int guaranteedIndex, List<byte[]> indexPermutationList) {
        int startingIndex = 1 + guaranteedIndex;
        if (size - startingIndex == 1) {
            indexPermutationList.add(array.clone());
            return;
        }

        for (int i = 0; i < size - startingIndex; i++) {
            sequentialHeapGeneration(array, size - 1, guaranteedIndex, indexPermutationList);
            int indexToSwap = size % 2 == 1 ? startingIndex : i;

            byte temp = array[indexToSwap];
            array[indexToSwap] = array[size - 1];
            array[size - 1] = temp;
        }
    }
}