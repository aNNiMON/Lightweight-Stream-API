package com.annimon.stream;

import java.util.Iterator;

/**
 * A base type for primitive specializations of {@code Iterator}. Specialized
 * subtypes are provided for {@link OfInt int} values.
 */
@SuppressWarnings("WeakerAccess")
public final class PrimitiveIterator {

    abstract static class OfInt implements Iterator<Integer> {

        public abstract int nextInt();

        @Override
        public Integer next() {
            return nextInt();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

}
