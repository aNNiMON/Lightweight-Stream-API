package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.UnaryOperator;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author aNNiMON
 */
public final class OptionalTest {
    
    private static final Student student = new Student("Lena", "Art", 3);
    
    @Test(expected = NoSuchElementException.class)
    public void get() {
        int value = Optional.of(10).get();
        assertEquals(10, value);
        
        assertEquals("Lena", Optional.of(student).get().getName());
        
        Optional.empty().get();
    }
    
    @Test
    public void isPresent() {
        assertTrue(Optional.of(10).isPresent());
        assertFalse(Optional.ofNullable(null).isPresent());
    }
    
    @Test
    public void ifPresent() {
        Optional.of(10).ifPresent(new Consumer<Integer>() {
            @Override
            public void accept(Integer value) {
                assertEquals(10, (int) value);
            }
        });
    }
    
    @Test
    public void filter() {
        assertFalse(
                Optional.of(10).filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer value) {
                        return value % 2 != 0;
                    }
                })
                .isPresent()
        );
    }
    
    @Test
    public void map() {
        assertEquals("A",
                Optional.of(65).map(new Function<Integer, String>() {
                @Override
                    public String apply(Integer value) {
                        return String.valueOf((char) value.intValue());
                    }
                })
                .get()
        );
        assertFalse(
                Optional.<Integer>empty()
                        .map(UnaryOperator.Util.<Integer>identity())
                        .isPresent());
    }
    
    @Test(expected = NullPointerException.class)
    public void flatMap() {
        assertEquals("A",
                Optional.of(65).flatMap(new Function<Integer, Optional<String>>() {
                    @Override
                    public Optional<String> apply(Integer value) {
                        return Optional.ofNullable(String.valueOf((char) value.intValue()));
                    }
                })
                .get()
        );
        
        assertFalse(
                Optional.<Integer>ofNullable(null)
                        .flatMap(new Function<Integer, Optional<String>>() {
                            @Override
                            public Optional<String> apply(Integer value) {
                                return Optional.ofNullable(String.valueOf((char) value.intValue()));
                            }
                        })
                        .isPresent());
        
        Optional.of(10)
                .flatMap(new Function<Integer, Optional<String>>() {
                    @Override
                    public Optional<String> apply(Integer value) {
                        return null;
                    }
                });
    }
    
    @Test
    public void orElse() {
        int value = Optional.<Integer>empty().orElse(42);
        assertEquals(42, value);
        
        assertEquals("Lena", Optional.<Student>empty().orElse(student).getName());
    }
    
    @Test
    public void orElseGet() {
        int value = Optional.<Integer>empty().orElseGet(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 42;
            }
        });
        assertEquals(42, value);
    }
    
    @Test(expected = ArithmeticException.class)
    public void orElseThrow() {
        Optional.empty().orElseThrow(new Supplier<RuntimeException>() {

            @Override
            public RuntimeException get() {
                return new ArithmeticException();
            }
        });
    }
    
    @Test
    public void equals() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Student> s2 = Optional.of(student);
        final Optional<Student> s3 = Optional.of(student);
        final Optional<Integer> empty1 = Optional.ofNullable(null);
        final Optional<Integer> empty2 = Optional.empty();
        final Optional<Integer> optInt = Optional.of(10);
        final Optional<Integer> optIntNullable = Optional.ofNullable(10);
        
        assertTrue(s1.equals(s1));
        
        assertFalse(s1.equals(10));
        
        assertTrue(s1.equals(s2));
        assertTrue(s2.equals(s1));
        assertTrue(s2.equals(s3));
        assertTrue(s1.equals(s3));
        
        assertFalse(s1.equals(optInt));
        assertTrue(optInt.equals(optIntNullable));
        
        assertTrue(empty1.equals(empty2));
    }
    
    @Test
    public void testHashCode() {
        final Optional<Student> s1 = Optional.of(student);
        final Optional<Student> s2 = Optional.of(student);
        final Optional<Integer> empty1 = Optional.ofNullable(null);
        final Optional<Integer> empty2 = Optional.empty();
        final Optional<Integer> optInt = Optional.of(10);
        final Optional<Integer> optIntNullable = Optional.ofNullable(10);
        
        int initial = s1.hashCode();
        assertEquals(initial, s1.hashCode());
        assertEquals(initial, s1.hashCode());
        assertEquals(initial, s2.hashCode());
        
        assertEquals(optInt.hashCode(), optIntNullable.hashCode());
        
        assertNotEquals(s1.hashCode(), optInt.hashCode());
        
        assertEquals(empty1.hashCode(), empty2.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("Optional.empty", Optional.empty().toString());
        assertEquals("Optional[10]", Optional.of(10).toString());
    }
}
