package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SingleTest {

    @Test(expected = NoSuchElementException.class)
    public void testSingleOnEmptyStream() {
        LongStream.empty().single();
    }

    @Test
    public void testSingleOnOneElementStream() {
        assertThat(LongStream.of(42).single(), is(42L));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleOnMoreElementsStream() {
        LongStream.of(0, 1, 2).single();
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleAfterFilteringToEmptyStream() {
        LongStream.of(5, 7, 9)
                .filter(Functions.remainderLong(2))
                .single();
    }

    @Test
    public void testSingleAfterFilteringToOneElementStream() {
        long result = LongStream.of(5, 10, -15)
                .filter(Functions.remainderLong(2))
                .single();
        assertThat(result, is(10L));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleAfterFilteringToMoreElementStream() {
        LongStream.of(5, 10, -15, -20)
                .filter(Functions.remainderLong(2))
                .single();
    }
}
