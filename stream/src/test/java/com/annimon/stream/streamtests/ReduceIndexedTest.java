package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class ReduceIndexedTest {

    @Test
    public void testReduceIndexed() {
        int result = Stream.rangeClosed(1, 5)
                .reduceIndexed(10, Functions.indexedAddition());
        assertEquals(35, result);
    }

    @Test
    public void testReduceIndexedWithStartAndStep() {
        int result = Stream.rangeClosed(1, 5)
                .reduceIndexed(1, 2, 0, Functions.indexedAddition());
        assertEquals(40, result);
    }
}
