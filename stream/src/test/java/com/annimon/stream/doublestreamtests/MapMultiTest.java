package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleConsumer;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapMultiTest {

    @Test
    public void testMapMulti() {
        DoubleStream.of(0.012, -3.039, 100d)
                .mapMulti(new DoubleStream.DoubleMapMultiConsumer() {
                    @Override
                    public void accept(double value, DoubleConsumer consumer) {
                        consumer.accept(value);
                        consumer.accept(-value);
                    }
                })
                .custom(assertElements(arrayContaining(
                        0.012, -0.012, -3.039, 3.039, 100d, -100d
                )));

        DoubleStream.of(0.012, -3.039, 100d)
                .mapMulti(new DoubleStream.DoubleMapMultiConsumer() {
                    @Override
                    public void accept(double value, DoubleConsumer consumer) {
                        if (value < 0) {
                            consumer.accept(value);
                        }
                    }
                })
                .custom(assertElements(arrayContaining(
                        -3.039
                )));
    }

    @Test(expected = NoSuchElementException.class)
    public void testMapMultiIterator() {
        DoubleStream.empty().mapMulti(new DoubleStream.DoubleMapMultiConsumer() {
            @Override
            public void accept(double value, DoubleConsumer consumer) {
                consumer.accept(value);
            }
        }).iterator().nextDouble();
    }

    @Test
    public void testMapMultiEmpty() {
        DoubleStream.of(1, 2).mapMulti(new DoubleStream.DoubleMapMultiConsumer() {
            @Override
            public void accept(double value, DoubleConsumer consumer) {
            }
        }).custom(assertIsEmpty());
    }
}
