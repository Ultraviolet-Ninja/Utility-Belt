package jasmine.jragon.transcode;

import jasmine.jragon.math.BigInt;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TranscoderTest {
    private static final String HEXADECIMAL = "0123456789ABCDEF";
    private static final String LETTER_BINARY = "AB";

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "String must contain all unique characters")
    public void nonUniqueExceptionTest() {
        new CustomBaseTranscoder("aaaa");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test(expectedExceptions = NullPointerException.class)
    public void nullCodexExceptionTest() {
        new CustomBaseTranscoder(null);
    }

    @DataProvider
    public Object[][] encryptionTestData() {
        return new Object[][] {
                {HEXADECIMAL, 15, "F"}, {HEXADECIMAL, 255, "FF"},
                {LETTER_BINARY, 10, "BABA"}
        };
    }

    @Test(dataProvider = "encryptionTestData")
    public void encryptionTest(String codexString, int plainText, String expected) {
        CustomBaseTranscoder transcoder = new CustomBaseTranscoder(codexString);
        BigInt testValue = new BigInt(plainText);

        String actual = transcoder.encrypt(testValue);
        assertEquals(actual, expected);
    }

    @DataProvider
    public Object[][] decryptionTestData() {
        return new Object[][] {
                {LETTER_BINARY, "BABA", "10"}, {LETTER_BINARY, "BBBB", "15"}
        };
    }

    @Test(dataProvider = "decryptionTestData")
    public void decryptionTest(String codexString, String encryptedString, String expected) {
        CustomBaseTranscoder transcoder = new CustomBaseTranscoder(codexString);

        String actual = transcoder.decrypt(encryptedString);
        assertEquals(actual, expected);
    }
}
