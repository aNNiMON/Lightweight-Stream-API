package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a non-null value.
 * 
 * @author aNNiMON
 */
public class Optional<T> {
    
    private static final Optional<?> EMPTY = new Optional();
    
    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }
    
    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? (Optional<T>) EMPTY : of(value);
    }
    
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
    
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }
    
    public boolean isPresent() {
        return value != null;
    }
    
    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);
    }
    
    public Optional<T> filter(Predicate<? super T> predicate) {
        if (!isPresent()) return this;
        return predicate.test(value) ? this : (Optional<T>) EMPTY;
    }
    
    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        if (!isPresent()) return empty();
        return Optional.ofNullable(mapper.apply(value));
    }
    
    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper) {
        if (!isPresent()) return empty();
        return Objects.requireNonNull(mapper.apply(value));
    }
    
    public T orElse(T other) {
        return value != null ? value : other;
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
