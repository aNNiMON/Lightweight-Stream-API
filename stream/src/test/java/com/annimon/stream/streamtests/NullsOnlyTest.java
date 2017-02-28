package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class NullsOnlyTest {

    @Test
    public void testNullsOnly() {
        final long nullsAmount = Stream.range(0, 10)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return integer % 3 == 0 ? null : "";
                    }
                })
                .nullsOnly()
                .count();
        assertEquals(4, nullsAmount);
    }
}
