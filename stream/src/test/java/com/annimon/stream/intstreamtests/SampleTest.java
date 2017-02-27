package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class SampleTest {

    @Test
    public void testSample() {
        int[] expected = { 1, 1, 1 };
        int[] actual = IntStream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(3).toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testSampleWithStep1() {
        int[] expected = { 1, 2, 3, 1, 2, 3, 1, 2, 3 };
        int[] actual = IntStream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(1).toArray();
        assertThat(actual, is(expected));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        IntStream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(-1).count();
    }
}
