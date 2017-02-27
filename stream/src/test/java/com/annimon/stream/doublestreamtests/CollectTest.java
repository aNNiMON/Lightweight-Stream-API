package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import com.annimon.stream.function.ObjDoubleConsumer;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class CollectTest {

    @Test
    public void testCollect() {
        String result = DoubleStream.of(1.0, 2.0, 3.0)
                .collect(Functions.stringBuilderSupplier(), new ObjDoubleConsumer<StringBuilder>() {
                    @Override
                    public void accept(StringBuilder t, double value) {
                        t.append(value);
                    }
                }).toString();
        assertThat(result, is("1.02.03.0"));
    }
}
