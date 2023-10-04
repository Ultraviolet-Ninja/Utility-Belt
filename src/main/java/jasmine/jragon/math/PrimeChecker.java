package jasmine.jragon.math;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static jasmine.jragon.math.BigInt.THREE;
import static jasmine.jragon.math.BigInt.TWO;

public final class PrimeChecker {
    public static boolean isPrime(@NotNull BigInt number) {
        if (number.compareTo(THREE) <= 0) {
            return number.equals(TWO) || number.equals(THREE);
        }
        Predicate<BigInt> stopPredicate = (n) -> {
            var temp = n.copy();
            temp.mul(temp);
            return temp.compareTo(number) <= 0;
        };

        for (BigInt i = new BigInt(5); stopPredicate.test(i); i.add(4)) {
            if (isDivisible(number, i)) {
                return false;
            }

            i.add(2);
            if (isDivisible(number, i)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isDivisible(BigInt base, BigInt mod) {
        var copy = base.copy();
        copy.mod(mod);
        return copy.isZero();
    }

    public static boolean isPrime(long n) {
        if (n < 2) {
            return false;
        }

        if(n % 2 == 0 || n % 3 == 0)
            return n == 2 || n == 3;

        long stop = (long) Math.sqrt(n);
        for (long i = 5; i <= stop; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }
}
