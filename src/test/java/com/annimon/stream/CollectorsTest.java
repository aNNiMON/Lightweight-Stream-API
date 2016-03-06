package com.annimon.stream;

import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.annimon.stream.function.UnaryOperator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests {@code Collectors}.
 * 
 * @see com.annimon.stream.Collectors
 */
public class CollectorsTest {
    
    @Test
    public void testToCollection() {
        Collection<Integer> result = Stream.range(0, 10)
                .collect(Collectors.toCollection(new Supplier<Collection<Integer>>() {
                    @Override
                        public Collection<Integer> get() {
                            return new LinkedList<Integer>();
                        }
                    }));
        
        assertEquals(10, result.size());
        int index = 0;
        for (int v : result) {
            assertEquals(index++, v);
        }
        assertThat(result, instanceOf(LinkedList.class));
    }
    
    @Test
    public void testToList() {
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> list = Stream.range(0, 10)
                .collect(Collectors.<Integer>toList());
        
        assertEquals(10, list.size());
        assertThat(list, is(expected));
    }

    @Test
    public void testToSet() {
        Set<Integer> set = Stream.of(1, 2, 2, 3, 3, 3)
                .collect(Collectors.<Integer>toSet());
        
        assertEquals(3, set.size());
        int index = 1;
        for (int v : set) {
            assertEquals(index++, v);
        }
    }
    
    @Test
    public void testToMapWithIdentityValueMapper() {
        final Function<String, Character> keyMapper = Functions.firstCharacterExtractor();
        Map<Character, String> chars = Stream.of("a", "b", "c", "d")
                .collect(Collectors.toMap(keyMapper, UnaryOperator.Util.<String>identity()));
        
        assertEquals(4, chars.size());
        assertEquals("a", chars.get('a'));
        assertEquals("b", chars.get('b'));
        assertEquals("c", chars.get('c'));
        assertEquals("d", chars.get('d'));
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
        
        assertEquals(3, chars.size());
        assertEquals("A", chars.get('a'));
        assertEquals("B", chars.get('b'));
        assertEquals("D", chars.get('d'));
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
        assertEquals(2.5, avg, 0.001);
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
        assertEquals(2.28, sumDiv, 0.01);
    }
    
    @Test
    public void testGroupingBy() {
        final Integer partitionItem = 1;
        List<Integer> items = Arrays.asList(1, 2, 3, 1, 2, 3, 1, 2, 3);
        
        Map<Boolean, List<Integer>> groupedBy = Stream.of(items)
                .collect(Collectors.groupingBy(Functions.equalityPartitionItem(partitionItem)));
        
        assertThat(groupedBy.get(true), is(Arrays.asList(1, 1, 1)));
        assertThat(groupedBy.get(false), is(Arrays.asList(2, 3, 2, 3, 2, 3)));
    }
    
    @Test
    public void testGroupingByCounting() {
        Map<Integer, Long> byCounting = Stream.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)
                .collect(Collectors.groupingBy(
                        UnaryOperator.Util.<Integer>identity(),
                        Collectors.counting()));
        for (int i = 1; i <= 4; i++) {
            assertEquals(i, byCounting.get(i).longValue());
        }
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
        
        assertEquals(3, byCourseCounting.get(1).longValue());
        assertEquals(3, byCourseCounting.get(2).longValue());
        assertEquals(2, byCourseCounting.get(3).longValue());
        assertEquals(1, byCourseCounting.get(4).longValue());
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
        
        TestUtils.assertArrayEqualsInAnyOrder(
                new String[] {
                    Students.MARIA_ECONOMICS_1.getName(),
                    Students.SERGEY_ECONOMICS_2.getName(),
                    Students.SOPHIA_ECONOMICS_2.getName()
                },
                namesBySpeciality.get("Economics").toArray());
        
        TestUtils.assertArrayEqualsInAnyOrder(
                new String[] {
                    Students.STEVE_CS_4.getName(),
                    Students.VICTORIA_CS_3.getName(),
                    Students.JOHN_CS_2.getName(),
                    Students.MARIA_CS_1.getName()
                },
                namesBySpeciality.get("CS").toArray());
        
        TestUtils.assertArrayEqualsInAnyOrder(
                new String[] {
                    Students.GEORGE_LAW_3.getName(),
                    Students.SERGEY_LAW_1.getName()
                },
                namesBySpeciality.get("Law").toArray());
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
        TestUtils.testPrivateConstructor(Collectors.class);
    }
}
