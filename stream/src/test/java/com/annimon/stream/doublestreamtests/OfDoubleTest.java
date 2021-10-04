package com.annimon.stream.doublestreamtests;

import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.DoubleStream;
import org.junit.Test;

public final class OfDoubleTest {

    @Test
    public void testStreamOfDouble() {
        DoubleStream.of(1.234).custom(assertElements(arrayContaining(1.234)));
    }
}
