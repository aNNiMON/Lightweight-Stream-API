package com.annimon.stream;

import java.util.Comparator;

/**
 * Common operations with Object.
 */
public final class Objects {
    
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
     * @param nullDefault  string to return if object is null
     * @return result of calling {@code toString} on object or {@code nullDefault} if object is null.
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
     * @param c  comparator
     * @return comparing result
     */
    public static <T> int compare(T a, T b, Comparator<? super T> c) {
        return (a == b) ? 0 : c.compare(a, b);
    }
    
    /**
     * Checks that object reference is not null.
     * 
     * @param <T> the type of the object
     * @param obj  an object
     * @return source object if it is not null
     * @throws NullPointerException if object is null
     * @see #requireNonNull(java.lang.Object, java.lang.String) 
     */
    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }
    
    /**
     * Checks that object reference is not null.
     * 
     * @param <T> the type of the object
     * @param obj  an object
     * @param message  a message to be used as exception details
     * @return source object if it is not null
     * @throws NullPointerException if object is null
     * @see #requireNonNull(java.lang.Object) 
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }
}
