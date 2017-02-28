package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapTest {

    @Test
    public void testMap() {
        IntStream.of(5)
                .map(new IntUnaryOperator() {
                    @Override
                    public int applyAsInt(int operand) {
                        return -operand;
                    }
                })
                .custom(assertElements(arrayContaining(
                        -5
                )));

        IntStream.of(1, 2, 3, 4, 5)
                .map(new IntUnaryOperator() {
                    @Override
                    public int applyAsInt(int operand) {
                        return -operand;
                    }
                })
                .custom(assertElements(arrayContaining(
                        -1, -2, -3, -4, -5
                )));
    }
}
