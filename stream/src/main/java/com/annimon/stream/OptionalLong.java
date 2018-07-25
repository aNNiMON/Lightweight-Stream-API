package com.annimon.stream;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.LongConsumer;
import com.annimon.stream.function.LongFunction;
import com.annimon.stream.function.LongPredicate;
import com.annimon.stream.function.LongSupplier;
import com.annimon.stream.function.LongToIntFunction;
import com.annimon.stream.function.LongUnaryOperator;
import com.annimon.stream.function.Supplier;
import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a {@code long} value.
 *
 * @since 1.1.4
 * @see Optional
 */
@SuppressWarnings("WeakerAccess")
public final class OptionalLong {

    private static final OptionalLong EMPTY = new OptionalLong();

    /**
     * Returns an empty {@code OptionalLong} instance.
     *
     * @return an empty {@code OptionalLong}
     */
    public static OptionalLong empty() {
        return EMPTY;
    }

    /**
     * Returns an {@code OptionalLong} with the specified value present.
     *
     * @param value  the value to be present
     * @return an {@code OptionalLong} with the value present
     */
    public static OptionalLong of(long value) {
        return new OptionalLong(value);
    }

    /**
     * Returns an {@code OptionalLong} with the specified value, or empty {@code OptionalLong} if value is null.
     *
     * @param value the value which can be null
     * @return an {@code OptionalLong}
     * @since 1.2.1
     */
    public static OptionalLong ofNullable(Long value) {
        return value == null ? EMPTY : new OptionalLong(value);
    }

    private final boolean isPresent;
    private final long value;

    private OptionalLong() {
        this.isPresent = false;
        this.value = 0;
    }

    private OptionalLong(long value) {
        this.isPresent = true;
        this.value = value;
    }

    /**
     * Returns an inner value if present, otherwise throws {@code NoSuchElementException}.
     *
     * Since 1.2.0 prefer {@link #orElseThrow()} method as it has readable name.
     *
     * @return the inner value of this {@code OptionalLong}
     * @throws NoSuchElementException if there is no value present
     * @see OptionalLong#isPresent()
     * @see #orElseThrow()
     */
    public long getAsLong() {
        return orElseThrow();
    }

    /**
     * Checks value present.
     *
     * @return {@code true} if a value present, {@code false} otherwise
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * Checks the value is not present.
     *
     * @return {@code true} if a value is not present, {@code false} otherwise
     * @since 1.2.1
     */
    public boolean isEmpty() {
        return !isPresent;
    }

    /**
     * Invokes consumer function with value if present, otherwise does nothing.
     *
     * @param consumer  the consumer function to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is null
     */
    public void ifPresent(LongConsumer consumer) {
        if (isPresent) {
            consumer.accept(value);
        }
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise performs the empty-based action.
     *
     * @param consumer  the consumer function to be executed, if a value is present
     * @param emptyAction  the empty-based action to be performed, if no value is present
     * @throws NullPointerException if a value is present and the given consumer function is null,
     *         or no value is present and the given empty-based action is null.
     */
    public void ifPresentOrElse(LongConsumer consumer, Runnable emptyAction) {
        if (isPresent) {
            consumer.accept(value);
        } else {
            emptyAction.run();
        }
    }

    /**
     * Invokes consumer function with the value if present.
     * This method same as {@code ifPresent}, but does not breaks chaining
     *
     * @param consumer  consumer function
     * @return this {@code OptionalLong}
     * @see #ifPresent(com.annimon.stream.function.LongConsumer)
     */
    public OptionalLong executeIfPresent(LongConsumer consumer) {
        ifPresent(consumer);
        return this;
    }

    /**
     * Invokes action function if value is absent.
     *
     * @param action  action that invokes if value absent
     * @return this {@code OptionalLong}
     */
    public OptionalLong executeIfAbsent(Runnable action) {
        if (!isPresent()) {
            action.run();
        }
        return this;
    }

    /**
     * Applies custom operator on {@code OptionalLong}.
     *
     * @param <R> the type of the result
     * @param function  a transforming function
     * @return a result of the transforming function
     * @throws NullPointerException if {@code function} is null
     * @since 1.1.9
     */
    public <R> R custom(Function<OptionalLong, R> function) {
        Objects.requireNonNull(function);
        return function.apply(this);
    }

    /**
     * Performs filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code OptionalLong} if the value is present and matches predicate,
     *         otherwise an empty {@code OptionalLong}
     */
    public OptionalLong filter(LongPredicate predicate) {
        if (!isPresent()) return this;
        return predicate.test(value) ? this : OptionalLong.empty();
    }

    /**
     * Performs negated filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code OptionalLong} if the value is present and doesn't matches predicate,
     *              otherwise an empty {@code OptionalLong}
     * @since 1.1.9
     */
    public OptionalLong filterNot(LongPredicate predicate) {
        return filter(LongPredicate.Util.negate(predicate));
    }

    /**
     * Invokes the given mapping function on inner value if present.
     *
     * @param mapper  mapping function
     * @return an {@code OptionalLong} with transformed value if present,
     *         otherwise an empty {@code OptionalLong}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     */
    public OptionalLong map(LongUnaryOperator mapper) {
        if (!isPresent()) {
            return empty();
        }
        Objects.requireNonNull(mapper);
        return OptionalLong.of(mapper.applyAsLong(value));
    }

    /**
     * Invokes the given mapping function on inner value if present.
     *
     * @param <U> the type of result value
     * @param mapper  mapping function
     * @return an {@code Optional} with transformed value if present,
     *         otherwise an empty {@code Optional}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     */
    public <U> Optional<U> mapToObj(LongFunction<U> mapper) {
        if (!isPresent()) {
            return Optional.empty();
        }
        Objects.requireNonNull(mapper);
        return Optional.ofNullable(mapper.apply(value));
    }

    /**
     * Invokes the given mapping function on inner value if present.
     *
     * @param mapper  mapping function
     * @return an {@code OptionalInt} with transformed value if present,
     *         otherwise an empty {@code OptionalInt}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     */
    public OptionalInt mapToInt(LongToIntFunction mapper) {
        if (!isPresent()) {
            return OptionalInt.empty();
        }
        Objects.requireNonNull(mapper);
        return OptionalInt.of(mapper.applyAsInt(value));
    }

    /**
     * Wraps a value into {@code LongStream} if present,
     * otherwise returns an empty {@code LongStream}.
     *
     * @return the optional value as an {@code LongStream}
     */
    public LongStream stream() {
        if (!isPresent()) {
            return LongStream.empty();
        }
        return LongStream.of(value);
    }

    /**
     * Returns current {@code OptionalLong} if value is present, otherwise
     * returns an {@code OptionalLong} produced by supplier function.
     *
     * @param supplier  supplier function that produces an {@code OptionalLong} to be returned
     * @return this {@code OptionalLong} if value is present, otherwise
     *         an {@code OptionalLong} produced by supplier function
     * @throws NullPointerException if value is not present and
     *         {@code supplier} or value produced by it is {@code null}
     */
    public OptionalLong or(Supplier<OptionalLong> supplier) {
        if (isPresent()) return this;
        Objects.requireNonNull(supplier);
        return Objects.requireNonNull(supplier.get());
    }

    /**
     * Returns inner value if present, otherwise returns {@code other}.
     *
     * @param other  the value to be returned if there is no value present
     * @return the value, if present, otherwise {@code other}
     */
    public long orElse(long other) {
        return isPresent ? value : other;
    }

    /**
     * Returns the value if present, otherwise returns value produced by supplier function.
     *
     * @param other  supplier function that produces value if inner value is not present
     * @return the value if present otherwise the result of {@code other.getAsLong()}
     * @throws NullPointerException if value is not present and {@code other} is null
     */
    public long orElseGet(LongSupplier other) {
        return isPresent ? value : other.getAsLong();
    }

    /**
     * Returns inner value if present, otherwise throws {@code NoSuchElementException}.
     *
     * @return inner value if present
     * @throws NoSuchElementException if inner value is not present
     * @since 1.2.0
     */
    public long orElseThrow() {
        if (!isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Returns the value if present, otherwise throws an exception provided by supplier function.
     *
     * @param <X> the type of exception to be thrown
     * @param exceptionSupplier  supplier function that produces an exception to be thrown
     * @return inner value if present
     * @throws X if inner value is not present
     */
    public <X extends Throwable> long orElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OptionalLong)) {
            return false;
        }

        OptionalLong other = (OptionalLong) obj;
        return (isPresent && other.isPresent)
                ? value == other.value
                : isPresent == other.isPresent;
    }

    @Override
    public int hashCode() {
        return isPresent ? Objects.hashCode(value) : 0;
    }

    @Override
    public String toString() {
        return isPresent
                ? String.format("OptionalLong[%s]", value)
                : "OptionalLong.empty";
    }
}
