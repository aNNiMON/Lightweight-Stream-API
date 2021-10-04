package com.annimon.stream.doublestreamtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import com.annimon.stream.function.ObjDoubleConsumer;
import org.junit.Test;

public final class CollectTest {

    @Test
    public void testCollect() {
        String result =
                DoubleStream.of(1.0, 2.0, 3.0)
                        .collect(
                                Functions.stringBuilderSupplier(),
                                new ObjDoubleConsumer<StringBuilder>() {
                                    @Override
                                    public void accept(StringBuilder t, double value) {
                                        t.append(value);
                                    }
                                })
                        .toString();
        assertThat(result, is("1.02.03.0"));
    }
}
