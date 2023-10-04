package jasmine.jragon.math;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PrimeCheckerTest {
    @DataProvider
    public Object[][] primitiveMethodTestProvider() {
        return new Object[][] {
                {23L, true}, {333333L, false},
                {-1L, false}, {7993, true}
        };
    }

    @Test(dataProvider = "primitiveMethodTestProvider")
    public void primitiveMethodTest(long testValue, boolean expectedResult) {
        assertEquals(PrimeChecker.isPrime(testValue), expectedResult);
    }

    @DataProvider
    public Object[][] bigIntMethodTestProvider() {
        return new Object[][] {
                {new BigInt(23), true}, {new BigInt(333333), false},
                {new BigInt(-1), false}, {new BigInt(7993), true}
        };
    }

    @Test(dataProvider = "bigIntMethodTestProvider")
    public void bigIntMethodTest(BigInt testValue, boolean expectedResult) {
        assertEquals(PrimeChecker.isPrime(testValue), expectedResult);
    }
}
