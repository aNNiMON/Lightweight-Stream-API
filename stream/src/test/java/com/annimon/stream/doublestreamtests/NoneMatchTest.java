package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class NoneMatchTest {

    @Test
    public void testNoneMatch() {
        assertFalse(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d)
                .noneMatch(Functions.greaterThan(Math.PI)));

        assertFalse(DoubleStream.of(10.347, 19.84, 100d)
                .noneMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.of(0.012, 3.039)
                .noneMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.empty()
                .noneMatch(Functions.greaterThan(Math.PI)));
    }
}
