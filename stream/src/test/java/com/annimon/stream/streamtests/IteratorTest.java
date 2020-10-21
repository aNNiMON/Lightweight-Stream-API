package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class IteratorTest {

    @Test
    public void testIterator() {
        assertThat(Stream.of(1).iterator(), is(not(nullValue())));
    }
}
