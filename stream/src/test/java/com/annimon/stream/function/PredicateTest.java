package com.annimon.stream.function;

import com.annimon.stream.Functions;
import static com.annimon.stream.test.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code Predicate}.
 * 
 * @see com.annimon.stream.function.Predicate
 */
public class PredicateTest {
    
    @Test
    public void testLessThan100() {
        assertTrue(lessThan100.test(10));
        assertFalse(lessThan100.test(1000));
    }
    
    @Test
    public void testIsEven() {
        assertTrue(isEven.test(54));
        assertFalse(isEven.test(55));
    }
    
    @Test
    public void testAndPredicate() {
        Predicate<Integer> predicate = Predicate.Util.and(lessThan100, isEven);
        
        assertTrue(predicate.test(50));
        assertFalse(predicate.test(55));
        assertFalse(predicate.test(1002));
    }
    
    @Test
    public void testOrPredicate() {
        Predicate<Integer> predicate = Predicate.Util.or(lessThan100, isEven);
        
        assertTrue(predicate.test(50));
        assertTrue(predicate.test(55));
        assertTrue(predicate.test(1002));
        assertFalse(predicate.test(1001));
    }
    
    @Test
    public void testXorPredicate() {
        Predicate<Integer> predicate = Predicate.Util.xor(lessThan100, isEven);
        
        assertFalse(predicate.test(50));
        assertTrue(predicate.test(55));
        assertTrue(predicate.test(1002));
        assertFalse(predicate.test(1001));
    }
    
    @Test
    public void testNegatePredicate() {
        Predicate<Integer> isOdd = Predicate.Util.negate(isEven);
        
        assertTrue(isOdd.test(55));
        assertFalse(isOdd.test(56));
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(Predicate.Util.class, hasOnlyPrivateConstructors());
    }
    
    private static final Predicate<Integer> lessThan100 = new Predicate<Integer>() {
        @Override
        public boolean test(Integer value) {
            return value < 100;
        }
    };
    
    private static final Predicate<Integer> isEven = Functions.remainder(2);
    
}
