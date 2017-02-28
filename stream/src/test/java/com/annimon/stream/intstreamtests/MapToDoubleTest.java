package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntToDoubleFunction;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;

public final class MapToDoubleTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testMapToDouble() {
        IntStream.rangeClosed(2, 4)
                .mapToDouble(new IntToDoubleFunction() {
                    @Override
                    public double applyAsDouble(int value) {
                        return value / 10d;
                    }
                })
                .custom(assertElements(arrayContaining(
                        closeTo(0.2, 0.00001),
                        closeTo(0.3, 0.00001),
                        closeTo(0.4, 0.00001)
                )));
    }
}
