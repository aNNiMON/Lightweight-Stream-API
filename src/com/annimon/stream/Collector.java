package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;

/**
 * Collector of stream data.
 * 
 * @author aNNiMON
 */
public interface Collector {
    
    Supplier supplier();
    
    BiConsumer accumulator();
    
    Function finisher();
}