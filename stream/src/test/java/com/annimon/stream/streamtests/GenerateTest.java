package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class GenerateTest {

    @Test
    public void testGenerate() {
        Stream.generate(Functions.fibonacci())
                .limit(10)
                .custom(assertElements(contains(
                        0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L
                )));
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateNull() {
        Stream.generate(null);
    }
}
