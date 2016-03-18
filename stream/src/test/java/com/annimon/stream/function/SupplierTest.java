package com.annimon.stream.function;

import com.annimon.stream.Functions;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code Supplier}.
 * 
 * @see com.annimon.stream.function.Supplier
 */
public class SupplierTest {
    
    @Test
    public void testGetString() {
        Supplier<String> supplier = new Supplier<String>() {

            @Override
            public String get() {
                return "fantastic";
            }
        };
        assertEquals("fantastic", supplier.get());
    }
    
    @Test
    public void testGetStringBuilder() {
        Supplier<StringBuilder> supplier = Functions.stringBuilderSupplier();
        
        assertThat(supplier.get(), instanceOf(StringBuilder.class));
        assertTrue(supplier.get().toString().isEmpty());
        
    }
    
    @Test
    public void testIncrement() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int counter = 0;

            @Override
            public Integer get() {
                return counter++;
            }
        };
        assertEquals(0, supplier.get().intValue());
        assertEquals(1, supplier.get().intValue());
        assertEquals(2, supplier.get().intValue());
        assertEquals(3, supplier.get().intValue());
    }
}
