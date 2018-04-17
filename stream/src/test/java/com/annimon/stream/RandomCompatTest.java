package com.annimon.stream;

import com.annimon.stream.function.DoubleConsumer;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.LongConsumer;
import java.security.SecureRandom;
import java.util.Random;
import org.junit.Test;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

public class RandomCompatTest {

    @Test
    public void testRandomConstructor() {
        assertNotNull(new RandomCompat(1).getRandom());
        assertNotNull(new RandomCompat().getRandom());

        final RandomCompat secureRandom = new RandomCompat(new SecureRandom());
        assertThat(secureRandom.getRandom(), instanceOf(SecureRandom.class));
    }


    @Test
    public void testRandomIntsSized() {
        assertEquals(10, new RandomCompat().ints(10).count());
        assertEquals(0, new RandomCompat(1).ints(0).count());
    }

    @Test
    public void testRandomLongsSized() {
        assertEquals(10, new RandomCompat().longs(10).count());
        assertEquals(0, new RandomCompat(1).longs(0).count());
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
    public void testRandimLongsSizedNegative() {
        new RandomCompat().longs(-5);
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
    public void testRandomLongs() {
        assertEquals(15, new RandomCompat(1).longs().skip(20).limit(15).count());
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
    public void testRandomLongsSizedBounded() {
        assertEquals(20, new RandomCompat().longs(20, 5, 42).peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                if (value < 5 || value >= 42)
                    fail();
            }
        }).count());

        new RandomCompat(100).longs(0, 1, 20).forEach(new LongConsumer() {
            @Override
            public void accept(long value) {
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
    public void testRandomLongsSizedBoundedNegative() {
        new RandomCompat().longs(-1, 0, 1);
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
    public void testRandomIntsBoundedAlmostFullRange() {
        assertEquals(30, new RandomCompat().ints(Integer.MIN_VALUE + 5, Integer.MAX_VALUE - 5).peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                if (value < (Integer.MIN_VALUE + 5) || value >= (Integer.MAX_VALUE - 5))
                    fail();
            }
        }).limit(30).count());
    }

    @Test
    public void testRandomLongsBounded() {
        assertEquals(10, new RandomCompat().longs(0, 100).peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                if (value < 0 || value > 99)
                    fail();
            }
        }).limit(20).skip(10).count());

        new RandomCompat().longs(Long.MIN_VALUE, Long.MIN_VALUE + 5).peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                if (value >= Long.MIN_VALUE + 5)
                    fail();
            }
        }).limit(30).count();

        new RandomCompat().longs(Long.MAX_VALUE - 5, Long.MAX_VALUE).peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                if (value < Long.MAX_VALUE - 5 || value == Long.MAX_VALUE)
                    fail();
            }
        }).limit(30).distinct().count();
    }

    @Test
    public void testRandomLongsBoundedPowerOfTwoRange() {
        assertEquals(30, new RandomCompat().longs(32, 64).peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                if (value < 32 || value >= 128)
                    fail();
            }
        }).limit(30).count());
    }

    @Test
    public void testRandomLongsBoundedAlmostFullRange() {
        assertEquals(30, new RandomCompat().longs(Long.MIN_VALUE + 5, Long.MAX_VALUE - 5).peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                if (value < (Long.MIN_VALUE + 5) || value >= (Long.MAX_VALUE - 5))
                    fail();
            }
        }).limit(30).count());
    }

    @Test
    public void testRandomLongsBoundedSpecialCases() {
        final Random random1 = new Random() {
            long index = 0;
            @Override
            public long nextLong() {
                index++;
                if (index % 15 == 0)
                    return Long.MIN_VALUE;
                if (index % 10 == 0)
                    return Long.MAX_VALUE;
                return super.nextLong();
            }
        };
        // Case 1: when range not representable as long
        new RandomCompat(random1).longs(Long.MIN_VALUE + 2, Long.MAX_VALUE).peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                if (value < (Long.MIN_VALUE + 2))
                    fail();
            }
        }).limit(30).count();


        final Random random2 = new Random() {
            long index = 0;
            @Override
            public long nextLong() {
                index++;
                if (index < 5)
                    return Long.MIN_VALUE >> 2;
                return super.nextLong();
            }
        };
        // Case 2: rejection on normal range
        new RandomCompat(random2).longs(Long.MIN_VALUE >>> 4, Long.MIN_VALUE >>> 1).limit(30).count();
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
    public void testRandomLongsBoundedInvalid() {
        new RandomCompat(111).longs(100, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomDoublesBoundedInvalid() {
        new RandomCompat(111).doubles(10.0, 10.0);
    }
}

