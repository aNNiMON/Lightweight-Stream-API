package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class OfDoubleTest {

    @Test
    public void testStreamOfDouble() {
        assertThat(DoubleStream.of(1.234), elements(arrayContaining(1.234)));
    }
}
