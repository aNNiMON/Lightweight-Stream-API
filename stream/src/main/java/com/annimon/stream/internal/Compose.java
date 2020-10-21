package com.annimon.stream.internal;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;

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

    public static Runnable closeables(final List<? extends Closeable> closeables) {
        return new Runnable() {
            @Override
            public void run() {
                final Iterator<? extends Closeable> iterator = closeables.iterator();
                while (iterator.hasNext()) {
                    try {
                        iterator.next().close();
                    } catch (Throwable currentExc) {
                        // close next closeables ignoring possible exceptions
                        while (iterator.hasNext()) {
                            try {
                                iterator.next().close();
                            } catch (Throwable ignore) { }
                        }
                        handleException(currentExc);
                    }
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
