package jasmine.jragon.stream.collector.restream.grouping;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SlidingWindow<T> implements Window<T, List<T>> {
    private final int windowSize;
    private final boolean isFrontLoaded;
    private final boolean isBackLoaded;

    private final Queue<T> runningQueue = new ArrayDeque<>();

    @Contract(pure = true)
    @Override
    public @NotNull BiConsumer<Stream.Builder<List<T>>, T> accumulator() {
        return (listBuilder, type) -> {
            runningQueue.add(type);

            if (runningQueue.size() > windowSize) {
                runningQueue.poll();
            }

            if (isFrontLoaded || runningQueue.size() == windowSize) {
                listBuilder.add(new ArrayList<>(runningQueue));
            }
        };
    }

    @Contract(pure = true)
    @Override
    public @NotNull Function<Stream.Builder<List<T>>, Stream<List<T>>> finisher() {
        if (!isBackLoaded) {
            return Stream.Builder::build;
        }

        return listBuilder -> {
            while (runningQueue.size() != 1) {
                runningQueue.poll();
                listBuilder.add(new ArrayList<>(runningQueue));
            }
            return listBuilder.build();
        };
    }


    @Contract("_ -> new")
    public static <T> @NotNull SlidingWindow<T> allElements(@Range(from = 2, to = Integer.MAX_VALUE) int windowSize) {
        checkNonPositiveSize(windowSize);
        return new SlidingWindow<>(windowSize, true, true);
    }

    @Contract("_ -> new")
    public static <T> @NotNull SlidingWindow<T> onlyWindowSizeElements(@Range(from = 2, to = Integer.MAX_VALUE) int windowSize) {
        checkNonPositiveSize(windowSize);
        return new SlidingWindow<>(windowSize, false, false);
    }

    @Contract("_ -> new")
    public static <T> @NotNull SlidingWindow<T> frontLoadedElements(@Range(from = 2, to = Integer.MAX_VALUE) int windowSize) {
        checkNonPositiveSize(windowSize);
        return new SlidingWindow<>(windowSize, true, false);
    }

    @Contract("_ -> new")
    public static <T> @NotNull SlidingWindow<T> backLoadedElements(@Range(from = 2, to = Integer.MAX_VALUE) int windowSize) {
        checkNonPositiveSize(windowSize);
        return new SlidingWindow<>(windowSize, false, true);
    }

    private static void checkNonPositiveSize(int windowSize) {
        if (windowSize < 2) {
            throw new IllegalArgumentException("Window size must be at least 2");
        }
    }
}
