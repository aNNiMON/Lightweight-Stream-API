package com.annimon.stream.function;

import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;

/**
 * Tests {@code IndexedFunction}.
 *
 * @see com.annimon.stream.function.IndexedFunction
 */
public class IndexedFunctionTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testApply() {
        assertEquals("x", charPlusIndexToString.apply(1, 'w'));
        assertEquals("k", charPlusIndexToString.apply(10, 'a'));
        assertEquals("5", charPlusIndexToString.apply(-1, '6'));
    }

    @Test
    public void testWrap() {
        IndexedFunction<Object, String> function = IndexedFunction.Util
                .wrap(Functions.convertToString());

        assertEquals("60", function.apply(0, 60));
        assertEquals("A", function.apply(10, (char)65));
    }

    private static final IndexedFunction<Character, String>
            charPlusIndexToString = new IndexedFunction<Character, String>() {

        @Override
        public String apply(int index, Character t) {
            return Character.toString((char) (t + index));
        }
    };
}
