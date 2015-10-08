package com.annimon.stream.function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class PredicateTest {
    
    @Test
    public void test() {
        assertTrue(lessThan100.test(10));
        assertFalse(lessThan100.test(1000));
        assertTrue(isEven.test(54));
        assertFalse(isEven.test(55));
    }
    
    @Test
    public void and() {
        Predicate<Integer> predicate = Predicate.Util.and(lessThan100, isEven);
        assertTrue(predicate.test(50));
        assertFalse(predicate.test(55));
        assertFalse(predicate.test(1002));
    }
    
    @Test
    public void or() {
        Predicate<Integer> predicate = Predicate.Util.or(lessThan100, isEven);
        assertTrue(predicate.test(50));
        assertTrue(predicate.test(55));
        assertTrue(predicate.test(1002));
        assertFalse(predicate.test(1001));
    }
    
    @Test
    public void xor() {
        Predicate<Integer> predicate = Predicate.Util.xor(lessThan100, isEven);
        assertFalse(predicate.test(50));
        assertTrue(predicate.test(55));
        assertTrue(predicate.test(1002));
        assertFalse(predicate.test(1001));
    }
    
    @Test
    public void negate() {
        Predicate<Integer> isOdd = Predicate.Util.negate(isEven);
        assertTrue(isOdd.test(55));
        assertFalse(isOdd.test(56));
    }
    
    private static final Predicate lessThan100 = new Predicate<Integer>() {
        @Override
        public boolean test(Integer value) {
            return value < 100;
        }
    };
    
    private static final Predicate isEven = new Predicate<Integer>() {
        @Override
        public boolean test(Integer value) {
            return value % 2 == 0;
        }
    };
    
}
