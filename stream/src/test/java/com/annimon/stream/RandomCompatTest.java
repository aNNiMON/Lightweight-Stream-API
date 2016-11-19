package com.annimon.stream;

import com.annimon.stream.function.DoubleConsumer;
import com.annimon.stream.function.IntConsumer;
import java.security.SecureRandom;
import java.util.Random;
import org.junit.Test;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    @Test
    public void testRandomDoublesSized() {
        assertEquals(10, new RandomCompat().doubles(10).count());
        assertEquals(0, new RandomCompat(1).doubles(0).count());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testRandimIntsSizedNegative() {
        new RandomCompat().ints(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandimDoublesSizedNegative() {
        new RandomCompat().doubles(-5);
    }


    @Test
    public void testRandomInts() {
        assertEquals(15, new RandomCompat(1).ints().skip(20).limit(15).count());
    }

    @Test
    public void testRandomDoubles() {
        assertEquals(15, new RandomCompat(1).doubles().skip(20).limit(15).count());
    }

    @Test
    public void testRandomDoublesDefaultBound() {
        new RandomCompat(1).doubles().peek(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                if (value < 0.0 || value >= 1.0)
                    fail();
            }
        }).limit(20).count();
    }

    @Test
    public void testRandomDoublesSameValues() {
        final long seed = System.currentTimeMillis();
        final Random random = new Random(seed);
        new RandomCompat(new Random(seed)).doubles().peek(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                assertThat(value, closeTo(random.nextDouble(), 0.000000001));
            }
        }).limit(20).count();
    }


    @Test
    public void testRandomIntsSizedBounded() {
        assertEquals(20, new RandomCompat().ints(20, 5, 42).peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                if (value < 5 || value >= 42)
                    fail();
            }
        }).count());

        new RandomCompat(100).ints(0, 1, 20).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                fail();
            }
        });
    }

    @Test
    public void testRandomDoublesSizedBounded() {
        assertEquals(20, new RandomCompat().doubles(20, -0.5, 4.2).peek(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                if (value < -0.5 || value >= 4.2)
                    fail();
            }
        }).count());

        new RandomCompat(100).doubles(0, 1d, 20d).forEach(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                fail();
            }
        });
    }


    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntsSizedBoundedNegative() {
        new RandomCompat().ints(-1, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomDoublesSizedBoundedNegative() {
        new RandomCompat().doubles(-1, 0d, 1d);
    }


    @Test
    public void testRandomIntsBounded() {
        assertEquals(10, new RandomCompat().ints(0, 100).peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                if (value < 0 || value > 99)
                    fail();
            }
        }).limit(20).skip(10).count());
    }

    @Test
    public void testRandomDoublesBounded() {
        assertEquals(10, new RandomCompat().doubles(-1.19, -1.119).peek(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                if (value < -1.19 || value > -1.119)
                    fail();
            }
        }).limit(20).skip(10).count());

        new RandomCompat().doubles(0.001002003000, 0.001002003001).peek(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                if (value < 0.001002003000 || value > 0.001002003001)
                    fail();
            }
        }).limit(20).count();

        new RandomCompat(new Random() {
            @Override
            public double nextDouble() {
                return 1d;
            }
        }).doubles(0.1, 0.2).peek(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                if (value < 0.1 || value > 0.2)
                    fail();
            }
        }).limit(10).count();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testRandomIntsBoundedInvalid() {
        new RandomCompat(111).ints(100, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomDoublesBoundedInvalid() {
        new RandomCompat(111).doubles(10.0, 10.0);
    }
}

