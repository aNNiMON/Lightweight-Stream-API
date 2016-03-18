package com.annimon.stream.function;

import com.annimon.stream.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests {@code Consumer}.
 * 
 * @see com.annimon.stream.function.Consumer
 */
public class ConsumerTest {
    
    @Test
    public void testAccept() {
        IntHolder holder = new IntHolder(10);
        
        increment.accept(holder);
        assertEquals(11, holder.value);
        
        increment.accept(holder);
        assertEquals(12, holder.value);
        
        mulBy2.accept(holder);
        assertEquals(24, holder.value);
    }
    
    @Test
    public void testAndThen() {
        Consumer<IntHolder> consumer = Consumer.Util.andThen(increment, mulBy2);
        IntHolder holder = new IntHolder(10);
        // (10+1) * 2
        consumer.accept(holder);
        assertEquals(22, holder.value);
        // (22+1) * 2
        consumer.accept(holder);
        assertEquals(46, holder.value);
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        TestUtils.testPrivateConstructor(Consumer.Util.class);
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
