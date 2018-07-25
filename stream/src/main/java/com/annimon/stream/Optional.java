package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.ToDoubleFunction;
import com.annimon.stream.function.ToIntFunction;
import com.annimon.stream.function.ToLongFunction;
import com.annimon.stream.function.ToBooleanFunction;

import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a non-null value.
 *
 * @param <T> the type of the inner value
 */
public class Optional<T> {

    private static final Optional<?> EMPTY = new Optional();

    /**
     * Returns an {@code Optional} with the specified present non-null value.
     *
     * @param <T> the type of value
     * @param value  the value to be present, must be non-null
     * @return an {@code Optional}
     * @throws NullPointerException if value is null
     * @see #ofNullable(java.lang.Object)
     */
    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    /**
     * Returns an {@code Optional} with the specified value, or empty {@code Optional} if value is null.
     *
     * @param <T> the type of value
     * @param value  the value which can be null
     * @return an {@code Optional}
     * @see #of(java.lang.Object)
     */
    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? Optional.<T>empty() : of(value);
    }

    /**
     * Returns an empty {@code Optional}.
     *
     * @param <T> the type of value
     * @return an {@code Optional}
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    private final T value;

    private Optional() {
        this.value = null;
    }

    private Optional(T value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns an inner value if present, otherwise throws {@code NoSuchElementException}.
     *
     * Since 1.2.0 prefer {@link #orElseThrow()} method as it has readable name.
     *
     * @return the inner value of {@code Optional}
     * @throws NoSuchElementException if value is not present
     * @see #orElseThrow()
     */
    public T get() {
        return orElseThrow();
    }

    /**
     * Checks value present.
     *
     * @return {@code true} if a value present, {@code false} otherwise
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Checks the value is not present.
     *
     * @return {@code true} if a value is not present, {@code false} otherwise
     * @since 1.2.1
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * Invokes consumer function with value if present.
     *
     * @param consumer  the consumer function
     */
    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);
    }

    /**
     * If a value is present, performs the given action with the value, otherwise performs the given empty-based action.
     *
     * @param consumer  the consumer function to be executed, if a value is present
     * @param emptyAction  the empty-based action to be performed, if no value is present
     *
     * @throws NullPointerException if a value is present and the given consumer function is null,
     *         or no value is present and the given empty-based action is null.
     */
    public void ifPresentOrElse(Consumer<? super T> consumer, Runnable emptyAction) {
        if (value != null) {
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
     * @return this {@code Optional}
     * @see #ifPresent(com.annimon.stream.function.Consumer)
     * @since 1.1.2
     */
    public Optional<T> executeIfPresent(Consumer<? super T> consumer) {
        ifPresent(consumer);
        return this;
    }

    /**
     * Invokes action function if value is absent.
     *
     * @param action  action that invokes if value absent
     * @return this {@code Optional}
     * @since 1.1.2
     */
    public Optional<T> executeIfAbsent(Runnable action) {
        if (value == null)
            action.run();
        return this;
    }

    /**
     * Applies custom operator on {@code Optional}.
     *
     * @param <R> the type of the result
     * @param function  a transforming function
     * @return a result of the transforming function
     * @throws NullPointerException if {@code function} is null
     * @since 1.1.9
     */
    public <R> R custom(Function<Optional<T>, R> function) {
        Objects.requireNonNull(function);
        return function.apply(this);
    }

    /**
     * Performs filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code Optional} if the value is present and matches predicate,
     *              otherwise an empty {@code Optional}
     */
    public Optional<T> filter(Predicate<? super T> predicate) {
        if (!isPresent()) return this;
        return predicate.test(value) ? this : Optional.<T>empty();
    }

    /**
     * Performs negated filtering on inner value if it is present.
     *
     * @param predicate  a predicate function
     * @return this {@code Optional} if the value is present and doesn't matches predicate,
     *              otherwise an empty {@code Optional}
     * @since 1.1.9
     */
    public Optional<T> filterNot(Predicate<? super T> predicate) {
        return filter(Predicate.Util.negate(predicate));
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
    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        if (!isPresent()) return empty();
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
     * @since 1.1.3
     */
    public OptionalInt mapToInt(ToIntFunction<? super T> mapper) {
        if (!isPresent()) return OptionalInt.empty();
        return OptionalInt.of(mapper.applyAsInt(value));
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
    public OptionalLong mapToLong(ToLongFunction<? super T> mapper) {
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
    public OptionalDouble mapToDouble(ToDoubleFunction<? super T> mapper) {
        if (!isPresent()) return OptionalDouble.empty();
        return OptionalDouble.of(mapper.applyAsDouble(value));
    }

    /**
     * Invokes mapping function on inner value if present.
     *
     * @param mapper  mapping function
     * @return an {@code OptionalBoolean} with transformed value if present,
     *         otherwise an empty {@code OptionalBoolean}
     * @throws NullPointerException if value is present and
     *         {@code mapper} is {@code null}
     */
    public OptionalBoolean mapToBoolean(ToBooleanFunction<? super T> mapper) {
        if (!isPresent()) return OptionalBoolean.empty();
        return OptionalBoolean.of(mapper.applyAsBoolean(value));
    }

    /**
     * Invokes mapping function with {@code Optional} result if value is present.
     *
     * @param <U> the type of result value
     * @param mapper  mapping function
     * @return an {@code Optional} with transformed value if present, otherwise an empty {@code Optional}
     */
    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        if (!isPresent()) return empty();
        return Objects.requireNonNull(mapper.apply(value));
    }

    /**
     * Wraps a value into {@code Stream} if present, otherwise returns an empty {@code Stream}.
     *
     * @return the optional value as a {@code Stream}
     */
    @SuppressWarnings("unchecked")
    public Stream<T> stream() {
        if (!isPresent()) return Stream.empty();
        return Stream.of(value);
    }

    /**
     * Keeps inner value only if is present and instance of given class.
     *
     * @param <R> a type of instance to select.
     * @param clazz a class which instance should be selected
     * @return an {@code Optional} with value of type class if present, otherwise an empty {@code Optional}
     */
    @SuppressWarnings("unchecked")
    public <R> Optional<R> select(Class<R> clazz) {
        Objects.requireNonNull(clazz);
        if (!isPresent()) return empty();
        return (Optional<R>) Optional.ofNullable(clazz.isInstance(value) ? value : null);
    }

    /**
     * Returns current {@code Optional} if value is present, otherwise
     * returns an {@code Optional} produced by supplier function.
     *
     * @param supplier  supplier function that produces an {@code Optional} to be returned
     * @return this {@code Optional} if value is present, otherwise
     *         an {@code Optional} produced by supplier function
     * @throws NullPointerException if value is not present and
     *         {@code supplier} or value produced by it is {@code null}
     */
    public Optional<T> or(Supplier<Optional<T>> supplier) {
        if (isPresent()) return this;
        Objects.requireNonNull(supplier);
        return Objects.requireNonNull(supplier.get());
    }

    /**
     * Returns inner value if present, otherwise returns {@code other}.
     *
     * @param other  the value to be returned if inner value is not present
     * @return inner value if present, otherwise {@code other}
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }

    /**
     * Returns inner value if present, otherwise returns value produced by supplier function.
     *
     * @param other  supplier function that produces value if inner value is not present
     * @return inner value if present, otherwise value produced by supplier function
     */
    public T orElseGet(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    /**
     * Returns inner value if present, otherwise throws {@code NoSuchElementException}.
     *
     * @return inner value if present
     * @throws NoSuchElementException if inner value is not present
     * @since 1.2.0
     */
    public T orElseThrow() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Returns inner value if present, otherwise throws the exception provided by supplier function.
     *
     * @param <X> the type of exception to be thrown
     * @param exc  supplier function that produces an exception to be thrown
     * @return inner value if present
     * @throws X if inner value is not present
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exc) throws X {
        if (value != null) return value;
        else throw exc.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional)) {
            return false;
        }

        Optional<?> other = (Optional<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value != null
            ? String.format("Optional[%s]", value)
            : "Optional.empty";
    }
}
