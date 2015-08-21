package com.annimon.stream.function;

/**
 * Supply result which can thrown an exception.
 * 
 * @author aNNiMON
 * @param <T> the type of the result
 * @param <E> the type of the exception
 */
@FunctionalInterface
public interface ThrowableSupplier<T, E extends Throwable> {
    
    T get() throws E;
}
