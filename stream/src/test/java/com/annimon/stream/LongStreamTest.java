package com.annimon.stream;

import com.annimon.stream.function.LongBinaryOperator;
import com.annimon.stream.function.LongConsumer;
import com.annimon.stream.function.LongFunction;
import com.annimon.stream.function.LongPredicate;
import com.annimon.stream.function.LongSupplier;
import com.annimon.stream.function.LongToDoubleFunction;
import com.annimon.stream.function.LongToIntFunction;
import com.annimon.stream.function.LongUnaryOperator;
import com.annimon.stream.function.ObjLongConsumer;
import com.annimon.stream.test.hamcrest.DoubleStreamMatcher;
import com.annimon.stream.test.hamcrest.OptionalLongMatcher;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests {@code Stream}.
 *
 * @see com.annimon.stream.Stream
 */
public class LongStreamTest {

    @Test
    public void testStreamEmpty() {
        assertThat(LongStream.empty(), isEmpty());
    }

    @Test
    public void testStreamOfPrimitiveIterator() {
        LongStream stream = LongStream.of(new PrimitiveIterator.OfLong() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public long nextLong() {
                return ++index;
            }
        });
        assertThat(stream, elements(arrayContaining(1L, 2L, 3L)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfPrimitiveIteratorNull() {
        LongStream.of((PrimitiveIterator.OfLong) null);
    }

    @Test
    public void testStreamOfLongs() {
        assertThat(LongStream.of(32, 28), elements(arrayContaining(32L, 28L)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfLongsNull() {
        LongStream.of((long[]) null);
    }

    @Test
    public void testStreamOfLong() {
        assertThat(LongStream.of(1234), elements(arrayContaining(1234L)));
    }

    @Test
    public void testStreamRange() {
        assertTrue(LongStream.range(1, 5).sum() == 10);
        assertTrue(LongStream.range(2, 2).count() == 0);
    }

    @Test(timeout = 1000)
    public void testStreamRangeOnMinValue() {
        assertThat(LongStream.range(Long.MIN_VALUE, Long.MIN_VALUE + 5).count(), is(5L));
    }

    @Test(timeout = 1000)
    public void testStreamRangeOnEqualValues() {
        assertThat(LongStream.range(Long.MIN_VALUE, Long.MIN_VALUE), isEmpty());

        assertThat(LongStream.range(0, 0), isEmpty());

        assertThat(LongStream.range(Long.MAX_VALUE, Long.MAX_VALUE), isEmpty());
    }

    @Test(timeout = 1000)
    public void testStreamRangeOnMaxValue() {
        assertThat(LongStream.range(Long.MAX_VALUE - 5, Long.MAX_VALUE).count(), is(5L));
    }

    @Test
    public void testStreamRangeClosed() {
        assertThat(LongStream.rangeClosed(1, 5).sum(), is(15L));
        assertThat(LongStream.rangeClosed(1, 5).count(), is(5L));
    }

    @Test
    public void testStreamRangeClosedStartGreaterThanEnd() {
        assertThat(LongStream.rangeClosed(5, 1), isEmpty());
    }

    @Test(timeout = 1000)
    public void testStreamRangeClosedOnMinValue() {
        assertThat(LongStream.rangeClosed(Long.MIN_VALUE, Long.MIN_VALUE + 5).count(), is(6L));
    }

    @Test(timeout = 1000)
    public void testStreamRangeClosedOnEqualValues() {
        assertThat(LongStream.rangeClosed(Long.MIN_VALUE, Long.MIN_VALUE),
                elements(arrayContaining(Long.MIN_VALUE)));

        assertThat(LongStream.rangeClosed(0, 0),
                elements(arrayContaining(0L)));

        assertThat(LongStream.rangeClosed(Long.MAX_VALUE, Long.MAX_VALUE),
                elements(arrayContaining(Long.MAX_VALUE)));
    }

    @Test(timeout = 1000)
    public void testStreamRangeClosedOnMaxValue() {
        assertThat(LongStream.rangeClosed(Long.MAX_VALUE - 5, Long.MAX_VALUE).count(), is(6L));
    }

    @Test
    public void testStreamGenerate() {
        LongStream stream = LongStream.generate(new LongSupplier() {
            @Override
            public long getAsLong() {
                return 1234L;
            }
        });
        assertThat(stream.limit(3), elements(arrayContaining(1234L, 1234L, 1234L)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamGenerateNull() {
        LongStream.generate(null);
    }

    @Test
    public void testStreamIterate() {
        LongUnaryOperator operator = new LongUnaryOperator() {
            @Override
            public long applyAsLong(long operand) {
                return operand + 1000000;
            }
        };

        assertThat(LongStream.iterate(0L, operator).limit(4),
                elements(arrayContaining(0L, 1000000L, 2000000L, 3000000L))
        );
    }

    @Test(expected = NullPointerException.class)
    public void testStreamIterateNull() {
        LongStream.iterate(0, null);
    }

    @Test
    public void testStreamConcat() {
        LongStream a1 = LongStream.empty();
        LongStream b1 = LongStream.empty();
        assertThat(LongStream.concat(a1, b1), isEmpty());

        LongStream a2 = LongStream.of(100200300L, 1234567L);
        LongStream b2 = LongStream.empty();
        assertThat(LongStream.concat(a2, b2),
                elements(arrayContaining(100200300L, 1234567L)));

        LongStream a3 = LongStream.of(100200300L, 1234567L);
        LongStream b3 = LongStream.empty();
        assertThat(LongStream.concat(a3, b3),
                elements(arrayContaining(100200300L, 1234567L)));

        LongStream a4 = LongStream.of(-5L, 1234567L, -Integer.MAX_VALUE, Long.MAX_VALUE);
        LongStream b4 = LongStream.of(Integer.MAX_VALUE, 100200300L);
        assertThat(LongStream.concat(a4, b4),
                elements(arrayContaining(-5L, 1234567L, (long) -Integer.MAX_VALUE, Long.MAX_VALUE,
                        (long) Integer.MAX_VALUE, 100200300L)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullA() {
        LongStream.concat(null, LongStream.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullB() {
        LongStream.concat(LongStream.empty(), null);
    }

    @Test
    public void testIterator() {
        assertThat(LongStream.of(1234567L).iterator().nextLong(), is(1234567L));

        assertThat(LongStream.empty().iterator().hasNext(), is(false));

        assertThat(LongStream.empty().iterator().nextLong(), is(0L));
    }

    @Test
    public void testBoxed() {
        assertThat(LongStream.of(10L, 20L, 30L).boxed(),
                StreamMatcher.elements(is(Arrays.asList(10L, 20L, 30L))));
    }

    @Test
    public void testFilter() {
        final LongPredicate predicate = Functions.remainderLong(111);
        assertThat(LongStream.of(322, 555, 666, 1984, 1998).filter(predicate),
                elements(arrayContaining(555L, 666L, 1998L)));

        assertThat(LongStream.of(12, -10).filter(predicate),
                isEmpty());
    }

    @Test
    public void testFilterNot() {
        final LongPredicate predicate = Functions.remainderLong(111);
        assertThat(LongStream.of(322, 555, 666, 1984, 1998).filterNot(predicate),
                elements(arrayContaining(322L, 1984L)));

        assertThat(LongStream.of(777, 999).filterNot(predicate),
                isEmpty());
    }

    @Test
    public void testMap() {
        LongUnaryOperator negator = new LongUnaryOperator() {
            @Override
            public long applyAsLong(long operand) {
                return -operand;
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L).map(negator),
                elements(arrayContaining(-10L, -20L, -30L)));

        assertThat(LongStream.empty().map(negator),
                isEmpty());
    }

    @Test
    public void testMapToObj() {
        LongFunction<String> longToString = new LongFunction<String>() {
            @Override
            public String apply(long value) {
                return Long.toString(value);
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L).mapToObj(longToString),
                StreamMatcher.elements(is(Arrays.asList("10", "20", "30"))));

        assertThat(LongStream.empty().mapToObj(longToString),
                StreamMatcher.isEmpty());
    }

    @Test
    public void testMapToInt() {
        LongToIntFunction mapper = new LongToIntFunction() {
            @Override
            public int applyAsInt(long value) {
                return (int) (value / 10);
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L, 40L).mapToInt(mapper).toArray(),
                is(new int[] {1, 2, 3, 4}));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMapToDouble() {
        DoubleStream stream = LongStream.rangeClosed(2, 4)
                .mapToDouble(new LongToDoubleFunction() {
                    @Override
                    public double applyAsDouble(long value) {
                        return value / 10d;
                    }
                });
        assertThat(stream, DoubleStreamMatcher.elements(array(
                closeTo(0.2, 0.00001),
                closeTo(0.3, 0.00001),
                closeTo(0.4, 0.00001)
        )));
    }

    @Test
    public void testFlatMap() {
        LongFunction<LongStream> twicer = new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                return LongStream.of(value, value);
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L).flatMap(twicer),
                elements(arrayContaining(10L, 10L, 20L, 20L, 30L, 30L)));

        assertThat(LongStream.of(10L, 20L, -30L).flatMap(new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                if (value < 0) return LongStream.of(value);
                return null;
            }
        }), elements(arrayContaining(-30L)));

        assertThat(LongStream.of(10L, 20L, -30L).flatMap(new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                if (value < 0) return LongStream.empty();
                return LongStream.of(value);
            }
        }), elements(arrayContaining(10L, 20L)));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIterator() {
        LongStream.empty().flatMap(new LongFunction<LongStream>() {
            @Override
            public LongStream apply(long value) {
                return LongStream.of(value);
            }
        }).iterator().nextLong();
    }

    @Test
    public void testDistinct() {
        assertThat(LongStream.of(9, 12, 0, 22, 9, 12, 32, 9).distinct(),
                elements(arrayContaining(9L, 12L, 0L, 22L, 32L)));

        assertThat(LongStream.of(8, 800, 5, 5, 5, 3, 5, 3, 5).distinct(),
                elements(arrayContaining(8L, 800L, 5L, 3L)));
    }

    @Test
    public void testSorted() {
        assertThat(LongStream.of(12, 32, 9, 22).sorted(),
                elements(arrayContaining(9L, 12L, 22L, 32L)));

        assertThat(LongStream.empty().sorted(),
                isEmpty());
    }

    @Test
    public void testSortedWithComparator() {
        assertThat(LongStream.of(12, 32, 9, 22).sorted(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                // reverse order
                return Long.compare(o2, o1);
            }
        }), elements(arrayContaining(32L, 22L, 12L, 9L)));
    }

    @Test
    public void testSample() {
        assertThat(LongStream.of(12, 32, 9, 22, 41, 42).sample(2),
                elements(arrayContaining(12L, 9L, 41L)));

        assertThat(LongStream.of(12, 32, 9, 22, 41, 42).skip(1).sample(2),
                elements(arrayContaining(32L, 22L, 42L)));
    }

    @Test
    public void testSampleWithStep1() {
        assertThat(LongStream.of(12, 32, 9, 22, 41, 42).sample(1),
                elements(arrayContaining(12L, 32L, 9L, 22L, 41L, 42L)));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        LongStream.of(12, 34).sample(-1).count();
    }

    @Test
    public void testPeek() {
        assertThat(LongStream.empty().peek(new LongConsumer() {
            @Override
            public void accept(long value) {
                fail();
            }
        }), isEmpty());

        final long[] expected = {12, 34};
        assertThat(LongStream.of(12, 34).peek(new LongConsumer() {

            private int index = 0;

            @Override
            public void accept(long value) {
                assertThat(value, is(expected[index++]));
            }
        }).count(), is(2L));
    }

    @Test
    public void testTakeWhile() {
        LongStream stream;
        stream = LongStream.of(12, 32, 22, 9, 30, 41, 42)
                    .takeWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(12L, 32L, 22L)));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        LongStream stream;
        stream = LongStream.of(5, 32, 22, 9, 30, 41, 42)
                    .takeWhile(Functions.remainderLong(2));
        assertThat(stream, isEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        LongStream stream;
        stream = LongStream.of(10, 20, 30)
                    .takeWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(10L, 20L, 30L)));
    }

    @Test
    public void testDropWhile() {
        LongStream stream;
        stream = LongStream.of(12, 32, 22, 9, 30, 41, 42)
                    .dropWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(9L, 30L, 41L, 42L)));
    }
    @Test
    public void testDropWhileNonFirstMatch() {
        LongStream stream;
        stream = LongStream.of(5, 32, 22, 9)
                    .dropWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(5L, 32L, 22L, 9L)));
    }

    @Test
    public void testDropWhileAllMatch() {
        LongStream stream;
        stream = LongStream.of(10, 20, 30)
                    .dropWhile(Functions.remainderLong(2));
        assertThat(stream, isEmpty());
    }

    @Test
    public void testLimit() {
        assertThat(LongStream.of(12L, 32L, 22L, 9L).limit(2),
                elements(arrayContaining(12L, 32L)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        LongStream.of(12L, 32L).limit(-2).count();
    }

    @Test
    public void testLimitZero() {
        assertThat(LongStream.of(12L, 32L).limit(0),
                isEmpty());
    }

    @Test
    public void testLimitMoreThanCount() {
        assertThat(LongStream.of(12L, 32L, 22L).limit(5),
                elements(arrayContaining(12L, 32L, 22L)));
    }

    @Test
    public void testSkip() {
        assertThat(LongStream.of(12L, 32L, 22L).skip(2),
                elements(arrayContaining(22L)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        LongStream.of(12L, 32L).skip(-2).count();
    }

    @Test
    public void testSkipZero() {
        assertThat(LongStream.of(12L, 32L, 22L).skip(0),
                elements(arrayContaining(12L, 32L, 22L)));
    }

    @Test
    public void testSkipMoreThanCount() {
        assertThat(LongStream.of(12L, 32L, 22L).skip(5),
                isEmpty());
    }

    @Test
    public void testForEach() {
        final long[] expected = {12L, 32L, 22L, 9L};
        LongStream.of(12L, 32L, 22L, 9L).forEach(new LongConsumer() {

            private int index = 0;

            @Override
            public void accept(long value) {
                assertThat(value, is(expected[index++]));
            }
        });
    }

    @Test
    public void testForEachOnEmptyStream() {
        LongStream.empty().forEach(new LongConsumer() {
            @Override
            public void accept(long value) {
                fail();
            }
        });
    }

    @Test
    public void testReduceWithIdentity() {
        long result = LongStream.of(12, -3772, 3039, 19840, 100000)
                .reduce(0L, new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        });
        assertThat(result, is(119119L));
    }

    @Test
    public void testReduceWithIdentityOnEmptyStream() {
        long result = LongStream.empty().reduce(1234567L, new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        });
        assertThat(result, is(1234567L));
    }

    @Test
    public void testReduce() {
        assertThat(LongStream.of(12, -3772, 3039, 19840, 100000)
                .reduce(new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        }), OptionalLongMatcher.hasValue(119119L));
    }

    @Test
    public void testReduceOnEmptyStream() {
        assertThat(LongStream.empty().reduce(new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        }), OptionalLongMatcher.isEmpty());
    }

    @Test
    public void testToArray() {
        assertThat(LongStream.of(12L, 32L, 22L, 9L).toArray(),
                is(new long[] {12L, 32L, 22L, 9L}));
    }

    @Test
    public void testToArrayOnEmptyStream() {
        assertThat(LongStream.empty().toArray().length, is(0));
    }

    @Test
    public void testCollect() {
        String result = LongStream.of(10, 20, 30)
                .collect(Functions.stringBuilderSupplier(), new ObjLongConsumer<StringBuilder>() {
                    @Override
                    public void accept(StringBuilder t, long value) {
                        t.append(value);
                    }
                }).toString();
        assertThat(result, is("102030"));
    }

    @Test
    public void testSum() {
        assertThat(LongStream.of(100, 20, 3).sum(), is(123L));
        assertThat(LongStream.empty().sum(), is(0L));
    }

    @Test
    public void testMin() {
        assertThat(LongStream.of(100, 20, 3).min(), hasValue(3L));
        assertThat(LongStream.empty().min(), OptionalLongMatcher.isEmpty());
    }

    @Test
    public void testMax() {
        assertThat(LongStream.of(100, 20, 3).max(), hasValue(100L));
        assertThat(LongStream.empty().max(), OptionalLongMatcher.isEmpty());
    }

    @Test
    public void testCount() {
        assertThat(LongStream.of(100, 20, 3).count(), is(3L));
        assertThat(LongStream.empty().count(), is(0L));
    }

    @Test
    public void testAnyMatch() {
        assertTrue(LongStream.of(3, 10, 19, 4, 50)
                .anyMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.of(10, 4, 50)
                .anyMatch(Functions.remainderLong(2)));

        assertFalse(LongStream.of(3, 19)
                .anyMatch(Functions.remainderLong(2)));

        assertFalse(LongStream.empty()
                .anyMatch(Functions.remainderLong(2)));
    }

    @Test
    public void testAllMatch() {
        assertFalse(LongStream.of(3, 10, 19, 4, 50)
                .allMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.of(10, 4, 50)
                .allMatch(Functions.remainderLong(2)));

        assertFalse(LongStream.of(3, 19)
                .allMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.empty()
                .allMatch(Functions.remainderLong(2)));
    }

    @Test
    public void testNoneMatch() {
        assertFalse(LongStream.of(3, 10, 19, 4, 50)
                .noneMatch(Functions.remainderLong(2)));

        assertFalse(LongStream.of(10, 4, 50)
                .noneMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.of(3, 19)
                .noneMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.empty()
                .noneMatch(Functions.remainderLong(2)));
    }

    @Test
    public void testFindFirst() {
        assertThat(LongStream.of(3, 10, 19, 4, 50).findFirst(),
                hasValue(3L));

        assertThat(LongStream.empty().findFirst(),
                OptionalLongMatcher.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleOnEmptyStream() {
        LongStream.empty().single();
    }

    @Test
    public void testSingleOnOneElementStream() {
        assertThat(LongStream.of(42).single(), is(42L));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleOnMoreElementsStream() {
        LongStream.of(0, 1, 2).single();
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleAfterFilteringToEmptyStream() {
        LongStream.of(5, 7, 9)
                .filter(Functions.remainderLong(2))
                .single();
    }

    @Test
    public void testSingleAfterFilteringToOneElementStream() {
        long result = LongStream.of(5, 10, -15)
                .filter(Functions.remainderLong(2))
                .single();
        assertThat(result, is(10L));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleAfterFilteringToMoreElementStream() {
        LongStream.of(5, 10, -15, -20)
                .filter(Functions.remainderLong(2))
                .single();
    }

    @Test
    public void testFindSingleOnEmptyStream() {
        assertThat(LongStream.empty().findSingle(),
                OptionalLongMatcher.isEmpty());
    }

    @Test
    public void testFindSingleOnOneElementStream() {
        assertThat(LongStream.of(42L).findSingle(), hasValue(42L));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleOnMoreElementsStream() {
        LongStream.of(1, 2).findSingle();
    }

    @Test
    public void testFindSingleAfterFilteringToEmptyStream() {
        OptionalLong result = LongStream.of(5, 7, 9)
                .filter(Functions.remainderLong(2))
                .findSingle();

        assertThat(result, OptionalLongMatcher.isEmpty());
    }

    @Test
    public void testFindSingleAfterFilteringToOneElementStream() {
        OptionalLong result = LongStream.of(5, 10, -15)
                .filter(Functions.remainderLong(2))
                .findSingle();

        assertThat(result, hasValue(10L));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleAfterFilteringToMoreElementStream() {
        LongStream.of(5, 10, -15, -20)
                .filter(Functions.remainderLong(2))
                .findSingle();
    }

    @Test(expected = NullPointerException.class)
    public void testCustom() {
        LongStream.empty().custom(null);
    }

    @Test
    public void testCustomLongermediateOperator_Zip() {
        final LongBinaryOperator op = new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        };
        LongStream s1 = LongStream.of(1, 3,  5,  7, 9);
        LongStream s2 = LongStream.of(2, 4,  6,  8);
        long[] expected =           {3, 7, 11, 15};
        LongStream result = s1.custom(new CustomOperators.ZipLong(s2, op));
        assertThat(result.toArray(), is(expected));
    }

    @Test
    public void testCustomTerminalOperator_Average() {
        long[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double average = LongStream.of(input).custom(new CustomOperators.AverageLong());
        assertThat(average, closeTo(4.5, 0.001));
    }
}
