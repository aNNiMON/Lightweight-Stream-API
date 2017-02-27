package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class BoxedTest {

    @Test
    public void testBoxed() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).boxed(),
                StreamMatcher.elements(is(Arrays.asList(0.1, 0.2, 0.3))));
    }
}
