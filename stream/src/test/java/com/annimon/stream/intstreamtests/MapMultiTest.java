package com.annimon.stream.intstreamtests;

import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntConsumer;
import java.util.NoSuchElementException;
import org.junit.Test;

public final class MapMultiTest {

    @Test
    public void testMapMulti() {
        IntStream.of(10, 20, 30)
                .mapMulti(
                        new IntStream.IntMapMultiConsumer() {
                            @Override
                            public void accept(int value, IntConsumer consumer) {
                                consumer.accept(value);
                                consumer.accept(-value);
                            }
                        })
                .custom(
                        assertElements(
                                arrayContaining(
                                        10, -10,
                                        20, -20,
                                        30, -30)));

        IntStream.of(10, 20, -30)
                .mapMulti(
                        new IntStream.IntMapMultiConsumer() {
                            @Override
                            public void accept(int value, IntConsumer consumer) {
                                if (value < 0) {
                                    consumer.accept(value);
                                }
                            }
                        })
                .custom(assertElements(arrayContaining(-30)));
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapMultiIterator() {
        IntStream.empty()
                .mapMulti(
                        new IntStream.IntMapMultiConsumer() {
                            @Override
                            public void accept(int value, IntConsumer consumer) {
                                consumer.accept(value);
                            }
                        })
                .iterator()
                .nextInt();
    }

    @Test
    public void testMapMultiEmpty() {
        IntStream.of(1, 2)
                .mapMulti(
                        new IntStream.IntMapMultiConsumer() {
                            @Override
                            public void accept(int value, IntConsumer consumer) {}
                        })
                .custom(assertIsEmpty());
    }
}
