package com.annimon.stream;

import com.annimon.stream.function.BooleanConsumer;
import com.annimon.stream.function.BooleanFunction;
import com.annimon.stream.function.BooleanPredicate;
import com.annimon.stream.function.BooleanSupplier;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.test.hamcrest.OptionalMatcher;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalBooleanMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalBooleanMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalBooleanMatcher.isPresent;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Tests for {@link OptionalBoolean}
 */
public class OptionalBooleanTest {

    @Test
    public void testGetWithPresentValue() {
        boolean value = OptionalBoolean.of(true).getAsBoolean();
        assertTrue(value);
    }

    @Test
    public void testOfNullableWithPresentValue() {
        assertThat(OptionalBoolean.ofNullable(true), hasValue(true));
    }

    @Test
    public void testOfNullableWithAbsentValue() {
        assertThat(OptionalBoolean.ofNullable(null), isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOnEmptyOptional() {
        OptionalBoolean.empty().getAsBoolean();
    }

    @Test
    public void testIsPresent() {
        assertThat(OptionalBoolean.of(false), isPresent());
    }

    @Test
    public void testIsPresentOnEmptyOptional() {
        assertThat(OptionalBoolean.empty(), isEmpty());
    }

    @Test
    public void testIfPresent() {
        OptionalBoolean.empty().ifPresent(new BooleanConsumer() {
            @Override
            public void accept(boolean value) {
                fail();
            }
        });

        OptionalBoolean.of(true).ifPresent(new BooleanConsumer() {
            @Override
            public void accept(boolean value) {
                assertTrue(value);
            }
        });
    }

    @Test
    public void testIfPresentOrElseWhenValuePresent() {
        OptionalBoolean.of(false).ifPresentOrElse(new BooleanConsumer() {
            @Override
            public void accept(boolean value) {
                assertFalse(value);
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
        OptionalBoolean.empty().ifPresentOrElse(new BooleanConsumer() {
            @Override
            public void accept(boolean value) {
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
        OptionalBoolean.of(true).ifPresentOrElse(new BooleanConsumer() {
            @Override
            public void accept(boolean value) {
                assertTrue(value);
            }
        }, null);
    }

    @Test(expected = RuntimeException.class)
    public void testIfPresentOrElseWhenValueAbsentAndConsumerNull() {
        OptionalBoolean.empty().ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException();
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValuePresentAndConsumerNull() {
        OptionalBoolean.of(false).ifPresentOrElse(null, new Runnable() {
            @Override
            public void run() {
                fail("Should not have been executed.");
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testIfPresentOrElseWhenValueAbsentAndEmptyActionNull() {
        OptionalBoolean.empty().ifPresentOrElse(new BooleanConsumer() {
            @Override
            public void accept(boolean value) {
                fail("Should not have been executed.");
            }
        }, null);
    }

    @Test
    public void testExecuteIfPresent() {
        boolean value = OptionalBoolean.of(true)
                .executeIfPresent(new BooleanConsumer() {
                    @Override
                    public void accept(boolean value) {
                        assertTrue(value);
                    }
                })
                .getAsBoolean();
        assertTrue(value);
    }

    @Test
    public void testExecuteIfPresentOnAbsentValue() {
        OptionalBoolean.empty()
                .executeIfPresent(new BooleanConsumer() {
                    @Override
                    public void accept(boolean value) {
                        fail();
                    }
                });
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteIfAbsent() {
        OptionalBoolean.empty()
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        throw new RuntimeException();
                    }
                });
    }

    @Test
    public void testExecuteIfAbsentOnPresentValue() {
        OptionalBoolean.of(false)
                .executeIfAbsent(new Runnable() {
                    @Override
                    public void run() {
                        fail();
                    }
                });
    }

    @Test
    public void testCustomIntermediate() {
        OptionalBoolean result = OptionalBoolean.of(true)
                .custom(new Function<OptionalBoolean, OptionalBoolean>() {
                    @Override
                    public OptionalBoolean apply(OptionalBoolean optional) {
                        return optional.filter(BooleanPredicate.Util.identity());
                    }
                });

        assertThat(result, hasValue(true));
    }

    @Test
    public void testCustomTerminal() {
        Boolean result = OptionalBoolean.empty()
                .custom(new Function<OptionalBoolean, Boolean>() {
                    @Override
                    public Boolean apply(OptionalBoolean optional) {
                        return optional.orElse(false);
                    }
                });

        assertThat(result, is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testCustomException() {
        OptionalBoolean.empty().custom(null);
    }

    @Test
    public void testFilter() {
        final BooleanPredicate predicate = new BooleanPredicate() {
            @Override
            public boolean test(boolean value) {
                return value || false;
            }
        };
        OptionalBoolean result;
        result = OptionalBoolean.of(true)
                .filter(predicate);
        assertThat(result, hasValue(true));

        result = OptionalBoolean.empty()
                .filter(predicate);
        assertThat(result, isEmpty());

        result = OptionalBoolean.of(false)
                .filter(predicate);
        assertThat(result, isEmpty());
    }

    @Test
    public void testFilterNot() {
        final BooleanPredicate predicate = new BooleanPredicate() {
            @Override
            public boolean test(boolean value) {
                return value || false;
            }
        };
        OptionalBoolean result;
        result = OptionalBoolean.of(false)
                .filterNot(predicate);
        assertThat(result, hasValue(false));

        result = OptionalBoolean.empty()
                .filterNot(predicate);
        assertThat(result, isEmpty());

        result = OptionalBoolean.of(true)
                .filterNot(predicate);
        assertThat(result, isEmpty());
    }


    @Test
    public void testMap() {
        final BooleanPredicate negate = BooleanPredicate.Util.negate(BooleanPredicate.Util.identity());

        OptionalBoolean result;
        result = OptionalBoolean.empty().map(negate);
        assertThat(result, isEmpty());

        result = OptionalBoolean.of(true).map(negate);
        assertThat(result, hasValue(false));

        result = OptionalBoolean.of(false).map(negate);
        assertThat(result, hasValue(true));
    }

    @Test
    public void testMapToObj() {
        final BooleanFunction<String> toString = new BooleanFunction<String>() {

            @Override
            public String apply(boolean value) {
                return Boolean.toString(value);
            }
        };

        Optional<String> result;
        result = OptionalBoolean.empty().mapToObj(toString);
        assertThat(result, OptionalMatcher.isEmpty());

        result = OptionalBoolean.of(false).mapToObj(toString);
        assertThat(result, OptionalMatcher.hasValue("false"));

        result = OptionalBoolean.of(true).mapToObj(toString);
        assertThat(result, OptionalMatcher.hasValue("true"));

        result = OptionalBoolean.empty().mapToObj(new BooleanFunction<String>() {
            @Override
            public String apply(boolean value) {
                return null;
            }
        });
        assertThat(result, OptionalMatcher.isEmpty());
    }

    @Test
    public void testOr() {
        boolean value = OptionalBoolean.of(true).or(new Supplier<OptionalBoolean>() {
            @Override
            public OptionalBoolean get() {
                return OptionalBoolean.of(false);
            }
        }).getAsBoolean();
        assertTrue(value);
    }

    @Test
    public void testOrOnEmptyOptional() {
        boolean value = OptionalBoolean.empty().or(new Supplier<OptionalBoolean>() {
            @Override
            public OptionalBoolean get() {
                return OptionalBoolean.of(false);
            }
        }).getAsBoolean();
        assertFalse(value);
    }

    @Test
    public void testOrOnEmptyOptionalAndEmptySupplierOptional() {
        final OptionalBoolean optional = OptionalBoolean.empty().or(new Supplier<OptionalBoolean>() {
            @Override
            public OptionalBoolean get() {
                return OptionalBoolean.empty();
            }
        });
        assertThat(optional, isEmpty());
    }

    @Test
    public void testOrElse() {
        assertTrue(OptionalBoolean.empty().orElse(true));
        assertFalse(OptionalBoolean.empty().orElse(false));
    }

    @Test
    public void testOrElseGet() {
        assertTrue(OptionalBoolean.empty().orElseGet(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return true;
            }
        }));

        assertTrue(OptionalBoolean.of(true).orElseGet(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                throw new IllegalStateException();
            }
        }));
    }

    @Test
    public void testOrElseThrowWithPresentValue() {
        boolean value = OptionalBoolean.of(true).orElseThrow();
        assertTrue(value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOrElseThrowOnEmptyOptional() {
        OptionalBoolean.empty().orElseThrow();
    }

    @Test
    public void testOrElseThrow() {
        try {
            assertFalse(OptionalBoolean.of(false).orElseThrow(new Supplier<NoSuchElementException>() {
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
        OptionalBoolean.empty().orElseThrow(new Supplier<NoSuchElementException>() {
            @Override
            public NoSuchElementException get() {
                return new NoSuchElementException();
            }
        });
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void testEquals() {
        assertEquals(OptionalBoolean.empty(), OptionalBoolean.empty());
        assertNotEquals(OptionalBoolean.empty(), Optional.empty());

        assertEquals(OptionalBoolean.of(true), OptionalBoolean.of(true));

        assertNotEquals(OptionalBoolean.of(false), OptionalBoolean.of(true));
        assertNotEquals(OptionalBoolean.of(false), OptionalBoolean.empty());
    }

    @Test
    public void testSingleInstance() {
        assertSame(OptionalBoolean.of(true), OptionalBoolean.of(true));
        assertSame(OptionalBoolean.of(true), OptionalBoolean.of(false).map(new BooleanPredicate() {
            @Override
            public boolean test(boolean value) {
                return !value;
            }
        }));
        assertSame(OptionalBoolean.of(false), OptionalBoolean.of(false));
    }

    @Test
    public void testHashCode() {
        assertEquals(OptionalBoolean.empty().hashCode(), 0);
        assertEquals(1231, OptionalBoolean.of(true).hashCode());
        assertEquals(1237, OptionalBoolean.of(false).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(OptionalBoolean.empty().toString(), "OptionalBoolean.empty");
        assertEquals(OptionalBoolean.of(true).toString(), "OptionalBoolean[true]");
    }

}
