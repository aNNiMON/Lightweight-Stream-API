package com.annimon.stream.function;

/**
 * Supply result.
 * 
 * @author aNNiMON
 * @param <T> the type of the result
 */
@FunctionalInterface
public interface Supplier<T> {
    
    T get();
}
