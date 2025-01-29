package jasmine.jragon.stream.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class IntermediateUtils {
    public static <T> Stream<T> eliminateOptional(Optional<T> optional) {
        return optional.stream();
    }
}

