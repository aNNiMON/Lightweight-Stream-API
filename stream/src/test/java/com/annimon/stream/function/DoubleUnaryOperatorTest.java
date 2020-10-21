package com.annimon.stream.function;

import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

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