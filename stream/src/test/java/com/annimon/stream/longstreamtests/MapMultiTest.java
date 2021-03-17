package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongConsumer;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapMultiTest {

    @Test
    public void testMapMulti() {
        LongStream.of(10L, 20L, 30L)
                .mapMulti(new LongStream.LongMapMultiConsumer() {
                    @Override
                    public void accept(long value, LongConsumer consumer) {
                        consumer.accept(value);
                        consumer.accept(-value);
                    }
                })
                .custom(assertElements(arrayContaining(
                        10L, -10L,
                        20L, -20L,
                        30L, -30L
                )));

        LongStream.of(10L, 20L, -30L)
                .mapMulti(new LongStream.LongMapMultiConsumer() {
                    @Override
                    public void accept(long value, LongConsumer consumer) {
                        if (value < 0) {
                            consumer.accept(value);
                        }
                    }
                })
                .custom(assertElements(arrayContaining(
                        -30L
                )));
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapMultiIterator() {
        LongStream.empty().mapMulti(new LongStream.LongMapMultiConsumer() {
            @Override
            public void accept(long value, LongConsumer consumer) {
                consumer.accept(value);
            }
        }).iterator().nextLong();
    }

    @Test
    public void testMapMultiEmpty() {
        LongStream.of(1, 2).mapMulti(new LongStream.LongMapMultiConsumer() {
            @Override
            public void accept(long value, LongConsumer consumer) {
            }
        }).custom(assertIsEmpty());
    }
}
