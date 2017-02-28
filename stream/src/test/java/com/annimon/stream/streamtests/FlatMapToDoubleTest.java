package com.annimon.stream.streamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.closeTo;

public final class FlatMapToDoubleTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testFlatMapToDouble() {
        Stream.of(2, 4)
                .flatMapToDouble(new Function<Integer, DoubleStream>() {
                    @Override
                    public DoubleStream apply(Integer t) {
                        return DoubleStream.of(t / 10d, t / 20d);
                    }
                })
                .custom(assertElements(array(
                        closeTo(0.2, 0.0001),
                        closeTo(0.1, 0.0001),
                        closeTo(0.4, 0.0001),
                        closeTo(0.2, 0.0001)
                )));
    }
}
