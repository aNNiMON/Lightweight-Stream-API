package com.annimon.stream.function;

import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link IndexedLongUnaryOperator}
 */
public class IndexedLongUnaryOperatorTest {

    @Test
    public void testWrap() {
        IndexedLongUnaryOperator identity = IndexedLongUnaryOperator.Util
                .wrap(LongUnaryOperator.Util.identity());

        assertThat(identity.applyAsLong(1, 3228), is(3228L));
    }

    @Test
    public void testPrivateUtilConstructor() {
        assertThat(IndexedLongUnaryOperator.Util.class, hasOnlyPrivateConstructors());
    }
}