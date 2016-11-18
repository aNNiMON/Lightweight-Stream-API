package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests {@link DoubleUnaryOperator}
 */
public class DoubleUnaryOperatorTest {

    @Test
    public void testIdentity() {
        DoubleUnaryOperator identity = DoubleUnaryOperator.Util.identity();

        assertThat(identity.applyAsDouble(3.228), closeTo(3.228, 0.001));
    }

    @Test
    public void testPrivateUtilConstructor() {
        assertThat(DoubleUnaryOperator.Util.class, hasOnlyPrivateConstructors());
    }
}