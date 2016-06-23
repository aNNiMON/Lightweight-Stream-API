package com.annimon.stream.function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests {@code ThrowableFunction}.
 *
 * @see com.annimon.stream.function.ThrowableFunction
 */
public class ThrowableFunctionTest {

    @Test
    public void testApply() {
        assertEquals(100, (int) toInt.apply("100"));
    }


    @Test(expected = NumberFormatException.class)
    public void testApplyWithRuntimeException() {
        toInt.apply("oops");
    }

    private static final ThrowableFunction<String, Integer, NumberFormatException> toInt
            = new ThrowableFunction<String, Integer, NumberFormatException>() {
        @Override
        public Integer apply(String value) throws NumberFormatException {
            return Integer.parseInt(value);
        }
    };

}
