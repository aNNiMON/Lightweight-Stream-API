package com.annimon.stream.function;

import java.io.IOException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code DoubleFunction}.
 *
 * @see DoubleFunction
 */
public class DoubleFunctionTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(DoubleFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testSafe() {
        DoubleFunction<String> function = DoubleFunction.Util.<String>safe(new UnsafeFunction());

        assertThat(function.apply(10.36), is("10.0"));
        assertThat(function.apply(-5.90), is(nullValue()));
    }

    @Test
    public void testSafeWithResultIfFailed() {
        DoubleFunction<String> function = DoubleFunction.Util.safe(new UnsafeFunction(), "default");

        assertThat(function.apply(10.36), is("10.0"));
        assertThat(function.apply(-5.90), is("default"));
    }

    private static class UnsafeFunction implements ThrowableDoubleFunction<String, Throwable> {

        @Override
        public String apply(double value) throws IOException {
            if (value < 0) {
                throw new IOException();
            }
            return Double.toString(Math.floor(value));
        }
    }

}
