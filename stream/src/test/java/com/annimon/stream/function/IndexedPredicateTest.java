package com.annimon.stream.function;

import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;

/**
 * Tests {@code IndexedPredicate}.
 *
 * @see com.annimon.stream.function.IndexedPredicate
 */
public class IndexedPredicateTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedPredicate.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testEven() {
        assertTrue(areIndexAndValueEven.test(10, 10));
        assertFalse(areIndexAndValueEven.test(5, 10));
        assertFalse(areIndexAndValueEven.test(5, 5));
    }

    @Test
    public void testWrap() {
        IndexedPredicate<Integer> predicate = IndexedPredicate.Util
                .wrap(Functions.remainder(2));

        assertTrue(predicate.test(0, 50));
        assertFalse(predicate.test(2, 55));
        assertFalse(predicate.test(9, 9));
    }

    private static final IndexedPredicate<Integer> areIndexAndValueEven
            = new IndexedPredicate<Integer>() {

        @Override
        public boolean test(int index, Integer value) {
            return (index % 2 == 0) && (value % 2 == 0);
        }
    };
}
