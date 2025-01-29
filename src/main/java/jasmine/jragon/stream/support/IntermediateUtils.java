package jasmine.jragon.stream.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class IntermediateUtils {
    public static <T> void removeOptional(Optional<T> optional, Consumer<T> consumer) {
        optional.ifPresent(consumer);
    }
}

