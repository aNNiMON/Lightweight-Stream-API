package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.StreamMatcher.isEmpty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class OfNullableTest {

    @Test
    public void testStreamOfNullable() {
        assertThat(Stream.ofNullable(null), isEmpty());

        assertThat(Stream.ofNullable(5), elements(is(Arrays.asList(5))));
    }

    @Test
    public void testStreamOfNullableWithIterable() {
        assertThat(Stream.ofNullable((List<?>) null), isEmpty());

        assertThat(Stream.ofNullable(Arrays.asList(5, 10, 15)),
                elements(is(Arrays.asList(5, 10, 15))));
    }
}
