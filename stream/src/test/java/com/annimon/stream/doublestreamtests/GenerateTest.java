package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleSupplier;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class GenerateTest {

    @Test
    public void testStreamGenerate() {
        DoubleStream stream = DoubleStream.generate(new DoubleSupplier() {
            @Override
            public double getAsDouble() {
                return 1.234;
            }
        });
        assertThat(stream.limit(3), elements(arrayContaining(1.234, 1.234, 1.234)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamGenerateNull() {
        DoubleStream.generate(null);
    }
}
