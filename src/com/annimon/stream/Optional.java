package com.annimon.stream;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import java.util.NoSuchElementException;

/**
 * A container object which may or may not contain a non-null value.
 * 
 * @author aNNiMON
 */
public class Optional {
    
    private static final Optional EMPTY = new Optional();
    
    public static  Optional of(Object value) {
        return new Optional(value);
    }
    
    public static  Optional ofNullable(Object value) {
        return value == null ? (Optional) EMPTY : of(value);
    }
    
    public static  Optional empty() {
        return (Optional) EMPTY;
    }
    
    private final Object value;

    private Optional() {
        this.value = null;
    }
    
    private Optional(Object value) {
        this.value = Objects.requireNonNull(value);
    }
    
    public Object get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }
    
    public boolean isPresent() {
        return value != null;
    }
    
    public void ifPresent(Consumer consumer) {
        if (value != null)
            consumer.accept(value);
    }
    
    public Optional filter(Predicate predicate) {
        if (!isPresent()) return this;
        return predicate.test(value) ? this : (Optional) EMPTY;
    }
    
    public Optional map(Function mapper) {
        if (!isPresent()) return empty();
        return Optional.ofNullable(mapper.apply(value));
    }
    
    public Optional flatMap(Function mapper) {
        if (!isPresent()) return empty();
        return (Optional) Objects.requireNonNull(mapper.apply(value));
    }
    
    public Object orElse(Object other) {
        return value != null ? value : other;
    }
    
    public Object orElseGet(Supplier other) {
        return value != null ? value : other.get();
    }
    
    public Object orElseThrow(Supplier exc) throws Throwable {
        if (value != null) return value;
        else throw (Throwable) exc.get();
    }
    
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional)) {
            return false;
        }

        Optional other = (Optional) obj;
        return Objects.equals(value, other.value);
    }
    
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    public String toString() {
        return value != null
            ? "Optional[ " + value + "]"
            : "Optional.empty";
    }
}
