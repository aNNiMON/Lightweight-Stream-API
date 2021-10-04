package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import com.annimon.stream.Functions;
import org.junit.Test;

/**
 * Tests {@code IndexedIntFunction}.
 *
 * @see IndexedIntFunction
 */
public class IndexedIntFunctionTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedIntFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testApply() {
        assertEquals("4", multiplyEvaluator.apply(2, 2));
        assertEquals("-20", multiplyEvaluator.apply(10, -2));
        assertEquals("0", multiplyEvaluator.apply(-1, 0));
    }

    @Test
    public void testWrap() {
        IndexedIntFunction<String> function =
                IndexedIntFunction.Util.wrap(Functions.convertIntToString());

        assertEquals("60", function.apply(0, 60));
        assertEquals("-10", function.apply(10, -10));
    }

    private static final IndexedIntFunction<String> multiplyEvaluator =
            new IndexedIntFunction<String>() {

                @Override
                public String apply(int first, int second) {
                    return String.valueOf(first * second);
                }
            };
}
