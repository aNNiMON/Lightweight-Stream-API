package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class ConcatTest {

    @Test
    public void testStreamConcat() {
        IntStream a1 = IntStream.empty();
        IntStream b1 = IntStream.empty();

        assertTrue(IntStream.concat(a1,b1).count() == 0);

        IntStream a2 = IntStream.of(1,2,3);
        IntStream b2 = IntStream.empty();

        assertTrue(IntStream.concat(a2, b2).count() == 3);

        IntStream a3 = IntStream.empty();
        IntStream b3 = IntStream.of(42);

        assertTrue(IntStream.concat(a3, b3).findFirst().getAsInt() == 42);

        IntStream a4 = IntStream.of(2, 4, 6, 8);
        IntStream b4 = IntStream.of(1, 3, 5, 7, 9);

        assertTrue(IntStream.concat(a4, b4).count() == 9);
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullA() {
        IntStream.concat(null, IntStream.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullB() {
        IntStream.concat(IntStream.empty(), null);
    }
}
