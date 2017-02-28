package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import org.junit.Test;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class ToArrayTest {

    @Test
    public void testToArray() {
        Object[] objects = Stream.range(0, 200)
               .filter(Functions.remainder(4))
               .toArray();

        assertEquals(50, objects.length);
        assertNotNull(objects[10]);
        assertThat(objects[0], instanceOf(Integer.class));
    }

    @Test
    public void testToArrayWithGenerator() {
        Integer[] numbers = Stream.range(1, 1000)
               .filter(Functions.remainder(2))
               .toArray(Functions.arrayGenerator(Integer[].class));

        assertTrue(numbers.length > 0);
        assertNotNull(numbers[100]);
    }
}
