package com.annimon.stream.internal;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Compatibility methods for Android API &lt; 9.
 */
public final class Compat {

    static final long MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private static final String BAD_SIZE = "Stream size exceeds max array size";

    public static <T> Queue<T> queue() {
        // ArrayDeque was introduced in Android 2.3
        try {
            return new ArrayDeque<T>();
        } catch (NoClassDefFoundError nce) {
            return new LinkedList<T>();
        }
    }

    @SafeVarargs
    public static <E> E[] newArray(int length, E... array) {
        try {
            return Arrays.copyOf(array, length);
        } catch (NoSuchMethodError nme) {
            return newArrayCompat(array, length);
        }
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] newArrayCompat(E[] array, int length) {
        final E[] res = (E[]) Array.newInstance(array.getClass().getComponentType(), length);
        System.arraycopy(array, 0, res, 0, Math.min(length, array.length));
        return res;
    }

    static void checkMaxArraySize(long size) {
        if (size >= MAX_ARRAY_SIZE) {
            throw new IllegalArgumentException(BAD_SIZE);
        }
    }
}
