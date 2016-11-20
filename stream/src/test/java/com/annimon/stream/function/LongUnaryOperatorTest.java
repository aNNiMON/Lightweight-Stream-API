package com.annimon.stream.function;

import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link LongUnaryOperator}
 */
public class LongUnaryOperatorTest {

    @Test
    public void testIdentity() {
        LongUnaryOperator identity = LongUnaryOperator.Util.identity();

        assertThat(identity.applyAsLong(3228), is(3228L));
    }

    @Test
    public void testPrivateUtilConstructor() {
        assertThat(LongUnaryOperator.Util.class, hasOnlyPrivateConstructors());
    }
}