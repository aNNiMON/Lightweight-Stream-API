package com.annimon.stream.function.ints;

import com.annimon.stream.IntStream;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 * Tsts {@link IntUnaryOperator}
 */
public class IntUnaryOperatorTest {

    @Test
    public void testIdentity() {
        IntUnaryOperator identity = IntUnaryOperator.Util.identity();

        assertEquals(15, IntStream.of(1, 2, 3, 4, 5).map(identity).sum());
    }

    @Test(expected = InvocationTargetException.class)
    public void testPrivateUtilConstructor() throws InvocationTargetException {

        try {
            Constructor<IntUnaryOperator.Util> c = IntUnaryOperator.Util.class.getDeclaredConstructor();
            c.setAccessible(true);
            IntUnaryOperator.Util u = c.newInstance();
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