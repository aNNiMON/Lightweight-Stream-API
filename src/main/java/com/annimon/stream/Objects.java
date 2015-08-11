package com.annimon.stream;

import java.util.Comparator;

/**
 * Common operations with Object.
 * @author aNNiMON
 */
public final class Objects {
    
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
    
    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }
    
    public static int hash(Object... values) {
        if (values == null) return 0;
        
        int result = 1;
        for (Object element : values)
            result = 31 * result + hashCode(element);
        return result;
    }
    
    public static String toString(Object o, String nullDefault) {
        return (o != null) ? o.toString() : nullDefault;
    }
    
    public static <T> int compare(T a, T b, Comparator<? super T> c) {
        return (a == b) ? 0 : c.compare(a, b);
    }
    
    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }
    
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }
}
