package com.annimon.stream.iterator;

import java.util.NoSuchElementException;

public final class PrimitiveIterators {

    public static class OfIntEmpty extends PrimitiveIterator.OfInt {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public int nextInt() {
           throw new NoSuchElementException();
        }
    }

    public static class OfInt extends PrimitiveIterator.OfInt {

        private int next = 0;

        @Override
        public boolean hasNext() {
            return next < 2;
        }

        @Override
        public int nextInt() {
            if (!hasNext()) throw new NoSuchElementException();
            return ++next;
        }
    }

    public static class OfLongEmpty extends PrimitiveIterator.OfLong {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public long nextLong() {
           throw new NoSuchElementException();
        }
    }

    public static class OfLong extends PrimitiveIterator.OfLong {

        private long next = 0;

        @Override
        public boolean hasNext() {
            return next < 2;
        }

        @Override
        public long nextLong() {
            if (!hasNext()) throw new NoSuchElementException();
            return ++next;
        }
    }

    public static class OfDoubleEmpty extends PrimitiveIterator.OfDouble {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public double nextDouble() {
           throw new NoSuchElementException();
        }
    }

    public static class OfDouble extends PrimitiveIterator.OfDouble {

        private double next = 0;

        @Override
        public boolean hasNext() {
            return next < 2;
        }

        @Override
        public double nextDouble() {
            if (!hasNext()) throw new NoSuchElementException();
            next += 1.01;
            return next;
        }
    }
}
