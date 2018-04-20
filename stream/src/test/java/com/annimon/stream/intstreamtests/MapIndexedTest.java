package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapIndexedTest {

    @Test
    public void testMapIndexed() {
        IntStream.rangeClosed(4, 8)
                .mapIndexed(new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int index, int value) {
                        return index * value;
                    }
                })
                .custom(assertElements(arrayContaining(
                       0,  // (0 * 4)
                       5,  // (1 * 5)
                       12, // (2 * 6)
                       21, // (3 * 7)
                       32  // (4 * 8)
                )));
    }

    @Test
    public void testMapIndexedWithStartAndStep() {
        IntStream.rangeClosed(4, 8)
                .mapIndexed(20, -5, new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int index, int value) {
                        return index * value;
                    }
                })
                .custom(assertElements(arrayContaining(
                       80, // (20 * 4)
                       75, // (15 * 5)
                       60, // (10 * 6)
                       35, // (5  * 7)
                       0   // (0  * 8)
                )));
    }
}
