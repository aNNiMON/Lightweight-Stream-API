package com.annimon.stream;

import java.util.Iterator;

/**
 * A base type for primitive specializations of {@code Iterator}. Specialized
 * subtypes are provided for {@link OfInt int} values.
 */
public final class PrimitiveIterator {

    private PrimitiveIterator() { }

    public abstract static class OfInt implements Iterator<Integer> {

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

    public abstract static class OfDouble implements Iterator<Double> {

        public abstract double nextDouble();

        @Override
        public Double next() {
            return nextDouble();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }
}
