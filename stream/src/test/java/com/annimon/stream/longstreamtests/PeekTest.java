package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongConsumer;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class PeekTest {

    @Test
    public void testPeek() {
        LongStream.empty()
                .peek(new LongConsumer() {
                    @Override
                    public void accept(long value) {
                        fail();
                    }
                })
                .custom(assertIsEmpty());

        final long[] expected = {12, 34};
        assertThat(LongStream.of(12, 34).peek(new LongConsumer() {

            private int index = 0;

            @Override
            public void accept(long value) {
                assertThat(value, is(expected[index++]));
            }
        }).count(), is(2L));
    }
}
