package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongSupplier;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class GenerateTest {

    @Test
    public void testStreamGenerate() {
        LongStream stream = LongStream.generate(new LongSupplier() {
            @Override
            public long getAsLong() {
                return 1234L;
            }
        });
        assertThat(stream.limit(3), elements(arrayContaining(1234L, 1234L, 1234L)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamGenerateNull() {
        LongStream.generate(null);
    }
}
