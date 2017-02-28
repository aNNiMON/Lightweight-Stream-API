package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Supplier;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class CollectTest {

    @Test
    public void testCollectWithCollector() {
        String text = Stream.range(0, 10)
                .map(Functions.<Integer>convertToString())
                .collect(Functions.joiningCollector());
        assertEquals("0123456789", text);
    }

    @Test
    public void testCollectWithSupplierAndAccumulator() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Functions.stringBuilderSupplier(), Functions.joiningAccumulator())
                .toString();
        assertEquals("abcdefg", text);
    }

    @Test
    public void testCollect123() {
        String string123 = Stream.of("1", "2", "3")
                .collect(new Supplier<StringBuilder>() {
                    @Override
                    public StringBuilder get() {
                        return new StringBuilder();
                    }
                }, new BiConsumer<StringBuilder, String>() {

                    @Override
                    public void accept(StringBuilder value1, String value2) {
                        value1.append(value2);
                    }
                })
                .toString();
        assertEquals("123", string123);
    }
}
