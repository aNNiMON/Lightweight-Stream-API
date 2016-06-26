package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests {@code UnaryOperator}.
 * 
 * @see com.annimon.stream.function.UnaryOperator
 */
public class UnaryOperatorTest {
    
    private static List<Integer> data;
    
    @BeforeClass
    public static void setUpData() {
        data = new ArrayList<Integer>(10);
        for (int i = 0; i < 10; i++) {
            data.add(i);
        }
    }
    
    @Test
    public void testIdentity() {
        final UnaryOperator<Integer> op = UnaryOperator.Util.identity();
        for (Integer value : data) {
            assertEquals(value, op.apply(value));
        }
    }
    
    @Test
    public void testSquare() {
        final UnaryOperator<Integer> op = new UnaryOperator<Integer>() {

            @Override
            public Integer apply(Integer value) {
                return value * value;
            }
        };
        for (Integer value : data) {
            final Integer expected = value * value;
            assertEquals(expected, op.apply(value));
        }
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(UnaryOperator.Util.class, hasOnlyPrivateConstructors());
    }
}
