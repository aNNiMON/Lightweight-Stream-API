package com.annimon.stream.function;

import com.annimon.stream.IntStream;
import org.junit.Test;



import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link IntUnaryOperator}
 */
public class IntUnaryOperatorTest {

    @Test
    public void testIdentity() {
        IntUnaryOperator identity = IntUnaryOperator.Util.identity();

        assertEquals(15, IntStream.of(1, 2, 3, 4, 5).map(identity).sum());
    }

    @Test
    public void testPrivateUtilConstructor() {
        assertThat(IntUnaryOperator.Util.class, hasOnlyPrivateConstructors());
    }
}