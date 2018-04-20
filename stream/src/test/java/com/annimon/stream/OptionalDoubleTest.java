package com.annimon.stream;

import com.annimon.stream.function.DoubleConsumer;
import com.annimon.stream.function.DoubleFunction;
import com.annimon.stream.function.DoubleSupplier;
import com.annimon.stream.function.DoubleToIntFunction;
import com.annimon.stream.function.DoubleToLongFunction;
import com.annimon.stream.function.DoubleUnaryOperator;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValueThat;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

/**
 * Tests for {@link OptionalDouble}
 */
public class OptionalDoubleTest {

    @Test
    public void testGetWithPresentValue() {
        double value = OptionalDouble.of(10.123).getAsDouble();
        assertThat(value, closeTo(10.123, 0.0001));
    }

    @Test
    public void testOfNullableWithPresentValue() {
        assertThat(OptionalDouble.ofNullable(10.123), hasValueThat(closeTo(10.123, 0.0001)));
    }

    @Test
    public void testOfNullableWithAbsentValue() {
        assertThat(OptionalDouble.ofNullable(null), isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOnEmptyOptional() {
        OptionalDouble.empty().getAsDouble();
    }

    @Test
    public void testIsPresent() {
        assertTrue(OptionalDouble.of(10.123).isPresent());
    }

    @Test
    public void testIsPresentOnEmptyOptional() {
        assertFalse(OptionalDouble.empty().isPresent());
    }

    @Test
    public void testIfPresent() {
        OptionalDouble.empty().ifPresent(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                fail();
            }
        });

        OptionalDouble.of(10.123).ifPresent(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                assertThat(value, closeTo(10.123, 0.0001));
            }
        });
    }

    @Test
    public void testIfPresentOrElseWhenValuePresent() {
        OptionalDouble.of(Math.PI).ifPresentOrElse(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                assertThat(value, closeTo(Math.PI, 0.0001));
            }
        }, new Runnable() {
            @Override
            public void run() {
                fail("Should not execute empty action when value is present.");
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void testIfPresentOrElseWhenValueAbsent() {
        OptionalDouble.empty().ifPresentOrElse(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                fail();
            }
        }, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        });
    }

    @Test
    public void testIfPresentOrElseWhenValuePresentAndEmptyActionNull() {
        OptionalDouble.of(Math.PI).ifPresentOrElse(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                assertThat(value, closeTo(Math.PI, 0.0001));
            }
        }, null);
    }

    @Test(expected = RuntimeException.class)
    public void testIfPresentOrElseWhenValueAbsentAndConsumerNull() {
        OptionalDouble.empty().ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValuePresentAndConsumerNull() {
        OptionalDouble.of(Math.PI).ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                fail("Should not have been executed.");
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValueAbsentAndEmptyActionNull() {
        OptionalDouble.empty().ifPresentOrElse(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                fail("Should not have been executed.");
            }
        }, null);
    }

    @Test
    public void testExecuteIfPresent() {
        double value = OptionalDouble.of(10.123)
                .executeIfPresent(new DoubleConsumer() {
                    @Override
                    public void accept(double value) {
                        assertThat(value, closeTo(10.123, 0.0001));
                    }
                })
                .getAsDouble();
        assertThat(value, closeTo(10.123, 0.0001));
    }

    @Test
    public void testExecuteIfPresentOnAbsentValue() {
        OptionalDouble.empty()
                .executeIfPresent(new DoubleConsumer() {
                    @Override
                    public void accept(double value) {
                        fail();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteIfAbsent() {
        OptionalDouble.empty()
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException();
                    }
                });
    }

    @Test
    public void testExecuteIfAbsentOnPresentValue() {
        OptionalDouble.of(10.123)
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        fail();
                    }
                });
    }

    @Test
    public void testCustomIntermediate() {
        OptionalDouble result = OptionalDouble.of(10)
                .custom(new Function<OptionalDouble, OptionalDouble>() {
                    @Override
                    public OptionalDouble apply(OptionalDouble optional) {
                        return optional.filter(Functions.greaterThan(Math.PI));
                    }
                });

        assertThat(result, hasValueThat(closeTo(10, 0.0001)));
    }

    @Test
    public void testCustomTerminal() {
        Double result = OptionalDouble.empty()
                .custom(new Function<OptionalDouble, Double>() {
                    @Override
                    public Double apply(OptionalDouble optional) {
                        return optional.orElse(0);
                    }
                });

        assertThat(result, closeTo(0, 0.0001));
    }

    @Test(expected = NullPointerException.class)
    public void testCustomException() {
        OptionalDouble.empty().custom(null);
    }

    @Test
    public void testFilter() {
        OptionalDouble result;
        result = OptionalDouble.of(10d)
                .filter(Functions.greaterThan(Math.PI));
        assertThat(result, hasValueThat(closeTo(10d, 0.000001)));

        result = OptionalDouble.empty()
                .filter(Functions.greaterThan(Math.PI));
        assertThat(result, isEmpty());

        result = OptionalDouble.of(1.19)
                .filter(Functions.greaterThan(Math.PI));
        assertThat(result, isEmpty());
    }

    @Test
    public void testFilterNot() {
        OptionalDouble result;
        result = OptionalDouble.of(1.19)
                .filterNot(Functions.greaterThan(Math.PI));
        assertThat(result, hasValueThat(closeTo(1.19, 0.000001)));

        result = OptionalDouble.empty()
                .filterNot(Functions.greaterThan(Math.PI));
        assertThat(result, isEmpty());

        result = OptionalDouble.of(10d)
                .filterNot(Functions.greaterThan(Math.PI));
        assertThat(result, isEmpty());
    }

    @Test
    public void testMap() {
        final DoubleUnaryOperator negatorFunction = new DoubleUnaryOperator() {

            @Override
            public double applyAsDouble(double operand) {
                return -operand;
            }
        };

        OptionalDouble result;
        result = OptionalDouble.empty().map(negatorFunction);
        assertThat(result, isEmpty());

        result = OptionalDouble.of(10.123).map(negatorFunction);
        assertThat(result, hasValueThat(closeTo(-10.123, 0.0001)));
    }

    @Test
    public void testMapToObj() {
        final DoubleFunction<String> asciiToString = new DoubleFunction<String>() {

            @Override
            public String apply(double value) {
                return String.valueOf((char) value);
            }
        };

        Optional<String> result;
        result = OptionalDouble.empty().mapToObj(asciiToString);
        assertThat(result, OptionalMatcher.isEmpty());

        result = OptionalDouble.of(65d).mapToObj(asciiToString);
        assertThat(result, OptionalMatcher.hasValue("A"));

        result = OptionalDouble.empty().mapToObj(new DoubleFunction<String>() {
            @Override
            public String apply(double value) {
                return null;
            }
        });
        assertThat(result, OptionalMatcher.isEmpty());
    }

    @Test
    public void testMapToInt() {
        assertThat(OptionalDouble.of(0.2).mapToInt(new DoubleToIntFunction() {
            @Override
            public int applyAsInt(double value) {
                return (int) (value * 10);
            }
        }).getAsInt(), is(2));

        assertFalse(OptionalDouble.empty().mapToInt(new DoubleToIntFunction() {
            @Override
            public int applyAsInt(double value) {
                fail();
                return 0;
            }
        }).isPresent());
    }

    @Test
    public void testMapToLong() {
        assertThat(OptionalDouble.of(0.2).mapToLong(new DoubleToLongFunction() {
            @Override
            public long applyAsLong(double value) {
                return (long) (value * 10);
            }
        }).getAsLong(), is(2L));

        assertFalse(OptionalDouble.empty().mapToLong(new DoubleToLongFunction() {
            @Override
            public long applyAsLong(double value) {
                fail();
                return 0;
            }
        }).isPresent());
    }

    @Test
    public void testStream() {
        long count = OptionalDouble.of(10d).stream().count();
        assertThat(count, is(1L));
    }

    @Test
    public void testStreamOnEmptyOptional() {
        long count = OptionalDouble.empty().stream().count();
        assertThat(count, is(0L));
    }

    @Test
    public void testOr() {
        double value = OptionalDouble.of(10.123).or(new Supplier<OptionalDouble>() {
            @Override
            public OptionalDouble get() {
                return OptionalDouble.of(19);
            }
        }).getAsDouble();
        assertThat(value, closeTo(10.123, 0.0001));
    }

    @Test
    public void testOrOnEmptyOptional() {
        double value = OptionalDouble.empty().or(new Supplier<OptionalDouble>() {
            @Override
            public OptionalDouble get() {
                return OptionalDouble.of(Math.PI);
            }
        }).getAsDouble();
        assertThat(value, closeTo(Math.PI, 0.0001));
    }

    @Test
    public void testOrOnEmptyOptionalAndEmptySupplierOptional() {
        final OptionalDouble optional = OptionalDouble.empty().or(new Supplier<OptionalDouble>() {
            @Override
            public OptionalDouble get() {
                return OptionalDouble.empty();
            }
        });
        assertThat(optional, isEmpty());
    }

    @Test
    public void testOrElse() {
        assertThat(OptionalDouble.empty().orElse(10.123), closeTo(10.123, 0.0001));
        assertThat(OptionalDouble.of(10.123).orElse(0d), closeTo(10.123, 0.0001));
    }

    @Test
    public void testOrElseGet() {
        assertThat(OptionalDouble.empty().orElseGet(new DoubleSupplier() {
            @Override
            public double getAsDouble() {
                return 21.098;
            }
        }), closeTo(21.098, 0.0001));

        assertThat(OptionalDouble.of(21.098).orElseGet(new DoubleSupplier() {
            @Override
            public double getAsDouble() {
                throw new IllegalStateException();
            }
        }), closeTo(21.098, 0.0001));
    }

    @Test
    public void testOrElseThrowWithPresentValue() {
        double value = OptionalDouble.of(10.123).orElseThrow();
        assertThat(value, closeTo(10.123, 0.0001));
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrowOnEmptyOptional() {
        OptionalDouble.empty().orElseThrow();
    }

    @Test
    public void testOrElseThrow() {
        try {
            assertThat(OptionalDouble.of(10.123).orElseThrow(new Supplier<NoSuchElementException>() {
                @Override
                public NoSuchElementException get() {
                    throw new IllegalStateException();
                }
            }), closeTo(10.123, 0.0001));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrow2() {
        OptionalDouble.empty().orElseThrow(new Supplier<NoSuchElementException>() {
            @Override
            public NoSuchElementException get() {
                return new NoSuchElementException();
            }
        });
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void testEquals() {
        assertEquals(OptionalDouble.empty(), OptionalDouble.empty());
        assertNotEquals(OptionalDouble.empty(), Optional.empty());

        assertEquals(OptionalDouble.of(Math.PI), OptionalDouble.of(Math.PI));

        assertNotEquals(OptionalDouble.of(41d), OptionalDouble.of(42d));
        assertNotEquals(OptionalDouble.of(0d), OptionalDouble.empty());
    }

    @Test
    public void testHashCode() {
        assertEquals(OptionalDouble.empty().hashCode(), 0);
        assertEquals(Double.valueOf(31d).hashCode(), OptionalDouble.of(31d).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(OptionalDouble.empty().toString(), "OptionalDouble.empty");
        assertThat(OptionalDouble.of(42d).toString(), containsString("OptionalDouble[42"));
    }

}
