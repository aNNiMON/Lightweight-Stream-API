package com.annimon.stream.longstreamtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongConsumer;
import org.junit.Test;

public final class ForEachTest {

    @Test
    public void testForEach() {
        final long[] expected = {12L, 32L, 22L, 9L};
        LongStream.of(12L, 32L, 22L, 9L)
                .forEach(
                        new LongConsumer() {

                            private int index = 0;

                            @Override
                            public void accept(long value) {
                                assertThat(value, is(expected[index++]));
                            }
                        });
    }

    @Test
    public void testForEachOnEmptyStream() {
        LongStream.empty()
                .forEach(
                        new LongConsumer() {
                            @Override
                            public void accept(long value) {
                                fail();
                            }
                        });
    }
}
