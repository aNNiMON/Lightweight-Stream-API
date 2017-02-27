package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isPresent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public final class MaxTest {

    @Test
    public void testMax() {
        Optional<Integer> max = Stream.of(6, 3, 9, 0, -7, 19)
                .max(Functions.naturalOrder());

        assertThat(max, isPresent());
        assertNotNull(max.get());
        assertEquals(19, (int) max.get());
    }

    @Test
    public void testMaxDescendingOrder() {
        Optional<Integer> max = Stream.of(6, 3, 9, 0, -7, 19)
                .max(Functions.descendingAbsoluteOrder());

        assertThat(max, isPresent());
        assertNotNull(max.get());
        assertEquals(0, (int) max.get());
    }

    @Test
    public void testMaxEmpty() {
        Optional<Integer> max = Stream.<Integer>empty()
                .max(Functions.naturalOrder());

        assertThat(max, OptionalMatcher.isEmpty());
    }
}
