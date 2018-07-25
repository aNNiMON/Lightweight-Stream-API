package com.annimon.stream;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.IntFunction;
import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.function.IntSupplier;
import com.annimon.stream.function.IntToDoubleFunction;
import com.annimon.stream.function.IntToLongFunction;
import com.annimon.stream.function.IntUnaryOperator;
import com.annimon.stream.function.Supplier;
import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a {@code int} value.
 * If a value is present, {@code isPresent()} will return {@code true} and
 * {@code getAsInt()} will return the value.
 */
@SuppressWarnings("WeakerAccess")
public final class OptionalInt {
    /**
     * Common instance for {@code empty()}.
     */
    private static final OptionalInt EMPTY = new OptionalInt();

    /**
     * If true then the value is present, otherwise indicates no value is present
     */
    private final boolean isPresent;
    private final int value;

    /**
     * Construct an empty instance.
     */
    private OptionalInt() {
        this.isPresent = false;
        this.value = 0;
    }

    /**
     * Returns an empty {@code OptionalInt} instance.  No value is present for this
     * OptionalInt.
     *
     * @return an empty {@code OptionalInt}
     */
    public static OptionalInt empty() {
        return EMPTY;
    }

    /**
     * Construct an instance with the value present.
     *
     * @param value the int value to be present
     */
    private OptionalInt(int value) {
        this.isPresent = true;
        this.value = value;
    }

    /**
     * Return an {@code OptionalInt} with the specified value present.
     *
     * @param value the value to be present
     * @return an {@code OptionalInt} with the value present
     */
    public static OptionalInt of(int value) {
        return new OptionalInt(value);
    }

    /**
     * Returns an {@code OptionalInt} with the specified value, or empty {@code OptionalInt} if value is null.
     *
     * @param value the value which can be null
     * @return an {@code OptionalInt}
     * @since 1.2.1
     */
    public static OptionalInt ofNullable(Integer value) {
        return value == null ? EMPTY : new OptionalInt(value);
    }

    /**
     * If a value is present in this {@code OptionalInt}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * Since 1.2.0 prefer {@link #orElseThrow()} method as it has readable name.
     *
     * @return the value held by this {@code OptionalInt}
     * @throws NoSuchElementException if there is no value present
     * @see OptionalInt#isPresent()
     * @see #orElseThrow()
     */
    public int getAsInt() {
        return orElseThrow();
    }

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
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
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is
     *         null
     */
    public void ifPresent(IntConsumer consumer) {
        if (isPresent)
            consumer.accept(value);
    }

    /**
     * If a value is present, performs the given action with the value,
     * otherwise performs the empty-based action.
     *
     * @param consumer  the consumer function to be executed, if a value is present
     * @param emptyAction  the empty-based action to be performed, if no value is present
     * @throws NullPointerException if a value is present and the given consumer function is null,
     *         or no value is present and the given empty-based action is null.
     * @since 1.1.4
     */
    public void ifPresentOrElse(IntConsumer consumer, Runnable emptyAction) {
        if (isPresent) {
            consumer.accept(value);
        } else {
            emptyAction.run();
        }
    }

    /**
     * Invokes consumer function with value if present.
     * This method same as {@code ifPresent}, but does not break chaining
     *
     * @param consumer  consumer function
     * @return this {@code OptionalInt}
     * @see #ifPresent(com.annimon.stream.function.IntConsumer)
     * @since 1.1.2
     */
    public OptionalInt executeIfPresent(IntConsumer consumer) {
        ifPresent(consumer);
        return this;
    }

    /**
     * Invokes action function if value is absent.
     *
     * @param action  action that invokes if value absent
     * @return this {@code OptionalInt}
     * @since 1.1.2
     */
    public OptionalInt executeIfAbsent(Runnable action) {
        if (!isPresent())
            action.run();
        return this;
    }

    /**
     * Applies custom operator on {@code OptionalInt}.
     *
     * @param <R> the type of the result
     * @param function  a transforming function
     * @return a result of the transforming function
     * @throws NullPointerException if {@code function} is null
     * @since 1.1.9
     */
    public <R> R custom(Function<OptionalInt, R> function) {
        Objects.requireNonNull(function);
        return function.apply(this);
    }

    /**
     * Performs filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code OptionalInt} if the value is present and matches predicate,
     *         otherwise an empty {@code OptionalInt}
     * @since 1.1.4
     */
    public OptionalInt filter(IntPredicate predicate) {
        if (!isPresent()) return this;
        return predicate.test(value) ? this : OptionalInt.empty();
    }

    /**
     * Performs negated filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code OptionalInt} if the value is present and doesn't matches predicate,
     *              otherwise an empty {@code OptionalInt}
     * @since 1.1.9
     */
    public OptionalInt filterNot(IntPredicate predicate) {
        return filter(IntPredicate.Util.negate(predicate));
    }

    /**
     * Invokes mapping function on inner value if present.
     *
     * @param mapper  mapping function
     * @return an {@code OptionalInt} with transformed value if present,
     *         otherwise an empty {@code OptionalInt}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     * @since 1.1.3
     */
    public OptionalInt map(IntUnaryOperator mapper) {
        if (!isPresent()) return empty();
        return OptionalInt.of(mapper.applyAsInt(value));
    }

    /**
     * Invokes mapping function on inner value if present.
     *
     * @param <U> the type of result value
     * @param mapper  mapping function
     * @return an {@code Optional} with transformed value if present,
     *         otherwise an empty {@code Optional}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     * @since 1.1.3
     */
    public <U> Optional<U> mapToObj(IntFunction<U> mapper) {
        if (!isPresent()) return Optional.empty();
        return Optional.ofNullable(mapper.apply(value));
    }

    /**
     * Invokes mapping function on inner value if present.
     *
     * @param mapper  mapping function
     * @return an {@code OptionalLong} with transformed value if present,
     *         otherwise an empty {@code OptionalLong}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     * @since 1.1.4
     */
    public OptionalLong mapToLong(IntToLongFunction mapper) {
        if (!isPresent()) return OptionalLong.empty();
        return OptionalLong.of(mapper.applyAsLong(value));
    }

    /**
     * Invokes mapping function on inner value if present.
     *
     * @param mapper  mapping function
     * @return an {@code OptionalDouble} with transformed value if present,
     *         otherwise an empty {@code OptionalDouble}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     * @since 1.1.4
     */
    public OptionalDouble mapToDouble(IntToDoubleFunction mapper) {
        if (!isPresent()) return OptionalDouble.empty();
        return OptionalDouble.of(mapper.applyAsDouble(value));
    }

    /**
     * Wraps a value into {@code IntStream} if present, otherwise returns an empty {@code IntStream}.
     *
     * @return the optional value as an {@code IntStream}
     */
    public IntStream stream() {
        if (!isPresent()) return IntStream.empty();
        return IntStream.of(value);
    }

    /**
     * Returns current {@code OptionalInt} if value is present, otherwise
     * returns an {@code OptionalInt} produced by supplier function.
     *
     * @param supplier  supplier function that produces an {@code OptionalInt} to be returned
     * @return this {@code OptionalInt} if value is present, otherwise
     *         an {@code OptionalInt} produced by supplier function
     * @throws NullPointerException if value is not present and
     *         {@code supplier} or value produced by it is {@code null}
     */
    public OptionalInt or(Supplier<OptionalInt> supplier) {
        if (isPresent()) return this;
        Objects.requireNonNull(supplier);
        return Objects.requireNonNull(supplier.get());
    }

    /**
     * Returns the value if present, otherwise returns {@code other}.
     *
     * @param other  the value to be returned if there is no value present
     * @return the value, if present, otherwise {@code other}
     */
    public int orElse(int other) {
        return isPresent ? value : other;
    }

    /**
     * Returns the value if present, otherwise invokes {@code other} and returns
     * the result of that invocation.
     *
     * @param other a {@code IntSupplier} whose result is returned if no value
     *              is present
     * @return the value if present otherwise the result of {@code other.getAsInt()}
     * @throws NullPointerException if value is not present and {@code other} is
     *         null
     */
    public int orElseGet(IntSupplier other) {
        return isPresent ? value : other.getAsInt();
    }

    /**
     * Returns inner value if present, otherwise throws {@code NoSuchElementException}.
     *
     * @return inner value if present
     * @throws NoSuchElementException if inner value is not present
     * @since 1.2.0
     */
    public int orElseThrow() {
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
    public <X extends Throwable> int orElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isPresent) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Indicates whether some other object is "equal to" this OptionalInt. The
     * other object is considered equal if:
     * <ul>
     *    <li> it is also an {@code OptionalInt} and;
     *    <li> both instances have no value present or;
     *    <li> the present values are "equal to" each other via {@code ==}.
     * </ul>
     *
     * @param obj an object to be tested for equality
     * @return {@code true} if the other object is "equal to" this object
     *         otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(!(obj instanceof OptionalInt)) {
            return false;
        }

        OptionalInt other = (OptionalInt) obj;
        return (isPresent && other.isPresent)
                ? value == other.value
                : isPresent == other.isPresent;
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    @Override
    public int hashCode() {
        return isPresent ? value : 0;
    }

    /**
     * Returns a non-empty string representation of this object suitable for
     * debugging.
     *
     * @return the string representation of this instance
     */
    @Override
    public String toString() {
        return isPresent
                ? String.format("OptionalInt[%s]", value)
                : "OptionalInt.empty";
    }
}
