package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleFunction;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class MapToObjTest {

    @Test
    public void testMapToObj() {
        DoubleFunction<String> doubleToString = new DoubleFunction<String>() {
            @Override
            public String apply(double value) {
                return Double.toString(value);
            }
        };
        DoubleStream.of(1.0, 2.12, 3.234)
                .mapToObj(doubleToString)
                .custom(assertElements(contains(
                        "1.0", "2.12", "3.234"
                )));

        DoubleStream.empty()
                .mapToObj(doubleToString)
                .custom(StreamMatcher.<String>assertIsEmpty());
    }
}
