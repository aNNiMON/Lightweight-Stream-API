package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class MapTest {

    @Test
    public void testMap() {
        LongUnaryOperator negator = new LongUnaryOperator() {
            @Override
            public long applyAsLong(long operand) {
                return -operand;
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L).map(negator),
                elements(arrayContaining(-10L, -20L, -30L)));

        assertThat(LongStream.empty().map(negator),
                isEmpty());
    }
}
