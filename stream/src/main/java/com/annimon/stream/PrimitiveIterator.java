package com.annimon.stream;

import com.annimon.stream.function.IntConsumer;

import java.util.Iterator;

/**
 * A base type for primitive specializations of {@code Iterator}. Specialized
 * subtypes are provided for {@link OfInt int} values.
 *
 * @param <T> the type of the iterator elements
 * @param <T_CONS> the type of consumer
 */
@SuppressWarnings("WeakerAccess")
public interface PrimitiveIterator<T, T_CONS> extends Iterator<T> {

    void forEachRemaining(T_CONS action);

    abstract class OfInt implements PrimitiveIterator<Integer, IntConsumer> {

        public abstract int nextInt();

        @Override
        public void forEachRemaining(IntConsumer action) {
            Objects.requireNonNull(action);
            while(hasNext())
                action.accept(nextInt());
        }

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
