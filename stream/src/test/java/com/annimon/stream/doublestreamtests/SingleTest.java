package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SingleTest {

    @Test(expected = NoSuchElementException.class)
    public void testSingleOnEmptyStream() {
        DoubleStream.empty().single();
    }

    @Test
    public void testSingleOnOneElementStream() {
        assertThat(DoubleStream.of(42d).single(), is(42d));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleOnMoreElementsStream() {
        DoubleStream.of(0, 1, 2).single();
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleAfterFilteringToEmptyStream() {
        DoubleStream.of(0, 1, 2)
                .filter(Functions.greaterThan(Math.PI))
                .single();
    }

    @Test
    public void testSingleAfterFilteringToOneElementStream() {
        double result = DoubleStream.of(1.0, 10.12, -3.01)
                .filter(Functions.greaterThan(Math.PI))
                .single();
        assertThat(result, is(10.12d));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleAfterFilteringToMoreElementStream() {
        DoubleStream.of(1.0, 10.12, -3.01, 6.45)
                .filter(Functions.greaterThan(Math.PI))
                .single();
    }
}
