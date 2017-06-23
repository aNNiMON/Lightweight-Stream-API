package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindLastTest {

    @Test
    public void testFindLast() {
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).findLast(),
                hasValue(100d));

        assertThat(DoubleStream.of(100d).findLast(),
                hasValue(100d));

        assertThat(DoubleStream.empty().findLast(),
                isEmpty());
    }
}
