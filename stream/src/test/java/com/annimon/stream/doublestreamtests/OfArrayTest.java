package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class OfArrayTest {

    @Test
    public void testStreamOfDoubles() {
        assertThat(DoubleStream.of(3.2, 2.8), elements(arrayContaining(3.2, 2.8)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfDoublesNull() {
        DoubleStream.of((double[]) null);
    }

    @Test
    public void testStreamOfEmptyArray() {
        DoubleStream.of(new double[0])
                .custom(assertIsEmpty());
    }
}
