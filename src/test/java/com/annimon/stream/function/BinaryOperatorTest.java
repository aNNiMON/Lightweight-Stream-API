package com.annimon.stream.function;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author aNNiMON
 */
public class BinaryOperatorTest {
    
    private static final List<Integer> data = new ArrayList<Integer>();
    private static final Comparator<Integer> integerComparator = new Comparator<Integer>() {

        @Override
        public int compare(Integer value1, Integer value2) {
            return Integer.compare(value1, value2);
        }
    };
    
    @BeforeClass
    public static void setUpData() {
        for (int i = 0; i < 10; i++) {
            data.add(i);
        }
    }
    
    @Test
    public void square() {
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
    public void minBy() {
        final BinaryOperator<Integer> op = BinaryOperator.Util.minBy(integerComparator);
        final int size = data.size();
        for (int i = 0; i < size; i++) {
            final Integer value1 = data.get(i);
            final Integer value2 = data.get(size - 1 - i);
            final Integer expected = Math.min(value1, value2);
            assertEquals(expected, op.apply(value1, value2));
        }
    }
    
    @Test
    public void maxBy() {
        final BinaryOperator<Integer> op = BinaryOperator.Util.maxBy(integerComparator);
        final int size = data.size();
        for (int i = 0; i < size; i++) {
            final Integer value1 = data.get(i);
            final Integer value2 = data.get(size - 1 - i);
            final Integer expected = Math.max(value1, value2);
            assertEquals(expected, op.apply(value1, value2));
        }
    }
}
