package com.annimon.stream;

import com.annimon.stream.function.BooleanConsumer;
import com.annimon.stream.function.BooleanFunction;
import com.annimon.stream.function.BooleanPredicate;
import com.annimon.stream.function.BooleanSupplier;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a {@code boolean} value.
 *
 * @since 1.1.8
 * @see Optional
 */
@SuppressWarnings("WeakerAccess")
public final class OptionalBoolean {

    private static final OptionalBoolean EMPTY = new OptionalBoolean();
    private static final OptionalBoolean TRUE = new OptionalBoolean(true);
    private static final OptionalBoolean FALSE = new OptionalBoolean(false);

    /**
     * Returns an empty {@code OptionalBoolean} instance.
     *
     * @return an empty {@code OptionalBoolean}
     */
    public static OptionalBoolean empty() {
        return EMPTY;
    }

    /**
     * Returns an {@code OptionalBoolean} with the specified value present.
     *
     * @param value  the value to be present
     * @return an {@code OptionalBoolean} with the value present
     */
    public static OptionalBoolean of(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Returns an {@code OptionalBoolean} with the specified value, or empty {@code OptionalBoolean} if value is null.
     *
     * @param value  the value which can be null
     * @return an {@code OptionalBoolean}
     * @since 1.2.1
     */
    public static OptionalBoolean ofNullable(Boolean value) {
        return value == null ? EMPTY : of(value);
    }

    private final boolean isPresent;
    private final boolean value;

    private OptionalBoolean() {
        this.isPresent = false;
        this.value = false;
    }

    private OptionalBoolean(boolean value) {
        this.isPresent = true;
        this.value = value;
    }

    /**
     * Returns an inner value if present, otherwise throws {@code NoSuchElementException}.
     *
     * Since 1.2.0 prefer {@link #orElseThrow()} method as it has readable name.
     *
     * @return the inner value of this {@code OptionalBoolean}
     * @throws NoSuchElementException if there is no value present
     * @see OptionalBoolean#isPresent()
     * @see #orElseThrow()
     */
    public boolean getAsBoolean() {
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
    public void ifPresent(BooleanConsumer consumer) {
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
    public void ifPresentOrElse(BooleanConsumer consumer, Runnable emptyAction) {
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
     * @return this {@code OptionalBoolean}
     * @see #ifPresent(BooleanConsumer)
     */
    public OptionalBoolean executeIfPresent(BooleanConsumer consumer) {
        ifPresent(consumer);
        return this;
    }

    /**
     * Invokes action function if value is absent.
     *
     * @param action  action that invokes if value absent
     * @return this {@code OptionalBoolean}
     */
    public OptionalBoolean executeIfAbsent(Runnable action) {
        if (!isPresent()) {
            action.run();
        }
        return this;
    }

    /**
     * Applies custom operator on {@code OptionalBoolean}.
     *
     * @param <R> the type of the result
     * @param function  a transforming function
     * @return a result of the transforming function
     * @throws NullPointerException if {@code function} is null
     * @since 1.1.9
     */
    public <R> R custom(Function<OptionalBoolean, R> function) {
        Objects.requireNonNull(function);
        return function.apply(this);
    }

    /**
     * Performs filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code OptionalBoolean} if the value is present and matches predicate,
     *         otherwise an empty {@code OptionalBoolean}
     */
    public OptionalBoolean filter(BooleanPredicate predicate) {
        if (!isPresent()) return this;
        return predicate.test(value) ? this : OptionalBoolean.empty();
    }

    /**
     * Performs negated filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code OptionalBoolean} if the value is present and doesn't matches predicate,
     *              otherwise an empty {@code OptionalBoolean}
     * @since 1.1.9
     */
    public OptionalBoolean filterNot(BooleanPredicate predicate) {
        return filter(BooleanPredicate.Util.negate(predicate));
    }

    /**
     * Invokes the given mapping function on inner value if present.
     *
     * @param mapper  mapping function
     * @return an {@code OptionalBoolean} with transformed value if present,
     *         otherwise an empty {@code OptionalBoolean}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     */
    public OptionalBoolean map(BooleanPredicate mapper) {
        if (!isPresent()) {
            return empty();
        }
        Objects.requireNonNull(mapper);
        return OptionalBoolean.of(mapper.test(value));
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
    public <U> Optional<U> mapToObj(BooleanFunction<U> mapper) {
        if (!isPresent()) {
            return Optional.empty();
        }
        Objects.requireNonNull(mapper);
        return Optional.ofNullable(mapper.apply(value));
    }

    /**
     * Returns current {@code OptionalBoolean} if value is present, otherwise
     * returns an {@code OptionalBoolean} produced by supplier function.
     *
     * @param supplier  supplier function that produces an {@code OptionalBoolean} to be returned
     * @return this {@code OptionalBoolean} if value is present, otherwise
     *         an {@code OptionalBoolean} produced by supplier function
     * @throws NullPointerException if value is not present and
     *         {@code supplier} or value produced by it is {@code null}
     */
    public OptionalBoolean or(Supplier<OptionalBoolean> supplier) {
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
    public boolean orElse(boolean other) {
        return isPresent ? value : other;
    }

    /**
     * Returns the value if present, otherwise returns value produced by supplier function.
     *
     * @param other  supplier function that produces value if inner value is not present
     * @return the value if present otherwise the result of {@code other.getAsBoolean()}
     * @throws NullPointerException if value is not present and {@code other} is null
     */
    public boolean orElseGet(BooleanSupplier other) {
        return isPresent ? value : other.getAsBoolean();
    }

    /**
     * Returns inner value if present, otherwise throws {@code NoSuchElementException}.
     *
     * @return inner value if present
     * @throws NoSuchElementException if inner value is not present
     * @since 1.2.0
     */
    public boolean orElseThrow() {
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
    public <X extends Throwable> boolean orElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OptionalBoolean)) {
            return false;
        }

        OptionalBoolean other = (OptionalBoolean) obj;
        return (isPresent && other.isPresent)
                ? value == other.value
                : isPresent == other.isPresent;
    }

    @Override
    public int hashCode() {
        return isPresent ? (value ? 1231 : 1237) : 0;
    }

    @Override
    public String toString() {
        return isPresent
                ? (value ? "OptionalBoolean[true]" : "OptionalBoolean[false]")
                : "OptionalBoolean.empty";
    }
}
