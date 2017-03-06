package com.annimon.stream.function;

import java.io.IOException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code IntFunction}.
 *
 * @see IntFunction
 */
public class IntFunctionTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IntFunction.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testSafe() {
        IntFunction<String> function = IntFunction.Util.<String>safe(new UnsafeFunction());

        assertThat(function.apply(10), is("10"));
        assertThat(function.apply(-5), is(nullValue()));
    }

    @Test
    public void testSafeWithResultIfFailed() {
        IntFunction<String> function = IntFunction.Util.safe(new UnsafeFunction(), "default");

        assertThat(function.apply(10), is("10"));
        assertThat(function.apply(-5), is("default"));
    }

    private static class UnsafeFunction implements ThrowableIntFunction<String, Throwable> {

        @Override
        public String apply(int value) throws IOException {
            if (value < 0) {
                throw new IOException();
            }
            return Integer.toString(value);
        }
    }

}
