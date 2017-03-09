package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class OfPrimitiveIteratorTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testStreamOfPrimitiveIterator() {
        DoubleStream stream = DoubleStream.of(new PrimitiveIterator.OfDouble() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public double nextDouble() {
                index++;
                return index + 0.0021;
            }
        });
        assertThat(stream, elements(arrayContaining(
                closeTo(1.0021, 0.00001),
                closeTo(2.0021, 0.00001),
                closeTo(3.0021, 0.00001)
        )));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfPrimitiveIteratorNull() {
        DoubleStream.of((PrimitiveIterator.OfDouble) null);
    }
}
