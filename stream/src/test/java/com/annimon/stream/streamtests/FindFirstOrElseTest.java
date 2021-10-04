package com.annimon.stream.streamtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import org.junit.Test;

public final class FindFirstOrElseTest {

    @Test
    public void testFindFirstOrElse() {
        Integer result = Stream.range(0, 10).findFirstOrElse(10);
        assertThat(result, is(0));
    }

    @Test
    public void testFindFirstOrElseOnEmptyStream() {
        Integer result = Stream.<Integer>empty().findFirstOrElse(10);
        assertThat(result, is(10));
    }

    @Test
    public void testFindFirstOrElseAfterFiltering() {
        Integer result = Stream.range(1, 1000).filter(Functions.remainder(6)).findFirstOrElse(10);
        assertThat(result, is(6));
    }
}
