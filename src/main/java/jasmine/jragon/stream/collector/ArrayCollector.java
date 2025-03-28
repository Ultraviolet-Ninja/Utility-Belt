package jasmine.jragon.stream.collector;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(staticName = "toArray")
public final class ArrayCollector<E> extends ListAccumulator<E, E[]> {
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Function<List<E>, E[]> finisher() {
        return l -> (E[]) l.toArray();
    }
}
