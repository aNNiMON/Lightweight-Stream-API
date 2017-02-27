package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class GenerateTest {

    @Test
    public void testGenerate() {
        List<Long> expected = Arrays.asList(0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L);
        Stream<Long> stream = Stream.generate(Functions.fibonacci()).limit(10);
        assertThat(stream, elements(is(expected)));
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateNull() {
        Stream.generate(null);
    }
}
