package com.annimon.stream;

import com.annimon.stream.function.DoubleBinaryOperator;
import com.annimon.stream.function.DoubleConsumer;
import com.annimon.stream.function.DoubleFunction;
import com.annimon.stream.function.DoublePredicate;
import com.annimon.stream.function.DoubleSupplier;
import com.annimon.stream.function.DoubleToIntFunction;
import com.annimon.stream.function.DoubleToLongFunction;
import com.annimon.stream.function.DoubleUnaryOperator;
import com.annimon.stream.function.ObjDoubleConsumer;
import com.annimon.stream.test.hamcrest.OptionalDoubleMatcher;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
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
public class DoubleStreamTest {

    @Test
    public void testStreamEmpty() {
        assertThat(DoubleStream.empty(), isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStreamOfPrimitiveIterator() {
        DoubleStream stream = DoubleStream.of(new PrimitiveIterator.OfDouble() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < 3;
            }

            @Override
            public double nextDouble() {
                index++;
                return index + 0.0021;
            }
        });
        assertThat(stream, elements(array(
                closeTo(1.0021, 0.00001),
                closeTo(2.0021, 0.00001),
                closeTo(3.0021, 0.00001)
        )));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfPrimitiveIteratorNull() {
        DoubleStream.of((PrimitiveIterator.OfDouble) null);
    }

    @Test
    public void testStreamOfDoubles() {
        assertThat(DoubleStream.of(3.2, 2.8), elements(arrayContaining(3.2, 2.8)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfDoublesNull() {
        DoubleStream.of((double[]) null);
    }

    @Test
    public void testStreamOfDouble() {
        assertThat(DoubleStream.of(1.234), elements(arrayContaining(1.234)));
    }

    @Test
    public void testStreamGenerate() {
        DoubleStream stream = DoubleStream.generate(new DoubleSupplier() {
            @Override
            public double getAsDouble() {
                return 1.234;
            }
        });
        assertThat(stream.limit(3), elements(arrayContaining(1.234, 1.234, 1.234)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamGenerateNull() {
        DoubleStream.generate(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStreamIterate() {
        DoubleUnaryOperator operator = new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double operand) {
                return operand + 0.01;
            }
        };

        assertThat(DoubleStream.iterate(0.0, operator).limit(3),
                elements(array(
                    closeTo(0.00, 0.00001),
                    closeTo(0.01, 0.00001),
                    closeTo(0.02, 0.00001)
                ))
        );
    }

    @Test(expected = NullPointerException.class)
    public void testStreamIterateNull() {
        DoubleStream.iterate(0, null);
    }

    @Test
    public void testStreamConcat() {
        DoubleStream a1 = DoubleStream.empty();
        DoubleStream b1 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a1, b1), isEmpty());

        DoubleStream a2 = DoubleStream.of(10.123, Math.PI);
        DoubleStream b2 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a2, b2),
                elements(arrayContaining(10.123, Math.PI)));

        DoubleStream a3 = DoubleStream.of(10.123, Math.PI);
        DoubleStream b3 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a3, b3),
                elements(arrayContaining(10.123, Math.PI)));

        DoubleStream a4 = DoubleStream.of(10.123, Math.PI, -1e11, -1e8);
        DoubleStream b4 = DoubleStream.of(1.617, 9.81);
        assertThat(DoubleStream.concat(a4, b4),
                elements(arrayContaining(10.123, Math.PI, -1e11, -1e8, 1.617, 9.81)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullA() {
        DoubleStream.concat(null, DoubleStream.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullB() {
        DoubleStream.concat(DoubleStream.empty(), null);
    }

    @Test
    public void testIterator() {
        assertThat(DoubleStream.of(1.0123).iterator().nextDouble(), closeTo(1.0123, 0.0001));

        assertThat(DoubleStream.empty().iterator().hasNext(), is(false));

        assertThat(DoubleStream.empty().iterator().nextDouble(), is(0d));
    }

    @Test
    public void testBoxed() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).boxed(),
                StreamMatcher.elements(is(Arrays.asList(0.1, 0.2, 0.3))));
    }

    @Test
    public void testFilter() {
        final DoublePredicate predicate = Functions.greaterThan(Math.PI);
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).filter(predicate),
                elements(arrayContaining(10.347, 19.84, 100d)));

        assertThat(DoubleStream.of(0.012, -10).filter(predicate),
                isEmpty());
    }

    @Test
    public void testFilterNot() {
        final DoublePredicate predicate = Functions.greaterThan(Math.PI);
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).filterNot(predicate),
                elements(arrayContaining(0.012, 3.039)));

        assertThat(DoubleStream.of(4.096, 12).filterNot(predicate),
                isEmpty());
    }

    @Test
    public void testMap() {
        DoubleUnaryOperator negator = new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double operand) {
                return -operand;
            }
        };
        assertThat(DoubleStream.of(0.012, 3.039, 100d).map(negator),
                elements(arrayContaining(-0.012, -3.039, -100d)));

        assertThat(DoubleStream.empty().map(negator),
                isEmpty());
    }

    @Test
    public void testMapToObj() {
        DoubleFunction<String> doubleToString = new DoubleFunction<String>() {
            @Override
            public String apply(double value) {
                return Double.toString(value);
            }
        };
        assertThat(DoubleStream.of(1.0, 2.12, 3.234).mapToObj(doubleToString),
                StreamMatcher.elements(is(Arrays.asList("1.0", "2.12", "3.234"))));

        assertThat(DoubleStream.empty().mapToObj(doubleToString),
                StreamMatcher.isEmpty());
    }

    @Test
    public void testMapToInt() {
        DoubleToIntFunction mapper = new DoubleToIntFunction() {
            @Override
            public int applyAsInt(double value) {
                return (int) (value * 10);
            }
        };
        assertThat(DoubleStream.of(0.2, 0.3, 0.4).mapToInt(mapper).toArray(),
                is(new int[] {2, 3, 4}));
    }

    @Test
    public void testMapToLong() {
        DoubleToLongFunction mapper = new DoubleToLongFunction() {
            @Override
            public long applyAsLong(double value) {
                return (long) (value * 10000000000L);
            }
        };
        assertThat(DoubleStream.of(0.2, 0.3, 0.004).mapToLong(mapper).toArray(),
                is(new long[] {2000000000L, 3000000000L, 40000000L}));
    }

    @Test
    public void testFlatMap() {
        DoubleFunction<DoubleStream> twicer = new DoubleFunction<DoubleStream>() {
            @Override
            public DoubleStream apply(double value) {
                return DoubleStream.of(value, value);
            }
        };
        assertThat(DoubleStream.of(0.012, -3.039, 100d).flatMap(twicer),
                elements(arrayContaining(0.012, 0.012, -3.039, -3.039, 100d, 100d)));

        assertThat(DoubleStream.of(0.012, -3.039, 100d).flatMap(new DoubleFunction<DoubleStream>() {
            @Override
            public DoubleStream apply(double value) {
                if (value < 0) return DoubleStream.of(value);
                return null;
            }
        }), elements(arrayContaining(-3.039)));

        assertThat(DoubleStream.of(0.012, -3.039, 100d).flatMap(new DoubleFunction<DoubleStream>() {
            @Override
            public DoubleStream apply(double value) {
                if (value < 0) return DoubleStream.empty();
                return DoubleStream.of(value);
            }
        }), elements(arrayContaining(0.012, 100d)));
    }

    @Test
    public void testDistinct() {
        assertThat(DoubleStream.of(0.09, 1.2, 0, 2.2, 0.09, 1.2, 3.2, 0.09).distinct(),
                elements(arrayContaining(0.09, 1.2, 0d, 2.2, 3.2)));
    }

    @Test
    public void testSorted() {
        assertThat(DoubleStream.of(1.2, 3.234, 0.09, 2.2).sorted(),
                elements(arrayContaining(0.09, 1.2, 2.2, 3.234)));

        assertThat(DoubleStream.empty().sorted(),
                isEmpty());
    }

    @Test
    public void testSortedWithComparator() {
        assertThat(DoubleStream.of(1.2, 3.234, 0.09, 2.2).sorted(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                // reverse order
                return Double.compare(o2, o1);
            }
        }), elements(arrayContaining(3.234, 2.2, 1.2, 0.09)));
    }

    @Test
    public void testSample() {
        assertThat(DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d).sample(2),
                elements(arrayContaining(1.2, 0.09, 80d)));
    }

    @Test
    public void testSampleWithStep1() {
        assertThat(DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d).sample(1),
                elements(arrayContaining(1.2, 3.234, 0.09, 2.2, 80d)));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        DoubleStream.of(1.2, 3.234).sample(-1).count();
    }

    @Test
    public void testPeek() {
        assertThat(DoubleStream.empty().peek(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                fail();
            }
        }), isEmpty());

        final double[] expected = {1.2, 3.456};
        assertThat(DoubleStream.of(1.2, 3.456).peek(new DoubleConsumer() {

            private int index = 0;

            @Override
            public void accept(double value) {
                assertThat(value, closeTo(expected[index++], 0.0001));
            }
        }).count(), is(2L));
    }

    @Test
    public void testTakeWhile() {
        DoubleStream stream;
        stream = DoubleStream.of(10.2, 30.234, 10.09, 2.2, 80d)
                    .takeWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(10.2, 30.234, 10.09)));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                    .takeWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, isEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(10.2, 30.234, 10.09)
                    .takeWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(10.2, 30.234, 10.09)));
    }

    @Test
    public void testDropWhile() {
        DoubleStream stream;
        stream = DoubleStream.of(10.2, 30.234, 10.09, 2.2, 80d)
                    .dropWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(2.2, 80d)));
    }
    @Test
    public void testDropWhileNonFirstMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                    .dropWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(1.2, 3.234, 0.09, 2.2, 80d)));
    }

    @Test
    public void testDropWhileAllMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(10.2, 30.234, 80d)
                    .dropWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, isEmpty());
    }

    @Test
    public void testLimit() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).limit(2),
                elements(arrayContaining(0.1, 0.2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        DoubleStream.of(0.1, 2.345).limit(-2).count();
    }

    @Test
    public void testLimitZero() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).limit(0),
                isEmpty());
    }

    @Test
    public void testLimitMoreThanCount() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).limit(5),
                elements(arrayContaining(0.1, 0.2, 0.3)));
    }

    @Test
    public void testSkip() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).skip(2),
                elements(arrayContaining(0.3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        DoubleStream.of(0.1, 2.345).skip(-2).count();
    }

    @Test
    public void testSkipZero() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).skip(0),
                elements(arrayContaining(0.1, 0.2, 0.3)));
    }

    @Test
    public void testSkipMoreThanCount() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).skip(5),
                isEmpty());
    }

    @Test
    public void testForEach() {
        final double[] expected = {1.2, 3.456};
        DoubleStream.of(1.2, 3.456).forEach(new DoubleConsumer() {

            private int index = 0;

            @Override
            public void accept(double value) {
                assertThat(value, closeTo(expected[index++], 0.0001));
            }
        });
    }

    @Test
    public void testForEachOnEmptyStream() {
        DoubleStream.empty().forEach(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                fail();
            }
        });
    }

    @Test
    public void testReduceWithIdentity() {
        double result = DoubleStream.of(0.012, -3.772, 3.039, 19.84, 100d)
                .reduce(0d, new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        });
        assertThat(result, closeTo(119.119, 0.0001));
    }

    @Test
    public void testReduceWithIdentityOnEmptyStream() {
        double result = DoubleStream.empty().reduce(Math.PI, new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        });
        assertThat(result, closeTo(Math.PI, 0.00001));
    }

    @Test
    public void testReduce() {
        assertThat(DoubleStream.of(0.012, -3.772, 3.039, 19.84, 100d)
                .reduce(new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        }), OptionalDoubleMatcher.hasValueThat(closeTo(119.119, 0.0001)));
    }

    @Test
    public void testReduceOnEmptyStream() {
        assertThat(DoubleStream.empty().reduce(new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        }), OptionalDoubleMatcher.isEmpty());
    }

    @Test
    public void testToArray() {
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).toArray(),
                is(new double[] {0.012, 10.347, 3.039, 19.84, 100d}));
    }

    @Test
    public void testToArrayOnEmptyStream() {
        assertThat(DoubleStream.empty().toArray().length, is(0));
    }

    @Test
    public void testCollect() {
        String result = DoubleStream.of(1.0, 2.0, 3.0)
                .collect(Functions.stringBuilderSupplier(), new ObjDoubleConsumer<StringBuilder>() {
                    @Override
                    public void accept(StringBuilder t, double value) {
                        t.append(value);
                    }
                }).toString();
        assertThat(result, is("1.02.03.0"));
    }

    @Test
    public void testSum() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).sum(), closeTo(0.123, 0.0001));
        assertThat(DoubleStream.empty().sum(), is(0d));
    }

    @Test
    public void testMin() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).min(), hasValue(0.003));
        assertThat(DoubleStream.empty().min(), OptionalDoubleMatcher.isEmpty());
    }

    @Test
    public void testMax() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).max(), hasValue(0.1));
        assertThat(DoubleStream.empty().max(), OptionalDoubleMatcher.isEmpty());
    }

    @Test
    public void testCount() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).count(), is(3L));
        assertThat(DoubleStream.empty().count(), is(0L));
    }

    @Test
    public void testAverage() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).average(),
                OptionalDoubleMatcher.hasValueThat(closeTo(0.041, 0.00001)));

        assertThat(DoubleStream.empty().average(), OptionalDoubleMatcher.isEmpty());
    }

    @Test
    public void testAnyMatch() {
        assertTrue(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d)
                .anyMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.of(10.347, 19.84, 100d)
                .anyMatch(Functions.greaterThan(Math.PI)));

        assertFalse(DoubleStream.of(0.012, 3.039)
                .anyMatch(Functions.greaterThan(Math.PI)));

        assertFalse(DoubleStream.empty()
                .anyMatch(Functions.greaterThan(Math.PI)));
    }

    @Test
    public void testAllMatch() {
        assertFalse(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d)
                .allMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.of(10.347, 19.84, 100d)
                .allMatch(Functions.greaterThan(Math.PI)));

        assertFalse(DoubleStream.of(0.012, 3.039)
                .allMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.empty()
                .allMatch(Functions.greaterThan(Math.PI)));
    }

    @Test
    public void testNoneMatch() {
        assertFalse(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d)
                .noneMatch(Functions.greaterThan(Math.PI)));

        assertFalse(DoubleStream.of(10.347, 19.84, 100d)
                .noneMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.of(0.012, 3.039)
                .noneMatch(Functions.greaterThan(Math.PI)));

        assertTrue(DoubleStream.empty()
                .noneMatch(Functions.greaterThan(Math.PI)));
    }

    @Test
    public void testFindFirst() {
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).findFirst(),
                hasValue(0.012));

        assertThat(DoubleStream.empty().findFirst(),
                OptionalDoubleMatcher.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleOnEmptyStream() {
        DoubleStream.empty().single();
    }

    @Test
    public void testSingleOnOneElementStream() {
        assertThat(DoubleStream.of(42d).single(), is(42d));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleOnMoreElementsStream() {
        DoubleStream.of(0, 1, 2).single();
    }

    @Test(expected = NoSuchElementException.class)
    public void testSingleAfterFilteringToEmptyStream() {
        DoubleStream.of(0, 1, 2)
                .filter(Functions.greaterThan(Math.PI))
                .single();
    }

    @Test
    public void testSingleAfterFilteringToOneElementStream() {
        double result = DoubleStream.of(1.0, 10.12, -3.01)
                .filter(Functions.greaterThan(Math.PI))
                .single();
        assertThat(result, is(10.12d));
    }

    @Test(expected = IllegalStateException.class)
    public void testSingleAfterFilteringToMoreElementStream() {
        DoubleStream.of(1.0, 10.12, -3.01, 6.45)
                .filter(Functions.greaterThan(Math.PI))
                .single();
    }

    @Test
    public void testFindSingleOnEmptyStream() {
        assertThat(DoubleStream.empty().findSingle(),
                OptionalDoubleMatcher.isEmpty());
    }

    @Test
    public void testFindSingleOnOneElementStream() {
        assertThat(DoubleStream.of(42d).findSingle(), hasValue(42d));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleOnMoreElementsStream() {
        DoubleStream.of(1, 2).findSingle();
    }

    @Test
    public void testFindSingleAfterFilteringToEmptyStream() {
        OptionalDouble result = DoubleStream.of(0, 1, 2)
                .filter(Functions.greaterThan(Math.PI))
                .findSingle();

        assertThat(result, OptionalDoubleMatcher.isEmpty());
    }

    @Test
    public void testFindSingleAfterFilteringToOneElementStream() {
        OptionalDouble result = DoubleStream.of(1.0, 10.12, -3.01)
                .filter(Functions.greaterThan(Math.PI))
                .findSingle();

        assertThat(result, hasValue(10.12));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleAfterFilteringToMoreElementStream() {
        DoubleStream.of(1.0, 10.12, -3.01, 6.45)
                .filter(Functions.greaterThan(Math.PI))
                .findSingle();
    }

    @Test(expected = NullPointerException.class)
    public void testCustom() {
        DoubleStream.empty().custom(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCustomIntermediateOperator_Zip() {
        final DoubleBinaryOperator op = new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left * right;
            }
        };
        DoubleStream s1 = DoubleStream.of(1.01, 2.02, 3.03);
        IntStream s2 = IntStream.range(2, 5);
        DoubleStream result = s1.custom(new CustomOperators.ZipWithIntStream(s2, op));
        assertThat(result, elements(array(
                closeTo(2.02, 0.00001),
                closeTo(6.06, 0.00001),
                closeTo(12.12, 0.00001)
        )));
    }

    @Test
    public void testCustomTerminalOperator_DoubleSummaryStatistics() {
        double[] result = DoubleStream.of(0.1, 0.02, 0.003).custom(new CustomOperators.DoubleSummaryStatistics());
        double count = result[0], sum = result[1], average = result[2];
        assertThat(count, closeTo(3, 0.0001));
        assertThat(sum, closeTo(0.123, 0.0001));
        assertThat(average, closeTo(0.041, 0.0001));
    }
}
