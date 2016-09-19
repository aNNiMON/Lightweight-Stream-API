package com.annimon.stream;

import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.UnaryOperator;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code Collectors}.
 *
 * @see com.annimon.stream.Collectors
 */
public class CollectorsTest {

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
    public void testToSet() {
        Set<Integer> set = Stream.of(1, 2, 2, 3, 3, 3)
                .collect(Collectors.<Integer>toSet());
        assertThat(set, containsInAnyOrder(1, 2, 3));
    }

    @Test
    public void testToSortedSet() {
        SortedSet<Integer> set = Stream.of(4, 3, 3, 2, 1)
            .collect(Collectors.<Integer>toSortedSet());
        assertThat(set, contains(1, 2, 3, 4));
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
    public void testToMapWithCustomValueMapper() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        Map<Character, String> chars = Stream.of("a0", "b0", "c0", "d0")
                .collect(Collectors.toMap(keyMapper, new UnaryOperator<String>() {

            @Override
            public String apply(String value) {
                if ("c0".equals(value)) return null;
                return String.valueOf(Character.toUpperCase(value.charAt(0)));
            }
        }));

        assertThat(chars.size(), is(3));
        assertThat(chars, allOf(
                hasEntry('a', "A"),
                hasEntry('b', "B"),
                hasEntry('d', "D")
        ));
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
    public void testAveraging() {
        double avg = Stream.of(10, 20, 30, 40)
                .collect(Collectors.averaging(new Function<Integer, Double>() {
                    @Override
                    public Double apply(Integer value) {
                        return value / 10d;
                    }
                }));
        assertThat(avg, closeTo(2.5, 0.001));
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
