package com.annimon.stream.function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class ConsumerTest {
    
    @Test
    public void accept() {
        IntHolder holder = new IntHolder(10);
        increment.accept(holder);
        assertEquals(11, holder.value);
        increment.accept(holder);
        assertEquals(12, holder.value);
        mulBy2.accept(holder);
        assertEquals(24, holder.value);
    }
    
    @Test
    public void andThen() {
        Consumer<IntHolder> consumer = Consumer.Util.andThen(increment, mulBy2);
        IntHolder holder = new IntHolder(10);
        consumer.accept(holder);
        assertEquals(22, holder.value);
        consumer.accept(holder);
        assertEquals(46, holder.value);
    }
    
    private static final Consumer<IntHolder> increment = new Consumer<IntHolder>() {
        @Override
        public void accept(IntHolder holder) {
            holder.value++;
        }
    };
    
    private static final Consumer<IntHolder> mulBy2 = new Consumer<IntHolder>() {
        @Override
        public void accept(IntHolder holder) {
            holder.value *= 2;
        }
    };
    
    class IntHolder {
        int value;

        public IntHolder(int value) {
            this.value = value;
        }
    }
}
