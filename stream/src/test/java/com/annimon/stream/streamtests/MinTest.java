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

public final class MinTest {

    @Test
    public void testMin() {
        Optional<Integer> min = Stream.of(6, 3, 9, 0, -7, 19)
                .min(Functions.naturalOrder());

        assertThat(min, isPresent());
        assertNotNull(min.get());
        assertEquals(-7, (int) min.get());
    }

    @Test
    public void testMinDescendingOrder() {
        Optional<Integer> min = Stream.of(6, 3, 9, 0, -7, 19)
                .min(Functions.descendingAbsoluteOrder());

        assertThat(min, isPresent());
        assertNotNull(min.get());
        assertEquals(19, (int) min.get());
    }

    @Test
    public void testMinEmpty() {
        Optional<Integer> min = Stream.<Integer>empty()
                .min(Functions.naturalOrder());
        assertThat(min, OptionalMatcher.isEmpty());
    }
}
