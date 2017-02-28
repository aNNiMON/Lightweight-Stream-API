package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import org.junit.Test;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public final class ToListTest {

    @Test
    public void testToList() {
        assertThat(Stream.range(0, 5).toList(),
                contains(0, 1, 2, 3, 4));
    }
}
