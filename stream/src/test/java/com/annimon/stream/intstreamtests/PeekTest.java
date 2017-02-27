package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.IntSupplier;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class PeekTest {

    @Test
    public void testPeek() {
        assertTrue(IntStream.empty().peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalStateException();
            }
        }).count() == 0);

        assertTrue(IntStream.generate(new IntSupplier() {
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
                if(value != curValue*2)
                    throw new IllegalStateException();

                curValue = value;
            }
        }).limit(10).count() == 10);
    }
}
