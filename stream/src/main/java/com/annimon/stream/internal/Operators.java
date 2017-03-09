package com.annimon.stream.internal;

import com.annimon.stream.function.IntFunction;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Operators {

    private Operators() {}

    public static <T> List<T> toList(Iterator<? extends T> iterator) {
        final List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T, R> R[] toArray(Iterator<? extends T> iterator, IntFunction<R[]> generator) {
        final List<T> container = Operators.<T>toList(iterator);
        final int size = container.size();

        Compat.checkMaxArraySize(size);

        //noinspection unchecked
        T[] source = container.toArray(Compat.<T>newArray(size));
        R[] boxed = generator.apply(size);

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(source, 0, boxed, 0, size);
        return boxed;
    }

    public static int[] toIntArray(PrimitiveIterator.OfInt iterator) {
        final SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        while (iterator.hasNext()) {
            b.accept(iterator.nextInt());
        }
        return b.asPrimitiveArray();
    }

    public static long[] toLongArray(PrimitiveIterator.OfLong iterator) {
        final SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        while (iterator.hasNext()) {
            b.accept(iterator.nextLong());
        }
        return b.asPrimitiveArray();
    }

    public static double[] toDoubleArray(PrimitiveIterator.OfDouble iterator) {
        final SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        while (iterator.hasNext()) {
            b.accept(iterator.nextDouble());
        }
        return b.asPrimitiveArray();
    }
}
