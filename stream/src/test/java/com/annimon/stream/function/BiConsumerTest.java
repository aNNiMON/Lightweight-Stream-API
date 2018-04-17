package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code BiConsumer}.
 * 
 * @see com.annimon.stream.function.BiConsumer
 */
public class BiConsumerTest {
    
    @Test
    public void testAccept() {
        IntHolder holder1 = new IntHolder(10);
        IntHolder holder2 = new IntHolder(20);
        
        increment.accept(holder1, holder2);
        assertEquals(11, holder1.value);
        assertEquals(21, holder2.value);
        
        increment.accept(holder1, holder2);
        assertEquals(12, holder1.value);
        assertEquals(22, holder2.value);
        
        mulBy2.accept(holder1, holder2);
        assertEquals(24, holder1.value);
        assertEquals(44, holder2.value);
    }
    
    @Test
    public void testAndThen() {
        BiConsumer<IntHolder, IntHolder> consumer = BiConsumer.Util.andThen(increment, mulBy2);
        IntHolder holder1 = new IntHolder(10);
        IntHolder holder2 = new IntHolder(20);
        
        consumer.accept(holder1, holder2);
        assertEquals(22, holder1.value);
        assertEquals(42, holder2.value);
        
        consumer.accept(holder1, holder2);
        assertEquals(46, holder1.value);
        assertEquals(86, holder2.value);
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(BiConsumer.Util.class, hasOnlyPrivateConstructors());
    }
    
    private static final BiConsumer<IntHolder, IntHolder> increment = new BiConsumer<IntHolder, IntHolder>() {
        @Override
        public void accept(IntHolder holder1, IntHolder holder2) {
            holder1.value++;
            holder2.value++;
        }
    };
    
    private static final BiConsumer<IntHolder, IntHolder> mulBy2 = new BiConsumer<IntHolder, IntHolder>() {
        @Override
        public void accept(IntHolder holder1, IntHolder holder2) {
            holder1.value *= 2;
            holder2.value *= 2;
        }
    };
    
    static class IntHolder {
        int value;

        IntHolder(int value) {
            this.value = value;
        }
    }
}
