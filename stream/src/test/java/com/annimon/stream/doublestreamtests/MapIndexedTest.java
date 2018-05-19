package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.IndexedDoubleUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;

public final class MapIndexedTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testMapIndexed() {
        DoubleStream.of(0.1, 0.3, 0.8, 1.2)
                .mapIndexed(new IndexedDoubleUnaryOperator() {
                    @Override
                    public double applyAsDouble(int index, double value) {
                        return index * value;
                    }
                })
                .custom(assertElements(arrayContaining(
                       closeTo(0, 0.001), // (0 * 0.1)
                       closeTo(0.3, 0.001), // (1 * 0.3)
                       closeTo(1.6, 0.001), // (2 * 0.8)
                       closeTo(3.6, 0.001) // (3 * 1.2)
                )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMapIndexedWithStartAndStep() {
        DoubleStream.of(0.1, 0.3, 0.8, 1.2)
                .mapIndexed(4, -2, new IndexedDoubleUnaryOperator() {
                    @Override
                    public double applyAsDouble(int index, double value) {
                        return index * value;
                    }
                })
                .custom(assertElements(arrayContaining(
                       closeTo(0.4, 0.001), // (4 * 0.1)
                       closeTo(0.6, 0.001), // (2 * 0.3)
                       closeTo(0, 0.001), // (0 * 0.8)
                       closeTo(-2.4, 0.001) // (-2 * 1.2)
                )));
    }
}
