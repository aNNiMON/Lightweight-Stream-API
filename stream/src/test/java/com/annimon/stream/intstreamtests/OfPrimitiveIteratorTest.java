package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class OfPrimitiveIteratorTest {

    @Test
    public void testStreamOfPrimitiveIterator() {
        int[] expected = { 0, 1 };
        IntStream stream = IntStream.of(new PrimitiveIterator.OfInt() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return index < 2;
            }

            @Override
            public int nextInt() {
                return index++;
            }
        });
        assertThat(stream.toArray(), is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfPrimitiveIteratorNull() {
        IntStream.of((PrimitiveIterator.OfInt) null);
    }
}
