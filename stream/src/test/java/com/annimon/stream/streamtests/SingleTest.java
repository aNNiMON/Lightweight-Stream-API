package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SingleTest {

    @Test(expected = NoSuchElementException.class)
    public void testSingleOnEmptyStream() {
        Stream.empty().single();
    }

    @Test
    public void testSingleOnOneElementStream() {
        Integer result = Stream.of(42).single();

        assertThat(result, is(42));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleOnMoreElementsStream() {
        Stream.rangeClosed(1, 2).single();
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleAfterFilteringToEmptyStream() {
        Stream.range(1, 5)
                .filter(Functions.remainder(6))
                .single();
    }

    @Test
    public void testSingleAfterFilteringToOneElementStream() {
        Integer result = Stream.range(1, 10)
                .filter(Functions.remainder(6))
                .single();

        assertThat(result, is(6));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleAfterFilteringToMoreElementStream() {
        Stream.range(1, 100)
                .filter(Functions.remainder(6))
                .single();
    }
}
