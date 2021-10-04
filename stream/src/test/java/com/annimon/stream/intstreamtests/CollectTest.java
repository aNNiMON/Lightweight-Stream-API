package com.annimon.stream.intstreamtests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.ObjIntConsumer;
import org.junit.Test;

public final class CollectTest {

    @Test
    public void testCollect() {
        String result =
                IntStream.of(0, 1, 5, 10)
                        .collect(
                                Functions.stringBuilderSupplier(),
                                new ObjIntConsumer<StringBuilder>() {
                                    @Override
                                    public void accept(StringBuilder t, int value) {
                                        t.append(value);
                                    }
                                })
                        .toString();
        assertThat(result, is("01510"));
    }
}
