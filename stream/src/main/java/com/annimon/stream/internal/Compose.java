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
                    if (e1 instanceof RuntimeException) {
                        throw (RuntimeException) e1;
                    }
                    throw (Error) e1;
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
                    if (e1 instanceof RuntimeException) {
                        throw (RuntimeException) e1;
                    }
                    throw (Error) e1;
                }
                try {
                    b.close();
                } catch (Throwable e2) {
                    if (e2 instanceof RuntimeException) {
                        throw (RuntimeException) e2;
                    } else if (e2 instanceof Error) {
                        throw (Error) e2;
                    } else {
                        throw new RuntimeException(e2);
                    }
                }
            }
        };
    }
}
