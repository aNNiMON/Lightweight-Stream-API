package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.annimon.stream.Functions;
import org.junit.Test;

/**
 * Tests {@code IndexedDoublePredicate}.
 *
 * @see IndexedDoublePredicate
 */
public class IndexedDoublePredicateTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedDoublePredicate.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testEven() {
        assertTrue(areIndexAndValueGreaterThan20.test(30, 40.0));
        assertFalse(areIndexAndValueGreaterThan20.test(0, 2.0));
        assertFalse(areIndexAndValueGreaterThan20.test(20, 20.0));
    }

    @Test
    public void testWrap() {
        IndexedDoublePredicate predicate = IndexedDoublePredicate.Util.wrap(greaterThan20);

        assertTrue(predicate.test(42, 30.0));
        assertFalse(predicate.test(19, 18.0));
        assertFalse(predicate.test(20, 20.0));
    }

    private static final DoublePredicate greaterThan20 = Functions.greaterThan(20.0);

    private static final IndexedDoublePredicate areIndexAndValueGreaterThan20 =
            new IndexedDoublePredicate() {

                @Override
                public boolean test(int index, double value) {
                    return greaterThan20.test(index) && greaterThan20.test(value);
                }
            };
}
