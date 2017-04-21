package com.annimon.stream.internal;

import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ComposeTest {

    @Test
    public void testPrivateConstructor() {
        assertThat(Compose.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testComposeRunnables() {
        final int[] counter = new int[] { 0 };
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
            }
        };

        Runnable composed = Compose.runnables(runnable, runnable);
        composed.run();
        assertThat(counter[0], is(2));
    }

    @Test
    public void testComposeRunnablesWithExceptionOnFirst() {
        final int[] counter = new int[] { 0 };
        final Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
                throw new IllegalStateException("ok");
            }
        };
        final Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
            }
        };

        Runnable composed = Compose.runnables(runnable1, runnable2);
        try {
            composed.run();
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage(), is("ok"));
        }
        assertThat(counter[0], is(2));
    }

    @Test
    public void testComposeRunnablesWithExceptionOnBoth() {
        final int[] counter = new int[] { 0 };
        final Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
                throw new IllegalStateException("1");
            }
        };
        final Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
                throw new IllegalArgumentException("2");
            }
        };

        Runnable composed = Compose.runnables(runnable1, runnable2);
        try {
            composed.run();
        } catch (IllegalStateException ex1) {
            assertThat(ex1.getMessage(), is("1"));
        } catch (IllegalArgumentException ex2) {
            fail();
        }
        assertThat(counter[0], is(2));
    }

    @Test
    public void testComposeRunnablesWithError() {
        final int[] counter = new int[] { 0 };
        final Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
                throw new AssertionError("1");
            }
        };
        final Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                counter[0]++;
                throw new AssertionError("2");
            }
        };

        Runnable composed = Compose.runnables(runnable1, runnable2);
        try {
            composed.run();
        } catch (Throwable throwable) {
            assertThat(throwable, instanceOf(RuntimeException.class));
            assertThat(throwable.getCause(), instanceOf(AssertionError.class));
            assertThat(throwable.getCause().getMessage(), is("1"));
        }
        assertThat(counter[0], is(2));
    }
}
