package jasmine.jragon.math;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static jasmine.jragon.math.BigInt.ONE;
import static jasmine.jragon.math.BigInt.THREE;
import static jasmine.jragon.math.BigInt.TWO;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BigIntTest {
    @Test
    public void assertOneTest() {
        assertFalse(ONE.isZero());
        assertTrue(ONE.isOne());
        assertFalse(ONE.isTwo());
        assertFalse(ONE.isNegativeOne());
    }

    @Test
    public void assertTwoTest() {
        assertFalse(TWO.isZero());
        assertFalse(TWO.isOne());
        assertTrue(TWO.isTwo());
        assertFalse(TWO.isNegativeOne());
    }

    @Test
    public void assertThreeTest() {
        assertFalse(THREE.isZero());
        assertFalse(THREE.isOne());
        assertFalse(THREE.isTwo());
        assertFalse(THREE.isNegativeOne());
    }

    @Test
    public void assertNegativeOneTest() {
        var negativeOne = new BigInt(-1);
        assertFalse(negativeOne.isZero());
        assertFalse(negativeOne.isOne());
        assertFalse(negativeOne.isTwo());
        assertTrue(negativeOne.isNegativeOne());
    }

    @DataProvider
    public Object[][] positiveExponentTestProvider() {
        return new Object[][] {
                {2, 1, 2}, {1, 10, 1}, {2, 8, 256}, {-1, 10, 1}, {-1, 11, -1}
        };
    }

    @Test(dataProvider = "positiveExponentTestProvider")
    public void positiveExponentTest(int value, int exponent, int expectedValue) {
        var base = new BigInt(value);
        base.pow(exponent);
        assertEquals(base, new BigInt(expectedValue));
    }

    @DataProvider
    public Object[][] zeroExponentTestProvider() {
        return new Object[][] {
                {100}, {-1}, {Long.MAX_VALUE}
        };
    }

    @Test(dataProvider = "zeroExponentTestProvider")
    public void zeroExponentTest(long value) {
        var base = new BigInt(value);
        base.pow(0);
        assertTrue(base.isOne());
    }

    @Test(expectedExceptions = ArithmeticException.class,
            expectedExceptionsMessageRegExp = "Zero to the power of zero is not allowed")
    public void zeroToZeroExponentTest() {
        var zero = new BigInt(0);
        zero.pow(0);
    }

    @Test(expectedExceptions = ArithmeticException.class,
            expectedExceptionsMessageRegExp = "Infinity cannot be represented")
    public void zeroToNegativeExponentTest() {
        var zero = new BigInt(0);
        zero.pow(-1);
    }

    @DataProvider
    public Object[][] numberToNegativeExponentTestProvider() {
        return new Object[][] {
                {ONE}, {TWO}
        };
    }

    @Test(dataProvider = "numberToNegativeExponentTestProvider",
            expectedExceptions = ArithmeticException.class,
            expectedExceptionsMessageRegExp = "Negative exponents cannot be represented as an integer")
    public void numberToNegativeExponentTest(BigInt bigInt) {
        bigInt.pow(-1);
    }
}
