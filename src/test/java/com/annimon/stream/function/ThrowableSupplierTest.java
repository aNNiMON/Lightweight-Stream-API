package com.annimon.stream.function;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class ThrowableSupplierTest {
    
    @Test
    public void get() {
        assertEquals("fantastic", (new ThrowableSupplier<String, IOException>() {

            @Override
            public String get() {
                return "fantastic";
            }
        }).get());
    }
}
