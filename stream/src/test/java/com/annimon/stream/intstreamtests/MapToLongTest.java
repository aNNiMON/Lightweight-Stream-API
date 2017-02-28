package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntToLongFunction;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapToLongTest {

    @Test
    public void testMapToLong() {
        IntStream.rangeClosed(2, 4)
                .mapToLong(new IntToLongFunction() {
                    @Override
                    public long applyAsLong(int value) {
                        return value * 10000000000L;
                    }
                })
                .custom(assertElements(arrayContaining(
                        20000000000L,
                        30000000000L,
                        40000000000L
                )));
    }
}
