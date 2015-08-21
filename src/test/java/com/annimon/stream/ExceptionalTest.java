package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.ThrowableSupplier;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * 
 * @author aNNiMON
 */
public class ExceptionalTest {

    @Test
    public void get() {
        int value = Exceptional
                .of(new ThrowableSupplier<Integer, Throwable>() {
                    @Override
                    public Integer get() throws Throwable {
                        return 10;
                    }
                })
                .get();
        assertEquals(10, value);
    }
    
    @Test(expected = IOException.class)
    public void getOrThrow() throws Throwable {
        int value = Exceptional
                .of(new ThrowableSupplier<Integer, Throwable>() {
                    @Override
                    public Integer get() throws IOException {
                        throwIO();
                        return 10;
                    }
                })
                .getOrThrow();
    }
    
    @Test(expected = RuntimeException.class)
    public void getOrThrowRuntimeException() throws Throwable {
        int value = Exceptional
                .of(new ThrowableSupplier<Integer, Throwable>() {
                    @Override
                    public Integer get() throws IOException {
                        throwIO();
                        return 10;
                    }
                })
                .getOrThrowRuntimeException();
    }
    
    @Test(expected = ArithmeticException.class)
    public void getOrThrowNewException() throws Throwable {
        int value = Exceptional
                .of(new ThrowableSupplier<Integer, Throwable>() {
                    @Override
                    public Integer get() throws IOException {
                        throwIO();
                        return 10;
                    }
                })
                .getOrThrow(new ArithmeticException());
    }
    
    @Test
    public void getOrThrowNewExceptionTestCause() {
        try {
            Exceptional
                    .of(new ThrowableSupplier<Integer, Throwable>() {
                        @Override
                        public Integer get() throws IOException {
                            throwIO();
                            return 10;
                        }
                    })
                    .getOrThrow(new ArithmeticException());
        } catch (ArithmeticException ae) {
            assertTrue(ae.getCause() instanceof IOException);
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
