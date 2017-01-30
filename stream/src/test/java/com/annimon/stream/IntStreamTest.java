package com.annimon.stream;

import com.annimon.stream.function.IntBinaryOperator;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.IntFunction;
import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.function.IntSupplier;
import com.annimon.stream.function.IntToDoubleFunction;
import com.annimon.stream.function.IntToLongFunction;
import com.annimon.stream.function.IntUnaryOperator;
import com.annimon.stream.function.ObjIntConsumer;
import com.annimon.stream.test.hamcrest.DoubleStreamMatcher;
import com.annimon.stream.test.hamcrest.LongStreamMatcher;
import com.annimon.stream.test.hamcrest.OptionalIntMatcher;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import org.hamcrest.Matchers;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

/**
 * Tests {@code IntStream}
 *
 * @see com.annimon.stream.IntStream
 */
public class IntStreamTest {

    @Test
    public void testStreamEmpty() {
        assertTrue(IntStream.empty().count() == 0);
        assertTrue(IntStream.empty().iterator().nextInt() == 0);
    }

    @Test
    public void testStreamOfPrimitiveIterator() {
        int[] expected = { 0, 1 };
        IntStream stream = IntStream.of(new PrimitiveIterator.OfInt() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return index < 2;
            }

            @Override
            public int nextInt() {
                return index++;
            }
        });
        assertThat(stream.toArray(), is(expected));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfPrimitiveIteratorNull() {
        IntStream.of((PrimitiveIterator.OfInt) null);
    }

    @Test
    public void testStreamOfInts() {
        int[] data1 = {1, 2, 3, 4, 5};
        int[] data2 = {42};
        int[] data3 = {};

        assertTrue(IntStream.of(data1).count() == 5);
        assertTrue(IntStream.of(data2).findFirst().getAsInt() == 42);
        assertTrue(!IntStream.of(data3).findFirst().isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfIntsNull() {
        IntStream.of((int[]) null);
    }

    @Test
    public void testStreamOfInt() {
        assertTrue(IntStream.of(42).count() == 1);
        assertTrue(IntStream.of(42).findFirst().isPresent());
    }

    @Test
    public void testStreamRange() {
        assertTrue(IntStream.range(1, 5).sum() == 10);
        assertTrue(IntStream.range(2, 2).count() == 0);
    }

    @Test
    public void testStreamRangeOnMinValue() {
        assertThat(IntStream.range(Integer.MIN_VALUE, Integer.MIN_VALUE + 5).count(), is(5L));
    }

    @Test
    public void testStreamRangeOnEqualValues() {
        assertThat(IntStream.range(Integer.MIN_VALUE, Integer.MIN_VALUE).count(), is(0L));

        assertThat(IntStream.range(0, 0).count(), is(0L));

        assertThat(IntStream.range(Integer.MAX_VALUE, Integer.MAX_VALUE).count(), is(0L));
    }

    @Test
    public void testStreamRangeOnMaxValue() {
        assertThat(IntStream.range(Integer.MAX_VALUE - 5, Integer.MAX_VALUE).count(), is(5L));
    }

    @Test
    public void testStreamRangeClosed() {
        assertTrue(IntStream.rangeClosed(1, 5).sum() == 15);
        assertTrue(IntStream.rangeClosed(1, 5).count() == 5);
    }

    @Test
    public void testStreamRangeClosedStartGreaterThanEnd() {
        assertThat(IntStream.rangeClosed(5, 1).count(), is(0L));
    }

    @Test
    public void testStreamRangeClosedOnMinValue() {
        assertThat(IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE + 5).count(), is(6L));
    }

    @Test
    public void testStreamRangeClosedOnEqualValues() {
        assertThat(IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE).single(), is(Integer.MIN_VALUE));

        assertThat(IntStream.rangeClosed(0, 0).single(), is(0));

        assertThat(IntStream.rangeClosed(Integer.MAX_VALUE, Integer.MAX_VALUE).single(), is(Integer.MAX_VALUE));
    }

    @Test
    public void testStreamRangeClosedOnMaxValue() {
        assertThat(IntStream.rangeClosed(Integer.MAX_VALUE - 5, Integer.MAX_VALUE).count(), is(6L));
    }

    @Test
    public void testStreamGenerate() {
        IntSupplier s = new IntSupplier() {
            @Override
            public int getAsInt() {
                return 42;
            }
        };

        assertTrue(IntStream.generate(s).findFirst().getAsInt() == 42);
    }

    @Test(expected = NullPointerException.class)
    public void testStreamGenerateNull() {
        IntStream.generate(null);
    }

    @Test
    public void testStreamIterate() {
        IntUnaryOperator operator = new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand + 1;
            }
        };

        assertTrue(IntStream.iterate(1, operator).limit(3).sum() == 6);
        assertTrue(IntStream.iterate(1, operator).iterator().hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamIterateNull() {
        IntStream.iterate(0, null);
    }

    @Test
    public void testStreamIterateWithPredicate() {
        IntPredicate condition = new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value < 20;
            }
        };
        IntUnaryOperator increment = new IntUnaryOperator() {
            @Override
            public int applyAsInt(int t) {
                return t + 5;
            }
        };
        IntStream stream = IntStream.iterate(0, condition, increment);

        assertThat(stream.toArray(), is(new int[] {0, 5, 10, 15}));
    }

    @Test
    public void testStreamConcat() {
        IntStream a1 = IntStream.empty();
        IntStream b1 = IntStream.empty();

        assertTrue(IntStream.concat(a1,b1).count() == 0);

        IntStream a2 = IntStream.of(1,2,3);
        IntStream b2 = IntStream.empty();

        assertTrue(IntStream.concat(a2, b2).count() == 3);

        IntStream a3 = IntStream.empty();
        IntStream b3 = IntStream.of(42);

        assertTrue(IntStream.concat(a3, b3).findFirst().getAsInt() == 42);

        IntStream a4 = IntStream.of(2, 4, 6, 8);
        IntStream b4 = IntStream.of(1, 3, 5, 7, 9);

        assertTrue(IntStream.concat(a4, b4).count() == 9);
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullA() {
        IntStream.concat(null, IntStream.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullB() {
        IntStream.concat(IntStream.empty(), null);
    }

    @Test
    public void testBoxed() {
        assertThat(IntStream.of(1, 10, 20).boxed().reduce(Functions.addition()),
                OptionalMatcher.hasValue(31));
    }

    @Test
    public void testFilter() {
        assertTrue(IntStream.rangeClosed(1, 10).filter(Functions.remainderInt(2)).count() == 5);

        assertTrue(IntStream.rangeClosed(1, 10).filter(Functions.remainderInt(2)).sum() == 30);

        assertTrue(IntStream.iterate(0, new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand + 1;
            }
        }).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 0;
            }
        }).findFirst().getAsInt() == 0);
    }

    @Test
    public void testFilterNot() {
        assertTrue(IntStream.rangeClosed(1, 10).filterNot(Functions.remainderInt(2)).count() == 5);

        assertTrue(IntStream.rangeClosed(1, 10).filterNot(Functions.remainderInt(2)).sum() == 25);
    }

    @Test
    public void testMap() {
        assertTrue(IntStream.of(5).map(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand;
            }
        }).findFirst().getAsInt() == -5);

        assertTrue(IntStream.of(1,2,3,4,5).map(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand;
            }
        }).sum() < 0);
    }

    @Test
    public void testMapToObj() {
        Stream<String> stream = IntStream.rangeClosed(2, 4)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int value) {
                        return Integer.toString(value);
                    }
                });
        List<String> expected = Arrays.asList("2", "3", "4");
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testMapToLong() {
        LongStream stream = IntStream.rangeClosed(2, 4)
                .mapToLong(new IntToLongFunction() {
                    @Override
                    public long applyAsLong(int value) {
                        return value * 10000000000L;
                    }
                });
        assertThat(stream, LongStreamMatcher.elements(arrayContaining(
                20000000000L,
                30000000000L,
                40000000000L
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMapToDouble() {
        DoubleStream stream = IntStream.rangeClosed(2, 4)
                .mapToDouble(new IntToDoubleFunction() {
                    @Override
                    public double applyAsDouble(int value) {
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
        assertTrue(IntStream.range(-1, 5).flatMap(new IntFunction<IntStream>() {
            @Override
            public IntStream apply(int value) {

                if(value < 0) {
                    return null;
                }

                if(value == 0) {
                    return IntStream.empty();
                }

                return IntStream.range(0, value);
            }
        }).count() == 10);
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIterator() {
        IntStream.empty().flatMap(new IntFunction<IntStream>() {
            @Override
            public IntStream apply(int value) {
                return IntStream.of(value);
            }
        }).iterator().nextInt();
    }

    @Test
    public void testDistinct() {
        assertTrue(IntStream.of(1, 2, -1, 10, 1, 1, -1, 5).distinct().count() == 5);
        assertTrue(IntStream.of(1, 2, -1, 10, 1, 1, -1, 5).distinct().sum() == 17);
    }

    @Test
    public void testDistinctLazy() {
        Integer[] expected = { 1, 2, 3, 5, -1 };

        List<Integer> input = new ArrayList<Integer>();
        input.addAll(Arrays.asList(1, 1, 2, 3, 5));
        IntStream stream = Stream.of(input)
                .mapToInt(Functions.toInt())
                .distinct();
        input.addAll(Arrays.asList(3, 2, 1, 1, -1));


        List<Integer> actual = stream.boxed().toList();
        assertThat(actual, Matchers.contains(expected));
    }

    @Test
    public void testSorted() {
        assertTrue(IntStream.empty().sorted().count() == 0);
        assertTrue(IntStream.of(42).sorted().findFirst().getAsInt() == 42);

        final boolean[] wrongOrder = new boolean[]{false};

        IntStream.iterate(2, new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand + 1;
            }
        })
        .limit(1000)
        .sorted()
        .forEach(new IntConsumer() {

            int currentValue = Integer.MIN_VALUE;

            @Override
            public void accept(int value) {
                if(value < currentValue) {
                    wrongOrder[0] = true;
                }
                currentValue = value;
            }
        });

        assertTrue(!wrongOrder[0]);
    }

    @Test
    public void testSortedLazy() {
        int[] expected = { -7, 0, 3, 6, 9, 19 };

        List<Integer> input = new ArrayList<Integer>(6);
        input.addAll(Arrays.asList(6, 3, 9));
        IntStream stream = Stream.of(input).mapToInt(Functions.toInt()).sorted();
        input.addAll(Arrays.asList(0, -7, 19));

        assertThat(stream.toArray(), is(expected));
    }

    @Test
    public void testSortedWithComparator() {
        int[] expected = { 19, 9, -7, 6, 3, 0 };

        int[] actual = IntStream.of(6, 3, 9, 0, -7, 19)
                .sorted(Functions.descendingAbsoluteOrder())
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testSample() {
        int[] expected = { 1, 1, 1 };
        int[] actual = IntStream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(3).toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testSampleWithStep1() {
        int[] expected = { 1, 2, 3, 1, 2, 3, 1, 2, 3 };
        int[] actual = IntStream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(1).toArray();
        assertThat(actual, is(expected));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        IntStream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(-1).count();
    }

    @Test
    public void testPeek() {
        assertTrue(IntStream.empty().peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalStateException();
            }
        }).count() == 0);

        assertTrue(IntStream.generate(new IntSupplier() {
            int value = 2;
            @Override
            public int getAsInt() {
                int v = value;
                value *= 2;
                return v;
            }
        }).peek(new IntConsumer() {
            int curValue = 1;
            @Override
            public void accept(int value) {
                if(value != curValue*2)
                    throw new IllegalStateException();

                curValue = value;
            }
        }).limit(10).count() == 10);
    }

    @Test
    public void testTakeWhile() {
        int[] expected = {2, 4, 6};
        int[] actual = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainderInt(2))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        assertThat(
                IntStream.of(2, 4, 6, 7, 8, 10, 11)
                        .takeWhile(Functions.remainderInt(3))
                         .count(),
                is(0L));
    }

    @Test
    public void testTakeWhileAllMatch() {
        long count = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainderInt(1))
                .count();
        assertEquals(7, count);
    }

    @Test
    public void testDropWhile() {
        int[] expected = {7, 8, 10, 11};
        int[] actual  = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainderInt(2))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testDropWhileNonFirstMatch() {
        long count = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainderInt(3))
                .count();
        assertEquals(7, count);
    }

    @Test
    public void testDropWhileAllMatch() {
        assertThat(
                IntStream.of(2, 4, 6, 7, 8, 10, 11)
                        .dropWhile(Functions.remainderInt(1))
                        .count(),
                is(0L));
    }

    @Test
    public void testLimit() {
        assertTrue(IntStream.of(1,2,3,4,5,6).limit(3).count() == 3);
        assertTrue(IntStream.generate(new IntSupplier() {

            int current = 42;

            @Override
            public int getAsInt() {
                current = current + current<<1;
                return current;
            }
        }).limit(6).count() == 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        IntStream.of(42).limit(-1).count();
    }

    @Test
    public void testLimitZero() {
         assertTrue(IntStream.of(1,2).limit(0).count() == 0);
    }

    @Test
    public void testLimitMoreThanCount() {
        assertThat(IntStream.range(0, 5).limit(15).count(), is(5L));
    }

    @Test
    public void testSkip() {
        assertTrue(IntStream.empty().skip(2).count() == 0);
        assertTrue(IntStream.range(10, 20).skip(5).count() == 5);
        assertTrue(IntStream.range(10, 20).skip(0).count() == 10);
        assertTrue(IntStream.range(10, 20).skip(10).count() == 0);
        assertTrue(IntStream.range(10, 20).skip(20).count() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        IntStream.empty().skip(-5);
    }

    @Test
    public void testSkipZero() {
         assertTrue(IntStream.of(1,2).skip(0).count() == 2);
    }

    @Test
    public void testSkipMoreThanCount() {
        int[] expected = {2, 3, 4, 5, 6};
        int[] actual = IntStream.range(0, 10)
                .skip(2)
                .limit(5)
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testForEach() {
        IntStream.empty().forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalStateException();
            }
        });

        IntStream.of(42).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                assertTrue(value == 42);
            }
        });

        final int[] sum = new int[1];

        IntStream.rangeClosed(10, 20).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                sum[0] += value;
            }
        });

        assertEquals(sum[0], 165);
    }

    @Test
    public void testReduceIdentity() {
        assertEquals(IntStream.empty().reduce(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        }), 1);

        assertEquals(IntStream.of(42).reduce(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        }), 43);

        assertEquals(IntStream.of(5, 7, 3, 9, 1).reduce(Integer.MIN_VALUE, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                if (left >= right)
                    return left;

                return right;
            }
        }), 9);
    }

    @Test
    public void testReduce() {
        assertFalse(IntStream.empty().reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                throw new IllegalStateException();
            }
        }).isPresent());

        assertEquals(IntStream.of(42).reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                throw new IllegalStateException();
            }
        }).getAsInt(), 42);

        assertEquals(IntStream.of(41, 42).reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                if (right > left)
                    return right;
                return left;
            }
        }).getAsInt(), 42);
    }

    @Test
    public void testToArray() {
        assertEquals(IntStream.empty().toArray().length, 0);
        assertEquals(IntStream.of(100).toArray()[0], 100);
        assertEquals(IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).skip(4).toArray().length, 5);

        assertEquals(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return -1;
            }
        }).limit(14).toArray().length, 14);

        assertEquals(IntStream.of(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return -1;
            }
        }).limit(14).toArray()).sum(), -14);
    }

    @Test
    public void testCollect() {
        String result = IntStream.of(0, 1, 5, 10)
                .collect(Functions.stringBuilderSupplier(), new ObjIntConsumer<StringBuilder>() {
            @Override
            public void accept(StringBuilder t, int value) {
                t.append(value);
            }
        }).toString();
        assertThat(result, is("01510"));
    }

    @Test
    public void testSum() {
        assertEquals(IntStream.empty().sum(), 0);
        assertEquals(IntStream.of(42).sum(), 42);
        assertEquals(IntStream.rangeClosed(4, 8).sum(), 30);
    }

    @Test
    public void testMin() {
        assertFalse(IntStream.empty().min().isPresent());

        assertTrue(IntStream.of(42).min().isPresent());
        assertEquals(IntStream.of(42).min().getAsInt(), 42);

        assertEquals(IntStream.of(-1, -2, -3, -2, -3, -5, -2, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .min().getAsInt(), Integer.MIN_VALUE);
    }

    @Test
    public void testMax() {
        assertFalse(IntStream.empty().max().isPresent());

        assertTrue(IntStream.of(42).max().isPresent());
        assertEquals(IntStream.of(42).max().getAsInt(), 42);

        assertEquals(IntStream.of(-1, -2, -3, -2, -3, -5, -2, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .max().getAsInt(), Integer.MAX_VALUE);
    }

    @Test
    public void testCount() {
        assertEquals(IntStream.empty().count(), 0);
        assertEquals(IntStream.of(42).count(), 1);
        assertEquals(IntStream.range(1, 7).count(), 6);
        assertEquals(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 1;
            }
        }).limit(10).count(), 10);

        assertEquals(IntStream.rangeClosed(1, 7).skip(3).count(), 4);
    }

    @Test
    public void testAnyMatch() {
        IntStream.empty().anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertTrue(IntStream.of(42).anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertTrue(IntStream.of(5, 7, 9, 10, 7, 5).anyMatch(Functions.remainderInt(2)));

        assertFalse(IntStream.of(5, 7, 9, 11, 7, 5).anyMatch(Functions.remainderInt(2)));
    }

    @Test
    public void testAllMatch() {
        IntStream.empty().allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertTrue(IntStream.of(42).allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertFalse(IntStream.of(5, 7, 9, 10, 7, 5).allMatch(
                IntPredicate.Util.negate(Functions.remainderInt(2))));

        assertTrue(IntStream.of(5, 7, 9, 11, 7, 5).allMatch(
                IntPredicate.Util.negate(Functions.remainderInt(2))));
    }

    @Test
    public void testNoneMatch() {
        IntStream.empty().noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertFalse(IntStream.of(42).noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertFalse(IntStream.of(5, 7, 9, 10, 7, 5).noneMatch(Functions.remainderInt(2)));

        assertTrue(IntStream.of(5, 7, 9, 11, 7, 5).noneMatch(Functions.remainderInt(2)));
    }

    @Test
    public void testFindFirst() {
        assertFalse(IntStream.empty().findFirst().isPresent());
        assertEquals(IntStream.of(42).findFirst().getAsInt(), 42);
        assertTrue(IntStream.rangeClosed(2, 5).findFirst().isPresent());
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleOnEmptyStream() {
        IntStream.empty().single();
    }

    @Test
    public void testSingleOnOneElementStream() {
        int result = IntStream.of(42).single();

        assertThat(result, is(42));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleOnMoreElementsStream() {
        IntStream.rangeClosed(1, 2).single();
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleAfterFilteringToEmptyStream() {
        IntStream.range(1, 5)
                .filter(Functions.remainderInt(6))
                .single();
    }

    @Test
    public void testSingleAfterFilteringToOneElementStream() {
        int result = IntStream.range(1, 10)
                .filter(Functions.remainderInt(6))
                .single();

        assertThat(result, is(6));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleAfterFilteringToMoreElementStream() {
        IntStream.range(1, 100)
                .filter(Functions.remainderInt(6))
                .single();
    }

    @Test
    public void testFindSingleOnEmptyStream() {
        assertThat(IntStream.empty().findSingle(), OptionalIntMatcher.isEmpty());
    }

    @Test
    public void testFindSingleOnOneElementStream() {
        OptionalInt result = IntStream.of(42).findSingle();

        assertThat(result, hasValue(42));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleOnMoreElementsStream() {
        IntStream.rangeClosed(1, 2).findSingle();
    }

    @Test
    public void testFindSingleAfterFilteringToEmptyStream() {
        OptionalInt result = IntStream.range(1, 5)
                .filter(Functions.remainderInt(6))
                .findSingle();

        assertThat(result, OptionalIntMatcher.isEmpty());
    }

    @Test
    public void testFindSingleAfterFilteringToOneElementStream() {
        OptionalInt result = IntStream.range(1, 10)
                .filter(Functions.remainderInt(6))
                .findSingle();

        assertThat(result, hasValue(6));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleAfterFilteringToMoreElementStream() {
        IntStream.range(1, 100)
                .filter(Functions.remainderInt(6))
                .findSingle();
    }

    @Test(expected = NullPointerException.class)
    public void testCustomNull() {
        IntStream.empty().custom(null);
    }

    @Test
    public void testCustomIntermediateOperator_Zip() {
        final IntBinaryOperator op = new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        };
        IntStream s1 = IntStream.of(1, 3,  5,  7, 9);
        IntStream s2 = IntStream.of(2, 4,  6,  8);
        int[] expected =           {3, 7, 11, 15};
        IntStream result = s1.custom(new CustomOperators.Zip(s2, op));
        assertThat(result.toArray(), is(expected));
    }

    @Test
    public void testCustomTerminalOperator_Average() {
        double average = IntStream.range(0, 10).custom(new CustomOperators.Average());
        assertThat(average, closeTo(4.5, 0.001));
    }
}
