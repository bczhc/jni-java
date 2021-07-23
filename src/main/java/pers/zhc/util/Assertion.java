package pers.zhc.util;

/**
 * @author bczhc
 */
public class Assertion {
    public static void doAssertion(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }
}
