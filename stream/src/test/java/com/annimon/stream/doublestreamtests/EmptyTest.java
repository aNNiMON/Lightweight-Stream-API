package com.annimon.stream.doublestreamtests;

import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.DoubleStream;
import org.junit.Test;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertThat(DoubleStream.empty(), isEmpty());
    }
}
