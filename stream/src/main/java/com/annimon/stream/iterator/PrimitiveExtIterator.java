package com.annimon.stream.iterator;

import java.util.NoSuchElementException;

/**
 * Extended PrimitiveIterator for common purposes.
 */
public final class PrimitiveExtIterator {

    private PrimitiveExtIterator() { }

    public static abstract class OfInt extends PrimitiveIterator.OfInt {

        protected int next;
        protected boolean hasNext, isInit;

        @Override
        public boolean hasNext() {
            if (!isInit) {
                // First call to hasNext() on new iterator
                nextIteration();
                isInit = true;
            }
            return hasNext;
        }

        @Override
        public int nextInt() {
            if (!isInit) {
                // First call to next() or nextInt() on new iterator
                hasNext();
            }
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            final int result = next;
            nextIteration();
            return result;
        }

        protected abstract void nextIteration();
    }

    public static abstract class OfLong extends PrimitiveIterator.OfLong {

        protected long next;
        protected boolean hasNext, isInit;

        @Override
        public boolean hasNext() {
            if (!isInit) {
                // First call to hasNext() on new iterator
                nextIteration();
                isInit = true;
            }
            return hasNext;
        }

        @Override
        public long nextLong() {
            if (!isInit) {
                // First call to next() or nextLong() on new iterator
                hasNext();
            }
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            final long result = next;
            nextIteration();
            return result;
        }

        protected abstract void nextIteration();
    }

    public static abstract class OfDouble extends PrimitiveIterator.OfDouble {

        protected double next;
        protected boolean hasNext, isInit;

        @Override
        public boolean hasNext() {
            if (!isInit) {
                // First call to hasNext() on new iterator
                nextIteration();
                isInit = true;
            }
            return hasNext;
        }

        @Override
        public double nextDouble() {
            if (!isInit) {
                // First call to next() or nextDouble() on new iterator
                hasNext();
            }
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            final double result = next;
            nextIteration();
            return result;
        }

        protected abstract void nextIteration();
    }

}
