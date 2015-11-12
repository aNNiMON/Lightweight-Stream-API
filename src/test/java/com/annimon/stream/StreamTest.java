package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.UnaryOperator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author aNNiMON
 */
public class StreamTest {
    
    private static final Integer[] array10 = new Integer[10];
    private static final List<Integer> list10 = new LinkedList<Integer>();
    private static final List<Integer> listRandom10 = new LinkedList<Integer>();
    private PrintConsumer pc1, pc2;
    
    @BeforeClass
    public static void setUpData() {
        final Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            array10[i] = i;
            list10.add(i);
            listRandom10.add(rnd.nextInt(10));
        }
    }
    
    @Before
    public void setUp() {
         pc1 = new PrintConsumer();
         pc2 = new PrintConsumer();
    }
    
    @Test
    public void limit() {
        Stream.of(array10).limit(2).forEach(pc1.getConsumer());
        assertEquals("01", pc1.toString());
        
        Stream.of(list10).limit(2).forEach(pc2.getConsumer());
        assertEquals(pc1.out(), pc2.out());
    }
    
    @Test
    public void skip() {
        Stream.of(array10).skip(7).forEach(pc1.getConsumer());
        assertEquals("789", pc1.toString());
        
        Stream.of(list10).skip(7).forEach(pc2.getConsumer());
        assertEquals(pc1.out(), pc2.out());
    }
    
    @Test
    public void limitAndSkip() {
        Stream.of(array10).skip(2).limit(5).forEach(pc1.getConsumer());
        assertEquals("23456", pc1.out());
        
        Stream.of(list10).limit(5).skip(2).forEach(pc1.getConsumer());
        assertEquals("234", pc1.out());
        
        Stream.of(list10).skip(8).limit(15).forEach(pc1.getConsumer());
        assertEquals("89", pc1.out());
    }
    
    @Test
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
    
    @Test
    public void map() {
        final Function<Number, String> mapToString = new Function<Number, String>() {
            @Override
            public String apply(Number t) {
                return String.format("[%d]", (int) Math.sqrt(t.intValue()));
            }
        };
        final Function<String, Integer> mapToInt = new Function<String, Integer>() {
            @Override
            public Integer apply(String t) {
                final String str = t.substring(1, t.length() - 1);
                final int value = Integer.parseInt(str);
                return value * value;
            }
        };
        Stream.of(4, 9, 16).map(mapToString).forEach(pc1.getConsumer());
        assertEquals("[2][3][4]", pc1.out());
        
        Stream s1 = Stream.of(25, 64, 625);
        Stream s2 = Stream.of("[5]", "[8]", "[25]");
        s2.forEach(pc2.getConsumer());
        s1.map(mapToString).forEach(pc1.getConsumer());
        assertEquals(pc1.out(), pc2.out());
        s2.map(mapToInt).forEach(pc2.getConsumer());
        s1.forEach(pc1.getConsumer());
        assertEquals(pc1.out(), pc2.out());
        
        final Function<Integer, Integer> mapPlus1 = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 1;
            }
        };
        final Function<Integer, Integer> mapPlus2 = Function.Util.andThen(mapPlus1, mapPlus1);
        Stream.ofRange(-10, 0)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .forEach(pc1.getConsumer());
        assertEquals("0123456789", pc1.out());
    }
    
    @Test
    public void flatMap() {
        Stream.ofRange(2, 4)
                .flatMap(new Function<Integer, Stream<String>>() {

                    @Override
                    public Stream<String> apply(final Integer i) {
                        return Stream.ofRange(2, 5)
                                .filter(pc1.getFilter(2))
                                .map(new Function<Integer, String>() {

                            @Override
                            public String apply(Integer p) {
                                return String.format("%d*%d=%d\n", i, p, (i*p));
                            }
                        });
                    }
                })
                .forEach(pc1.getConsumer());
        assertEquals("2*2=4\n2*4=8\n3*2=6\n3*4=12\n", pc1.out());
    }
    
    @Test
    public void distinct() {
        long count = Stream.of(1, 1, 2, 3, 5, 3, 2, 1, 1, -1)
                .distinct().filter(new Predicate<Integer>() {

            @Override
            public boolean test(Integer value) {
                return value == 1;
            }
        }).count();
        assertEquals(1, count);
        
        Stream.of(1, 1, 2, 3, 5, 3, 2, 1, 1, -1)
                .distinct().sorted().forEach(pc1.getConsumer());
        assertEquals("-11235", pc1.out());
    }
    
    @Test
    public void sorted() {
        Stream.of(6, 3, 9, 0, -7, 19).sorted().forEach(pc1.getConsumer());
        assertEquals("-7036919", pc1.out());
        
        Stream.of(array10).sorted().forEach(pc1.getConsumer());
        Stream.of(list10).sorted().forEach(pc2.getConsumer());
        assertEquals(pc1.out(), pc2.out());
        
        Stream.of(listRandom10).sorted().forEach(pc1.getConsumer());
        List<Integer> list = new ArrayList<Integer>(listRandom10);
        Collections.sort(list);
        Stream.of(list).forEach(pc2.getConsumer());
        assertEquals(pc1.out(), pc2.out());
    }
    
    @Test
    public void sortBy() {
        final Student STEVE_CS_4 = new Student("Steve", "CS", 4);
        final Student MARIA_ECONOMICS_1 = new Student("Maria", "Economics", 1);
        final Student VICTORIA_CS_3 = new Student("Victoria", "CS", 3);
        final Student JOHN_CS_2 = new Student("John", "Law", 2);
        final List<Student> students = Arrays.asList(
                STEVE_CS_4, MARIA_ECONOMICS_1, VICTORIA_CS_3, JOHN_CS_2
        );
        
        List<Student> byName = Stream.of(students)
                .sortBy(new Function<Student, String>() {
                    @Override
                    public String apply(Student student) {
                        return student.getName();
                    }
                })
                .collect(Collectors.<Student>toList());
        assertArrayEquals(
                new Student[] {JOHN_CS_2, MARIA_ECONOMICS_1, STEVE_CS_4, VICTORIA_CS_3},
                byName.toArray());
        
        List<Student> byCourseDesc = Stream.of(students)
                .sortBy(new Function<Student, Integer>() {
                    @Override
                    public Integer apply(Student student) {
                        return -student.getCourse();
                    }
                })
                .collect(Collectors.<Student>toList());
        assertArrayEquals(
                new Student[] {STEVE_CS_4, VICTORIA_CS_3, JOHN_CS_2, MARIA_ECONOMICS_1},
                byCourseDesc.toArray());
    }
    
    @Test
    public void groupBy() {
        final Integer partitionItem = 1;
        Stream.of(1, 2, 3, 1, 2, 3, 1, 2, 3)
                .groupBy(new Function<Integer, Boolean>() {
                    
                    @Override
                    public Boolean apply(Integer value) {
                        return value.equals(partitionItem);
                    }
                })
                .forEach(new Consumer<Map.Entry<Boolean, List<Integer>>>() {

                    @Override
                    public void accept(Map.Entry<Boolean, List<Integer>> entry) {
                        (entry.getKey() ? pc1.getConsumer() : pc2.getConsumer())
                                .accept(entry.getValue());
                    }
                });
        
        assertEquals("[1, 1, 1]", pc1.out());
        assertEquals("[2, 3, 2, 3, 2, 3]", pc2.out());
    }
    
    @Test
    public void collect() {
        final Supplier<StringBuilder> joinSupplier = new Supplier<StringBuilder>() {

            @Override
            public StringBuilder get() {
                return new StringBuilder();
            }
        };
        final BiConsumer<StringBuilder, CharSequence> joinAccumulator =
                new BiConsumer<StringBuilder, CharSequence>() {

            @Override
            public void accept(StringBuilder t, CharSequence u) {
                t.append(u);
            }
        };
        final Collector<CharSequence, ?, String> join = new Collector<CharSequence, StringBuilder, String>() {

            @Override
            public Supplier<StringBuilder> supplier() {
                return joinSupplier;
            }

            @Override
            public BiConsumer<StringBuilder, CharSequence> accumulator() {
                return joinAccumulator;
            }

            @Override
            public Function<StringBuilder, String> finisher() {
                return new Function<StringBuilder, String>() {

                    @Override
                    public String apply(StringBuilder value) {
                        return value.toString();
                    }
                };
            }
        };
        
        String text = Stream.ofRange(0, 10)
                .map(new Function<Integer, String>() {

                    @Override
                    public String apply(Integer value) {
                        return Integer.toString(value);
                    }
                })
                .collect(join);
        assertEquals("0123456789", text);
        
        text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(joinSupplier, joinAccumulator).toString();
        assertEquals("abcdefg", text);
    }
    
    @Test
    public void minAndMax() {
        final Comparator<Integer> comparator = new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        Optional<Integer> min, max;
        
        min = Stream.of(list10).min(comparator);
        max = Stream.of(list10).max(comparator);
        assertEquals(0, (int) min.get());
        assertEquals(9, (int) max.get());
        
        min = Stream.of(list10).filter(pc1.getFilter(2)).min(comparator);
        max = Stream.of(list10).filter(pc1.getFilter(2)).max(comparator);
        assertEquals(0, (int) min.get());
        assertEquals(8, (int) max.get());
    }
    
    @Test
    public void count() {
        long count = Stream.ofRange(10000000000L, 10000010000L).count();
        assertEquals(10000, count);
        
        long count1 = Stream.of(array10).peek(pc1.getConsumer()).count();
        long count2 = Stream.of(list10).peek(pc2.getConsumer()).count();
        assertEquals(count1, count2);
        assertEquals(pc1.out(), pc2.out());
    }
    
    @Test
    public void match() {
        boolean match;
        
        match = Stream.of(array10).anyMatch(pc1.getFilter(2));
        assertEquals(true, match);
        match = Stream.of(2, 3, 5, 8, 13).anyMatch(pc1.getFilter(10));
        assertEquals(false, match);
        
        match = Stream.of(array10).allMatch(pc1.getFilter(2));
        assertEquals(false, match);
        match = Stream.of(2, 4, 6, 8, 10).allMatch(pc1.getFilter(2));
        assertEquals(true, match);
        
        match = Stream.of(array10).noneMatch(pc1.getFilter(2));
        assertEquals(false, match);
        match = Stream.of(2, 3, 5, 8, 13).noneMatch(pc1.getFilter(10));
        assertEquals(true, match);
    }
    
    @Test
    public void reduce() {
        final BiFunction<Integer, Integer, Integer> add = new BiFunction<Integer, Integer, Integer>() {

            @Override
            public Integer apply(Integer value1, Integer value2) {
                return value1 + value2;
            }
        };
        
        int result;
        
        result = Stream.of(array10).reduce(0, add);
        assertEquals(45, result);
        result = Stream.of(array10).reduce(-45, add);
        assertEquals(0, result);
        
        Optional<Integer> optional;
        optional = Stream.of(array10).reduce(add);
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
        assertEquals(45, (int) optional.get());
        
        optional = Stream.of(1, 3, 5, 7, 9).filter(pc1.getFilter(2)).reduce(add);
        assertFalse(optional.isPresent());
        assertEquals(119, (int) optional.orElse(119));
    }
    
    @Test
    public void findFirst() {
        Optional<Integer> optional;
        
        optional = Stream.of(array10).findFirst();
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
        assertEquals(0, (int) optional.get());
        
        optional = Stream.of(1, 3, 5, 7, 9).filter(pc1.getFilter(2)).findFirst();
        assertFalse(optional.isPresent());
        
        optional = Stream.ofRange(1, 1000000)
                .filter(pc1.getFilter(6))
                .peek(pc1.getConsumer())
                .findFirst();
        assertTrue(optional.isPresent());
        assertNotNull(optional.get());
        assertEquals(6, (int) optional.get());
        assertEquals("6", pc1.out());
    }
    
    @Test
    public void customIntermediateOperator() {
        Stream.ofRange(0, 10)
                .custom(new CustomOperators.Reverse<Integer>())
                .forEach(pc1.getConsumer());
        assertEquals("9876543210", pc1.out());
        
        Stream.ofRange(0, 10)
                .custom(new CustomOperators.SkipAndLimit<Integer>(5, 2))
                .forEach(pc1.getConsumer());
        assertEquals("56", pc1.out());
        
        List<List> lists = new ArrayList<List>();
        for (char ch = 'a'; ch <= 'f'; ch++) {
            lists.add( new ArrayList<Character>(Arrays.asList(ch)) );
        }
        List<Character> chars = Stream.of(lists)
                .custom(new CustomOperators.FlatMap<List, Object>(new Function<List, Stream<Object>>() {
                    @Override
                    public Stream<Object> apply(List value) {
                        return Stream.of(value);
                    }
                }))
                .custom(new CustomOperators.Cast<Object, Character>(Character.class))
                .collect(Collectors.<Character>toList());
        assertArrayEquals(new Character[] {'a', 'b', 'c', 'd', 'e', 'f'}, chars.toArray());
    }
    
    @Test
    public void customTerminalOperator() {
        int sum = Stream.of(1, 2, 3, 4, 5)
                .custom(new CustomOperators.Sum());
        assertEquals(15, sum);
        
        Stream.ofRange(0, 10)
                .custom(new CustomOperators.ForEach<Integer>(pc1.getConsumer()));
        assertEquals("0123456789", pc1.out());
    }
    
    @Test
    public void generateFibonacci() {
        List<Long> expected = Arrays.asList(0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L);
        List<Long> data = Stream.generate(new Supplier<Long>() {
            private long beforePrevious = 0;
            private long previous = 0;

            @Override
            public Long get() {
                final long result = beforePrevious + previous;
                if (result == 0) previous = 1;
                beforePrevious = previous;
                previous = result;
                return result;
            }
        }).limit(10).collect(Collectors.<Long>toList());
        assertThat(data, is(expected));
    }
    
    @Test
    public void iterate() {
        final BigInteger two = BigInteger.valueOf(2);
        BigInteger sum = Stream.iterate(BigInteger.ONE, new UnaryOperator<BigInteger>() {
            @Override
            public BigInteger apply(BigInteger value) {
                return value.multiply(two);
            }
        }).limit(100).reduce(BigInteger.ZERO, new BinaryOperator<BigInteger>() {
            @Override
            public BigInteger apply(BigInteger value1, BigInteger value2) {
                return value1.add(value2);
            }
        });
        assertEquals(new BigInteger("1267650600228229401496703205375"), sum);
    }
    
    private class PrintConsumer {
        
        private final StringBuilder out = new StringBuilder();

        public <T> Consumer<T> getConsumer() {
            return new Consumer<T>() {

                @Override
                public void accept(T value) {
                    out.append(value);
                }
            };
        }
        
        private Predicate<Integer> getFilter(final int val) {
            return new Predicate<Integer>() {

                @Override
                public boolean test(Integer v) {
                    return (v % val == 0);
                }
            };
        }
        
        public String out() {
            String result = out.toString();
            out.setLength(0);
            return result;
        }

        @Override
        public String toString() {
            return out.toString();
        }
    }
}
