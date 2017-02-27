package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import org.junit.Test;
import static org.junit.Assert.assertThat;

public final class BoxedTest {

    @Test
    public void testBoxed() {
        assertThat(IntStream.of(1, 10, 20).boxed().reduce(Functions.addition()),
                OptionalMatcher.hasValue(31));
    }
}
