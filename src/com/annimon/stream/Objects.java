package com.annimon.stream;

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
    
    public static int hash(Object[] values) {
        if (values == null) return 0;
        
        int result = 1;
        for (int i = 0; i < values.length; i++) {
            result = 31 * result + hashCode(values[i]);
        }
        return result;
    }
    
    public static String toString(Object o, String nullDefault) {
        return (o != null) ? o.toString() : nullDefault;
    }
    
    public static int compare(Object a, Object b, Comparator c) {
        return (a == b) ? 0 : c.compare(a, b);
    }
    
    public static Object requireNonNull(Object obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }
    
    public static Object requireNonNull(Object obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }
}
