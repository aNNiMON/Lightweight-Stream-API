package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.ThrowableFunction;
import com.annimon.stream.function.ThrowableSupplier;

/**
 * A container for values which provided by {@code ThrowableSupplier}.
 * 
 * Stores value which provided by {@link ThrowableSupplier} or an exception which were thrown.
 * 
 * <pre><code>
 *     Exceptional.of(new ThrowableSupplier&lt;String, Throwable&gt;() {
 *          &#64;Override
 *          public String get() throws Throwable {
 *              return IOUtils.read(inputStream);
 *          }
 *      }).ifExceptionIs(IOException.class, new Consumer&lt;IOException&gt;() {
 *          &#64;Override
 *          public void accept(IOException exception) {
 *              logger.log(Level.WARNING, "read file", exception);
 *          }
 *      }).getOrElse("default string");
 *       
 *      Exceptional.of(() -&gt; IOUtils.readBytes(inputStream)).getOrElse(new byte[0]);
 * </code></pre>
 * 
 * @param <T> the type of the inner value
 */
public class Exceptional<T> {
    
    /**
     * Returns an {@code Exceptional} with value provided by given {@code ThrowableSupplier} function.
     * 
     * @param <T> the type of value
     * @param supplier  a supplier function
     * @return an {@code Exceptional}
     */
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
    
    /**
     * Returns inner value.
     * 
     * @return inner value.
     */
    public T get() {
        return value;
    }
    
    /**
     * Returns inner value if there were no exceptions, otherwise returns {@code other}.
     * 
     * @param other  the value to be returned if there were any exception 
     * @return inner value if there were no exceptions, otherwise {@code other}
     */
    public T getOrElse(T other) {
        return throwable == null ? value : other;
    }
    
    /**
     * Wraps inner value with {@code Optional} container
     * 
     * @return an {@code Optional}
     */
    public Optional<T> getOptional() {
        return Optional.ofNullable(value);
    }

    /**
     * Returns exception.
     * 
     * @return exception
     */
    public Throwable getException() {
        return throwable;
    }
    
    /**
     * Returns inner value if there were no exceptions, otherwise throws an exception.
     * 
     * @return inner value if there were no exceptions
     * @throws Throwable that was thrown in supplier function
     */
    public T getOrThrow() throws Throwable {
        if (throwable != null) {
            throw throwable;
        }
        return value;
    }
    
    /**
     * Returns inner value if there were no exceptions, otherwise throws {@code RuntimeException}.
     * 
     * @return inner value if there were no exceptions
     * @throws RuntimeException with wrapped exception which was thrown in supplier function
     */
    public T getOrThrowRuntimeException() throws RuntimeException {
        if (throwable != null) {
            throw new RuntimeException(throwable);
        }
        return value;
    }
    
    /**
     * Returns inner value if there were no exceptions, otherwise throws the given {@code exception}.
     * 
     * @param <E> the type of exception
     * @param exception  an exception to be thrown
     * @return inner value if there were no exceptions
     * @throws E if there were exceptions in supplier function
     */
    public <E extends Throwable> T getOrThrow(E exception) throws E {
        if (throwable != null) {
            exception.initCause(throwable);
            throw exception;
        }
        return value;
    }

    /**
     * Invokes mapping function on inner value if there were no exceptions.
     *
     * @param <U> the type of result value
     * @param mapper  mapping function
     * @return an {@code Exceptional} with transformed value if there were no exceptions
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U> Exceptional<U> map(ThrowableFunction<? super T, ? extends U, Throwable> mapper) {
        if (throwable != null) {
            return new Exceptional<U>(null, throwable);
        }
        Objects.requireNonNull(mapper);
        try {
            return new Exceptional<U>(mapper.apply(value), null);
        } catch (Throwable t) {
            return new Exceptional<U>(null, t);
        }
    }
    
    /**
     * Invokes consumer function if there were any exception.
     * 
     * @param consumer  a consumer function
     * @return an {@code Exceptional}
     */
    public Exceptional<T> ifException(Consumer<Throwable> consumer) {
        if (throwable != null) {
            consumer.accept(throwable);
        }
        return this;
    }
    
    /**
     * Invokes consumer function if exception class matches {@code throwableClass}.
     * 
     * @param <E> the type of exception
     * @param throwableClass  the class of an exception to be compared
     * @param consumer  a consumer function
     * @return an {@code Exceptional}
     */
    @SuppressWarnings("unchecked")
    public <E extends Throwable> Exceptional<T> ifExceptionIs(Class<E> throwableClass, Consumer<? super E> consumer) {
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
