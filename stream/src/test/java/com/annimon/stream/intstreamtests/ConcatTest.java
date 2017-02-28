package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class ConcatTest {

    @Test
    public void testStreamConcat() {
        IntStream a1 = IntStream.empty();
        IntStream b1 = IntStream.empty();
        IntStream.concat(a1,b1)
                .custom(assertIsEmpty());

        IntStream a2 = IntStream.of(1, 2, 3);
        IntStream b2 = IntStream.empty();
        IntStream.concat(a2, b2)
                .custom(assertElements(arrayContaining(
                        1, 2, 3
                )));

        IntStream a3 = IntStream.empty();
        IntStream b3 = IntStream.of(42);
        IntStream.concat(a3, b3)
                .custom(assertElements(arrayContaining(
                        42
                )));

        IntStream a4 = IntStream.of(2, 4, 6, 8);
        IntStream b4 = IntStream.of(1, 3, 5, 7, 9);
        IntStream.concat(a4, b4)
                .custom(assertElements(arrayContaining(
                        2, 4, 6, 8, 1, 3, 5, 7, 9
                )));
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
