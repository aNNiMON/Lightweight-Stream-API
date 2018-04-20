package com.annimon.stream;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.LongConsumer;
import com.annimon.stream.function.LongFunction;
import com.annimon.stream.function.LongSupplier;
import com.annimon.stream.function.LongToIntFunction;
import com.annimon.stream.function.LongUnaryOperator;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.test.hamcrest.OptionalIntMatcher;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isPresent;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Tests for {@link OptionalLong}
 */
public class OptionalLongTest {

    @Test
    public void testGetWithPresentValue() {
        long value = OptionalLong.of(10).getAsLong();
        assertEquals(10, value);
    }

    @Test
    public void testOfNullableWithPresentValue() {
        assertThat(OptionalLong.ofNullable(10L), hasValue(10L));
    }

    @Test
    public void testOfNullableWithAbsentValue() {
        assertThat(OptionalLong.ofNullable(null), isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOnEmptyOptional() {
        OptionalLong.empty().getAsLong();
    }

    @Test
    public void testIsPresent() {
        assertThat(OptionalLong.of(10), isPresent());
    }

    @Test
    public void testIsPresentOnEmptyOptional() {
        assertThat(OptionalLong.empty(), isEmpty());
    }

    @Test
    public void testIfPresent() {
        OptionalLong.empty().ifPresent(new LongConsumer() {
            @Override
            public void accept(long value) {
                fail();
            }
        });

        OptionalLong.of(15).ifPresent(new LongConsumer() {
            @Override
            public void accept(long value) {
                assertEquals(15, value);
            }
        });
    }

    @Test
    public void testIfPresentOrElseWhenValuePresent() {
        OptionalLong.of(10L).ifPresentOrElse(new LongConsumer() {
            @Override
            public void accept(long value) {
                assertThat(value, is(10L));
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
        OptionalLong.empty().ifPresentOrElse(new LongConsumer() {
            @Override
            public void accept(long value) {
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
        OptionalLong.of(10).ifPresentOrElse(new LongConsumer() {
            @Override
            public void accept(long value) {
                assertThat(value, is(10L));
            }
        }, null);
    }

    @Test(expected = RuntimeException.class)
    public void testIfPresentOrElseWhenValueAbsentAndConsumerNull() {
        OptionalLong.empty().ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValuePresentAndConsumerNull() {
        OptionalLong.of(10).ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                fail("Should not have been executed.");
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValueAbsentAndEmptyActionNull() {
        OptionalLong.empty().ifPresentOrElse(new LongConsumer() {
            @Override
            public void accept(long value) {
                fail("Should not have been executed.");
            }
        }, null);
    }

    @Test
    public void testExecuteIfPresent() {
        long value = OptionalLong.of(10)
                .executeIfPresent(new LongConsumer() {
                    @Override
                    public void accept(long value) {
                        assertEquals(10, value);
                    }
                })
                .getAsLong();
        assertEquals(10L, value);
    }

    @Test
    public void testExecuteIfPresentOnAbsentValue() {
        OptionalLong.empty()
                .executeIfPresent(new LongConsumer() {
                    @Override
                    public void accept(long value) {
                        fail();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteIfAbsent() {
        OptionalLong.empty()
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException();
                    }
                });
    }

    @Test
    public void testExecuteIfAbsentOnPresentValue() {
        OptionalLong.of(10)
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        fail();
                    }
                });
    }

    @Test
    public void testCustomIntermediate() {
        OptionalLong result = OptionalLong.of(10L)
                .custom(new Function<OptionalLong, OptionalLong>() {
                    @Override
                    public OptionalLong apply(OptionalLong optional) {
                        return optional.filter(Functions.remainderLong(2));
                    }
                });

        assertThat(result, hasValue(10L));
    }

    @Test
    public void testCustomTerminal() {
        Long result = OptionalLong.empty()
                .custom(new Function<OptionalLong, Long>() {
                    @Override
                    public Long apply(OptionalLong optional) {
                        return optional.orElse(0L);
                    }
                });

        assertThat(result, is(0L));
    }

    @Test(expected = NullPointerException.class)
    public void testCustomException() {
        OptionalLong.empty().custom(null);
    }

    @Test
    public void testFilter() {
        OptionalLong result;
        result = OptionalLong.of(4)
                .filter(Functions.remainderLong(2));
        assertThat(result, hasValue(4));

        result = OptionalLong.empty()
                .filter(Functions.remainderLong(2));
        assertThat(result, isEmpty());

        result = OptionalLong.of(9)
                .filter(Functions.remainderLong(2));
        assertThat(result, isEmpty());
    }

    @Test
    public void testFilterNot() {
        OptionalLong result;
        result = OptionalLong.of(4)
                .filterNot(Functions.remainderLong(3));
        assertThat(result, hasValue(4));

        result = OptionalLong.empty()
                .filterNot(Functions.remainderLong(3));
        assertThat(result, isEmpty());

        result = OptionalLong.of(9)
                .filterNot(Functions.remainderLong(3));
        assertThat(result, isEmpty());
    }


    @Test
    public void testMap() {
        final LongUnaryOperator negatorFunction = new LongUnaryOperator() {

            @Override
            public long applyAsLong(long operand) {
                return -operand;
            }
        };

        OptionalLong result;
        result = OptionalLong.empty().map(negatorFunction);
        assertThat(result, isEmpty());

        result = OptionalLong.of(10).map(negatorFunction);
        assertThat(result, hasValue(-10));
    }

    @Test
    public void testMapToInt() {
        final LongToIntFunction mapper = new LongToIntFunction() {

            @Override
            public int applyAsInt(long operand) {
                return (int) (operand / 10000000000L);
            }
        };

        OptionalInt result;
        result = OptionalLong.empty().mapToInt(mapper);
        assertThat(result, OptionalIntMatcher.isEmpty());

        result = OptionalLong.of(100000000000L).mapToInt(mapper);
        assertThat(result, OptionalIntMatcher.hasValue(10));
    }

    @Test
    public void testMapToObj() {
        final LongFunction<String> asciiToString = new LongFunction<String>() {

            @Override
            public String apply(long value) {
                return String.valueOf((char) value);
            }
        };

        Optional<String> result;
        result = OptionalLong.empty().mapToObj(asciiToString);
        assertThat(result, OptionalMatcher.isEmpty());

        result = OptionalLong.of(65).mapToObj(asciiToString);
        assertThat(result, OptionalMatcher.hasValue("A"));

        result = OptionalLong.empty().mapToObj(new LongFunction<String>() {
            @Override
            public String apply(long value) {
                return null;
            }
        });
        assertThat(result, OptionalMatcher.isEmpty());
    }

    @Test
    public void testStream() {
        long count = OptionalLong.of(10).stream().count();
        assertThat(count, is(1L));
    }

    @Test
    public void testStreamOnEmptyOptional() {
        long count = OptionalLong.empty().stream().count();
        assertThat(count, is(0L));
    }

    @Test
    public void testOr() {
        long value = OptionalLong.of(42).or(new Supplier<OptionalLong>() {
            @Override
            public OptionalLong get() {
                return OptionalLong.of(19);
            }
        }).getAsLong();
        assertEquals(42, value);
    }

    @Test
    public void testOrOnEmptyOptional() {
        long value = OptionalLong.empty().or(new Supplier<OptionalLong>() {
            @Override
            public OptionalLong get() {
                return OptionalLong.of(19);
            }
        }).getAsLong();
        assertEquals(19, value);
    }

    @Test
    public void testOrOnEmptyOptionalAndEmptySupplierOptional() {
        final OptionalLong optional = OptionalLong.empty().or(new Supplier<OptionalLong>() {
            @Override
            public OptionalLong get() {
                return OptionalLong.empty();
            }
        });
        assertThat(optional, isEmpty());
    }

    @Test
    public void testOrElse() {
        assertEquals(17, OptionalLong.empty().orElse(17));
        assertEquals(17, OptionalLong.of(17).orElse(0));
    }

    @Test
    public void testOrElseGet() {
        assertEquals(21, OptionalLong.empty().orElseGet(new LongSupplier() {
            @Override
            public long getAsLong() {
                return 21;
            }
        }));

        assertEquals(21, OptionalLong.of(21).orElseGet(new LongSupplier() {
            @Override
            public long getAsLong() {
                throw new IllegalStateException();
            }
        }));
    }

    @Test
    public void testOrElseThrowWithPresentValue() {
        long value = OptionalLong.of(10).orElseThrow();
        assertEquals(10, value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrowOnEmptyOptional() {
        OptionalLong.empty().orElseThrow();
    }

    @Test
    public void testOrElseThrow() {
        try {
            assertEquals(25, OptionalLong.of(25).orElseThrow(new Supplier<NoSuchElementException>() {
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
        assertEquals(25, OptionalLong.empty().orElseThrow(new Supplier<NoSuchElementException>() {
            @Override
            public NoSuchElementException get() {
                return new NoSuchElementException();
            }
        }));
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void testEquals() {
        assertEquals(OptionalLong.empty(), OptionalLong.empty());
        assertNotEquals(OptionalLong.empty(), Optional.empty());

        assertEquals(OptionalLong.of(42), OptionalLong.of(42));

        assertNotEquals(OptionalLong.of(41), OptionalLong.of(42));
        assertNotEquals(OptionalLong.of(0), OptionalLong.empty());
    }

    @Test
    public void testHashCode() {
        assertEquals(OptionalLong.empty().hashCode(), 0);
        assertEquals(31, OptionalLong.of(31).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(OptionalLong.empty().toString(), "OptionalLong.empty");
        assertEquals(OptionalLong.of(42).toString(), "OptionalLong[42]");
    }

}
