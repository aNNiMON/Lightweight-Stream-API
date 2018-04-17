package com.annimon.stream.function;

import com.annimon.stream.Functions;
import com.annimon.stream.IntPair;
import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code IndexedLongFunction}.
 *
 * @see IndexedLongFunction
 */
public class IndexedLongFunctionTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedLongFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testApply() {
        assertEquals(new IntPair<String>(1, "42"), wrapper.apply(1, 42L));
        assertEquals(new IntPair<String>(0, "0"), wrapper.apply(0, 0L));
    }

    @Test
    public void testWrap() {
        IndexedLongFunction<String> function = IndexedLongFunction.Util.wrap(toString);

        assertEquals("60", function.apply(0, 60L));
        assertEquals("-10", function.apply(10, -10L));
    }

    private static final LongFunction<String> toString = Functions.convertLongToString();

    private static final IndexedLongFunction<IntPair<String>>
            wrapper = new IndexedLongFunction<IntPair<String>>() {

        @Override
        public IntPair<String> apply(int index, long value) {
            return new IntPair<String>(index, toString.apply(value));
        }
    };
}
