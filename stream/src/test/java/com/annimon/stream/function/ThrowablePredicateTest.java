package com.annimon.stream.function;

import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code ThrowablePredicate}.
 * 
 * @see com.annimon.stream.function.ThrowablePredicate
 */
public class ThrowablePredicateTest {
    
    @Test
    public void testNormal() throws IOException {
        assertFalse(throwablePredicate.test(false));
    }

    @Test(expected = IOException.class)
    public void testThrow() throws IOException {
        throwablePredicate.test(true);
    }
    
    private static final ThrowablePredicate<Boolean, IOException> throwablePredicate =
            new ThrowablePredicate<Boolean, IOException>() {
        @Override
        public boolean test(Boolean value) throws IOException {
            if (value) {
                throw new IOException();
            }
            return value;
        }
    };
    
}
