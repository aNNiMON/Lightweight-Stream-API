package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.ToDoubleFunction;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;

public final class MapToDoubleTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testMapToDouble() {
        final ToDoubleFunction<String> stringToDouble = new ToDoubleFunction<String>() {
            @Override
            public double applyAsDouble(String t) {
                return Double.parseDouble(t);
            }
        };
        Stream.of("1.23", "4.56789", "10.1112")
                .mapToDouble(stringToDouble)
                .custom(assertElements(arrayContaining(
                        closeTo(1.23, 0.000001),
                        closeTo(4.56789, 0.000001),
                        closeTo(10.1112, 0.000001)
                )));
    }
}
