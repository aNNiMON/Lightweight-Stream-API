package com.annimon.stream.internal;

import org.jetbrains.annotations.NotNull;

/**
 * Parameters for streams.
 */
public final class Params {

    public Runnable closeHandler;

    public static Params wrapWithCloseHandler(Params params, @NotNull Runnable closeHandler) {
        final Params newParams;
        if (params == null) {
            newParams = new Params();
            newParams.closeHandler = closeHandler;
        } else {
            newParams = params;
            final Runnable firstHandler = newParams.closeHandler;
            newParams.closeHandler = Compose.runnables(firstHandler, closeHandler);
        }
        return newParams;
    }
}
