package com.annimon.stream.function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class SupplierTest {
    
    @Test
    public void get() {
        assertEquals("fantastic", (new Supplier<String>() {

            @Override
            public String get() {
                return "fantastic";
            }
        }).get());
    }
}
