package com.annimon.stream.internal;

import static com.annimon.stream.Functions.arrayGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.annimon.stream.function.IntFunction;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.Iterator;
import org.junit.Test;

/** Tests for {@link SpinedBuffer} */
public class SpinedBufferTest {

    @Test
    public void testEmptyConstructor() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
    }

    @Test
    public void testOfIntEmptyConstructor() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
    }

    @Test
    public void testOfLongEmptyConstructor() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
    }

    @Test
    public void testOfDoubleEmptyConstructor() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
    }

    @Test
    public void testConstructorCapacity() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>(1);
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
        SpinedBuffer.Of<String> b2 = new SpinedBuffer.Of<String>(33);
        assertEquals(64, b2.capacity());

        SpinedBuffer.Of<String> b3 = new SpinedBuffer.Of<String>(1735);
        assertEquals(0, b3.capacity() % SpinedBuffer.MIN_CHUNK_SIZE);
    }

    @Test
    public void testOfIntConstructorCapacity() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt(1);
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
        SpinedBuffer.OfInt b2 = new SpinedBuffer.OfInt(33);
        assertEquals(64, b2.capacity());

        SpinedBuffer.OfInt b3 = new SpinedBuffer.OfInt(1735);
        assertEquals(0, b3.capacity() % SpinedBuffer.MIN_CHUNK_SIZE);
    }

    @Test
    public void testOfLongConstructorCapacity() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong(1);
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
        SpinedBuffer.OfLong b2 = new SpinedBuffer.OfLong(33);
        assertEquals(64, b2.capacity());

        SpinedBuffer.OfLong b3 = new SpinedBuffer.OfLong(1735);
        assertEquals(0, b3.capacity() % SpinedBuffer.MIN_CHUNK_SIZE);
    }

    @Test
    public void testOfDoubleConstructorCapacity() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble(1);
        assertEquals(SpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
        SpinedBuffer.OfDouble b2 = new SpinedBuffer.OfDouble(33);
        assertEquals(64, b2.capacity());

        SpinedBuffer.OfDouble b3 = new SpinedBuffer.OfDouble(1735);
        assertEquals(0, b3.capacity() % SpinedBuffer.MIN_CHUNK_SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidCapacity() {
        new SpinedBuffer.Of<String>(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfIntConstructorInvalidCapacity() {
        new SpinedBuffer.OfInt(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfLongConstructorInvalidCapacity() {
        new SpinedBuffer.OfLong(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfDoubleConstructorInvalidCapacity() {
        new SpinedBuffer.OfDouble(-5);
    }

    @Test
    public void testEmpty() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        assertTrue(b.isEmpty());

        SpinedBuffer.Of<String> b2 = new SpinedBuffer.Of<String>(32);
        assertTrue(b.isEmpty());

        b2.accept("a");
        assertFalse(b2.isEmpty());
    }

    @Test
    public void testIntEmpty() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        assertTrue(b.isEmpty());

        SpinedBuffer.OfInt b2 = new SpinedBuffer.OfInt(32);
        assertTrue(b.isEmpty());

        b2.accept(25);
        assertFalse(b2.isEmpty());
    }

    @Test
    public void testLongEmpty() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        assertTrue(b.isEmpty());

        SpinedBuffer.OfLong b2 = new SpinedBuffer.OfLong(32);
        assertTrue(b.isEmpty());

        b2.accept(25);
        assertFalse(b2.isEmpty());
    }

    @Test
    public void testDoubleEmpty() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        assertTrue(b.isEmpty());

        SpinedBuffer.OfDouble b2 = new SpinedBuffer.OfDouble(32);
        assertTrue(b.isEmpty());

        b2.accept(25);
        assertFalse(b2.isEmpty());
    }

    @Test
    public void testCount() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        assertEquals(0, b.count());

        b.accept("a");
        b.accept("b");
        b.accept("c");

        assertEquals(3, b.count());
        for (char ch = 'd'; ch < 'q'; ch++) {
            b.accept(String.valueOf(ch));
        }

        assertEquals(16, b.count());

        b.accept("q");

        assertEquals(17, b.count());
    }

    @Test
    public void testIntCount() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        assertEquals(0, b.count());

        b.accept(1);
        b.accept(2);
        b.accept(3);

        assertEquals(3, b.count());

        for (int i = 4; i < 17; i++) {
            b.accept(i);
        }

        assertEquals(16, b.count());

        b.accept(17);

        assertEquals(17, b.count());
    }

    @Test
    public void testLongCount() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        assertEquals(0, b.count());

        b.accept(1);
        b.accept(2);
        b.accept(3);

        assertEquals(3, b.count());

        for (long i = 4; i < 17; i++) {
            b.accept(i);
        }

        assertEquals(16, b.count());

        b.accept(17);

        assertEquals(17, b.count());
    }

    @Test
    public void testDoubleCount() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        assertEquals(0, b.count());

        b.accept(1);
        b.accept(2);
        b.accept(3);

        assertEquals(3, b.count());

        for (int i = 4; i < 17; i++) {
            b.accept(i);
        }

        assertEquals(16, b.count());

        b.accept(17);

        assertEquals(17, b.count());
    }

    @Test
    public void testAccept() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();

        for (int i = 0; i < 1000; i++) b.accept(Integer.toString(i));

        assertEquals(b.count(), 1000);
        assertEquals(b.capacity(), 1024);

        for (int i = 0; i < 1000; i++) {
            String s = Integer.toString(i);
            assertEquals(s, b.get(i));
        }

        SpinedBuffer.Of<String> b2 = new SpinedBuffer.Of<String>();
        b2.accept("abc");
        assertEquals(b2.get(0), "abc");
    }

    @Test
    public void testIntAccept() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for (int i = 0; i < 1000; i++) b.accept(i);

        assertEquals(b.count(), 1000);
        assertEquals(b.capacity(), 1024);

        for (int i = 0; i < 1000; i++) assertEquals(i, b.get(i));

        SpinedBuffer.OfInt b2 = new SpinedBuffer.OfInt();
        b2.accept(42);
        assertEquals(b2.get(0), 42);
    }

    @Test
    public void testLongAccept() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();

        for (long i = 0; i < 1000; i++) b.accept(i);

        assertEquals(b.count(), 1000);
        assertEquals(b.capacity(), 1024);

        for (long i = 0; i < 1000; i++) assertEquals(i, b.get(i));

        SpinedBuffer.OfLong b2 = new SpinedBuffer.OfLong();
        b2.accept(42);
        assertEquals(b2.get(0), 42);
    }

    @Test
    public void testDoubleAccept() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();

        for (int i = 0; i < 1000; i++) b.accept(i);

        assertEquals(b.count(), 1000);
        assertEquals(b.capacity(), 1024);

        for (int i = 0; i < 1000; i++) assertThat(b.get(i), closeTo(i, 0.0001));

        SpinedBuffer.OfDouble b2 = new SpinedBuffer.OfDouble();
        b2.accept(Math.PI);
        assertThat(b2.get(0), closeTo(Math.PI, 0.0001));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        for (int i = 0; i < 5; i++) b.accept("a");
        // test when special case - one chunk(<16 elements) present
        b.get(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIntGetOutOfBounds() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        for (int i = 0; i < 5; i++) b.accept(i);
        // test when special case - one chunk(<16 elements) present
        b.get(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLongGetOutOfBounds() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        for (long i = 0; i < 5; i++) b.accept(i);
        // test when special case - one chunk(<16 elements) present
        b.get(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDoubleGetOutOfBounds() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        for (int i = 0; i < 5; i++) b.accept(i);
        // test when special case - one chunk(<16 elements) present
        b.get(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds2() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        for (int i = 0; i < 32; i++) b.accept("a");
        // test when spine(several chunks, >16 elements)
        b.get(40);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIntGetOutOfBounds2() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        for (int i = 0; i < 32; i++) b.accept(i);
        // test when spine(several chunks, >16 elements)
        b.get(40);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLongGetOutOfBounds2() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        for (long i = 0; i < 32; i++) b.accept(i);
        // test when spine(several chunks, >16 elements)
        b.get(40);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDoubleGetOutOfBounds2() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        for (int i = 0; i < 32; i++) b.accept(i);
        // test when spine(several chunks, >16 elements)
        b.get(40);
    }

    @SuppressWarnings("RedundantCast")
    @Test
    public void testEnsureCapacity() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        int m = SpinedBuffer.MIN_CHUNK_POWER;
        int count =
                (1 << m)
                        + (1 << m)
                        + (1 << m + 1)
                        + (1 << m + 2)
                        + (1 << m + 3)
                        + (1 << m + 4)
                        + (1 << m + 5)
                        + (1 << m + 6);
        for (int i = 0; i < count; i++) b.accept("a");
        b.accept("b");

        assertEquals(16, ((Object[][]) b.spine).length);
    }

    @Test
    public void testIntEnsureCapacity() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        int m = SpinedBuffer.MIN_CHUNK_POWER;
        int count =
                (1 << m)
                        + (1 << m)
                        + (1 << m + 1)
                        + (1 << m + 2)
                        + (1 << m + 3)
                        + (1 << m + 4)
                        + (1 << m + 5)
                        + (1 << m + 6);
        for (int i = 0; i < count; i++) b.accept(i);
        b.accept(42);

        assertEquals(16, b.spine.length);
    }

    @Test
    public void testLongEnsureCapacity() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        long m = SpinedBuffer.MIN_CHUNK_POWER;
        long count =
                (1 << m)
                        + (1 << m)
                        + (1 << m + 1)
                        + (1 << m + 2)
                        + (1 << m + 3)
                        + (1 << m + 4)
                        + (1 << m + 5)
                        + (1 << m + 6);
        for (long i = 0; i < count; i++) b.accept(i);
        b.accept(42);

        assertEquals(16, b.spine.length);
    }

    @Test
    public void testDoubleEnsureCapacity() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        int m = SpinedBuffer.MIN_CHUNK_POWER;
        int count =
                (1 << m)
                        + (1 << m)
                        + (1 << m + 1)
                        + (1 << m + 2)
                        + (1 << m + 3)
                        + (1 << m + 4)
                        + (1 << m + 5)
                        + (1 << m + 6);
        for (int i = 0; i < count; i++) b.accept(i);
        b.accept(42);

        assertEquals(16, b.spine.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChunkForUnreachableEndException() {
        SpinedBuffer.Of<String> b =
                new SpinedBuffer.Of<String>() {
                    @Override
                    public long count() {
                        long superCount = super.count();
                        if (superCount == 200) return 1024;
                        return superCount;
                    }
                };

        // fill some data to fill first chunk and a bit more
        for (int i = 0; i < 200; i++) b.accept("a");
        b.chunkFor(300);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIntChunkForUnreachableEndException() {
        SpinedBuffer.OfInt b =
                new SpinedBuffer.OfInt() {
                    @Override
                    public long count() {
                        long superCount = super.count();
                        if (superCount == 200) return 1024;
                        return superCount;
                    }
                };

        // fill some data to fill first chunk and a bit more
        for (int i = 0; i < 200; i++) b.accept(i);
        b.chunkFor(300);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLongChunkForUnreachableEndException() {
        SpinedBuffer.OfLong b =
                new SpinedBuffer.OfLong() {
                    @Override
                    public long count() {
                        long superCount = super.count();
                        if (superCount == 200) return 1024;
                        return superCount;
                    }
                };

        // fill some data to fill first chunk and a bit more
        for (long i = 0; i < 200; i++) b.accept(i);
        b.chunkFor(300);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDoubleChunkForUnreachableEndException() {
        SpinedBuffer.OfDouble b =
                new SpinedBuffer.OfDouble() {
                    @Override
                    public long count() {
                        long superCount = super.count();
                        if (superCount == 200) return 1024;
                        return superCount;
                    }
                };

        // fill some data to fill first chunk and a bit more
        for (int i = 0; i < 200; i++) b.accept(i);
        b.chunkFor(300);
    }

    @Test
    public void testToArray() {
        SpinedBuffer.Of<String> e = new SpinedBuffer.Of<String>();
        IntFunction<String[]> arrayFactory = arrayGenerator(String[].class);

        String[] empty = e.asArray(arrayFactory);
        assertEquals(empty.length, 0);

        e.accept("a");
        String[] single = e.asArray(arrayFactory);
        assertEquals(single.length, 1);

        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        for (int i = 0; i < 52; i++) b.accept(Integer.toString(i * 2));
        String[] strings = b.asArray(arrayFactory);
        for (int i = 0; i < 52; i++) {
            String s = Integer.toString(i * 2);
            assertEquals(s, strings[i]);
        }
    }

    @Test
    public void testIntToArray() {
        SpinedBuffer.OfInt e = new SpinedBuffer.OfInt();

        int[] empty = e.asPrimitiveArray();
        assertEquals(empty.length, 0);

        e.accept(42);
        int[] single = e.asPrimitiveArray();
        assertEquals(single.length, 1);

        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        for (int i = 0; i < 52; i++) b.accept(i * 2);
        int[] ints = b.asPrimitiveArray();
        for (int i = 0; i < 52; i++) assertEquals(i * 2, ints[i]);
    }

    @Test
    public void testLongToArray() {
        SpinedBuffer.OfLong e = new SpinedBuffer.OfLong();

        long[] empty = e.asPrimitiveArray();
        assertEquals(empty.length, 0);

        e.accept(42);
        long[] single = e.asPrimitiveArray();
        assertEquals(single.length, 1);

        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        for (long i = 0; i < 52; i++) b.accept(i * 2);
        long[] longs = b.asPrimitiveArray();
        for (int i = 0; i < 52; i++) assertEquals(i * 2, longs[i]);
    }

    @Test
    public void testDoubleToArray() {
        SpinedBuffer.OfDouble e = new SpinedBuffer.OfDouble();

        double[] empty = e.asPrimitiveArray();
        assertEquals(empty.length, 0);

        e.accept(42);
        double[] single = e.asPrimitiveArray();
        assertEquals(single.length, 1);

        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        for (int i = 0; i < 52; i++) b.accept(i * 2);
        double[] doubles = b.asPrimitiveArray();
        for (int i = 0; i < 52; i++) assertThat(doubles[i], closeTo(i * 2, 0.0001));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToArrayTooBig() {
        SpinedBuffer.Of<String> b =
                new SpinedBuffer.Of<String>() {
                    @Override
                    public long count() {
                        return Compat.MAX_ARRAY_SIZE;
                    }
                };
        b.asArray(arrayGenerator(String[].class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntToArrayTooBig() {
        SpinedBuffer.OfInt b =
                new SpinedBuffer.OfInt() {
                    @Override
                    public long count() {
                        return Compat.MAX_ARRAY_SIZE;
                    }
                };
        b.asPrimitiveArray();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongToArrayTooBig() {
        SpinedBuffer.OfLong b =
                new SpinedBuffer.OfLong() {
                    @Override
                    public long count() {
                        return Compat.MAX_ARRAY_SIZE;
                    }
                };
        b.asPrimitiveArray();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleToArrayTooBig() {
        SpinedBuffer.OfDouble b =
                new SpinedBuffer.OfDouble() {
                    @Override
                    public long count() {
                        return Compat.MAX_ARRAY_SIZE;
                    }
                };
        b.asPrimitiveArray();
    }

    @Test
    public void testClear() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        for (int i = 0; i < 1024; i++) b.accept("a");
        b.clear();
        assertNull(b.spine);
        assertEquals(0, b.elementIndex);
        assertEquals(0, b.spineIndex);

        SpinedBuffer.Of<String> b2 = new SpinedBuffer.Of<String>();
        b2.accept("a");
        b2.clear();
        assertEquals(0, b2.elementIndex);
        assertEquals(0, b2.spineIndex);

        SpinedBuffer.Of<String> b3 = new SpinedBuffer.Of<String>();
        b3.clear();
        assertEquals(0, b3.count());
        assertEquals(0, b3.elementIndex);
    }

    @Test
    public void testIntClear() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        for (int i = 0; i < 1024; i++) b.accept(i);
        b.clear();
        assertNull(b.spine);
        assertEquals(0, b.elementIndex);
        assertEquals(0, b.spineIndex);

        SpinedBuffer.OfInt b2 = new SpinedBuffer.OfInt();
        b2.accept(42);
        b2.clear();
        assertEquals(0, b2.elementIndex);
        assertEquals(0, b2.spineIndex);

        SpinedBuffer.OfInt b3 = new SpinedBuffer.OfInt();
        b3.clear();
        assertEquals(0, b3.count());
        assertEquals(0, b3.elementIndex);
    }

    @Test
    public void testLongClear() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        for (long i = 0; i < 1024; i++) b.accept(i);
        b.clear();
        assertNull(b.spine);
        assertEquals(0, b.elementIndex);
        assertEquals(0, b.spineIndex);

        SpinedBuffer.OfLong b2 = new SpinedBuffer.OfLong();
        b2.accept(42);
        b2.clear();
        assertEquals(0, b2.elementIndex);
        assertEquals(0, b2.spineIndex);

        SpinedBuffer.OfLong b3 = new SpinedBuffer.OfLong();
        b3.clear();
        assertEquals(0, b3.count());
        assertEquals(0, b3.elementIndex);
    }

    @Test
    public void testDoubleClear() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        for (int i = 0; i < 1024; i++) b.accept(i);
        b.clear();
        assertNull(b.spine);
        assertEquals(0, b.elementIndex);
        assertEquals(0, b.spineIndex);

        SpinedBuffer.OfDouble b2 = new SpinedBuffer.OfDouble();
        b2.accept(42);
        b2.clear();
        assertEquals(0, b2.elementIndex);
        assertEquals(0, b2.spineIndex);

        SpinedBuffer.OfDouble b3 = new SpinedBuffer.OfDouble();
        b3.clear();
        assertEquals(0, b3.count());
        assertEquals(0, b3.elementIndex);
    }

    @Test
    public void testCopyInto() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        String[] array = {"", "", "", ""};

        b.copyInto(array, 0);
        assertEquals("", array[0]);

        b.accept("a");
        b.copyInto(array, 0);
        assertEquals("a", array[0]);

        b.accept("b");
        b.copyInto(array, 1);
        assertEquals("b", array[2]);
    }

    @Test
    public void testIntCopyInto() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        int[] array = new int[4];

        b.copyInto(array, 0);
        assertEquals(0, array[0]);

        b.accept(7);
        b.copyInto(array, 0);
        assertEquals(7, array[0]);

        b.accept(9);
        b.copyInto(array, 1);
        assertEquals(9, array[2]);
    }

    @Test
    public void testLongCopyInto() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        long[] array = new long[4];

        b.copyInto(array, 0);
        assertEquals(0, array[0]);

        b.accept(7);
        b.copyInto(array, 0);
        assertEquals(7, array[0]);

        b.accept(9);
        b.copyInto(array, 1);
        assertEquals(9, array[2]);
    }

    @Test
    public void testDoubleCopyInto() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        double[] array = new double[4];

        b.copyInto(array, 0);
        assertThat(array[0], closeTo(0, 0.0001));

        b.accept(7);
        b.copyInto(array, 0);
        assertThat(array[0], closeTo(7, 0.0001));

        b.accept(9);
        b.copyInto(array, 1);
        assertThat(array[2], closeTo(9, 0.0001));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCopyIntoNotFit() {
        SpinedBuffer.Of<String> b = new SpinedBuffer.Of<String>();
        for (int i = 0; i < 64; i++) b.accept("a");
        String[] array = new String[10];
        b.copyInto(array, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIntCopyIntoNotFit() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        for (int i = 0; i < 64; i++) b.accept(i);
        int[] array = new int[10];
        b.copyInto(array, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testLongCopyIntoNotFit() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        for (long i = 0; i < 64; i++) b.accept(i);
        long[] array = new long[10];
        b.copyInto(array, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDoubleCopyIntoNotFit() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        for (int i = 0; i < 64; i++) b.accept(i);
        double[] array = new double[10];
        b.copyInto(array, 0);
    }

    @Test
    public void testIterator() {
        SpinedBuffer.Of<Integer> b = new SpinedBuffer.Of<Integer>();
        for (int i = 0; i < 255; i++) b.accept(i);
        for (Integer i : b) {
            assertTrue(i >= 0);
            assertTrue(i < 255);
        }

        Iterator<Integer> iterator = b.iterator();
        int sum = 0;
        while (iterator.hasNext()) {
            sum += iterator.next();
        }
        assertEquals(32385, sum);
    }

    @Test
    public void testIntIterator() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        for (int i = 0; i < 255; i++) b.accept(i);
        for (Integer i : b) {
            assertTrue(i >= 0);
            assertTrue(i < 255);
        }

        PrimitiveIterator.OfInt iterator = b.iterator();
        int sum = 0;
        while (iterator.hasNext()) {
            sum += iterator.nextInt();
        }
        assertEquals(32385, sum);
    }

    @Test
    public void testLongIterator() {
        SpinedBuffer.OfLong b = new SpinedBuffer.OfLong();
        for (long i = 0; i < 255; i++) b.accept(i);
        for (Long i : b) {
            assertTrue(i >= 0);
            assertTrue(i < 255);
        }

        PrimitiveIterator.OfLong iterator = b.iterator();
        long sum = 0;
        while (iterator.hasNext()) {
            sum += iterator.nextLong();
        }
        assertEquals(32385, sum);
    }

    @Test
    public void testDoubleIterator() {
        SpinedBuffer.OfDouble b = new SpinedBuffer.OfDouble();
        for (int i = 0; i < 255; i++) b.accept(i);
        for (Double i : b) {
            assertTrue(i >= 0);
            assertTrue(i < 255);
        }

        PrimitiveIterator.OfDouble iterator = b.iterator();
        int sum = 0;
        while (iterator.hasNext()) {
            sum += iterator.nextDouble();
        }
        assertEquals(32385, sum);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        new SpinedBuffer.Of<String>().iterator().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIntIteratorRemove() {
        new SpinedBuffer.OfInt().iterator().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLongIteratorRemove() {
        new SpinedBuffer.OfLong().iterator().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoubleIteratorRemove() {
        new SpinedBuffer.OfDouble().iterator().remove();
    }
}
