package com.annimon.stream.doublestreamtests;

import static org.junit.Assert.assertEquals;

import com.annimon.stream.DoubleStream;
import org.junit.Test;

public final class FindFirstOrElseTest {

    @Test
    public void testFindFirstOrElse() {
        assertEquals(42, DoubleStream.of(42).findFirstOrElse(10), 0.1);
        assertEquals(10, DoubleStream.empty().findFirstOrElse(10), 0.1);
    }
}
