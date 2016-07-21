package com.annimon.stream;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Compatibility methods for Android API &lt; 9.
 */
final class Compat {

    static <T> Queue<T> queue() {
        // ArrayDeque was introduced in Android 2.3
        try {
            return new ArrayDeque<T>();
        } catch (NoClassDefFoundError nce) {
            return new LinkedList<T>();
        }
    }

    @SafeVarargs
    static <E> E[] newArray(int length, E... array) {
        try {
            return Arrays.copyOf(array, length);
        } catch (NoSuchMethodError nme) {
            return newArrayCompat(array, length);
        }
    }

    @SuppressWarnings("unchecked")
    static <E> E[] newArrayCompat(E[] array, int length) {
        final E[] res = (E[]) Array.newInstance(array.getClass().getComponentType(), length);
        System.arraycopy(array, 0, res, 0, Math.min(length, array.length));
        return res;
    }
}
