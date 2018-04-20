package com.annimon.stream;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.IntFunction;
import com.annimon.stream.function.IntSupplier;
import com.annimon.stream.function.IntToDoubleFunction;
import com.annimon.stream.function.IntToLongFunction;
import com.annimon.stream.function.IntUnaryOperator;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.test.hamcrest.OptionalDoubleMatcher;
import com.annimon.stream.test.hamcrest.OptionalLongMatcher;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.isPresent;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

/**
 * Tests for {@link OptionalInt}
 */
public class OptionalIntTest {

    @Test
    public void testGetWithPresentValue() {
        int value = OptionalInt.of(10).getAsInt();
        assertEquals(10, value);
    }

    @Test
    public void testOfNullableWithPresentValue() {
        assertThat(OptionalInt.ofNullable(10), hasValue(10));
    }

    @Test
    public void testOfNullableWithAbsentValue() {
        assertThat(OptionalInt.ofNullable(null), isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOnEmptyOptional() {
        OptionalInt.empty().getAsInt();
    }

    @Test
    public void testIsPresent() {
        assertThat(OptionalInt.of(10), isPresent());
    }

    @Test
    public void testIsPresentOnEmptyOptional() {
        assertThat(OptionalInt.empty(), isEmpty());
    }

    @Test
    public void testIfPresent() {
        OptionalInt.empty().ifPresent(new IntConsumer() {
            @Override
            public void accept(int value) {
                fail();
            }
        });

        OptionalInt.of(15).ifPresent(new IntConsumer() {
            @Override
            public void accept(int value) {
                assertEquals(15, value);
            }
        });
    }

    @Test
    public void testIfPresentOrElseWhenValuePresent() {
        OptionalInt.of(10).ifPresentOrElse(new IntConsumer() {
            @Override
            public void accept(int value) {
                assertThat(value, is(10));
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
        OptionalInt.empty().ifPresentOrElse(new IntConsumer() {
            @Override
            public void accept(int value) {
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
        OptionalInt.of(10).ifPresentOrElse(new IntConsumer() {
            @Override
            public void accept(int value) {
                assertThat(value, is(10));
            }
        }, null);
    }

    @Test(expected = RuntimeException.class)
    public void testIfPresentOrElseWhenValueAbsentAndConsumerNull() {
        OptionalInt.empty().ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValuePresentAndConsumerNull() {
        OptionalInt.of(10).ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                fail("Should not have been executed.");
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValueAbsentAndEmptyActionNull() {
        OptionalInt.empty().ifPresentOrElse(new IntConsumer() {
            @Override
            public void accept(int value) {
                fail("Should not have been executed.");
            }
        }, null);
    }

    @Test
    public void testExecuteIfPresent() {
        int value = OptionalInt.of(10)
                .executeIfPresent(new IntConsumer() {
                    @Override
                    public void accept(int value) {
                        assertEquals(10, value);
                    }
                })
                .getAsInt();
        assertEquals(10, value);
    }

    @Test
    public void testExecuteIfPresentOnAbsentValue() {
        OptionalInt.empty()
                .executeIfPresent(new IntConsumer() {
                    @Override
                    public void accept(int value) {
                        fail();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteIfAbsent() {
        OptionalInt.empty()
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException();
                    }
                });
    }

    @Test
    public void testExecuteIfAbsentOnPresentValue() {
        OptionalInt.of(10)
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        fail();
                    }
                });
    }

    @Test
    public void testCustomIntermediate() {
        OptionalInt result = OptionalInt.of(10)
                .custom(new Function<OptionalInt, OptionalInt>() {
                    @Override
                    public OptionalInt apply(OptionalInt optional) {
                        return optional.filter(Functions.remainderInt(2));
                    }
                });

        assertThat(result, hasValue(10));
    }

    @Test
    public void testCustomTerminal() {
        Integer result = OptionalInt.empty()
                .custom(new Function<OptionalInt, Integer>() {
                    @Override
                    public Integer apply(OptionalInt optional) {
                        return optional.orElse(0);
                    }
                });

        assertThat(result, is(0));
    }

    @Test(expected = NullPointerException.class)
    public void testCustomException() {
        OptionalInt.empty().custom(null);
    }

    @Test
    public void testFilter() {
        OptionalInt result;
        result = OptionalInt.of(4)
                .filter(Functions.remainderInt(2));
        assertThat(result, hasValue(4));

        result = OptionalInt.empty()
                .filter(Functions.remainderInt(2));
        assertThat(result, isEmpty());

        result = OptionalInt.of(9)
                .filter(Functions.remainderInt(2));
        assertThat(result, isEmpty());
    }

    @Test
    public void testFilterNot() {
        OptionalInt result;
        result = OptionalInt.of(4)
                .filterNot(Functions.remainderInt(3));
        assertThat(result, hasValue(4));

        result = OptionalInt.empty()
                .filterNot(Functions.remainderInt(3));
        assertThat(result, isEmpty());

        result = OptionalInt.of(9)
                .filterNot(Functions.remainderInt(3));
        assertThat(result, isEmpty());
    }


    @Test
    public void testMap() {
        final IntUnaryOperator negatorFunction = new IntUnaryOperator() {

            @Override
            public int applyAsInt(int operand) {
                return -operand;
            }
        };

        OptionalInt result;
        result = OptionalInt.empty().map(negatorFunction);
        assertThat(result, isEmpty());

        result = OptionalInt.of(10).map(negatorFunction);
        assertThat(result, hasValue(-10));
    }

    @Test
    public void testMapToObj() {
        final IntFunction<String> asciiToString = new IntFunction<String>() {

            @Override
            public String apply(int value) {
                return String.valueOf((char) value);
            }
        };

        Optional<String> result;
        result = OptionalInt.empty().mapToObj(asciiToString);
        assertThat(result, OptionalMatcher.isEmpty());

        result = OptionalInt.of(65).mapToObj(asciiToString);
        assertThat(result, OptionalMatcher.hasValue("A"));

        result = OptionalInt.empty().mapToObj(new IntFunction<String>() {
            @Override
            public String apply(int value) {
                return null;
            }
        });
        assertThat(result, OptionalMatcher.isEmpty());
    }

    @Test
    public void testMapToLong() {
        final IntToLongFunction mapper = new IntToLongFunction() {
            @Override
            public long applyAsLong(int value) {
                return value * 10000000000L;
            }
        };

        OptionalLong result;
        result = OptionalInt.empty().mapToLong(mapper);
        assertThat(result, OptionalLongMatcher.isEmpty());

        result = OptionalInt.of(65).mapToLong(mapper);
        assertThat(result, OptionalLongMatcher.hasValue(650000000000L));
    }

    @Test
    public void testMapToDouble() {
        final IntToDoubleFunction mapper = new IntToDoubleFunction() {
            @Override
            public double applyAsDouble(int value) {
                return value / 100d;
            }
        };

        OptionalDouble result;
        result = OptionalInt.empty().mapToDouble(mapper);
        assertThat(result, OptionalDoubleMatcher.isEmpty());

        result = OptionalInt.of(65).mapToDouble(mapper);
        assertThat(result, OptionalDoubleMatcher.hasValueThat(closeTo(0.65, 0.0001)));
    }

    @Test
    public void testStream() {
        long count = OptionalInt.of(10).stream().count();
        assertThat(count, is(1L));
    }

    @Test
    public void testStreamOnEmptyOptional() {
        long count = OptionalInt.empty().stream().count();
        assertThat(count, is(0L));
    }

    @Test
    public void testOr() {
        int value = OptionalInt.of(42).or(new Supplier<OptionalInt>() {
            @Override
            public OptionalInt get() {
                return OptionalInt.of(19);
            }
        }).getAsInt();
        assertEquals(42, value);
    }

    @Test
    public void testOrOnEmptyOptional() {
        int value = OptionalInt.empty().or(new Supplier<OptionalInt>() {
            @Override
            public OptionalInt get() {
                return OptionalInt.of(19);
            }
        }).getAsInt();
        assertEquals(19, value);
    }

    @Test
    public void testOrOnEmptyOptionalAndEmptySupplierOptional() {
        final OptionalInt optional = OptionalInt.empty().or(new Supplier<OptionalInt>() {
            @Override
            public OptionalInt get() {
                return OptionalInt.empty();
            }
        });
        assertThat(optional, isEmpty());
    }

    @Test
    public void testOrElse() {
        assertEquals(17, OptionalInt.empty().orElse(17));
        assertEquals(17, OptionalInt.of(17).orElse(0));
    }

    @Test
    public void testOrElseGet() {
        assertEquals(21, OptionalInt.empty().orElseGet(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 21;
            }
        }));

        assertEquals(21, OptionalInt.of(21).orElseGet(new IntSupplier() {
            @Override
            public int getAsInt() {
                throw new IllegalStateException();
            }
        }));
    }

    @Test
    public void testOrElseThrowWithPresentValue() {
        int value = OptionalInt.of(10).orElseThrow();
        assertEquals(10, value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrowOnEmptyOptional() {
        OptionalInt.empty().orElseThrow();
    }

    @Test
    public void testOrElseThrow() {
        try {
            assertEquals(25, OptionalInt.of(25).orElseThrow(new Supplier<NoSuchElementException>() {
                @Override
                public NoSuchElementException get() {
                    throw new IllegalStateException();
                }
            }));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrow2() {
        assertEquals(25, OptionalInt.empty().orElseThrow(new Supplier<NoSuchElementException>() {
            @Override
            public NoSuchElementException get() {
                return new NoSuchElementException();
            }
        }));
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void testEquals() {
        assertEquals(OptionalInt.empty(), OptionalInt.empty());
        assertNotEquals(OptionalInt.empty(), Optional.empty());

        assertEquals(OptionalInt.of(42), OptionalInt.of(42));

        assertNotEquals(OptionalInt.of(41), OptionalInt.of(42));
        assertNotEquals(OptionalInt.of(0), OptionalInt.empty());
    }

    @Test
    public void testHashCode() {
        assertEquals(OptionalInt.empty().hashCode(), 0);
        assertEquals(31, OptionalInt.of(31).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(OptionalInt.empty().toString(), "OptionalInt.empty");
        assertEquals(OptionalInt.of(42).toString(), "OptionalInt[42]");
    }

}
