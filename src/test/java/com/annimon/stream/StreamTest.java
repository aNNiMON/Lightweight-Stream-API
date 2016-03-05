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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code Stream}.
 *
 * @see com.annimon.stream.Stream
 */
public class StreamTest {

    @Test
    public void testStreamOfList() {
        final PrintConsumer<String> consumer = new PrintConsumer<String>();
        final List<String> list = new ArrayList<String>(4);
        list.add("This");
        list.add(" is ");
        list.add("a");
        list.add(" test");

        Stream.of(list)
                .forEach(consumer);
        assertEquals("This is a test", consumer.toString());
    }

    @Test
    public void testStreamOfMap() {
        final PrintConsumer<String> consumer = new PrintConsumer<String>();
        final Map<String, Integer> map = new HashMap<String, Integer>(4);
        map.put("This", 1);
        map.put(" is ", 2);
        map.put("a", 3);
        map.put(" test", 4);

        long count = Stream.of(map)
                .sortBy(Functions.<String, Integer>entryValue())
                .map(Functions.<String, Integer>entryKey())
                .peek(consumer)
                .count();
        assertEquals(4, count);
        assertEquals("This is a test", consumer.toString());
    }

    @Test
    public void testStreamOfIterator() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        long count = Stream.of(Functions.counterIterator())
                .limit(5)
                .peek(consumer)
                .count();
        assertEquals(5, count);
        assertEquals("01234", consumer.toString());
    }

    @Test
    public void testStreamOfIterable() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Iterable<Integer> iterable = new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return Functions.counterIterator();
            }
        };

        long count = Stream.of(iterable)
                .limit(5)
                .peek(consumer)
                .count();
        assertEquals(5, count);
        assertEquals("01234", consumer.toString());
    }

    @Test
    public void testStreamRange() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 5)
                .forEach(consumer);
        assertEquals("01234", consumer.toString());
    }

    @Test
    public void testStreamRangeOnMaxValues() {
        long count = Stream.range(Integer.MAX_VALUE - 10, Integer.MAX_VALUE).count();
        assertEquals(10L, count);
    }

    @Test
    public void testStreamRangeOnMaxLongValues() {
        long count = Stream.range(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(10L, count);
    }

    @Test
    public void testStreamRangeClosed() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.rangeClosed(0, 5)
                .forEach(consumer);
        assertEquals("012345", consumer.toString());
    }

    @Test
    public void testStreamRangeClosedOnMaxValues() {
        long count = Stream.rangeClosed(Integer.MAX_VALUE - 10, Integer.MAX_VALUE).count();
        assertEquals(11L, count);
    }

    @Test
    public void testStreamRangeClosedOnMaxLongValues() {
        long count = Stream.rangeClosed(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(11L, count);
    }

    @Test
    public void testStreamOfRange() {
        long count = Stream.ofRange(0, 5).count();
        assertEquals(5, count);
    }

    @Test
    public void testStreamOfRangeLong() {
        long count = Stream.ofRange(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(10L, count);
    }

    @Test
    public void testStreamOfRangeClosed() {
        long count = Stream.ofRangeClosed(0, 5).count();
        assertEquals(6, count);
    }

    @Test
    public void testStreamOfRangeClosedLong() {
        long count = Stream.ofRangeClosed(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(11L, count);
    }

    @Test
    public void testGenerate() {
        List<Long> expected = Arrays.asList(0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L);
        List<Long> data = Stream.generate(Functions.fibonacci())
                .limit(10)
                .collect(Collectors.<Long>toList());
        assertThat(data, is(expected));
    }

    @Test
    public void testIterate() {
        final BigInteger two = BigInteger.valueOf(2);
        BigInteger sum = Stream
                .iterate(BigInteger.ONE, new UnaryOperator<BigInteger>() {
                    @Override
                    public BigInteger apply(BigInteger value) {
                        return value.multiply(two);
                    }
                })
                .limit(100)
                .reduce(BigInteger.ZERO, new BinaryOperator<BigInteger>() {
                    @Override
                    public BigInteger apply(BigInteger value1, BigInteger value2) {
                        return value1.add(value2);
                    }
                });
        assertEquals(new BigInteger("1267650600228229401496703205375"), sum);
    }

    @Test
    public void testConcat() {
        final PrintConsumer<String> consumer = new PrintConsumer<String>();
        Stream<String> stream1 = Stream.of("a", "b", "c", "d");
        Stream<String> stream2 = Stream.of("e", "f", "g", "h");
        Stream<String> stream = Stream.concat(stream1, stream2);
        stream.forEach(consumer);
        assertEquals("abcdefgh", consumer.toString());
    }

    @Test
    public void testConcatOfFilter() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream<Integer> stream1 = Stream.range(0, 5).filter(Functions.remainder(1));
        Stream<Integer> stream2 = Stream.range(5, 10).filter(Functions.remainder(1));
        Stream.concat(stream1, stream2).forEach(consumer);
        assertEquals("0123456789", consumer.toString());
    }

    @Test
    public void testConcatOfFlatMap() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        final Function<Integer, Stream<Integer>> flatmapFunc = new Function<Integer, Stream<Integer>>() {
            @Override
            public Stream<Integer> apply(Integer value) {
                return Stream.of(value, value);
            }
        };
        Stream<Integer> stream1 = Stream.range(1, 3).flatMap(flatmapFunc); // 1122
        Stream<Integer> stream2 = Stream.range(3, 5).flatMap(flatmapFunc); // 3344
        Stream.concat(stream1, stream2).forEach(consumer);
        assertEquals("11223344", consumer.toString());
    }

    @Test
    public void testZip() {
        Stream<Integer> shorter = Stream.rangeClosed(1, 5);
        Stream<Integer> longer = Stream.rangeClosed(1, 10);
        List<Integer> zipped = Stream.zip(shorter, longer, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer value1, Integer value2) {
                return value1 + value2;
            }
        }).collect(Collectors.<Integer>toList());
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), zipped);
    }


    @Test
    public void testFilter() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .filter(Functions.remainder(2))
                .forEach(consumer);
        assertEquals("02468", consumer.toString());
    }

    @Test
    public void testFilterWithOrPredicate() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Predicate<Integer> predicate = Predicate.Util.or(Functions.remainder(2), Functions.remainder(3));
        Stream.range(0, 10)
                .filter(predicate)
                .forEach(consumer);
        assertEquals("0234689", consumer.toString());
    }

    @Test
    public void testFilterWithAndPredicate() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Predicate<Integer> predicate = Predicate.Util.and(Functions.remainder(2), Functions.remainder(3));
        Stream.range(0, 10)
                .filter(predicate)
                .forEach(consumer);
        assertEquals("06", consumer.toString());
    }

    @Test
    public void testFilterWithXorPredicate() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Predicate<Integer> predicate = Predicate.Util.xor(Functions.remainder(2), Functions.remainder(3));
        Stream.range(0, 10)
                .filter(predicate)
                .forEach(consumer);
        assertEquals("23489", consumer.toString());
    }

    @Test
    public void testMapIntToSqrtString() {
        Function<Number, String> intToSqrtString = new Function<Number, String>() {
            @Override
            public String apply(Number t) {
                return String.format("[%d]", (int) Math.sqrt(t.intValue()));
            }
        };
        List<String> expected = Arrays.asList("[2]", "[3]", "[4]", "[8]", "[25]");
        List<String> data = Stream.of(4, 9, 16, 64, 625)
                .map(intToSqrtString)
                .collect(Collectors.<String>toList());
        assertThat(data, is(expected));
    }

    @Test
    public void testMapStringToSquareInt() {
        final Function<String, Integer> stringToSquareInt = new Function<String, Integer>() {
            @Override
            public Integer apply(String t) {
                final String str = t.substring(1, t.length() - 1);
                final int value = Integer.parseInt(str);
                return value * value;
            }
        };
        List<Integer> expected = Arrays.asList(4, 9, 16, 64, 625);
        List<Integer> data = Stream.of("[2]", "[3]", "[4]", "[8]", "[25]")
                .map(stringToSquareInt)
                .collect(Collectors.<Integer>toList());
        assertThat(data, is(expected));
    }

    @Test
    public void testMapWithComposedFunction() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        final Function<Integer, Integer> mapPlus1 = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 1;
            }
        };
        final Function<Integer, Integer> mapPlus2 = Function.Util.andThen(mapPlus1, mapPlus1);
        Stream.range(-10, 0)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .forEach(consumer);
        assertEquals("0123456789", consumer.toString());
    }

    @Test
    public void testFlatMap() {
        final PrintConsumer<String> consumer = new PrintConsumer<String>();
        Stream.rangeClosed(2, 4)
                .flatMap(new Function<Integer, Stream<String>>() {

                    @Override
                    public Stream<String> apply(final Integer i) {
                        return Stream.rangeClosed(2, 4)
                                .filter(Functions.remainder(2))
                                .map(new Function<Integer, String>() {

                                    @Override
                                    public String apply(Integer p) {
                                        return String.format("%d * %d = %d\n", i, p, (i*p));
                                    }
                                });
                    }
                })
                .forEach(consumer);

        assertEquals(
                "2 * 2 = 4\n" +
                "2 * 4 = 8\n" +
                "3 * 2 = 6\n" +
                "3 * 4 = 12\n" +
                "4 * 2 = 8\n" +
                "4 * 4 = 16\n", consumer.toString());
    }

    @Test
    public void testDistinct() {
        List<Integer> expected = Arrays.asList(-1, 1, 2, 3, 5);
        List<Integer> data = Stream.of(1, 1, 2, 3, 5, 3, 2, 1, 1, -1)
                .distinct()
                .sorted()
                .collect(Collectors.<Integer>toList());
        assertThat(data, is(expected));
    }

    @Test
    public void testSorted() {
        List<Integer> expected = Arrays.asList(-7, 0, 3, 6, 9, 19);
        List<Integer> data = Stream.of(6, 3, 9, 0, -7, 19)
                .sorted()
                .collect(Collectors.<Integer>toList());
        assertThat(data, is(expected));
    }

    @Test
    public void testSortedWithComparator() {
        List<Integer> expected = Arrays.asList(19, 9, -7, 6, 3, 0);
        List<Integer> data = Stream.of(6, 3, 9, 0, -7, 19)
                .sorted(Functions.descendingAbsoluteOrder())
                .collect(Collectors.<Integer>toList());
        assertThat(data, is(expected));
    }

    @Test
    public void testSortByStringLength() {
        List<String> expected = Arrays.asList("a", "is", "This", "test");
        List<String> data = Stream.of("This", "is", "a", "test")
                .sortBy(new Function<String, Integer>() {

                    @Override
                    public Integer apply(String value) {
                        return value.length();
                    }
                })
                .collect(Collectors.<String>toList());
        assertThat(data, is(expected));
    }
    @Test
    public void testSortByStudentName() {
        final List<Student> students = Arrays.asList(
                Students.STEVE_CS_4,
                Students.MARIA_ECONOMICS_1,
                Students.VICTORIA_CS_3,
                Students.JOHN_CS_2
        );
        final List<Student> expected = Arrays.asList(
                Students.JOHN_CS_2,
                Students.MARIA_ECONOMICS_1,
                Students.STEVE_CS_4,
                Students.VICTORIA_CS_3
        );

        List<Student> data = Stream.of(students)
                .sortBy(new Function<Student, String>() {
                    @Override
                    public String apply(Student student) {
                        return student.getName();
                    }
                })
                .collect(Collectors.<Student>toList());
        assertThat(data, is(expected));
    }

    @Test
    public void testSortByStudentCourseDescending() {
        final List<Student> students = Arrays.asList(
                Students.STEVE_CS_4,
                Students.MARIA_ECONOMICS_1,
                Students.VICTORIA_CS_3,
                Students.JOHN_CS_2
        );
        final List<Student> expected = Arrays.asList(
                Students.STEVE_CS_4,
                Students.VICTORIA_CS_3,
                Students.JOHN_CS_2,
                Students.MARIA_ECONOMICS_1
        );
        List<Student> byCourseDesc = Stream.of(students)
                .sortBy(new Function<Student, Integer>() {
                    @Override
                    public Integer apply(Student student) {
                        return -student.getCourse();
                    }
                })
                .collect(Collectors.<Student>toList());
        assertThat(byCourseDesc, is(expected));
    }

    @Test
    public void testGroupBy() {
        final PrintConsumer<List<Integer>> pc1 = new PrintConsumer<List<Integer>>();
        final PrintConsumer<List<Integer>> pc2 = new PrintConsumer<List<Integer>>();
        final Integer partitionItem = 1;

        Stream.of(1, 2, 3, 1, 2, 3, 1, 2, 3)
                .groupBy(Functions.equalityPartitionItem(partitionItem))
                .forEach(new Consumer<Map.Entry<Boolean, List<Integer>>>() {

                    @Override
                    public void accept(Map.Entry<Boolean, List<Integer>> entry) {
                        (entry.getKey() ? pc1 : pc2)
                                .accept(entry.getValue());
                    }
                });

        assertEquals("[1, 1, 1]", pc1.toString());
        assertEquals("[2, 3, 2, 3, 2, 3]", pc2.toString());
    }

    @Test
    public void testPeek() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        long count = Stream.range(0, 5)
                .peek(consumer)
                .count();
        assertEquals(5, count);
        assertEquals("01234", consumer.toString());
    }

    @Test
    public void testTakeWhile() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        long count = Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(2))
                .peek(consumer)
                .count();
        assertEquals(3, count);
        assertEquals("246", consumer.toString());
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        long count = Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(3))
                .count();
        assertEquals(0, count);
    }

    @Test
    public void testTakeWhileAllMatch() {
        long count = Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(1))
                .count();
        assertEquals(7, count);
    }

    @Test
    public void testDropWhile() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        long count = Stream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainder(2))
                .peek(consumer)
                .count();
        assertEquals(4, count);
        assertEquals("781011", consumer.toString());
    }

    @Test
    public void testDropNonFirstMatch() {
        long count = Stream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainder(3))
                .count();
        assertEquals(7, count);
    }

    @Test
    public void testDropWhileAllMatch() {
        long count = Stream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainder(1))
                .count();
        assertEquals(0, count);
    }

    @Test
    public void testLimit() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .limit(2)
                .forEach(consumer);
        assertEquals("01", consumer.toString());
    }

    @Test
    public void testLimitMoreThanCount() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        long count = Stream.range(0, 5)
                .limit(15)
                .peek(consumer)
                .count();
        assertEquals(5, count);
        assertEquals("01234", consumer.toString());
    }

    @Test
    public void testSkip() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .skip(7)
                .forEach(consumer);
        assertEquals("789", consumer.toString());
    }

    @Test
    public void testSkipMoreThanCount() {
        long count = Stream.range(0, 10)
                .skip(15)
                .count();
        assertEquals(0, count);
    }

    @Test
    public void testSkipLazy() {
        final List<Integer> data = new ArrayList<Integer>(10);
        data.add(0);

        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream<Integer> stream = Stream.of(data).skip(3);
        data.addAll(Arrays.asList(1, 2, 3, 4, 5));
        stream = stream.peek(consumer);

        assertEquals(3, stream.count());
        assertEquals("345", consumer.toString());
    }

    @Test
    public void testSkipAndLimit() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .skip(2)  // 23456789
                .limit(5) // 23456
                .forEach(consumer);
        assertEquals("23456", consumer.toString());
    }

    @Test
    public void testLimitAndSkip() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .limit(5) // 01234
                .skip(2)  // 234
                .forEach(consumer);
        assertEquals("234", consumer.toString());
    }

    @Test
    public void testSkipAndLimitMoreThanCount() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .skip(8)   // 89
                .limit(15) // 89
                .forEach(consumer);
        assertEquals("89", consumer.toString());
    }

    @Test
    public void testSkipMoreThanCountAndLimit() {
        long count = Stream.range(0, 10)
                .skip(15)
                .limit(8)
                .count();
        assertEquals(0, count);
    }

    @Test
    public void testSkipAndLimitTwice() {
        final PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .skip(2)  // 23456789
                .limit(5) // 23456
                .skip(2)  // 456
                .limit(2) // 45
                .forEach(consumer);
        assertEquals("45", consumer.toString());
    }

    @Test
    public void testReduceSumFromZero() {
        int result = Stream.range(0, 10)
                .reduce(0, Functions.addition());
        assertEquals(45, result);
    }

    @Test
    public void testReduceSumFromMinus45() {
        int result = Stream.range(0, 10)
                .reduce(-45, Functions.addition());
        assertEquals(0, result);
    }

    @Test
    public void testReduceWithAnotherType() {
        int result = Stream.of("a", "bb", "ccc", "dddd")
                .reduce(0, new BiFunction<Integer, String, Integer>() {
                    @Override
                    public Integer apply(Integer length, String s) {
                        return length + s.length();
                    }
                });
        assertEquals(10, result);
    }

    @Test
    public void testReduceOptional() {
        Optional<Integer> result = Stream.range(0, 10)
                .reduce(Functions.addition());

        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(45, (int) result.get());
    }

    @Test
    public void testReduceOptionalOnEmptyStream() {
        Optional<Integer> result = Stream.of(1, 3, 5, 7, 9)
                .filter(Functions.remainder(2))
                .reduce(Functions.addition());

        assertFalse(result.isPresent());
        assertEquals(119, (int) result.orElse(119));
    }

    @Test
    public void testCollectWithCollector() {
        String text = Stream.range(0, 10)
                .map(Functions.<Integer>convertToString())
                .collect(Functions.joiningCollector());
        assertEquals("0123456789", text);
    }

    @Test
    public void testCollectWithSupplierAndAccumulator() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Functions.stringBuilderSupplier(), Functions.joiningAccumulator())
                .toString();
        assertEquals("abcdefg", text);
    }

    @Test
    public void testCollect123() {
        String string123 = Stream.of("1", "2", "3")
                .collect(new Supplier<StringBuilder>() {
                    @Override
                    public StringBuilder get() {
                        return new StringBuilder();
                    }
                }, new BiConsumer<StringBuilder, String>() {

                    @Override
                    public void accept(StringBuilder value1, String value2) {
                        value1.append(value2);
                    }
                })
                .toString();
        assertEquals("123", string123);
    }

    @Test
    public void testMin() {
        Optional<Integer> min = Stream.of(6, 3, 9, 0, -7, 19)
                .min(Functions.naturalOrder());

        assertTrue(min.isPresent());
        assertNotNull(min.get());
        assertEquals(-7, (int) min.get());
    }

    @Test
    public void testMinDescendingOrder() {
        Optional<Integer> min = Stream.of(6, 3, 9, 0, -7, 19)
                .min(Functions.descendingAbsoluteOrder());

        assertTrue(min.isPresent());
        assertNotNull(min.get());
        assertEquals(19, (int) min.get());
    }

    @Test
    public void testMinEmpty() {
        Optional<Integer> min = Stream.of(1, 3, 5, 7, 9)
                .filter(Functions.remainder(2))
                .min(Functions.naturalOrder());

        assertFalse(min.isPresent());
    }

    @Test
    public void testMax() {
        Optional<Integer> max = Stream.of(6, 3, 9, 0, -7, 19)
                .max(Functions.naturalOrder());

        assertTrue(max.isPresent());
        assertNotNull(max.get());
        assertEquals(19, (int) max.get());
    }

    @Test
    public void testMaxDescendingOrder() {
        Optional<Integer> max = Stream.of(6, 3, 9, 0, -7, 19)
                .max(Functions.descendingAbsoluteOrder());

        assertTrue(max.isPresent());
        assertNotNull(max.get());
        assertEquals(0, (int) max.get());
    }

    @Test
    public void testMaxEmpty() {
        Optional<Integer> max = Stream.of(1, 3, 5, 7, 9)
                .filter(Functions.remainder(2))
                .max(Functions.naturalOrder());

        assertFalse(max.isPresent());
    }

    @Test
    public void testCount() {
        long count = Stream.range(10000000000L, 10000002000L).count();
        assertEquals(2000, count);
    }

    @Test
    public void testCountMinValue() {
        long count = Stream.range(Integer.MIN_VALUE, Integer.MIN_VALUE + 100).count();
        assertEquals(100, count);
    }

    @Test
    public void testCountMaxValue() {
        long count = Stream.range(Long.MAX_VALUE - 100, Long.MAX_VALUE).count();
        assertEquals(100, count);
    }

    @Test
    public void testAnyMatchWithTrueResult() {
        boolean match = Stream.range(0, 10)
                .anyMatch(Functions.remainder(2));
        assertTrue(match);
    }

    @Test
    public void testAnyMatchWithFalseResult() {
        boolean match = Stream.of(2, 3, 5, 8, 13)
                .allMatch(Functions.remainder(10));
        assertFalse(match);
    }

    @Test
    public void testAllMatchWithFalseResult() {
        boolean match = Stream.range(0, 10)
                .allMatch(Functions.remainder(2));
        assertFalse(match);
    }

    @Test
    public void testAllMatchWithTrueResult() {
        boolean match = Stream.of(2, 4, 6, 8, 10)
                .anyMatch(Functions.remainder(2));
        assertTrue(match);
    }

    @Test
    public void testNoneMatchWithFalseResult() {
        boolean match = Stream.range(0, 10)
                .noneMatch(Functions.remainder(2));
        assertFalse(match);
    }

    @Test
    public void testNoneMatchWithTrueResult() {
        boolean match = Stream.of(2, 3, 5, 8, 13)
                .noneMatch(Functions.remainder(10));
        assertTrue(match);
    }

    @Test
    public void testFindFirst() {
        Optional<Integer> result = Stream.range(0, 10)
                .findFirst();
        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(0, (int) result.get());
    }

    @Test
    public void testFindFirstOnEmptyStream() {
        Optional<Integer> result = Stream.of(1, 3, 5, 7)
                .filter(Functions.remainder(2))
                .findFirst();
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindFirstAfterFiltering() {
        Optional<Integer> result = Stream.range(1, 1000)
                .filter(Functions.remainder(6))
                .findFirst();

        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(6, (int) result.get());
    }

    @Test
    public void testToArray() {
        Object[] objects = Stream.range(0, 200)
               .filter(Functions.remainder(4))
               .toArray();

        assertEquals(50, objects.length);
        assertNotNull(objects[10]);
        assertThat(objects[0], instanceOf(Integer.class));
    }

    @Test
    public void testToArrayWithGenerator() {
        Integer[] numbers = Stream.range(1, 1000)
               .filter(Functions.remainder(2))
               .toArray(Functions.arrayGenerator(Integer[].class));

        assertTrue(numbers.length > 0);
        assertNotNull(numbers[100]);
    }

    @Test
    public void testCustomIntermediateOperator_Reverse() {
        PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .custom(new CustomOperators.Reverse<Integer>())
                .forEach(consumer);
        assertEquals("9876543210", consumer.toString());
    }

    @Test
    public void testCustomIntermediateOperator_SkipAndLimit() {
        PrintConsumer<Integer> pc1 = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .custom(new CustomOperators.SkipAndLimit<Integer>(5, 2))
                .forEach(pc1);
        assertEquals("56", pc1.toString());
    }

    @Test
    public void testCustomIntermediateOperator_FlatMapAndCast() {
        List<Character> expected = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');

        List<List> lists = new ArrayList<List>();
        for (char ch = 'a'; ch <= 'f'; ch++) {
            lists.add( new ArrayList<Character>(Arrays.asList(ch)) );
        }
        List<Character> chars = Stream.of(lists)
                .custom(new CustomOperators.FlatMap<List, Object>(new Function<List, Stream<Object>>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Stream<Object> apply(List value) {
                        return Stream.of(value);
                    }
                }))
                .custom(new CustomOperators.Cast<Object, Character>(Character.class))
                .collect(Collectors.<Character>toList());

        assertThat(chars, is(expected));
    }

    @Test
    public void testCustomTerminalOperator_Sum() {
        int sum = Stream.of(1, 2, 3, 4, 5)
                .custom(new CustomOperators.Sum());
        assertEquals(15, sum);
    }

    @Test
    public void testCustomTerminalOperator_ForEach() {
        PrintConsumer<Integer> consumer = new PrintConsumer<Integer>();
        Stream.range(0, 10)
                .custom(new CustomOperators.ForEach<Integer>(consumer));
        assertEquals("0123456789", consumer.toString());
    }


    private static class PrintConsumer<T> implements Consumer<T> {

        private final StringBuilder out = new StringBuilder();

        @Override
        public void accept(T value) {
            out.append(value);
        }

        @Override
        public String toString() {
            return out.toString();
        }
    }
}
