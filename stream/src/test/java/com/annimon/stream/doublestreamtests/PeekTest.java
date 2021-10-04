package com.annimon.stream.doublestreamtests;

import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleConsumer;
import org.junit.Test;

public final class PeekTest {

    @Test
    public void testPeekOnEmptyStream() {
        DoubleStream.empty()
                .peek(
                        new DoubleConsumer() {
                            @Override
                            public void accept(double value) {
                                fail();
                            }
                        })
                .custom(assertIsEmpty());
    }

    @Test
    public void testPeek() {
        final double[] expected = {1.2, 3.456};
        assertThat(
                DoubleStream.of(1.2, 3.456)
                        .peek(
                                new DoubleConsumer() {

                                    private int index = 0;

                                    @Override
                                    public void accept(double value) {
                                        assertThat(value, closeTo(expected[index++], 0.0001));
                                    }
                                })
                        .count(),
                is(2L));
    }
}
