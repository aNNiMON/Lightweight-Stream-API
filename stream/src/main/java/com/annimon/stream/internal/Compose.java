package com.annimon.stream.internal;

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
                    throw new RuntimeException(e1);
                }
                b.run();
            }
        };
    }
}
