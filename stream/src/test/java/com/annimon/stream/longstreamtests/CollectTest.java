package com.annimon.stream.longstreamtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.ObjLongConsumer;
import org.junit.Test;

public final class CollectTest {

    @Test
    public void testCollect() {
        String result =
                LongStream.of(10, 20, 30)
                        .collect(
                                Functions.stringBuilderSupplier(),
                                new ObjLongConsumer<StringBuilder>() {
                                    @Override
                                    public void accept(StringBuilder t, long value) {
                                        t.append(value);
                                    }
                                })
                        .toString();
        assertThat(result, is("102030"));
    }
}
