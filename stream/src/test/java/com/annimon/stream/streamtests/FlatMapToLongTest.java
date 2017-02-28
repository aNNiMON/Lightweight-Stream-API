package com.annimon.stream.streamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.LongUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class FlatMapToLongTest {

    @Test
    public void testFlatMapToLong() {
        Stream.rangeClosed(2L, 4L)
                .flatMapToLong(new Function<Long, LongStream>() {
                    @Override
                    public LongStream apply(Long t) {
                        return LongStream
                                .iterate(t, LongUnaryOperator.Util.identity())
                                .limit(t);
                    }
                })
                .custom(assertElements(arrayContaining(
                        2L, 2L,
                        3L, 3L, 3L,
                        4L, 4L, 4L, 4L
                )));
    }
}
