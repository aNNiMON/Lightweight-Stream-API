package com.annimon.stream.function;

import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author aNNiMON
 */
public class UnaryOperatorTest {
    
    private static final List<Integer> data = new LinkedList<Integer>();
    
    @BeforeClass
    public static void setUpData() {
        for (int i = 0; i < 10; i++) {
            data.add(i);
        }
    }
    
    @Test
    public void identity() {
        final UnaryOperator<Integer> op = UnaryOperator.Util.identity();
        for (Integer value : data) {
            assertEquals(value, op.apply(value));
        }
    }
    
    @Test
    public void square() {
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
}
