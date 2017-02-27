package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class WithoutNullsTest {

     @Test
    public void testWithoutNulls() {
        final long notNullAmount = Stream.range(0, 10)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return integer % 3 == 0 ? null : "";
                    }
                })
                .withoutNulls()
                .count();
        assertEquals(6, notNullAmount);
    }
}
