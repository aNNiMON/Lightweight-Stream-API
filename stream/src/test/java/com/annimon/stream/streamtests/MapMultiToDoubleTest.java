package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.DoubleConsumer;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.closeTo;

public final class MapMultiToDoubleTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMapMultiToDouble() {
        Stream.of(2, 4)
                .mapMultiToDouble(new BiConsumer<Integer, DoubleConsumer>() {
                    @Override
                    public void accept(Integer value, DoubleConsumer consumer) {
                        consumer.accept(value / 10d);
                        consumer.accept(value / 20d);
                    }
                })
                .custom(assertElements(array(
                        closeTo(0.2, 0.0001),
                        closeTo(0.1, 0.0001),
                        closeTo(0.4, 0.0001),
                        closeTo(0.2, 0.0001)
                )));
    }

    @Test
    public void testMapMultiToDoubleEmpty() {
        Stream.rangeClosed(2, 4)
                .mapMultiToDouble(new BiConsumer<Integer, DoubleConsumer>() {
                    @Override
                    public void accept(Integer value, DoubleConsumer consumer) {
                    }
                })
                .custom(assertIsEmpty());
    }
}
