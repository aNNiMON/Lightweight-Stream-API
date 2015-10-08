package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.ThrowableSupplier;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 * 
 * @author aNNiMON
 */
public class ExceptionalTest {
    
    @Test
    public void get() {
        int value = Exceptional
                .of(tenSupplier)
                .get();
        assertEquals(10, value);
    }
    
    @Test
    public void getOrElse() {
        int value = Exceptional
                .of(ioExceptionSupplier)
                .getOrElse(20);
        assertEquals(20, value);
    }
    
    @Test
    public void getOptional() {
        Optional<Integer> value = Exceptional
                .of(ioExceptionSupplier)
                .getOptional();
        assertFalse(value.isPresent());
    }
    
    @Test
    public void getException() {
        Throwable throwable = Exceptional
                .of(ioExceptionSupplier)
                .getException();
        assertThat(throwable, instanceOf(IOException.class));
    }
    
    
    @Test(expected = IOException.class)
    public void getOrThrow() throws Throwable {
        int value = Exceptional
                .of(tenSupplier)
                .getOrThrow();
        assertEquals(10, value);
        
        Exceptional
                .of(ioExceptionSupplier)
                .getOrThrow();
    }
    
    @Test(expected = RuntimeException.class)
    public void getOrThrowRuntimeException() throws Throwable {
        int value = Exceptional
                .of(tenSupplier)
                .getOrThrowRuntimeException();
        assertEquals(10, value);
        
        Exceptional
                .of(ioExceptionSupplier)
                .getOrThrowRuntimeException();
    }
    
    @Test(expected = ArithmeticException.class)
    public void getOrThrowNewException() throws Throwable {
        int value = Exceptional
                .of(tenSupplier)
                .getOrThrow(new ArithmeticException());
        assertEquals(10, value);
        
        Exceptional
                .of(ioExceptionSupplier)
                .getOrThrow(new ArithmeticException());
    }
    
    @Test
    public void getOrThrowNewExceptionTestCause() {
        try {
            Exceptional
                    .of(ioExceptionSupplier)
                    .getOrThrow(new ArithmeticException());
        } catch (ArithmeticException ae) {
            assertThat(ae.getCause(), instanceOf(IOException.class));
        }
    }
    
    @Test
    public void ifException() {
        for (final ExceptionType type : ExceptionType.values()) {
            Exceptional
                    .of(new ThrowableSupplier<Integer, Throwable>() {
                        @Override
                        public Integer get() throws Throwable {
                            throwException(type);
                            return 10;
                        }
                    })
                    .ifException(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable value) {
                            assertEquals(type.getException(), value);
                        }
                    });
        }
    }
    
    @Test
    public void ifExceptionIs() {
        final int INTERRUPTED = 0;
        final int EXCEPTION = 1;
        final int FILE_NOT_FOUND = 2;
        final boolean[] data = new boolean[3];
        
        Exceptional
                .of(new ThrowableSupplier<Integer, Throwable>() {
                    @Override
                    public Integer get() throws Throwable {
                        throwException(ExceptionType.INTERRUPTED);
                        return 10;
                    }
                })
                .ifExceptionIs(InterruptedException.class, new Consumer<InterruptedException>() {
                    @Override
                    public void accept(InterruptedException value) {
                        data[INTERRUPTED] = true;
                    }
                })
                .ifExceptionIs(Exception.class, new Consumer<Exception>() {
                    @Override
                    public void accept(Exception value) {
                        data[EXCEPTION] = true;
                    }
                })
                .ifExceptionIs(FileNotFoundException.class, new Consumer<FileNotFoundException>() {
                    @Override
                    public void accept(FileNotFoundException value) {
                        data[FILE_NOT_FOUND] = true;
                    }
                });
        
        assertArrayEquals(new boolean[] { true, true, false }, data);
    }
    
    @Test
    public void equals() {
        final Exceptional<Integer> ten1 = Exceptional.of(tenSupplier);
        final Exceptional<Integer> ten2 = Exceptional.of(tenSupplier);
        final Exceptional<Integer> ten3 = Exceptional.of(tenSupplier);
        final Exceptional<Byte> tenByte = Exceptional.of(new ThrowableSupplier<Byte, Throwable>() {
            @Override
            public Byte get() throws Throwable {
                return (byte) 10;
            }
        });
        final Exceptional<Integer> io = Exceptional.of(ioExceptionSupplier);
        
        assertTrue(ten1.equals(ten1));
        
        assertFalse(ten1.equals(10));
        
        assertTrue(ten1.equals(ten2));
        assertTrue(ten2.equals(ten1));
        assertTrue(ten2.equals(ten3));
        assertTrue(ten1.equals(ten3));
        
        assertFalse(ten1.equals(tenByte));
        
        assertFalse(ten2.equals(io));
    }
    
    @Test
    public void testHashCode() {
        final Exceptional<Integer> ten1 = Exceptional.of(tenSupplier);
        final Exceptional<Integer> ten2 = Exceptional.of(tenSupplier);
        final Exceptional<Byte> tenByte = Exceptional.of(new ThrowableSupplier<Byte, Throwable>() {
            @Override
            public Byte get() throws Throwable {
                return (byte) 10;
            }
        });
        final Exceptional<Integer> io = Exceptional.of(ioExceptionSupplier);
        
        int initial = ten1.hashCode();
        assertEquals(initial, ten1.hashCode());
        assertEquals(initial, ten1.hashCode());
        assertEquals(initial, ten2.hashCode());
        
        assertNotEquals(io.hashCode(), tenByte.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("Exceptional value 10", Exceptional.of(tenSupplier).toString());
        assertEquals("Exceptional throwable java.io.IOException", Exceptional.of(ioExceptionSupplier).toString());
    }
    
    
    private static final ThrowableSupplier<Integer, Throwable> tenSupplier
            = new ThrowableSupplier<Integer, Throwable>() {
        @Override
        public Integer get() throws IOException {
            return 10;
        }
    };
    
    private static final ThrowableSupplier<Integer, Throwable> ioExceptionSupplier
            = new ThrowableSupplier<Integer, Throwable>() {
        @Override
        public Integer get() throws Throwable {
            throwIO();
            return 10;
        }
    };
    
    private static void throwException(ExceptionType type) throws Exception {
        throw type.getException();
    }
    
    private static void throwIO() throws IOException {
        throw new IOException();
    }
    
    private static enum ExceptionType {
        NULL_POINTER(new NullPointerException()),
        UNSUPPORTED_OPERATION(new UnsupportedOperationException()),
        FILE_NOT_FOUND(new FileNotFoundException()),
        INTERRUPTED(new InterruptedException()),
        UNSUPPORTED_ENCODING(new UnsupportedEncodingException()),
        IO(new IOException());
        
        private final Exception exception;

        private ExceptionType(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }
    }
}
