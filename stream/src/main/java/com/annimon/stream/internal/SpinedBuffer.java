package com.annimon.stream.internal;

import com.annimon.stream.function.DoubleConsumer;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.LongConsumer;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.Arrays;
import java.util.Iterator;

final class SpinedBuffer {

    /**
     * Minimum power-of-two for the first chunk.
     */
    static final int MIN_CHUNK_POWER = 4;

    /**
     * Minimum size for the first chunk.
     */
    static final int MIN_CHUNK_SIZE = 1 << MIN_CHUNK_POWER;

    /**
     * Max power-of-two for chunks.
     */
    private static final int MAX_CHUNK_POWER = 30;

    /**
     * Minimum array size for array-of-chunks.
     */
    static final int MIN_SPINE_SIZE = 8;

    private SpinedBuffer() {
    }

    /**
     * Base class for a data structure for gathering elements into a buffer and then
     * iterating them. Maintains an array of increasingly sized arrays, so there is
     * no copying cost associated with growing the data structure.
     */
    abstract static class OfPrimitive<E, T_ARR, T_CONS> implements Iterable<E> {

        /**
         * log2 of the size of the first chunk.
         */
        final int initialChunkPower;

        /**
         * Index of the *next* element to write; may point into, or just outside of,
         * the current chunk.
         */
        int elementIndex;

        /**
         * Index of the *current* chunk in the spine array, if the spine array is
         * non-null.
         */
        int spineIndex;

        /**
         * Count of elements in all prior chunks.
         */
        long[] priorElementCount;

        T_ARR curChunk;

        T_ARR[] spine;

        /**
         * Construct with a specified initial capacity.
         *
         * @param initialCapacity The minimum expected number of elements
         */
        OfPrimitive(int initialCapacity) {
            if (initialCapacity < 0)
                throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);

            this.initialChunkPower = Math.max(MIN_CHUNK_POWER,
                    Integer.SIZE - Integer.numberOfLeadingZeros(initialCapacity - 1));
            curChunk = newArray(1 << initialChunkPower);
        }

        OfPrimitive() {
            this.initialChunkPower = MIN_CHUNK_POWER;
            curChunk = newArray(1 << initialChunkPower);
        }

        @Override
        public abstract Iterator<E> iterator();

        protected abstract T_ARR[] newArrayArray(int size);

        protected abstract T_ARR newArray(int size);

        protected abstract int arrayLength(T_ARR array);

        /**
         * Is the buffer currently empty?
         */
        public boolean isEmpty() {
            return (spineIndex == 0) && (elementIndex == 0);
        }

        /**
         * How many elements are currently in the buffer?
         */
        public long count() {
            return (spineIndex == 0)
                    ? elementIndex
                    : priorElementCount[spineIndex] + elementIndex;
        }

        /**
         * How big should the nth chunk be?
         */
        int chunkSize(int n) {
            int power = (n == 0 || n == 1)
                    ? initialChunkPower
                    : Math.min(initialChunkPower + n - 1, MAX_CHUNK_POWER);
            return 1 << power;
        }

        long capacity() {
            return (spineIndex == 0)
                    ? arrayLength(curChunk)
                    : priorElementCount[spineIndex] + arrayLength(spine[spineIndex]);
        }

        private void inflateSpine() {
            if (spine == null) {
                spine = newArrayArray(MIN_SPINE_SIZE);
                priorElementCount = new long[MIN_SPINE_SIZE];
                spine[0] = curChunk;
            }
        }

        final void ensureCapacity(long targetSize) {
            long capacity = capacity();
            if (targetSize > capacity) {
                inflateSpine();
                for (int i=spineIndex+1; targetSize > capacity; i++) {
                    if (i >= spine.length) {
                        int newSpineSize = spine.length * 2;
                        spine = Arrays.copyOf(spine, newSpineSize);
                        priorElementCount = Arrays.copyOf(priorElementCount, newSpineSize);
                    }
                    int nextChunkSize = chunkSize(i);
                    spine[i] = newArray(nextChunkSize);
                    priorElementCount[i] = priorElementCount[i-1] + arrayLength(spine[i - 1]);
                    capacity += nextChunkSize;
                }
            }
        }

        void increaseCapacity() {
            ensureCapacity(capacity() + 1);
        }

        int chunkFor(long index) {
            if (spineIndex == 0) {
                if (index < elementIndex)
                    return 0;
                else
                    throw new IndexOutOfBoundsException(Long.toString(index));
            }

            if (index >= count())
                throw new IndexOutOfBoundsException(Long.toString(index));

            for (int j=0; j <= spineIndex; j++)
                if (index < priorElementCount[j] + arrayLength(spine[j]))
                    return j;

            throw new IndexOutOfBoundsException(Long.toString(index));
        }

        @SuppressWarnings("SuspiciousSystemArraycopy")
        void copyInto(T_ARR array, int offset) {
            long finalOffset = offset + count();
            if (finalOffset > arrayLength(array) || finalOffset < offset) {
                throw new IndexOutOfBoundsException("does not fit");
            }

            if (spineIndex == 0)
                System.arraycopy(curChunk, 0, array, offset, elementIndex);
            else {
                // full chunks
                for (int i=0; i < spineIndex; i++) {
                    System.arraycopy(spine[i], 0, array, offset, arrayLength(spine[i]));
                    offset += arrayLength(spine[i]);
                }
                if (elementIndex > 0)
                    System.arraycopy(curChunk, 0, array, offset, elementIndex);
            }
        }

        public T_ARR asPrimitiveArray() {
            long size = count();

            Compat.checkMaxArraySize(size);

            T_ARR result = newArray((int) size);
            copyInto(result, 0);
            return result;
        }

        void preAccept() {
            if (elementIndex == arrayLength(curChunk)) {
                inflateSpine();
                if (spineIndex+1 >= spine.length || spine[spineIndex+1] == null)
                    increaseCapacity();
                elementIndex = 0;
                ++spineIndex;
                curChunk = spine[spineIndex];
            }
        }

        /**
         * Remove all data from the buffer
         */
        public void clear() {
            if (spine != null) {
                curChunk = spine[0];
                spine = null;
                priorElementCount = null;
            }
            elementIndex = 0;
            spineIndex = 0;
        }
    }

    static class OfInt extends SpinedBuffer.OfPrimitive<Integer, int[], IntConsumer>
            implements IntConsumer {
        OfInt() { }

        OfInt(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        protected int[][] newArrayArray(int size) {
            return new int[size][];
        }

        @Override
        public int[] newArray(int size) {
            return new int[size];
        }

        @Override
        protected int arrayLength(int[] array) {
            return array.length;
        }

        @Override
        public void accept(int i) {
            preAccept();
            curChunk[elementIndex++] = i;
        }

        public int get(long index) {
            // Casts to int are safe since the spine array index is the index minus
            // the prior element count from the current spine
            int ch = chunkFor(index);
            if (spineIndex == 0 && ch == 0)
                return curChunk[(int) index];
            else
                return spine[ch][(int) (index - priorElementCount[ch])];
        }

        @Override
        public PrimitiveIterator.OfInt iterator() {
            return new PrimitiveIterator.OfInt() {

                long index = 0;

                @Override
                public int nextInt() {
                    return get(index++);
                }

                @Override
                public boolean hasNext() {
                    return index < count();
                }
            };
        }
    }

    static class OfLong extends SpinedBuffer.OfPrimitive<Long, long[], LongConsumer>
            implements LongConsumer {
        OfLong() { }

        OfLong(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        protected long[][] newArrayArray(int size) {
            return new long[size][];
        }

        @Override
        public long[] newArray(int size) {
            return new long[size];
        }

        @Override
        protected int arrayLength(long[] array) {
            return array.length;
        }

        @Override
        public void accept(long i) {
            preAccept();
            curChunk[elementIndex++] = i;
        }

        public long get(long index) {
            int ch = chunkFor(index);
            if (spineIndex == 0 && ch == 0)
                return curChunk[(int) index];
            else
                return spine[ch][(int) (index - priorElementCount[ch])];
        }

        @Override
        public PrimitiveIterator.OfLong iterator() {
            return new PrimitiveIterator.OfLong() {

                long index = 0;

                @Override
                public long nextLong() {
                    return get(index++);
                }

                @Override
                public boolean hasNext() {
                    return index < count();
                }
            };
        }
    }

    static class OfDouble extends SpinedBuffer.OfPrimitive<Double, double[], DoubleConsumer>
            implements DoubleConsumer {
        OfDouble() { }

        OfDouble(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        protected double[][] newArrayArray(int size) {
            return new double[size][];
        }

        @Override
        public double[] newArray(int size) {
            return new double[size];
        }

        @Override
        protected int arrayLength(double[] array) {
            return array.length;
        }

        @Override
        public void accept(double i) {
            preAccept();
            curChunk[elementIndex++] = i;
        }

        public double get(long index) {
            int ch = chunkFor(index);
            if (spineIndex == 0 && ch == 0)
                return curChunk[(int) index];
            else
                return spine[ch][(int) (index - priorElementCount[ch])];
        }

        @Override
        public PrimitiveIterator.OfDouble iterator() {
            return new PrimitiveIterator.OfDouble() {

                long index = 0;

                @Override
                public double nextDouble() {
                    return get(index++);
                }

                @Override
                public boolean hasNext() {
                    return index < count();
                }
            };
        }
    }
}
