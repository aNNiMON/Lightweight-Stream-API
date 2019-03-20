package com.annimon.stream.internal;

import com.annimon.stream.function.IntFunction;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class Operators {

    private Operators() {}

    @NotNull
    public static <T> List<T> toList(@NotNull Iterator<? extends T> iterator) {
        final List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <T, R> R[] toArray(@NotNull Iterator<? extends T> iterator,
                                     @NotNull IntFunction<R[]> generator) {
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

    @NotNull
    public static int[] toIntArray(@NotNull PrimitiveIterator.OfInt iterator) {
        final SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        while (iterator.hasNext()) {
            b.accept(iterator.nextInt());
        }
        return b.asPrimitiveArray();
    }

    @NotNull
    public static long[] toLongArray(@NotNull PrimitiveIterator.OfLong iterator) {
        final SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        while (iterator.hasNext()) {
            b.accept(iterator.nextLong());
        }
        return b.asPrimitiveArray();
    }

    @NotNull
    public static double[] toDoubleArray(@NotNull PrimitiveIterator.OfDouble iterator) {
        final SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        while (iterator.hasNext()) {
            b.accept(iterator.nextDouble());
        }
        return b.asPrimitiveArray();
    }
}
