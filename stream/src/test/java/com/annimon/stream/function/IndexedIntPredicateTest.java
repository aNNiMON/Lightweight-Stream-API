package com.annimon.stream.function;

import com.annimon.stream.Functions;
import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;

/**
 * Tests {@code IndexedIntPredicate}.
 *
 * @see IndexedIntPredicate
 */
public class IndexedIntPredicateTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedIntPredicate.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testEven() {
        assertTrue(areIndexAndValueEven.test(10, 10));
        assertFalse(areIndexAndValueEven.test(5, 10));
        assertFalse(areIndexAndValueEven.test(5, 5));
    }

    @Test
    public void testWrap() {
        IndexedIntPredicate predicate = IndexedIntPredicate.Util
                .wrap(Functions.remainderInt(2));

        assertTrue(predicate.test(0, 50));
        assertFalse(predicate.test(2, 55));
        assertFalse(predicate.test(9, 9));
    }

    private static final IndexedIntPredicate areIndexAndValueEven
            = new IndexedIntPredicate() {

        @Override
        public boolean test(int index, int value) {
            return (index % 2 == 0) && (value % 2 == 0);
        }
    };
}
