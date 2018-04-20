package com.annimon.stream.function;

import com.annimon.stream.Functions;
import com.annimon.stream.IntPair;
import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code IndexedDoubleFunction}.
 *
 * @see IndexedDoubleFunction
 */
public class IndexedDoubleFunctionTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedDoubleFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testApply() {
        assertEquals(new IntPair<String>(1, "4.2"), wrapper.apply(1, 4.2));
        assertEquals(new IntPair<String>(0, "0.0"), wrapper.apply(0, 0.0));
    }

    @Test
    public void testWrap() {
        IndexedDoubleFunction<String> function = IndexedDoubleFunction.Util.wrap(toString);

        assertEquals("60.0", function.apply(0, 60.0));
        assertEquals("-10.0", function.apply(10, -10.0));
    }

    private static final DoubleFunction<String> toString = Functions.convertDoubleToString();

    private static final IndexedDoubleFunction<IntPair<String>>
            wrapper = new IndexedDoubleFunction<IntPair<String>>() {

        @Override
        public IntPair<String> apply(int index, double value) {
            return new IntPair<String>(index, toString.apply(value));
        }
    };
}
