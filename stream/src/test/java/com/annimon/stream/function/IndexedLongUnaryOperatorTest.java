package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

/** Tests {@link IndexedLongUnaryOperator} */
public class IndexedLongUnaryOperatorTest {

    @Test
    public void testWrap() {
        IndexedLongUnaryOperator identity =
                IndexedLongUnaryOperator.Util.wrap(LongUnaryOperator.Util.identity());

        assertThat(identity.applyAsLong(1, 3228), is(3228L));
    }

    @Test
    public void testPrivateUtilConstructor() {
        assertThat(IndexedLongUnaryOperator.Util.class, hasOnlyPrivateConstructors());
    }
}
