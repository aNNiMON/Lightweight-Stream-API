package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.ToBooleanFunction;
import com.annimon.stream.function.ToDoubleFunction;
import com.annimon.stream.function.ToIntFunction;
import com.annimon.stream.function.ToLongFunction;
import com.annimon.stream.function.UnaryOperator;
import com.annimon.stream.test.hamcrest.OptionalBooleanMatcher;
import com.annimon.stream.test.hamcrest.OptionalDoubleMatcher;
import com.annimon.stream.test.hamcrest.OptionalIntMatcher;
import com.annimon.stream.test.hamcrest.OptionalLongMatcher;
import java.util.NoSuchElementException;
import org.junit.BeforeClass;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isPresent;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

/**
 * Tests {@code Optional}.
 *
 * @see com.annimon.stream.Optional
 */
public final class OptionalTest {

    private static Student student;

    @BeforeClass
    public static void setUpData() {
        student = new Student("Lena", "Art", 3);
    }

    @Test
    public void testGetWithPresentValue() {
        int value = Optional.of(10).get();
        assertEquals(10, value);
    }

    @Test
    public void testGetWithObject() {
        assertEquals("Lena", Optional.of(student).get().getName());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOnEmptyOptional() {
        Optional.empty().get();
    }

    @Test
    public void testIsPresent() {
        assertThat(Optional.of(10), isPresent());
    }

    @Test
    public void testIsPresentOnEmptyOptional() {
        assertThat(Optional.ofNullable(null), isEmpty());
    }

    @Test
    public void testIfPresent() {
        Optional.of(10).ifPresent(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                assertEquals(10, (int) value);
            }
        });
    }

    @Test
    public void testIfPresentOrElseWhenValuePresent() {
        Optional.of(10).ifPresentOrElse(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                assertEquals(10, (int) value);
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
        Optional.<Integer>empty().ifPresentOrElse(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
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
        Optional.of(10).ifPresentOrElse(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                assertEquals(10, (int) value);
            }
        }, null);
    }

    @Test(expected = RuntimeException.class)
    public void testIfPresentOrElseWhenValueAbsentAndConsumerNull() {
        Optional.<Integer>empty().ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValuePresentAndConsumerNull() {
        Optional.of(10).ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                fail("Should not have been executed.");
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValueAbsentAndEmptyActionNull() {
        Optional.<Integer>empty().ifPresentOrElse(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                fail("Should not have been executed.");
            }
        }, null);
    }

    @Test
    public void testExecuteIfPresent() {
        int value = Optional.of(10)
                .executeIfPresent(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer value) {
                        assertEquals(10, (int) value);
                    }
                })
                .get();
        assertEquals(10, value);
    }

    @Test
    public void testExecuteIfPresentOnAbsentValue() {
        Optional.<Integer>empty()
                .executeIfPresent(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer value) {
                        fail();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteIfAbsent() {
        Optional.empty()
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException();
                    }
                });
    }

    @Test
    public void testExecuteIfAbsentOnPresentValue() {
        Optional.of(10)
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        fail();
                    }
                });
    }

    @Test
    public void testCustomIntermediate() {
        Optional<Integer> result = Optional.of(10)
                .custom(new Function<Optional<Integer>, Optional<Integer>>() {
                    @Override
                    public Optional<Integer> apply(Optional<Integer> optional) {
                        return optional.filter(Functions.remainder(2));
                    }
                });

        assertThat(result, hasValue(10));
    }

    @Test
    public void testCustomTerminal() {
        Integer result = Optional.<Integer>empty()
                .custom(new Function<Optional<Integer>, Integer>() {
                    @Override
                    public Integer apply(Optional<Integer> optional) {
                        return optional.orElse(0);
                    }
                });

        assertThat(result, is(0));
    }

    @Test(expected = NullPointerException.class)
    public void testCustomException() {
        Optional.<Integer>empty().custom(null);
    }

    @Test
    public void testFilter() {
        Optional<Integer> result = Optional.of(10)
                .filter(Predicate.Util.negate(Functions.remainder(2)));

        assertThat(result, isEmpty());
    }

    @Test
    public void testFilterNot() {
        Optional<Integer> result = Optional.of(10)
                .filterNot(Functions.remainder(2));

        assertThat(result, isEmpty());
    }

    @Test
    public void testMapOnEmptyOptional() {
        assertFalse(
                Optional.<Integer>empty()
                        .map(UnaryOperator.Util.<Integer>identity())
                        .isPresent());
    }

    @Test
    public void testMapAsciiToString() {
        Optional<String> result = Optional.of(65)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer value) {
                        return String.valueOf((char) value.intValue());
                    }
                });

        assertThat(result, hasValue("A"));
    }

    @Test
    public void testMapToInt() {
        final ToIntFunction<String> firstCharToIntFunction = new ToIntFunction<String>() {

            @Override
            public int applyAsInt(String t) {
                return t.charAt(0);
            }
        };

        OptionalInt result;
        result = Optional.<String>empty()
                .mapToInt(firstCharToIntFunction);
        assertThat(result, OptionalIntMatcher.isEmpty());

        result = Optional.of("A")
                .mapToInt(firstCharToIntFunction);
        assertThat(result, OptionalIntMatcher.hasValue(65));
    }

    @Test
    public void testMapToLong() {
        final ToLongFunction<String> mapper = new ToLongFunction<String>() {
            @Override
            public long applyAsLong(String value) {
                return Long.parseLong(value) * 10000000000L;
            }
        };

        OptionalLong result;
        result = Optional.<String>empty().mapToLong(mapper);
        assertThat(result, OptionalLongMatcher.isEmpty());

        result = Optional.of("65").mapToLong(mapper);
        assertThat(result, OptionalLongMatcher.hasValue(650000000000L));
    }

    @Test
    public void testMapToDouble() {
        final ToDoubleFunction<String> mapper = new ToDoubleFunction<String>() {
            @Override
            public double applyAsDouble(String t) {
                return Double.parseDouble(t) / 100d;
            }
        };

        OptionalDouble result;
        result = Optional.<String>empty().mapToDouble(mapper);
        assertThat(result, OptionalDoubleMatcher.isEmpty());

        result = Optional.of("65").mapToDouble(mapper);
        assertThat(result, OptionalDoubleMatcher.hasValueThat(closeTo(0.65, 0.0001)));
    }

    @Test
    public void testMapToBoolean() {
        final ToBooleanFunction<String> mapper = new ToBooleanFunction<String>() {
            @Override
            public boolean applyAsBoolean(String s) {
                return "true".equalsIgnoreCase(s);
            }
        };

        OptionalBoolean result;
        result = Optional.<String>empty().mapToBoolean(mapper);
        assertThat(result, OptionalBooleanMatcher.isEmpty());

        result = Optional.of("true").mapToBoolean(mapper);
        assertThat(result, OptionalBooleanMatcher.hasValueThat(is(true)));
    }

    @Test
    public void testFlatMapAsciiToString() {
        Optional<String> result = Optional.of(65)
                .flatMap(new Function<Integer, Optional<String>>() {
                    @Override
                    public Optional<String> apply(Integer value) {
                        return Optional.ofNullable(String.valueOf((char) value.intValue()));
                    }
                });

        assertThat(result, hasValue("A"));
    }

    @Test
    public void testFlatMapOnEmptyOptional() {
        Optional<String> result = Optional.<Integer>ofNullable(null)
                .flatMap(new Function<Integer, Optional<String>>() {
                    @Override
                    public Optional<String> apply(Integer value) {
                        return Optional.ofNullable(String.valueOf((char) value.intValue()));
                    }
                });

        assertThat(result, isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testFlatMapWithNullResultFunction() {
        Optional.of(10)
                .flatMap(new Function<Integer, Optional<String>>() {
                    @Override
                    public Optional<String> apply(Integer value) {
                        return null;
                    }
                });
    }

    @Test
    public void testStream() {
        long count = Optional.of(10).stream().count();
        assertThat(count, is(1L));
    }

    @Test
    public void testStreamOnEmptyOptional() {
        long count = Optional.empty().stream().count();
        assertThat(count, is(0L));
    }

    @Test
    public void testSelectOnEmptyOptional() {
        Optional<Integer> result = Optional.empty()
                .select(Integer.class);

        assertThat(result, isEmpty());
    }

    @Test
    public void testSelectValidSubclassOnOptional() {
        Number number = 42;

        Optional<Integer> result = Optional.of(number)
                .select(Integer.class);

        assertThat(result, isPresent());
        assertThat(result, hasValue(42));
    }

    @Test
    public void testSelectInvalidSubclassOnOptional() {
        Number number = 42;

        Optional<String> result = Optional.of(number)
                .select(String.class);

        assertThat(result, isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testSelectWithNullClassOnPresentOptional() {
        Optional.of(42).select(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSelectWithNullClassOnEmptyOptional() {
        Optional.empty().select(null);
    }

    @Test
    public void testOr() {
        int value = Optional.of(42).or(new Supplier<Optional<Integer>>() {
            @Override
            public Optional<Integer> get() {
                return Optional.of(19);
            }
        }).get();
        assertEquals(42, value);
    }

    @Test
    public void testOrOnEmptyOptional() {
        int value = Optional.<Integer>empty().or(new Supplier<Optional<Integer>>() {
            @Override
            public Optional<Integer> get() {
                return Optional.of(19);
            }
        }).get();
        assertEquals(19, value);
    }

    @Test
    public void testOrOnEmptyOptionalAndEmptySupplierOptional() {
        final Optional<Integer> optional = Optional.<Integer>empty().or(new Supplier<Optional<Integer>>() {
            @Override
            public Optional<Integer> get() {
                return Optional.empty();
            }
        });
        assertThat(optional, isEmpty());
    }

    @Test
    public void testOrElseWithPresentValue() {
        int value = Optional.<Integer>empty().orElse(42);
        assertEquals(42, value);
    }

    @Test
    public void testOrElseOnEmptyOptional() {
        assertEquals("Lena", Optional.<Student>empty().orElse(student).getName());
    }

    @Test
    public void testOrElseGet() {
        int value = Optional.<Integer>empty().orElseGet(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 42;
            }
        });
        assertEquals(42, value);
    }

    @Test
    public void testOrElseThrowWithPresentValue() {
        int value = Optional.of(10).orElseThrow();
        assertEquals(10, value);
    }

    @Test
    public void testOrElseThrowWithObject() {
        assertEquals("Lena", Optional.of(student).orElseThrow().getName());
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrowOnEmptyOptional() {
        Optional.empty().orElseThrow();
    }

    @Test(expected = ArithmeticException.class)
    public void testOrElseThrow() {
        Optional.empty().orElseThrow(new Supplier<RuntimeException>() {

            @Override
            public RuntimeException get() {
                return new ArithmeticException();
            }
        });
    }

    @Test
    public void testEqualsReflexive() {
        final Optional<Student> s1 = Optional.of(student);
        assertEquals(s1, s1);
    }

    @Test
    public void testEqualsSymmetric() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Student> s2 = Optional.of(student);

        assertEquals(s1, s2);
        assertEquals(s2, s1);
    }

    @Test
    public void testEqualsTransitive() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Student> s2 = Optional.of(student);
        final Optional<Student> s3 = Optional.of(student);

        assertEquals(s1, s2);
        assertEquals(s2, s3);
        assertEquals(s1, s3);
    }

    @Test
    public void testEqualsWithDifferentTypes() {
        final Optional<Integer> optInt = Optional.of(10);
        assertFalse(optInt.equals(10));
    }

    @Test
    public void testEqualsWithDifferentGenericTypes() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Integer> optInt = Optional.of(10);

        assertNotEquals(s1, optInt);
    }

    @Test
    public void testEqualsWithDifferentNullableState() {
        final Optional<Integer> optInt = Optional.of(10);
        final Optional<Integer> optIntNullable = Optional.ofNullable(10);

        assertEquals(optInt, optIntNullable);
    }

    @Test
    public void testEqualsWithTwoEmptyOptional() {
        final Optional<Integer> empty1 = Optional.ofNullable(null);
        final Optional<Integer> empty2 = Optional.empty();

        assertEquals(empty1, empty2);
    }

    @Test
    public void testHashCodeWithSameObject() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Student> s2 = Optional.of(student);

        int initial = s1.hashCode();
        assertEquals(initial, s1.hashCode());
        assertEquals(initial, s1.hashCode());
        assertEquals(initial, s2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentGenericType() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Integer> optInt = Optional.of(10);

        assertNotEquals(s1.hashCode(), optInt.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentNullableState() {
        final Optional<Integer> optInt = Optional.of(10);
        final Optional<Integer> optIntNullable = Optional.ofNullable(10);

        assertEquals(optInt.hashCode(), optIntNullable.hashCode());
    }

    @Test
    public void testHashCodeWithTwoEmptyOptional() {
        final Optional<Integer> empty1 = Optional.ofNullable(null);
        final Optional<Integer> empty2 = Optional.empty();

        assertEquals(empty1.hashCode(), empty2.hashCode());
    }

    @Test
    public void testToStringOnEmptyOptional() {
        assertEquals("Optional.empty", Optional.empty().toString());
    }

    @Test
    public void testToStringWithPresentValue() {
        assertEquals("Optional[10]", Optional.of(10).toString());
    }
}
