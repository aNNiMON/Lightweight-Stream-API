package com.annimon.stream.streamtests;

import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.IntConsumer;
import org.junit.Test;

public final class MapMultiToIntTest {

    @Test
    public void testMapMultiToInt() {
        Stream.rangeClosed(2, 4)
                .mapMultiToInt(
                        new BiConsumer<Integer, IntConsumer>() {
                            @Override
                            public void accept(Integer value, IntConsumer consumer) {
                                for (int i = 0; i < value; i++) {
                                    consumer.accept(value);
                                }
                            }
                        })
                .custom(assertElements(arrayContaining(2, 2, 3, 3, 3, 4, 4, 4, 4)));
    }

    @Test
    public void testMapMultiToIntEmpty() {
        Stream.rangeClosed(2, 4)
                .mapMultiToInt(
                        new BiConsumer<Integer, IntConsumer>() {
                            @Override
                            public void accept(Integer value, IntConsumer consumer) {}
                        })
                .custom(assertIsEmpty());
    }
}
