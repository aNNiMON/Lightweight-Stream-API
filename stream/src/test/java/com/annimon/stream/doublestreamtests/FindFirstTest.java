package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindFirstTest {

    @Test
    public void testFindFirst() {
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).findFirst(),
                hasValue(0.012));

        assertThat(DoubleStream.empty().findFirst(),
                isEmpty());
    }
}
