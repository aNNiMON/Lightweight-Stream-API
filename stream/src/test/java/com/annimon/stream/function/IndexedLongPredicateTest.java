package com.annimon.stream.function;

import com.annimon.stream.Functions;
import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;

/**
 * Tests {@code IndexedLongPredicate}.
 *
 * @see IndexedLongPredicate
 */
public class IndexedLongPredicateTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedLongPredicate.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testEven() {
        assertTrue(areIndexAndValueEven.test(10, 10L));
        assertFalse(areIndexAndValueEven.test(5, 10L));
        assertFalse(areIndexAndValueEven.test(5, 5L));
    }

    @Test
    public void testWrap() {
        IndexedLongPredicate predicate = IndexedLongPredicate.Util
                .wrap(Functions.remainderLong(2));

        assertTrue(predicate.test(0, 50L));
        assertFalse(predicate.test(2, 55L));
        assertFalse(predicate.test(9, 9L));
    }

    private static final IndexedLongPredicate areIndexAndValueEven
            = new IndexedLongPredicate() {

        @Override
        public boolean test(int index, long value) {
            return (index % 2 == 0) && (value % 2 == 0);
        }
    };
}
