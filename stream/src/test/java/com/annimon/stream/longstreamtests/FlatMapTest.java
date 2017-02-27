package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongFunction;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class FlatMapTest {

    @Test
    public void testFlatMap() {
        LongFunction<LongStream> twicer = new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                return LongStream.of(value, value);
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L).flatMap(twicer),
                elements(arrayContaining(10L, 10L, 20L, 20L, 30L, 30L)));

        assertThat(LongStream.of(10L, 20L, -30L).flatMap(new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                if (value < 0) return LongStream.of(value);
                return null;
            }
        }), elements(arrayContaining(-30L)));

        assertThat(LongStream.of(10L, 20L, -30L).flatMap(new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                if (value < 0) return LongStream.empty();
                return LongStream.of(value);
            }
        }), elements(arrayContaining(10L, 20L)));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIterator() {
        LongStream.empty().flatMap(new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                return LongStream.of(value);
            }
        }).iterator().nextLong();
    }
}
