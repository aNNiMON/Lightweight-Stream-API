package com.annimon.stream;

import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntSupplier;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.ToDoubleFunction;
import com.annimon.stream.function.ToIntFunction;
import com.annimon.stream.function.ToLongFunction;
import com.annimon.stream.function.UnaryOperator;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Tests {@code Collectors}.
 *
 * @see com.annimon.stream.Collectors
 */
public class CollectorsTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testToCollection() {
        Collection<Integer> result = Stream.range(0, 5)
                .collect(Collectors.toCollection(new Supplier<Collection<Integer>>() {
                    @Override
                    public Collection<Integer> get() {
                        return new LinkedList<Integer>();
                    }
                }));
        assertThat(result, contains(0, 1, 2, 3, 4));
        assertThat(result, instanceOf(LinkedList.class));
    }

    @Test
    public void testToList() {
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> list = Stream.range(0, 10)
                .collect(Collectors.<Integer>toList());
        assertThat(list, is(expected));
    }

    @Test
    public void testToUnmodifiableList() {
        List<Integer> list = Stream.range(0, 6)
                .collect(Collectors.<Integer>toUnmodifiableList());
        assertThat(list, is(Arrays.asList(0, 1, 2, 3, 4, 5)));

        try {
            list.add(1);
            fail("Expected an UnsupportedOperationException to be thrown when add item to list");
        } catch (UnsupportedOperationException expected) { }

        try {
            list.clear();
            fail("Expected an UnsupportedOperationException to be thrown when clear the list");
        } catch (UnsupportedOperationException expected) { }

        try {
            list.subList(1, 2).clear();
            fail("Expected an UnsupportedOperationException to be thrown when clear the sublist");
        } catch (UnsupportedOperationException expected) { }
    }

    @Test
    public void testToUnmodifiableListWithNullValues() {
        expectedException.expect(NullPointerException.class);
        Stream.of(0, 1, null, 3, 4, null)
                .collect(Collectors.toUnmodifiableList());
    }

    @Test
    public void testToSet() {
        Set<Integer> set = Stream.of(1, 2, 2, 3, 3, 3)
                .collect(Collectors.<Integer>toSet());
        assertThat(set, containsInAnyOrder(1, 2, 3));
    }

    @Test
    public void testToUnmodifiableSet() {
        Set<Integer> set = Stream.range(0, 6)
                .collect(Collectors.<Integer>toUnmodifiableSet());
        assertThat(set, containsInAnyOrder(0, 1, 2, 3, 4, 5));

        try {
            set.add(1);
            fail("Expected an UnsupportedOperationException to be thrown when add item to set");
        } catch (UnsupportedOperationException expected) { }

        try {
            set.clear();
            fail("Expected an UnsupportedOperationException to be thrown when clear the set");
        } catch (UnsupportedOperationException expected) { }
    }

    @Test
    public void testToUnmodifiableSetWithNullValues() {
        expectedException.expect(NullPointerException.class);
        Stream.of(0, 1, null, 3, 4, null)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Test
    public void testToUnmodifiableSetWithDuplicates() {
        Set<Integer> set = Stream.of(0, 1, 2, 1, 3, 0)
                .collect(Collectors.<Integer>toUnmodifiableSet());
        assertThat(set, containsInAnyOrder(0, 1, 2, 3));
    }

    @Test
    public void testToMapWithDefaultValueMapper() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        Map<Character, String> chars = Stream.of("a", "b", "c", "d")
                .collect(Collectors.toMap(keyMapper));

        assertThat(chars.size(), is(4));
        assertThat(chars, allOf(
                hasEntry('a', "a"),
                hasEntry('b', "b"),
                hasEntry('c', "c"),
                hasEntry('d', "d")
        ));
    }

    @Test
    public void testToMapWithIdentityValueMapper() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        Map<Character, String> chars = Stream.of("a", "b", "c", "d")
                .collect(Collectors.toMap(keyMapper, UnaryOperator.Util.<String>identity()));

        assertThat(chars.size(), is(4));
        assertThat(chars, allOf(
                hasEntry('a', "a"),
                hasEntry('b', "b"),
                hasEntry('c', "c"),
                hasEntry('d', "d")
        ));
    }

    @Test
    public void testToMapWithValueMapperThatReturnsNullValue() {
        expectedException.expect(NullPointerException.class);
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        Stream.of("a0", "b0", "c0", "d0")
                .collect(Collectors.toMap(keyMapper, new UnaryOperator<String>() {

            @Override
            public String apply(String value) {
                if ("c0".equals(value)) return null;
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        }));
    }

    @Test
    public void testToMapWithDefaultValueMapperAndDuplicatingKeys() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Duplicate key a (attempted merging values a0 and a2)");
        Stream.of("a0", "b1", "a2", "d3")
                .collect(Collectors.toMap(Functions.firstCharacterExtractor()));
    }

    @Test
    public void testToUnmodifiableMapDuplicatingKeys() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Duplicate key a (attempted merging values a0 and a2)");
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = UnaryOperator.Util.identity();
        Stream.of("a0", "b1", "a2", "d3")
                .collect(Collectors.toUnmodifiableMap(keyMapper, valueMapper));
    }

    @Test
    public void testToUnmodifiableMapWithNullKey() {
        expectedException.expect(NullPointerException.class);
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = new UnaryOperator<String>() {
            @Override
            public String apply(String value) {
                if (value == null) return "";
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        };
        Stream.of("a0", "b1", null, "d3")
                .collect(Collectors.toUnmodifiableMap(keyMapper, valueMapper));
    }

    @Test
    public void testToUnmodifiableMapWithNullValue() {
        expectedException.expect(NullPointerException.class);
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = new UnaryOperator<String>() {
            @Override
            public String apply(String value) {
                if ("c2".equals(value)) return null;
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        };
        Stream.of("a0", "b1", "c2", "d3")
                .collect(Collectors.toUnmodifiableMap(keyMapper, valueMapper));
    }

    @Test
    public void testToMapWithMergerFunction() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = new UnaryOperator<String>() {
            @Override
            public String apply(String value) {
                if ("c0".equals(value)) return null;
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        };
        final BinaryOperator<String> merger = new BinaryOperator<String>() {
            @Override
            public String apply(String oldValue, String newValue) {
                return newValue;
            }
        };
        Map<Character, String> chars = Stream.of("a0", "b0", "c0", "d0", "b1", "b2")
                .collect(Collectors.toMap(keyMapper, valueMapper, merger));

        assertThat(chars.size(), is(3));
        assertThat(chars, allOf(
                hasEntry('a', "A"),
                hasEntry('b', "B"),
                hasEntry('d', "D")
        ));
    }

    @Test
    public void testToMapWithMergerFunctionAndLinkedHashMapSupplier() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = new UnaryOperator<String>() {
            @Override
            public String apply(String value) {
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        };
        final BinaryOperator<String> merger = new BinaryOperator<String>() {
            @Override
            public String apply(String oldValue, String newValue) {
                return newValue;
            }
        };
        final Supplier<Map<Character, String>> supplier = new Supplier<Map<Character, String>>() {
            @Override
            public Map<Character, String> get() {
                return new LinkedHashMap<Character, String>();
            }
        };
        Map<Character, String> chars = Stream.of("a0", "b1", "c2", "d3")
                .collect(Collectors.toMap(keyMapper, valueMapper, merger, supplier));

        assertThat(chars, instanceOf(LinkedHashMap.class));
        assertThat(chars.size(), is(4));
        assertThat(chars, allOf(
                hasEntry('a', "A"),
                hasEntry('b', "B"),
                hasEntry('c', "C"),
                hasEntry('d', "D")
        ));
    }

    @Test
    public void testToUnmodifiableMapWithMergerFunction() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = new UnaryOperator<String>() {
            @Override
            public String apply(String value) {
                if ("c0".equals(value)) return null;
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        };
        final BinaryOperator<String> merger = new BinaryOperator<String>() {
            @Override
            public String apply(String oldValue, String newValue) {
                return newValue;
            }
        };
        Map<Character, String> chars = Stream.of("a0", "b0", "c0", "d0")
                .collect(Collectors.toUnmodifiableMap(keyMapper, valueMapper, merger));

        assertThat(chars.size(), is(3));
        assertThat(chars, allOf(
                hasEntry('a', "A"),
                hasEntry('b', "B"),
                hasEntry('d', "D")
        ));

        try {
            chars.put('u', "U");
            fail("Expected an UnsupportedOperationException to be thrown when add item to map");
        } catch (UnsupportedOperationException expected) { }

        try {
            chars.clear();
            fail("Expected an UnsupportedOperationException to be thrown when clear the map");
        } catch (UnsupportedOperationException expected) { }
    }

    @Test
    public void testToUnmodifiableMapWithMergerFunctionAndNullKey() {
        expectedException.expect(NullPointerException.class);
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = new UnaryOperator<String>() {
            @Override
            public String apply(String value) {
                if (value == null) return "";
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        };
        final BinaryOperator<String> merger = new BinaryOperator<String>() {
            @Override
            public String apply(String oldValue, String newValue) {
                return newValue;
            }
        };
        Stream.of("a0", "b1", null, "d3")
                .collect(Collectors.toUnmodifiableMap(keyMapper, valueMapper, merger));
    }

    @Test
    public void testToUnmodifiableMapWithMergerFunctionAndNullValue() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        final UnaryOperator<String> valueMapper = new UnaryOperator<String>() {
            @Override
            public String apply(String value) {
                if ("c2".equals(value)) return null;
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        };
        final BinaryOperator<String> merger = new BinaryOperator<String>() {
            @Override
            public String apply(String oldValue, String newValue) {
                return newValue;
            }
        };
        Map<Character, String> chars = Stream.of("a0", "b1", "c2", "d3")
                .collect(Collectors.toUnmodifiableMap(keyMapper, valueMapper, merger));

        assertThat(chars.size(), is(3));
        assertThat(chars, allOf(
                hasEntry('a', "A"),
                hasEntry('b', "B"),
                hasEntry('d', "D")
        ));

        try {
            chars.put('u', "U");
            fail("Expected an UnsupportedOperationException to be thrown when add item to map");
        } catch (UnsupportedOperationException expected) { }

        try {
            chars.clear();
            fail("Expected an UnsupportedOperationException to be thrown when clear the map");
        } catch (UnsupportedOperationException expected) { }
    }

    @Test
    public void testJoining() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Collectors.joining());
        assertEquals("abcdefg", text);
    }

    @Test
    public void testJoiningWithDelimiter() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Collectors.joining(", "));
        assertEquals("a, b, c, def, , g", text);
    }

    @Test
    public void testJoiningWithDelimiterPrefixAndSuffixEmpty() {
        String text = Stream.<String>empty()
                .collect(Collectors.joining(", ", "prefix|", "|suffix", "empty"));
        assertEquals("empty", text);
    }

    @Test
    public void testJoiningWithDelimiterPrefixAndSuffixEmptyStream() {
        String text = Stream.<String>empty()
                .collect(Collectors.joining(", ", "prefix|", "|suffix"));
        assertEquals("prefix||suffix", text);
    }

    @Test
    public void testJoiningWithDelimiterPrefixAndSuffix() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Collectors.joining(", ", "prefix|", "|suffix"));
        assertEquals("prefix|a, b, c, def, , g|suffix", text);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testAveraging() {
        double avg;

        avg = Stream.<Integer>empty()
                .collect(Collectors.averaging(new Function<Integer, Double>() {
                    @Override
                    public Double apply(Integer t) {
                        return t.doubleValue();
                    }
                }));
        assertThat(avg, closeTo(0, 0.001));

        avg = Stream.of(10, 20, 30, 40)
                .collect(Collectors.averaging(new Function<Integer, Double>() {
                    @Override
                    public Double apply(Integer value) {
                        return value / 10d;
                    }
                }));
        assertThat(avg, closeTo(2.5, 0.001));
    }

    @Test
    public void testAveragingInt() {
        final ToIntFunction<Integer> identity = new ToIntFunction<Integer>() {
            @Override
            public int applyAsInt(Integer t) {
                return t;
            }
        };

        double avg;

        avg = Stream.<Integer>empty()
                .collect(Collectors.averagingInt(identity));
        assertThat(avg, closeTo(0, 0.001));

        avg = Stream.of(1, 2, 3, 4)
                .collect(Collectors.averagingInt(identity));
        assertThat(avg, closeTo(2.5, 0.001));

        avg = Stream.of(Integer.MAX_VALUE, Integer.MAX_VALUE)
                .collect(Collectors.averagingInt(identity));
        assertThat(avg, closeTo(Integer.MAX_VALUE, 0.001));
    }

    @Test
    public void testAveragingLong() {
        final ToLongFunction<Integer> identity = new ToLongFunction<Integer>() {
            @Override
            public long applyAsLong(Integer t) {
                return t;
            }
        };

        double avg;

        avg = Stream.<Integer>empty()
                .collect(Collectors.averagingLong(identity));
        assertThat(avg, closeTo(0, 0.001));

        avg = Stream.of(1, 2, 3, 4)
                .collect(Collectors.averagingLong(identity));
        assertThat(avg, closeTo(2.5, 0.001));

        avg = Stream.of(Integer.MAX_VALUE, Integer.MAX_VALUE)
                .collect(Collectors.averagingLong(identity));
        assertThat(avg, closeTo(Integer.MAX_VALUE, 0.001));
    }

    @Test
    public void testAveragingDouble() {
        final ToDoubleFunction<Integer> intToDoubleFunction = new ToDoubleFunction<Integer>() {
            @Override
            public double applyAsDouble(Integer t) {
                return t.doubleValue();
            }
        };
        double avg;

        avg = Stream.<Integer>empty()
                .collect(Collectors.averagingDouble(intToDoubleFunction));
        assertThat(avg, closeTo(0, 0.001));

        avg = Stream.of(1, 2, 3, 4)
                .collect(Collectors.averagingDouble(intToDoubleFunction));
        assertThat(avg, closeTo(2.5, 0.001));
    }

    @Test
    public void testSummingInt() {
        final ToIntFunction<Integer> identity = new ToIntFunction<Integer>() {
            @Override
            public int applyAsInt(Integer t) {
                return t;
            }
        };

        int sum;

        sum = Stream.<Integer>empty()
                .collect(Collectors.summingInt(identity));
        assertThat(sum, is(0));

        sum = Stream.of(1, 2, 3, 4)
                .collect(Collectors.summingInt(identity));
        assertThat(sum, is(10));
    }

    @Test
    public void testSummingLong() {
        final ToLongFunction<Long> identity = new ToLongFunction<Long>() {
            @Override
            public long applyAsLong(Long t) {
                return t;
            }
        };

        long sum;

        sum = Stream.<Long>empty()
                .collect(Collectors.summingLong(identity));
        assertThat(sum, is(0L));

        sum = Stream.of(1L, 2L, 3L, 4L)
                .collect(Collectors.summingLong(identity));
        assertThat(sum, is(10L));

        sum = Stream.of(1L, Long.MAX_VALUE - 1)
                .collect(Collectors.summingLong(identity));
        assertThat(sum, is(Long.MAX_VALUE));
    }

    @Test
    public void testSummingDouble() {
        final ToDoubleFunction<Double> identity = new ToDoubleFunction<Double>() {
            @Override
            public double applyAsDouble(Double t) {
                return t;
            }
        };

        double sum;

        sum = Stream.<Double>empty()
                .collect(Collectors.summingDouble(identity));
        assertThat(sum, closeTo(0, 0.001));

        sum = Stream.of(1d, 2d, 3d, 4d)
                .collect(Collectors.summingDouble(identity));
        assertThat(sum, closeTo(10, 0.001));
    }

    @Test
    public void testCounting() {
        long count = Stream.range(0, 20)
                .collect(Collectors.counting());
        assertEquals(20, count);
    }

    @Test
    public void testReducingMultiply() {
        long production = Stream.of(1, 2, 3, 4, 5).collect(
                Collectors.reducing(1, new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer value1, Integer value2) {
                        return value1 * value2;
                    }
                })
        );
        assertEquals(120, production);
    }

    @Test
    public void testReducingSumDivision() {
        double sumDiv = Stream.of(1, 2, 3, 4, 5).collect(
                Collectors.reducing(0d,
                        new Function<Integer, Double>() {
                            @Override
                            public Double apply(Integer value) {
                                return 1d / value;
                            }
                        },
                        new BinaryOperator<Double>() {
                            @Override
                            public Double apply(Double value1, Double value2) {
                                return value1 + value2;
                            }
                        })
        );
        assertThat(sumDiv, closeTo(2.28, 0.01));
    }

    @Test
    public void testGroupingBy() {
        final Integer partitionItem = 1;
        List<Integer> items = Arrays.asList(1, 2, 3, 1, 2, 3, 1, 2, 3);
        Map<Boolean, List<Integer>> groupedBy = Stream.of(items)
                .collect(Collectors.groupingBy(Functions.equalityPartitionItem(partitionItem)));

        assertThat(groupedBy, allOf(
                hasEntry(true, Arrays.asList(1, 1, 1)),
                hasEntry(false, Arrays.asList(2, 3, 2, 3, 2, 3))
        ));
    }

    @Test
    public void testGroupingByCounting() {
        Map<Integer, Long> byCounting = Stream.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)
                .collect(Collectors.groupingBy(
                        UnaryOperator.Util.<Integer>identity(),
                        Collectors.counting()));
        assertThat(byCounting, allOf(
                hasEntry(1, 1L),
                hasEntry(2, 2L),
                hasEntry(3, 3L),
                hasEntry(4, 4L)
        ));
    }

    @Test
    public void testGroupingByStudentSpeciality() {
        Map<String, List<Student>> bySpeciality = Stream.of(Students.ALL)
                .collect(Collectors.groupingBy(Students.speciality));

        assertThat(bySpeciality.get("CS"), is(Arrays.asList(
                Students.STEVE_CS_4,
                Students.VICTORIA_CS_3,
                Students.JOHN_CS_2,
                Students.MARIA_CS_1
        )));
        assertThat(bySpeciality.get("Economics"), is(Arrays.asList(
                Students.MARIA_ECONOMICS_1,
                Students.SERGEY_ECONOMICS_2,
                Students.SOPHIA_ECONOMICS_2
        )));
        assertThat(bySpeciality.get("Law"), is(Arrays.asList(
                Students.GEORGE_LAW_3,
                Students.SERGEY_LAW_1
        )));
    }

    @Test
    public void testGroupingByStudentCourse() {
        Map<Integer, List<Student>> byCourse = Stream.of(Students.ALL)
                .collect(Collectors.groupingBy(Students.course));

        assertThat(byCourse.get(1), is(Arrays.asList(
                Students.MARIA_ECONOMICS_1,
                Students.SERGEY_LAW_1,
                Students.MARIA_CS_1
        )));
        assertThat(byCourse.get(2), is(Arrays.asList(
                Students.JOHN_CS_2,
                Students.SERGEY_ECONOMICS_2,
                Students.SOPHIA_ECONOMICS_2
        )));
        assertThat(byCourse.get(3), is(Arrays.asList(
                Students.VICTORIA_CS_3,
                Students.GEORGE_LAW_3
        )));
        assertThat(byCourse.get(4), is(Arrays.asList(
                Students.STEVE_CS_4
        )));
    }

    @Test
    public void testGroupingByStudentSpecialityAndCourse() {
        Map<String, Map<Integer, List<Student>>> bySpecialityAndCourse = Stream.of(Students.ALL)
                .collect(Collectors.groupingBy(Students.speciality, Collectors.groupingBy(Students.course)));

        assertThat(bySpecialityAndCourse.get("Economics").get(2), is(Arrays.asList(
                Students.SERGEY_ECONOMICS_2,
                Students.SOPHIA_ECONOMICS_2
        )));
    }

    @Test
    public void testGroupingByStudentCourseCounting() {
        Map<Integer, Long> byCourseCounting = Stream.of(Students.ALL)
                .collect(Collectors.groupingBy(Students.course, Collectors.counting()));
        assertThat(byCourseCounting, allOf(
                hasEntry(1, 3L),
                hasEntry(2, 3L),
                hasEntry(3, 2L),
                hasEntry(4, 1L)
        ));
    }

    @Test
    public void testPartitioningByStudentCourse() {
        Map<Boolean, List<Student>> byCourse = Stream.of(Students.ALL)
                .collect(Collectors.partitioningBy​(new Predicate<Student>() {
                    @Override
                    public boolean test(Student student) {
                        return student.getCourse() == 2;
                    }
                }));
        assertThat(byCourse.get(true), is(Arrays.asList(
                Students.JOHN_CS_2,
                Students.SERGEY_ECONOMICS_2,
                Students.SOPHIA_ECONOMICS_2
        )));
        assertThat(byCourse.get(false), is(Arrays.asList(
                Students.STEVE_CS_4, Students.MARIA_ECONOMICS_1, Students.VICTORIA_CS_3,
                Students.GEORGE_LAW_3, Students.SERGEY_LAW_1, Students.MARIA_CS_1
        )));
    }

    @Test
    public void testPartitioningByStudentCourseToNames() {
        Map<Boolean, String> byCourse = Stream.of(Students.ALL)
                .collect(Collectors.partitioningBy​(new Predicate<Student>() {
                    @Override
                    public boolean test(Student student) {
                        return student.getCourse() > 2;
                    }
                }, Collectors.mapping(
                        Students.studentName,
                        Collectors.joining(", ")
                )));
        assertThat(byCourse.get(true), is("Steve, Victoria, George"));
        assertThat(byCourse.get(false), is("Maria, John, Sergey, Sergey, Sophia, Maria"));
    }

    @Test
    public void testFiltering() {
        List<Integer> list;
        list = Stream.rangeClosed(1, 6)
                .collect( Collectors.filtering(Functions.remainder(2), Collectors.<Integer>toList()) );
        assertThat(list, contains(2, 4, 6));

        list = Stream.rangeClosed(1, 6)
                .collect( Collectors.filtering(Functions.remainder(20), Collectors.<Integer>toList()) );
        assertThat(list, is(empty()));

        list = Stream.<Integer>empty()
                .collect( Collectors.filtering(Functions.remainder(20), Collectors.<Integer>toList()) );
        assertThat(list, is(empty()));
    }

    @Test
    public void testMappingSquareIntToString() {
        Function<Integer, String> squareToString = new Function<Integer, String>() {
            @Override
            public String apply(Integer value) {
                return Integer.toString(value * value);
            }
        };
        String result = Stream.of(1, 2, 3, 4)
                .collect( Collectors.mapping(squareToString, Collectors.joining(", ")) );
        assertEquals("1, 4, 9, 16", result);
    }

    @Test
    public void testMappingStudentNamesBySpeciality() {
        Map<String, Set<String>> namesBySpeciality = Stream.of(Students.ALL)
                .collect(Collectors.groupingBy(Students.speciality,
                        Collectors.mapping(Students.studentName, Collectors.<String>toSet())));

        assertThat(namesBySpeciality.get("Economics"),
                containsInAnyOrder(new String[] {
                    Students.MARIA_ECONOMICS_1.getName(),
                    Students.SERGEY_ECONOMICS_2.getName(),
                    Students.SOPHIA_ECONOMICS_2.getName()
                }));
        assertThat(namesBySpeciality.get("CS"),
                containsInAnyOrder(new String[] {
                    Students.STEVE_CS_4.getName(),
                    Students.VICTORIA_CS_3.getName(),
                    Students.JOHN_CS_2.getName(),
                    Students.MARIA_CS_1.getName()
                }));
        assertThat(namesBySpeciality.get("Law"),
                containsInAnyOrder(new String[] {
                    Students.GEORGE_LAW_3.getName(),
                    Students.SERGEY_LAW_1.getName()
                }));
    }

    @Test
    public void testFlatMapping() {
        Function<Integer, Stream<Integer>> repeaterFunction = new Function<Integer, Stream<Integer>>() {
            @Override
            public Stream<Integer> apply(final Integer value) {
                if (value < 0) return null;
                if (value == 0) return Stream.empty();
                return IntStream.generate(new IntSupplier() {
                    @Override
                    public int getAsInt() {
                        return value;
                    }
                }).limit(value).boxed();
            }
        };

        List<Integer> list;

        list = Stream.of(1, 2, 3, 4)
                .collect( Collectors.flatMapping(repeaterFunction, Collectors.<Integer>toList()) );
        assertThat(list, contains(1, 2, 2, 3, 3, 3, 4, 4, 4, 4));

        list = Stream.of(-1, -1)
                .collect( Collectors.flatMapping(repeaterFunction, Collectors.<Integer>toList()) );
        assertThat(list, is(empty()));

        list = Stream.of(0, 0)
                .collect( Collectors.flatMapping(repeaterFunction, Collectors.<Integer>toList()) );
        assertThat(list, is(empty()));

        list = Stream.of(2, 0, 3, -4)
                .collect( Collectors.flatMapping(repeaterFunction, Collectors.<Integer>toList()) );
        assertThat(list, contains(2, 2, 3, 3, 3));
    }

    @Test
    public void testCollectingAndThen() {
        List<Integer> result = Stream.of(1, 2, 3, 4).collect(
                Collectors.collectingAndThen(Collectors.<Integer>toList(),
                        new UnaryOperator<List<Integer>>() {
                            @Override
                            public List<Integer> apply(List<Integer> list) {
                                return new LinkedList<Integer>(list);
                            }
                        })
        );
        assertThat(result, instanceOf(LinkedList.class));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(Collectors.class, hasOnlyPrivateConstructors());
    }
}
