package com.annimon.stream.function.ints;

import com.annimon.stream.IntStream;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Tests {@link IntPredicate}
 */
public class IntPredicateTest {

    private static IntPredicate alwaysTrue = new IntPredicate() {
        @Override
        public boolean test(int value) {
            return true;
        }
    };

    private static IntPredicate alwaysFalse = new IntPredicate() {
        @Override
        public boolean test(int value) {
            return false;
        }
    };

    private static IntPredicate odd = new IntPredicate() {
        @Override
        public boolean test(int value) {
            return value%2 != 0;
        }
    };

    private static IntPredicate even = new IntPredicate() {
        @Override
        public boolean test(int value) {
            return value%2 == 0;
        }
    };

    private static IntPredicate divBy3 = new IntPredicate() {
        @Override
        public boolean test(int value) {
            return value%3 == 0;
        }
    };

    @Test
    public void testAnd() {

        IntPredicate doubleTrue = IntPredicate.Util.and(alwaysTrue, alwaysTrue);
        IntPredicate trueFalse = IntPredicate.Util.and(alwaysTrue, alwaysFalse);
        IntPredicate falseTrue = IntPredicate.Util.and(alwaysFalse, alwaysTrue);
        IntPredicate falseFalse = IntPredicate.Util.and(alwaysFalse, alwaysFalse);

        assertTrue(doubleTrue.test(1));
        assertFalse(trueFalse.test(1));
        assertFalse(falseTrue.test(1));
        assertFalse(falseFalse.test(1));

        IntPredicate evenOdd = IntPredicate.Util.and(even, odd);

        assertFalse(IntStream.of(1, 2, 3, 4, 5, 6).filter(evenOdd).findFirst().isPresent());

        IntPredicate oddAndDivBy3 = IntPredicate.Util.and(odd, divBy3);

        assertEquals(2, IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(oddAndDivBy3).count());

        IntPredicate evenAndDivBy3 = IntPredicate.Util.and(even, divBy3);

        assertEquals(1, IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(evenAndDivBy3).count());
    }

    @Test
    public void testOr() {

        IntPredicate doubleTrue = IntPredicate.Util.or(alwaysTrue, alwaysTrue);
        IntPredicate trueFalse = IntPredicate.Util.or(alwaysTrue, alwaysFalse);
        IntPredicate falseTrue = IntPredicate.Util.or(alwaysFalse, alwaysTrue);
        IntPredicate falseFalse = IntPredicate.Util.or(alwaysFalse, alwaysFalse);

        assertTrue(doubleTrue.test(1));
        assertTrue(trueFalse.test(1));
        assertTrue(falseTrue.test(1));
        assertFalse(falseFalse.test(1));

        IntPredicate evenOdd = IntPredicate.Util.or(even, odd);

        assertEquals(6, IntStream.of(1, 2, 3, 4, 5, 6).filter(evenOdd).count());

        IntPredicate oddOrDivBy3 = IntPredicate.Util.or(odd, divBy3);

        assertEquals(6, IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(oddOrDivBy3).count());

        IntPredicate evenOrDivBy3 = IntPredicate.Util.or(even, divBy3);

        assertEquals(6, IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(evenOrDivBy3).count());
    }

    @Test
    public void testXor() {

        IntPredicate doubleTrue = IntPredicate.Util.xor(alwaysTrue, alwaysTrue);
        IntPredicate trueFalse = IntPredicate.Util.xor(alwaysTrue, alwaysFalse);
        IntPredicate falseTrue = IntPredicate.Util.xor(alwaysFalse, alwaysTrue);
        IntPredicate falseFalse = IntPredicate.Util.xor(alwaysFalse, alwaysFalse);

        assertFalse(doubleTrue.test(1));
        assertTrue(trueFalse.test(1));
        assertTrue(falseTrue.test(1));
        assertFalse(falseFalse.test(1));

        IntPredicate evenxOdd = IntPredicate.Util.xor(even, odd);

        assertEquals(6, IntStream.of(1, 2, 3, 4, 5, 6).filter(evenxOdd).count());

        IntPredicate oddXorDivBy3 = IntPredicate.Util.xor(odd, divBy3);

        assertEquals(4, IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(oddXorDivBy3).count());

        IntPredicate evenXorDivBy3 = IntPredicate.Util.xor(even, divBy3);

        assertEquals(5, IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(evenXorDivBy3).count());
    }

    @Test
    public void testNegate() {

        IntPredicate negateTrue = IntPredicate.Util.negate(alwaysTrue);
        IntPredicate negateFalse = IntPredicate.Util.negate(alwaysFalse);

        assertFalse(negateTrue.test(1));
        assertTrue(negateFalse.test(1));

        IntPredicate notDivBy3 = IntPredicate.Util.negate(divBy3);

        assertEquals(IntStream.of(1,2,3,4,5,6,7,8,9).filter(notDivBy3).count(), 6);
    }

    @Test(expected = InvocationTargetException.class)
    public void testPrivateUtilConstructor() throws InvocationTargetException {

        try {
            Constructor<IntPredicate.Util> c = IntPredicate.Util.class.getDeclaredConstructor();
            c.setAccessible(true);
            IntPredicate.Util u = c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw e;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}