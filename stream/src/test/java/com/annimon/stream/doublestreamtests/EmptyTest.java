package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertThat(DoubleStream.empty(), isEmpty());
    }
}
