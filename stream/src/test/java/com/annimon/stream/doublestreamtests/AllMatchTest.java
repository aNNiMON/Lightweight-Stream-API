package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class AllMatchTest {

    @Test
    public void testAllMatch() {
        assertFalse(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d)
                .allMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.of(10.347, 19.84, 100d)
                .allMatch(Functions.greaterThan(Math.PI)));

        assertFalse(DoubleStream.of(0.012, 3.039)
                .allMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.empty()
                .allMatch(Functions.greaterThan(Math.PI)));
    }
}
