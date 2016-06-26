package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.UnaryOperator;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isPresent;
import java.util.NoSuchElementException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

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
    public void testFilter() {
        Optional<Integer> result = Optional.of(10)
                .filter(Predicate.Util.negate(Functions.remainder(2)));

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

        assertThat(result.get(), is("A"));
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
        
        assertThat(result.get(), is("A"));
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
    public void testSelectOnEmptyOptional() {
        Optional<Integer> result = Optional.empty()
                .select(Integer.class);

        assertFalse(result.isPresent());
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void testSelectValidSubclassOnOptional() {
        Number number = new Integer(42);

        Optional<Integer> result = Optional.of(number)
                .select(Integer.class);

        assertTrue(result.isPresent());
        assertEquals(result.get().intValue(), 42);
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void testSelectInvalidSubclassOnOptional() {
        Number number = new Integer(42);

        Optional<String> result = Optional.of(number)
                .select(String.class);

        assertFalse(result.isPresent());
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
        assertTrue(s1.equals(s1));
    }
    
    @Test
    public void testEqualsSymmetric() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Student> s2 = Optional.of(student);
        
        assertTrue(s1.equals(s2));
        assertTrue(s2.equals(s1));
    }
    
    @Test
    public void testEqualsTransitive() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Student> s2 = Optional.of(student);
        final Optional<Student> s3 = Optional.of(student);
        
        assertTrue(s1.equals(s2));
        assertTrue(s2.equals(s3));
        assertTrue(s1.equals(s3));
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
        
        assertFalse(s1.equals(optInt));
    }
    
    @Test
    public void testEqualsWithDifferentNullableState() {
        final Optional<Integer> optInt = Optional.of(10);
        final Optional<Integer> optIntNullable = Optional.ofNullable(10);
        
        assertTrue(optInt.equals(optIntNullable));
    }
    
    @Test
    public void testEqualsWithTwoEmptyOptional() {
        final Optional<Integer> empty1 = Optional.ofNullable(null);
        final Optional<Integer> empty2 = Optional.empty();
        
        assertTrue(empty1.equals(empty2));
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
