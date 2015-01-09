/*
 * StreamTest.java
 * JMUnit based test
 *
 * Created on Jan 4, 2015, 10:41:57 PM
 */
package test;

import com.annimon.stream.Collector;
import com.annimon.stream.Comparator;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import java.util.Random;
import java.util.Vector;
import jmunit.framework.cldc10.*;

/**
 * @author aNNiMON
 */
public class StreamTest extends TestCase {
    
    private static final Integer[] array10 = new Integer[10];
    private static final Vector list10 = new Vector();
    private static final Vector listRandom10 = new Vector();
    private PrintConsumer pc1, pc2;
    
    public StreamTest() {
        super(14, "StreamTest");
        
        final Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            array10[i] = new Integer(i);
            list10.addElement(new Integer(i));
            listRandom10.addElement(new Integer(rnd.nextInt(10)));
        }
    }
    
    public void setUp() throws Throwable {
        pc1 = new PrintConsumer();
        pc2 = new PrintConsumer();
    }
    
    public void test(int testNumber) throws Throwable {
        switch (testNumber) {
            case 0: limit(); break;
            case 1: skip(); break;
            case 2: limitAndSkip(); break;
            case 3: filter(); break;
            case 4: map(); break;
            case 5: flatMap(); break;
            case 6: distinct(); break;
            case 7: sorted(); break;
            case 8: collect(); break;
            case 9: minAndMax(); break;
            case 10: count(); break;
            case 11: match(); break;
            case 12: reduce(); break;
            case 13: findFirst(); break;
        }
    }
    
    public void limit() {
        Stream.of(array10).limit(2).forEach(pc1.getConsumer());
        assertEquals("01", pc1.toString());
        
        Stream.of(list10).limit(2).forEach(pc2.getConsumer());
        assertEquals(pc1.out(), pc2.out());
    }
    
    public void skip() {
        Stream.of(array10).skip(7).forEach(pc1.getConsumer());
        assertEquals("789", pc1.toString());
        
        Stream.of(list10).skip(7).forEach(pc2.getConsumer());
        assertEquals(pc1.out(), pc2.out());
    }
    
    public void limitAndSkip() {
        Stream.of(array10).skip(2).limit(5).forEach(pc1.getConsumer());
        assertEquals("23456", pc1.out());
        
        Stream.of(list10).limit(5).skip(2).forEach(pc1.getConsumer());
        assertEquals("234", pc1.out());
        
        Stream.of(list10).skip(8).limit(15).forEach(pc1.getConsumer());
        assertEquals("89", pc1.out());
    }
    
    public void filter() {
        Stream.of(array10).filter(pc1.getFilter(2)).forEach(pc1.getConsumer());
        assertEquals("02468", pc1.toString());
        
        Stream.of(list10).filter(pc2.getFilter(2)).forEach(pc2.getConsumer());
        assertEquals("02468", pc1.toString());
        
        assertEquals(pc1.out(), pc2.out());
        
        Stream.of(array10).filter(Predicate.Util.or(pc1.getFilter(2), pc1.getFilter(3))).forEach(pc1.getConsumer());
        assertEquals("0234689", pc1.out());
        
        Stream.of(list10).filter(Predicate.Util.and(pc1.getFilter(2), pc1.getFilter(3))).forEach(pc1.getConsumer());
        assertEquals("06", pc1.out());
        
        Stream.of(list10).filter(pc1.getFilter(2)).filter(pc1.getFilter(3)).forEach(pc1.getConsumer());
        assertEquals("06", pc1.out());
    }
    
    public void map() {
        final Function mapToString = new Function() {
            public Object apply(Object t) {
                final int value = ((Integer) t).intValue();
                return "[" + ((int) Math.sqrt(value)) + "]";
            }
        };
        final Function mapToInt = new Function() {
            public Object apply(Object o) {
                String t = (String) o;
                final String str = t.substring(1, t.length() - 1);
                final int value = Integer.parseInt(str);
                return new Integer(value * value);
            }
        };
        Stream.of(new int[] {4, 9, 16}).map(mapToString).forEach(pc1.getConsumer());
        assertEquals("[2][3][4]", pc1.out());
        
        Stream s1 = Stream.of(new int[] {25, 64, 625});
        Stream s2 = Stream.of(new String[] {"[5]", "[8]", "[25]"});
        s2.forEach(pc2.getConsumer());
        s1.map(mapToString).forEach(pc1.getConsumer());
        assertEquals(pc1.out(), pc2.out());
        s2.map(mapToInt).forEach(pc2.getConsumer());
        s1.forEach(pc1.getConsumer());
        assertEquals(pc1.out(), pc2.out());
        
        final Function mapPlus1 = new Function() {
            public Object apply(Object x) {
                final int value = ((Integer) x).intValue();
                return new Integer(value + 1);
            }
        };
        final Function mapPlus2 = Function.Util.andThen(mapPlus1, mapPlus1);
        Stream.ofRange(-10, 0)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .forEach(pc1.getConsumer());
        assertEquals("0123456789", pc1.out());
    }
    
    public void flatMap() {
        Stream.ofRange(2, 4)
                .flatMap(new Function() {

                    public Object apply(final Object i) {
                        return Stream.ofRange(2, 5)
                                .filter(pc1.getFilter(2))
                                .map(new Function() {

                            public Object apply(Object p) {
                                final int v1 = ((Integer) i).intValue();
                                final int v2 = ((Integer) p).intValue();
                                return i + "*" + p + "=" + (v1*v2) + "\n";
                            }
                        });
                    }
                })
                .forEach(pc1.getConsumer());
        assertEquals("2*2=4\n2*4=8\n3*2=6\n3*4=12\n", pc1.out());
    }
    
    public void distinct() {
        long count = Stream.of(new int[] {1, 1, 2, 3, 5, 3, 2, 1, 1, -1})
                .distinct().filter(new Predicate() {

            public boolean test(Object value) {
                final int v = ((Integer) value).intValue();
                return v == 1;
            }
        }).count();
        assertEquals(1, count);
        
        Stream.of(new int[] {1, 1, 2, 3, 5, 3, 2, 1, 1, -1})
                .distinct().sorted(intComparator).forEach(pc1.getConsumer());
        assertEquals("-11235", pc1.out());
    }
    
    public void sorted() {
        Stream.of(new int[] {6, 3, 9, 0, -7, 19}).sorted(intComparator).forEach(pc1.getConsumer());
        assertEquals("-7036919", pc1.out());
        
        Stream.of(array10).sorted(intComparator).forEach(pc1.getConsumer());
        Stream.of(list10).sorted(intComparator).forEach(pc2.getConsumer());
        assertEquals(pc1.out(), pc2.out());
    }
    
    public void collect() {
        final Supplier joinSupplier = new Supplier() {
            public Object get() {
                return new StringBuffer();
            }
        };
        final BiConsumer joinAccumulator = new BiConsumer() {
            public void accept(Object t, Object u) {
                ((StringBuffer) t).append(u);
            }
        };
        final Collector join = new Collector() {
            public Supplier supplier() {
                return joinSupplier;
            }

            public BiConsumer accumulator() {
                return joinAccumulator;
            }

            public Function finisher() {
                return new Function() {
                    public Object apply(Object value) {
                        return value.toString();
                    }
                };
            }
        };
        
        String text = (String) Stream.ofRange(0, 10)
                .map(new Function() {
                    public Object apply(Object value) {
                        final int v = ((Integer) value).intValue();
                        return Integer.toString(v);
                    }
                })
                .collect(join);
        assertEquals("0123456789", text);
        
        text = Stream.of(new String[] {"a", "b", "c", "def", "", "g"})
                .collect(joinSupplier, joinAccumulator).toString();
        assertEquals("abcdefg", text);
    }
    
    public void minAndMax() {
        Optional min, max;
        
        min = Stream.of(list10).min(intComparator);
        max = Stream.of(list10).max(intComparator);
        assertEquals(0, ((Integer) min.get()).intValue());
        assertEquals(9, ((Integer) max.get()).intValue());
        
        min = Stream.of(list10).filter(pc1.getFilter(2)).min(intComparator);
        max = Stream.of(list10).filter(pc1.getFilter(2)).max(intComparator);
        assertEquals(0, ((Integer) min.get()).intValue());
        assertEquals(8, ((Integer) max.get()).intValue());
    }
    
    public void count() {
        long count = Stream.ofRange(10000000000L, 10000010000L).count();
        assertEquals(10000, count);
        
        long count1 = Stream.of(array10).peek(pc1.getConsumer()).count();
        long count2 = Stream.of(list10).peek(pc2.getConsumer()).count();
        assertEquals(count1, count2);
        assertEquals(pc1.out(), pc2.out());
    }
    
    public void match() {
        boolean match;
        
        match = Stream.of(array10).anyMatch(pc1.getFilter(2));
        assertEquals(true, match);
        match = Stream.of(new int[] {2, 3, 5, 8, 13}).anyMatch(pc1.getFilter(10));
        assertEquals(false, match);
        
        match = Stream.of(array10).allMatch(pc1.getFilter(2));
        assertEquals(false, match);
        match = Stream.of(new int[] {2, 4, 6, 8, 10}).allMatch(pc1.getFilter(2));
        assertEquals(true, match);
        
        match = Stream.of(array10).noneMatch(pc1.getFilter(2));
        assertEquals(false, match);
        match = Stream.of(new int[] {2, 3, 5, 8, 13}).noneMatch(pc1.getFilter(10));
        assertEquals(true, match);
    }
    
    public void reduce() {
        final BiFunction add = new BiFunction() {
            public Object apply(Object value1, Object value2) {
                final int x = ((Integer) value1).intValue();
                final int y = ((Integer) value2).intValue();
                return new Integer(x + y);
            }
        };
        
        int result;
        
        result = ((Integer) Stream.of(array10).reduce(new Integer(0), add)).intValue();
        assertEquals(45, result);
        result = ((Integer) Stream.of(array10).reduce(new Integer(-45), add)).intValue();
        assertEquals(0, result);
        
        Optional optional;
        optional = Stream.of(array10).reduce(add);
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
        assertEquals(45, ((Integer) optional.get()).intValue());
        
        optional = Stream.of(new int[] {1, 3, 5, 7, 9}).filter(pc1.getFilter(2)).reduce(add);
        assertFalse(optional.isPresent());
        assertEquals(119, ((Integer) optional.orElse(new Integer(119))).intValue());
    }
    
    public void findFirst() {
        Optional optional;
        
        optional = Stream.of(array10).findFirst();
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
        assertEquals(0, ((Integer) optional.get()).intValue());
        
        optional = Stream.of(new int[] {1, 3, 5, 7, 9}).filter(pc1.getFilter(2)).findFirst();
        assertFalse(optional.isPresent());
        
        optional = Stream.ofRange(1, 1000000)
                .filter(pc1.getFilter(6))
                .peek(pc1.getConsumer())
                .findFirst();
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
        assertEquals(6, ((Integer) optional.get()).intValue());
        assertEquals("6", pc1.out());
    }
    
    private class PrintConsumer {
        
        private final StringBuffer out = new StringBuffer();

        public Consumer getConsumer() {
            return new Consumer() {
                public void accept(Object value) {
                    out.append(value);
                }
            };
        }
        
        private Predicate getFilter(final int val) {
            return new Predicate() {
                public boolean test(Object value) {
                    int v = ((Integer) value).intValue();
                    return (v % val == 0);
                }
            };
        }
        
        public String out() {
            String result = out.toString();
            out.setLength(0);
            return result;
        }

        public String toString() {
            return out.toString();
        }
    }
    
    private static final Comparator intComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            final int x = ((Integer) o1).intValue();
            final int y = ((Integer) o2).intValue();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        }
    };
}
