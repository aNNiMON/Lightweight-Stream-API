package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.ThrowableSupplier;

/**
 * A container for values which received from {@link ThrowableSupplier}.
 * 
 * @author aNNiMON
 * @param <T> the type of the value
 */
public class Exceptional<T> {
    
    public static <T> Exceptional<T> of(ThrowableSupplier<T, Throwable> supplier) {
        try {
            return new Exceptional<T>(supplier.get(), null);
        } catch (Throwable throwable) {
            return new Exceptional<T>(null, throwable);
        }
    }
    
    private final T value;
    private final Throwable throwable;

    private Exceptional(T value, Throwable throwable) {
        this.value = value;
        this.throwable = throwable;
    }
    
    public T get() {
        return value;
    }
    
    public T getOrElse(T other) {
        return throwable == null ? value : other;
    }
    
    public Optional<T> getOptional() {
        return Optional.ofNullable(value);
    }

    public Throwable getException() {
        return throwable;
    }
    
    public T getOrThrow() throws Throwable {
        if (throwable != null) {
            throw throwable;
        }
        return value;
    }
    
    public T getOrThrowRuntimeException() throws RuntimeException {
        if (throwable != null) {
            throw new RuntimeException(throwable);
        }
        return value;
    }
    
    public <E extends Throwable> T getOrThrow(E exception) throws E {
        if (throwable != null) {
            exception.initCause(throwable);
            throw exception;
        }
        return value;
    }
    
    public Exceptional<T> ifException(Consumer<Throwable> consumer) {
        if (throwable != null) {
            consumer.accept(throwable);
        }
        return this;
    }
    
    public <E extends Throwable> Exceptional<T> ifExceptionIs(Class<E> throwableClass, Consumer<E> consumer) {
        if ( (throwable != null) &&
                (throwableClass.isAssignableFrom(throwable.getClass())) ) {
            consumer.accept((E) throwable);
        }
        return this;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Exceptional)) {
            return false;
        }

        Exceptional<?> other = (Exceptional<?>) obj;
        return Objects.equals(value, other.value) &&
                Objects.equals(throwable, other.throwable);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, throwable);
    }
    
    @Override
    public String toString() {
        return throwable == null
            ? String.format("Exceptional value %s", value)
            : String.format("Exceptional throwable %s", throwable);
    }
}
