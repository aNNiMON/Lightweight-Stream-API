package com.annimon.stream;

import com.annimon.stream.function.IntConsumer;
import java.security.SecureRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.instanceOf;

public class RandomCompatTest {

    @Test
    public void testRandomConstructor() {
        assertTrue(new RandomCompat(1).getRandom() != null);
        assertTrue(new RandomCompat().getRandom() != null);

        final RandomCompat secureRandom = new RandomCompat(new SecureRandom());
        assertThat(secureRandom.getRandom(), instanceOf(SecureRandom.class));
    }

    @Test
    public void testRandomIntsSized() {
        assertEquals(10, new RandomCompat().ints(10).count());
        assertEquals(0, new RandomCompat(1).ints(0).count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandimIntsSizedNegative() {
        new RandomCompat().ints(-5);
    }

    @Test
    public void testRandomInts() {
        assertEquals(15, new RandomCompat(1).ints().skip(20).limit(15).count());
    }

    @Test
    public void testRandomIntsSizedBounded() {
        assertEquals(20, new RandomCompat().ints(20, 5, 42).peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                if (value < 5 || value >= 42)
                    throw new IllegalStateException();
            }
        }).count());

        new RandomCompat(100).ints(0, 1, 20).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalArgumentException();
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntsSizedBoundedNegative() {
        new RandomCompat().ints(-1, 0, 1);
    }

    @Test
    public void testRandomIntsBounded() {
        assertEquals(10, new RandomCompat().ints(0, 100).peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                if (value < 0 || value > 99)
                    throw new IllegalStateException();
            }
        }).limit(20).skip(10).count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntsBoundedInvalid() {
        new RandomCompat(111).ints(100, 100);
    }
}

