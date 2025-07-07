package jasmine.jragon.stream.collector;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor(staticName = "toArray")
public final class ArrayCollector<E> extends ListAccumulator<E, E[]> {
    private final Class<E> componentType;

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Function<List<E>, E[]> finisher() {
        var conversion = (E[]) Array.newInstance(componentType, 0);
        return l -> (E[]) Arrays.copyOf(l.toArray(), l.size(), conversion.getClass());
    }
}
