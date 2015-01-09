package com.annimon.stream;

import com.annimon.stream.function.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A sequence of elements supporting aggregate operations.
 * 
 * @author aNNiMON
 */
public class Stream {
    
    public static Stream of(Enumeration enumeration) {
        return new Stream(enumeration);
    }
    
    public static Stream of(Vector vector) {
        return new Stream(vector.elements());
    }
    
    public static Stream of(final int[] array) {
        return new Stream(new Enumeration() {
            
            private int index = 0;
            
            public boolean hasMoreElements() {
                return index < array.length;
            }
            
            public Object nextElement() {
                return new Integer(array[index++]);
            }
        });
    }
    
    public static Stream of(final Object[] array) {
        return new Stream(new Enumeration() {
            
            private int index = 0;
            
            public boolean hasMoreElements() {
                return index < array.length;
            }
            
            public Object nextElement() {
                return array[index++];
            }
        });
    }
    
    public static Stream ofRange(final int from, final int to) {
        return new Stream(new Enumeration() {
            
            private int index = from;
            
            public boolean hasMoreElements() {
                return index < to;
            }
            
            public Object nextElement() {
                return new Integer(index++);
            }
        });
    }
    public static Stream ofRange(final long from, final long to) {
        return new Stream(new Enumeration() {
            
            private long index = from;
            
            public boolean hasMoreElements() {
                return index < to;
            }
            
            public Object nextElement() {
                return new Long(index++);
            }
        });
    }
    
    
//<editor-fold defaultstate="collapsed" desc="Implementation">
    private final Enumeration iterator;
    
    private Stream(Enumeration iterator) {
        this.iterator = iterator;
    }
    
    public Stream filter(final Predicate predicate) {
        return new Stream(new Enumeration() {
            
            private Object next;
            
            public boolean hasMoreElements() {
                while (iterator.hasMoreElements()) {
                    next = iterator.nextElement();
                    if (predicate.test(next)) {
                        return true;
                    }
                }
                return false;
            }
            
            public Object nextElement() {
                return next;
            }
        });
    }
    
    public Stream map(final Function mapper) {
        return new Stream(new Enumeration() {
            
            public boolean hasMoreElements() {
                return iterator.hasMoreElements();
            }
            
            public Object nextElement() {
                return mapper.apply(iterator.nextElement());
            }
        });
    }
    
    public Stream flatMap(final Function mapper) {
        return new Stream(new Enumeration() {
            
            private Object next;
            private Enumeration inner;
            
            public boolean hasMoreElements() {
                if ((inner != null) && inner.hasMoreElements()) {
                    next = inner.nextElement();
                    return true;
                }
                while (iterator.hasMoreElements()) {
                    if (inner == null || !inner.hasMoreElements()) {
                        final Object arg = iterator.nextElement();
                        final Stream result = (Stream) mapper.apply(arg);
                        if (result != null) {
                            inner = result.iterator;
                        }
                    }
                    if ((inner != null) && inner.hasMoreElements()) {
                        next = inner.nextElement();
                        return true;
                    }
                }
                return false;
            }
            
            public Object nextElement() {
                return next;
            }
        });
    }
    
    public Stream distinct() {
        final Vector list = new Vector();
        while (iterator.hasMoreElements()) {
            Object s = iterator.nextElement();
            if (!list.contains(s)) {
                list.addElement(s);
            }
        }
        return new Stream(list.elements());
    }
    
    public Stream sorted(Comparator comparator) {
        final Vector list = new Vector();
        int listSize = 0;
        while (iterator.hasMoreElements()) {
            Object s = iterator.nextElement();
            int i;
            for (i = 0; i < listSize; i++) {
                int c = comparator.compare(s, list.elementAt(i));
                if (c < 0) {
                    list.insertElementAt(s, i);
                    listSize++;
                    break;
                } else if (c == 0) {
                    break;
                }
            }
            if (i >= listSize) {
                list.addElement(s);
                listSize++;
            }
        }
        return new Stream(list.elements());
    }
    
    public Stream peek(final Consumer action) {
        return new Stream(new Enumeration() {
            
            public boolean hasMoreElements() {
                return iterator.hasMoreElements();
            }
            
            public Object nextElement() {
                final Object value = iterator.nextElement();
                action.accept(value);
                return value;
            }
        });
    }
    
    public Stream limit(final long maxSize) {
        return new Stream(new Enumeration() {
            
            private long index = 0;
            
            public boolean hasMoreElements() {
                return (index < maxSize) && iterator.hasMoreElements();
            }
            
            public Object nextElement() {
                index++;
                return iterator.nextElement();
            }
        });
    }
    
    public Stream skip(long n) {
        for (long i = 0; i < n && iterator.hasMoreElements(); i++) {
            iterator.nextElement();
        }
        return this;
    }
    
    public void forEach(final Consumer action) {
        while (iterator.hasMoreElements()) {
            action.accept(iterator.nextElement());
        }
    }
    
    public Object reduce(final Object identity, BiFunction accumulator) {
        Object result = identity;
        while (iterator.hasMoreElements()) {
            final Object value = iterator.nextElement();
            result = accumulator.apply(result, value);
        }
        return result;
    }
    
    public Optional reduce(BiFunction accumulator) {
        boolean foundAny = false;
        Object result = null;
        while (iterator.hasMoreElements()) {
            final Object value = iterator.nextElement();
            if (!foundAny) {
                foundAny = true;
                result = value;
            } else {
                result = accumulator.apply(result, value);
            }
        }
        return foundAny ? Optional.of(result) : (Optional) Optional.empty();
    }
    
    public Object collect(Supplier supplier, BiConsumer accumulator) {
        Object result = supplier.get();
        while (iterator.hasMoreElements()) {
            final Object value = iterator.nextElement();
            accumulator.accept(result, value);
        }
        return result;
    }
    
    public Object collect(Collector collector) {
        Object container = collector.supplier().get();
        while (iterator.hasMoreElements()) {
            final Object value = iterator.nextElement();
            collector.accumulator().accept(container, value);
        }
        if (collector.finisher() != null)
            return collector.finisher().apply(container);
        return container;
    }
    
    public Optional min(Comparator comparator) {
        return reduce(BiFunction.Util.minBy(comparator));
    }
    
    public Optional max(Comparator comparator) {
        return reduce(BiFunction.Util.maxBy(comparator));
    }
    
    public long count() {
        long count = 0;
        while (iterator.hasMoreElements()) {
            iterator.nextElement();
            count++;
        }
        return count;
    }
    
    public boolean anyMatch(Predicate predicate) {
        return match(predicate, MATCH_ANY);
    }
    
    public boolean allMatch(Predicate predicate) {
        return match(predicate, MATCH_ALL);
    }
    
    public boolean noneMatch(Predicate predicate) {
        return match(predicate, MATCH_NONE);
    }
    
    public Optional findFirst() {
        if (iterator.hasMoreElements()) {
            return Optional.of(iterator.nextElement());
        }
        return Optional.empty();
    }
    
    private static final int MATCH_ANY = 0;
    private static final int MATCH_ALL = 1;
    private static final int MATCH_NONE = 2;
    
    private boolean match(Predicate predicate, int matchKind) {
        final boolean kindAny = (matchKind == MATCH_ANY);
        final boolean kindAll = (matchKind == MATCH_ALL);
        
        while (iterator.hasMoreElements()) {
            final Object value = iterator.nextElement();
            
            /*if (predicate.test(value)) {
                // anyMatch -> true
                // noneMatch -> false
                if (!kindAll) {
                    return matchAny;
                }
            } else {
                // allMatch -> false
                if (kindAll) {
                    return false;
                }
            }*/
            // match && !kindAll -> kindAny
            // !match && kindAll -> false
            final boolean match = predicate.test(value);
            if (match ^ kindAll) {
                return kindAny && match; // (match ? kindAny : false);
            }
        }
        // anyMatch -> false
        // allMatch -> true
        // noneMatch -> true
        return !kindAny;
    }
    
//</editor-fold>
}
