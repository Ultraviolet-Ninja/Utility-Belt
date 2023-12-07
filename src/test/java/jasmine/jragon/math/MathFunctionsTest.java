package jasmine.jragon.math;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class MathFunctionsTest {
    @DataProvider
    public Object[][] perfectSquareTestProvider() {
        return new Object[][]{
                {25, true}, {36, true}, {26, false}, {-1, false},
        };
    }

    @Test(dataProvider = "perfectSquareTestProvider")
    public void perfectSquareTest(long potentialSquare, boolean expected) {
        if (expected) {
            assertTrue(MathFunctions.isPerfectSquare(potentialSquare));
        } else {
            assertFalse(MathFunctions.isPerfectSquare(potentialSquare));
        }
    }

    @Test
    public void isIntTest() {
        assertTrue(MathFunctions.isAnInteger(1.0));
        assertTrue(MathFunctions.isAnInteger(-1.0));
        assertFalse(MathFunctions.isAnInteger(1.1));
        assertFalse(MathFunctions.isAnInteger(-1.2));
    }

    @Test
    public void digitalRootFromLongTest() {
        assertEquals(MathFunctions.digitalRoot(1000), 1);
        assertEquals(MathFunctions.digitalRoot(1234), 1);
        assertEquals(MathFunctions.digitalRoot(234), 9);
        assertEquals(MathFunctions.digitalRoot(0), 0);
        assertEquals(MathFunctions.digitalRoot(-1000), 1);
        assertEquals(MathFunctions.digitalRoot(-1234), 1);
        assertEquals(MathFunctions.digitalRoot(-234), 9);
    }
}
