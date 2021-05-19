package com.annimon.stream.internal;

/**
 * Compatibility methods for Android API.
 */
@SuppressWarnings("WeakerAccess")
public final class Compat {

    static final long MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private static final String BAD_SIZE = "Stream size exceeds max array size";

    static void checkMaxArraySize(long size) {
        if (size >= MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException(BAD_SIZE);
        }
    }
}
