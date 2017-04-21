package com.annimon.stream.internal;

public class Compose {

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
                    throw new RuntimeException(e1);
                }
                b.run();
            }
        };
    }
}
