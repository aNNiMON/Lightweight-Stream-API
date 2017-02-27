package com.annimon.stream.function;

import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;

/**
 * Tests {@code IndexedBiFunction}.
 *
 * @see com.annimon.stream.function.IndexedBiFunction
 */
public class IndexedBiFunctionTest {

    private static final boolean TO_UPPER = true;
    private static final boolean TO_LOWER = false;

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedBiFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testApply() {
        assertEquals(Character.valueOf('Y'), charPlusIndexChangeCase.apply(1, 'x', TO_UPPER));
        assertEquals(Character.valueOf('z'), charPlusIndexChangeCase.apply(2, 'X', TO_LOWER));
    }

    @Test
    public void testWrap() {
        final IndexedBiFunction<Integer, Integer, Integer>
               addition = IndexedBiFunction.Util.wrap(Functions.addition());

        assertEquals(Integer.valueOf(20), addition.apply(0, 10, 10));
        assertEquals(Integer.valueOf(10), addition.apply(1000, 0, 10));
    }

    private static final IndexedBiFunction<Character, Boolean, Character>
            charPlusIndexChangeCase = new IndexedBiFunction<Character, Boolean, Character>() {

        @Override
        public Character apply(int index, Character value, Boolean flag) {
            final char ch = (char) (index + value);
            if (flag) return Character.toUpperCase(ch);
            return Character.toLowerCase(ch);
        }
    };

}
