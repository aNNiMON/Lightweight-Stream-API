package com.annimon.stream;

import com.annimon.stream.function.IntConsumer;

import java.util.Arrays;
import java.util.Iterator;

final class SpinedBuffer {

    private SpinedBuffer() {
    }

    abstract static class OfPrimitive<E, T_ARR, T_CONS>
            extends AbstractSpinedBuffer implements Iterable<E> {

        T_ARR curChunk;

        T_ARR[] spine;

        OfPrimitive(int initialCapacity) {
            super(initialCapacity);
            curChunk = newArray(1 << initialChunkPower);
        }

        OfPrimitive() {
            super();
            curChunk = newArray(1 << initialChunkPower);
        }

        @Override
        public abstract Iterator<E> iterator();

        protected abstract T_ARR[] newArrayArray(int size);

        public abstract T_ARR newArray(int size);

        protected abstract int arrayLength(T_ARR array);

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

        T_ARR asPrimitiveArray() {
            long size = count();

            if (size >= Stream.MAX_ARRAY_SIZE)
                throw new IllegalArgumentException(Stream.BAD_SIZE);

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

        @Override
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
                    index++;
                    return get(index-1);
                }

                @Override
                public boolean hasNext() {
                    return index < count();
                }
            };
        }
    }
}
