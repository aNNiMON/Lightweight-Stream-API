package com.annimon.stream;

import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.IntSupplier;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link OptionalInt}
 */
public class OptionalIntTest {

    @Test
    public void testGetWithPresentValue() {
        int value = OptionalInt.of(10).getAsInt();
        assertEquals(10, value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetOnEmptyOptional() {
        OptionalInt.empty().getAsInt();
    }

    @Test
    public void testIsPresent() {
        assertTrue(OptionalInt.of(10).isPresent());
    }

    @Test
    public void testIsPresentOnEmptyOptional() {
        assertFalse(OptionalInt.empty().isPresent());
    }

    @Test
    public void testIfPresent() {
        OptionalInt.empty().ifPresent(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalStateException();
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

    @Test
    public void testOrElseThrow2() {
        try {
            assertEquals(25, OptionalInt.empty().orElseThrow(new Supplier<NoSuchElementException>() {
                @Override
                public NoSuchElementException get() {
                    return new NoSuchElementException();
                }
            }));
        } catch (Exception ne) {
            assertEquals(NoSuchElementException.class, ne.getClass());
        }
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void testEquals() {
        assertEquals(OptionalInt.empty(), OptionalInt.empty());
        assertFalse(OptionalInt.empty().equals(Optional.empty()));

        assertEquals(OptionalInt.of(42), OptionalInt.of(42));

        assertFalse(OptionalInt.of(41).equals(OptionalInt.of(42)));
        assertFalse(OptionalInt.of(0).equals(OptionalInt.empty()));
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
