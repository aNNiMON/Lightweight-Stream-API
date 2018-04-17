package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.IntSupplier;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class PeekTest {

    @Test
    public void testPeek() {
        assertEquals(0, IntStream.empty().peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalStateException();
            }
        }).count());

        assertEquals(10, IntStream.generate(new IntSupplier() {
            int value = 2;

            @Override
            public int getAsInt() {
                int v = value;
                value *= 2;
                return v;
            }
        }).peek(new IntConsumer() {
            int curValue = 1;

            @Override
            public void accept(int value) {
                if (value != curValue * 2)
                    throw new IllegalStateException();

                curValue = value;
            }
        }).limit(10).count());
    }
}
