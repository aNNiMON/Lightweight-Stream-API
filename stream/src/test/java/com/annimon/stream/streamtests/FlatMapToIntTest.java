package com.annimon.stream.streamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class FlatMapToIntTest {

    @Test
    public void testFlatMapToInt() {
        Stream.rangeClosed(2, 4)
                .flatMapToInt(new Function<Integer, IntStream>() {

                    @Override
                    public IntStream apply(Integer t) {
                        return IntStream
                                .iterate(t, IntUnaryOperator.Util.identity())
                                .limit(t);
                    }
                })
                .custom(assertElements(arrayContaining(
                        2, 2,
                        3, 3, 3,
                        4, 4, 4, 4
                )));
    }
}
