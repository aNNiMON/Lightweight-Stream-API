package com.annimon.stream.function;

import java.io.IOException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code LongFunction}.
 *
 * @see LongFunction
 */
public class LongFunctionTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(LongFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testSafe() {
        LongFunction<String> function = LongFunction.Util.<String>safe(new UnsafeFunction());

        assertThat(function.apply(10L), is("10"));
        assertThat(function.apply(-5L), is(nullValue()));
    }

    @Test
    public void testSafeWithResultIfFailed() {
        LongFunction<String> function = LongFunction.Util.safe(new UnsafeFunction(), "default");

        assertThat(function.apply(10L), is("10"));
        assertThat(function.apply(-5L), is("default"));
    }

    private static class UnsafeFunction implements ThrowableLongFunction<String, Throwable> {

        @Override
        public String apply(long value) throws IOException {
            if (value < 0) {
                throw new IOException();
            }
            return Long.toString(value);
        }
    }

}
