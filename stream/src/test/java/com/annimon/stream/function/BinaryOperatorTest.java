package com.annimon.stream.function;

import com.annimon.stream.Functions;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests {@code BinaryOperator}.
 * 
 * @see com.annimon.stream.function.BinaryOperator
 */
public class BinaryOperatorTest {
    
    private static final List<Integer> data = new ArrayList<Integer>();
    
    @BeforeClass
    public static void setUpData() {
        for (int i = 0; i < 10; i++) {
            data.add(i);
        }
    }
    
    @Test
    public void testSquare() {
        final BinaryOperator<Integer> op = new BinaryOperator<Integer>() {

            @Override
            public Integer apply(Integer value1, Integer value2) {
                return value1 * value2;
            }
        };
        for (Integer value : data) {
            final Integer expected = value * value;
            assertEquals(expected, op.apply(value, value));
        }
    }
    
    @Test
    public void testMinBy() {
        final BinaryOperator<Integer> op = BinaryOperator.Util.minBy(Functions.naturalOrder());
        final int size = data.size();
        for (int i = 0; i < size; i++) {
            final Integer value1 = data.get(i);
            final Integer value2 = data.get(size - 1 - i);
            final Integer expected = Math.min(value1, value2);
            assertEquals(expected, op.apply(value1, value2));
        }
    }
    
    @Test
    public void testMaxBy() {
        final BinaryOperator<Integer> op = BinaryOperator.Util.maxBy(Functions.naturalOrder());
        final int size = data.size();
        for (int i = 0; i < size; i++) {
            final Integer value1 = data.get(i);
            final Integer value2 = data.get(size - 1 - i);
            final Integer expected = Math.max(value1, value2);
            assertEquals(expected, op.apply(value1, value2));
        }
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(BinaryOperator.Util.class, hasOnlyPrivateConstructors());
    }
}
