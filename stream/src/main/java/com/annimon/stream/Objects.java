package com.annimon.stream;

import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * Common operations with Object.
 */
public final class Objects {

    private Objects() { }

    /**
     * Checks equality of two objects.
     *
     * @param a  an object
     * @param b  an object
     * @return {@code true} if objects are equals, {@code false} otherwise
     */
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    /**
     * Checks deep equality of two objects.
     *
     * @param a  an object
     * @param b  an object
     * @return {@code true} if objects are deeply equals, {@code false} otherwise
     * @see Arrays#deepEquals(Object[], Object[])
     * @see Objects#equals(Object, Object)
     * @since 1.2.0
     */
    public static boolean deepEquals(Object a, Object b) {
        return (a == b)
                || (a != null && b != null)
                && Arrays.deepEquals(new Object[] { a }, new Object[] { b });
    }

    /**
     * Returns the hash code of object.
     *
     * @param o  an object
     * @return the hash code
     */
    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    /**
     * Returns the hash code for objects.
     *
     * @param values  the values
     * @return the hash code
     */
    public static int hash(Object... values) {
        if (values == null) return 0;

        int result = 1;
        for (Object element : values)
            result = 31 * result + hashCode(element);
        return result;
    }

    /**
     * Returns result of calling {@code toString} on object or {@code nullDefault} if object is null.
     *
     * @param o  an object
     * @param nullDefault  a string to return if object is null
     * @return a result of calling {@code toString} on object or {@code nullDefault} if object is null.
     */
    public static String toString(Object o, String nullDefault) {
        return (o != null) ? o.toString() : nullDefault;
    }

    /**
     * Compares two objects with provided comparator.
     *
     * @param <T> the type of the arguments
     * @param a  an object
     * @param b  an object
     * @param c  the comparator
     * @return comparing result
     */
    public static <T> int compare(T a, T b, Comparator<? super T> c) {
        return (a == b) ? 0 : c.compare(a, b);
    }

    /**
     * Compares two {@code int} values.
     *
     * @param x  the first {@code int} value
     * @param y  the second {@code int} value
     * @return comparing result
     * @since 1.1.6
     */
    public static int compareInt(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * Compares two {@code long} values.
     *
     * @param x  the first {@code long} value
     * @param y  the second {@code long} value
     * @return comparing result
     * @since 1.1.6
     */
    public static int compareLong(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * Checks that object reference is not {@code null}.
     *
     * @param <T> the type of the object
     * @param obj  an object
     * @return a source object if it is not {@code null}
     * @throws NullPointerException if object is {@code null}
     * @see #requireNonNull(java.lang.Object, java.lang.String)
     */
    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        else return obj;
    }

    /**
     * Checks that object reference is not {@code null}.
     *
     * @param <T> the type of the object
     * @param obj  an object
     * @param message  a message to be used as exception details
     * @return a source object if it is not {@code null}
     * @throws NullPointerException if object is {@code null}
     * @see #requireNonNull(java.lang.Object)
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        else return obj;
    }

    /**
     * Checks that object reference is not {@code null}.
     *
     * @param <T> the type of the object
     * @param obj  an object
     * @param messageSupplier  a supplier of the detail message
     *                         for {@code NullPointerException}.
     * @return a source object if it is not {@code null}
     * @throws NullPointerException if object is {@code null}
     * @see #requireNonNull(java.lang.Object)
     * @since 1.2.0
     */
    public static <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {
        if (obj == null)
            throw new NullPointerException(messageSupplier.get());
        else return obj;
    }

    /**
     * Returns the first object if it is non-{@code null},
     * returns the non-{@code null} second object otherwise.
     *
     * @param <T> the type of the objects
     * @param obj  an object
     * @param defaultObj  a non-{@code null} object to return
     *                    if the first object is {@code null}
     * @return the first object if it is non-{@code null},
     *         the non-{@code null} second object otherwise.
     * @since 1.2.0
     */
    public static <T> T requireNonNullElse(T obj, T defaultObj) {
        return (obj != null) ? obj : requireNonNull(defaultObj, "defaultObj");
    }

    /**
     * Returns the first object if it is non-{@code null},
     * returns the non-{@code null} supplier's result otherwise.
     *
     * @param <T> the type of the first object and return type
     * @param obj  an object
     * @param supplier  a supplier to return non-{@code null} object
     *                  if first object is {@code null}
     * @return the first object if it is non-{@code null},
     *         the non-{@code null} supplier's result otherwise
     * @since 1.2.0
     */
    public static <T> T requireNonNullElseGet(T obj, Supplier<? extends T> supplier) {
        if (obj != null) return obj;
        final T suppliedObj = requireNonNull(supplier, "supplier").get();
        return requireNonNull(suppliedObj, "supplier.get()");
    }

    /**
     * Checks that collection and its elements are non-{@code null}.
     *
     * @param <T> the type of the objects in the collection
     * @param collection  a collection to be checked for non-{@code null} elements
     * @return a collection
     * @throws NullPointerException if collection or its elements are {@code null}
     * @since 1.2.0
     */
    public static <T> Collection<T> requireNonNullElements(Collection<T> collection) {
        requireNonNull(collection);
        for (T t : collection) {
            requireNonNull(t);
        }
        return collection;
    }

    /**
     * Checks that object reference is {@code null}.
     *
     * @param obj  an object
     * @return {@code true} if the object reference is {@code null}, {@code false} otherwise
     * @see Predicate
     * @since 1.2.0
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * Checks that object reference is not {@code null}.
     *
     * @param obj  an object
     * @return {@code false} if the object reference is {@code null}, {@code true} otherwise
     * @see Predicate
     * @see Predicate.Util#notNull()
     * @since 1.2.0
     */
    public static boolean nonNull(Object obj) {
        return obj != null;
    }
}
