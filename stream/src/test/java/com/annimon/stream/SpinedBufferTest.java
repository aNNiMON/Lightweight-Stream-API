package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.IntConsumer;
import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;

/**
 * Tests for {@link SpinedBuffer}
 */
public class SpinedBufferTest {

    @Test
    public void testOfIntEmptyConstructor() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        assertEquals(AbstractSpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
    }

    @Test
    public void testOfIntConstructorCapacity() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt(1);
        assertEquals(AbstractSpinedBuffer.MIN_CHUNK_SIZE, b.capacity());
        SpinedBuffer.OfInt b2 = new SpinedBuffer.OfInt(33);
        assertEquals(64, b2.capacity());

        SpinedBuffer.OfInt b3 = new SpinedBuffer.OfInt(1735);
        assertTrue(b3.capacity() % AbstractSpinedBuffer.MIN_CHUNK_SIZE == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfIntConstructorInvalidCapacity() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt(-5);
    }

    @Test
    public void testEmpty() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        assertTrue(b.isEmpty());

        SpinedBuffer.OfInt b2 = new SpinedBuffer.OfInt(32);
        assertTrue(b.isEmpty());

        b2.accept(25);
        assertFalse(b2.isEmpty());
    }

    @Test
    public void testCount() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();
        assertEquals(0, b.count());

        b.accept(1);
        b.accept(2);
        b.accept(3);

        assertEquals(3, b.count());

        for(int i = 4; i< 17; i++) {
            b.accept(i);
        }

        assertEquals(16, b.count());

        b.accept(17);

        assertEquals(17, b.count());
    }

    @Test
    public void testAccept() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 1000; i++)
            b.accept(i);

        assertEquals(b.count(), 1000);
        assertEquals(b.capacity(), 1024);

        for(int i = 0; i < 1000; i++)
            assertEquals(i, b.get(i));

        SpinedBuffer.OfInt b2 = new SpinedBuffer.OfInt();
        b2.accept(42);
        assertEquals(b2.get(0), 42);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 5; i++)
            b.accept(i);

        // test when special case - one chunk(<16 elements) present
        b.get(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds2() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 32; i++)
            b.accept(i);

        // test when spine(several chunks, >16 elements)
        b.get(40);
    }

    @Test
    public void testEnsureCapacity() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        int m = AbstractSpinedBuffer.MIN_CHUNK_POWER;

        int count = (1<<m)+ (1<<m) + (1<<m+1) + (1<<m+2) + (1<<m + 3) + (1<<m+4) + (1<<m+5) + (1<<m+6);

        for(int i = 0; i < count; i++)
            b.accept(i);

        b.accept(42);

        assertEquals(16, b.spine.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testChunkForUnreachableEndException() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt() {
            @Override
            public long count() {
                long superCount = super.count();

                if(superCount == 200)
                    return 1024;

                return superCount;
            }
        };

        // fill some data to fill first chunk and a bit more
        for(int i = 0; i < 200; i++)
            b.accept(i);

        b.chunkFor(300);
    }

    @Test
    public void testToArray() {
        SpinedBuffer.OfInt e = new SpinedBuffer.OfInt();

        int[] empty = e.asPrimitiveArray();

        assertEquals(empty.length, 0);

        e.accept(42);

        int[] single = e.asPrimitiveArray();

        assertEquals(single.length, 1);

        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 52; i++)
            b.accept(i*2);

        int[] ints = b.asPrimitiveArray();

        for(int i = 0; i < 52; i++)
            assertEquals(i*2, ints[i]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToArrayTooBig() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt() {
            @Override
            public long count() {
                return Stream.MAX_ARRAY_SIZE;
            }
        };

        int[] array = b.asPrimitiveArray();
    }

    @Test
    public void testClear() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 1024; i++)
            b.accept(i);

        b.clear();

        assertTrue(b.spine == null);
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

    private class TestForEach implements Consumer<Integer>, IntConsumer {
        @Override
        public void accept(Integer value) {
            throw new IllegalStateException();
        }

        int cur = -1;

        @Override
        public void accept(int value) {
            assertTrue(cur < value);
            cur = value;
        }
    }

    @Test
    public void testForEach() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 64; i++)
            b.accept(i);

        b.forEach((Consumer<? super Integer>) new TestForEach());

        b.forEach(new Consumer<Integer>() {

            Integer cur = -1;

            @Override
            public void accept(Integer value) {
                assertTrue(cur < value);
                cur = value;
            }
        });
    }

    @Test
    public void testCopyInto() {
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

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCopyIntoNotFit() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 64; i++)
            b.accept(i);

        int[] array = new int[10];

        b.copyInto(array, 0);
    }

    @Test
    public void testIterator() {
        SpinedBuffer.OfInt b = new SpinedBuffer.OfInt();

        for(int i = 0; i < 255; i++)
            b.accept(i);

        for(Integer i: b) {
            assertTrue(i >= 0);
            assertTrue(i < 255);
        }

        final int[] sum = new int[1];

        b.iterator().forEachRemaining(new IntConsumer() {
            @Override
            public void accept(int value) {
                sum[0] += value;
            }
        });

        assertEquals(32385, sum[0]);

        PrimitiveIterator.OfInt iterator = b.iterator();

        int sum2 = 0;

        while(iterator.hasNext()) {
            sum2 += iterator.nextInt();
        }

        assertEquals(32385, sum2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        new SpinedBuffer.OfInt().iterator().remove();
    }

    @Test
    public void testPrivateConstructor() {
        assertThat(SpinedBuffer.class, hasOnlyPrivateConstructors());
    }
}
