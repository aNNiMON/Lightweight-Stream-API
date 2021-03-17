package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.LongConsumer;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapMultiToLongTest {

    @Test
    public void testMapMultiToLong() {
        Stream.rangeClosed(2L, 4L)
                .mapMultiToLong(new BiConsumer<Long, LongConsumer>() {
                    @Override
                    public void accept(Long value, LongConsumer consumer) {
                        for (long i = 0; i < value; i++) {
                            consumer.accept(value);
                        }
                    }
                })
                .custom(assertElements(arrayContaining(
                        2L, 2L,
                        3L, 3L, 3L,
                        4L, 4L, 4L, 4L
                )));
    }

    @Test
    public void testMapMultiToLongEmpty() {
        Stream.rangeClosed(2L, 4L)
                .mapMultiToLong(new BiConsumer<Long, LongConsumer>() {
                    @Override
                    public void accept(Long value, LongConsumer consumer) {
                    }
                })
                .custom(assertIsEmpty());
    }
}
