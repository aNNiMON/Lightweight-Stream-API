package com.annimon.stream.internal;

import java.io.Closeable;

public final class Compose {

    private Compose() { }

    public static Runnable runnables(final Runnable a, final Runnable b) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    a.run();
                } catch (Throwable e1) {
                    try {
                        b.run();
                    } catch (Throwable ignore) { }
                    handleException(e1);
                }
                b.run();
            }
        };
    }

    public static Runnable closeables(final Closeable a, final Closeable b) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    a.close();
                } catch (Throwable e1) {
                    try {
                        b.close();
                    } catch (Throwable ignore) { }
                    handleException(e1);
                }
                try {
                    b.close();
                } catch (Throwable e2) {
                    handleException(e2);
                }
            }
        };
    }

    private static Throwable handleException(Throwable e) {
        // Errors and runtime exceptions are thrown as is
        // Checked exceptions are wrapped in RuntimeException
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else if (e instanceof Error) {
            throw (Error) e;
        } else {
            throw new RuntimeException(e);
        }
    }
}
