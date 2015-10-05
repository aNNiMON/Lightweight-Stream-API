package com.annimon.stream;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.UnaryOperator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class CollectorsTest {
    
    @Test
    public void toList() {
        List<Integer> list = Stream.ofRange(0, 10).collect(Collectors.<Integer>toList());
        assertEquals(10, list.size());
        int index = 0;
        for (int v : list) {
            assertEquals(index++, v);
        }
    }

    @Test
    public void toSet() {
        Set<Integer> set = Stream.of(1, 2, 2, 3, 3, 3).collect(Collectors.<Integer>toSet());
        assertEquals(3, set.size());
        int index = 1;
        for (int v : set) {
            assertEquals(index++, v);
        }
    }
    
    @Test
    public void toMap() {
        Map<Character, String> chars = Stream.of("a", "b", "c", "d")
                .collect(Collectors.toMap(new Function<String, Character>() {

            @Override
            public Character apply(String value) {
                return value.charAt(0);
            }
        }, new Function<String, String>() {

            @Override
            public String apply(String value) {
                return value;
            }
        }));
        assertEquals(4, chars.size());
        assertEquals("a", chars.get('a'));
        assertEquals("b", chars.get('b'));
        assertEquals("c", chars.get('c'));
        assertEquals("d", chars.get('d'));
    }

    @Test
    public void joining() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Collectors.joining());
        assertEquals("abcdefg", text);
    }

    @Test
    public void joiningWithDelimiter() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Collectors.joining(", "));
        assertEquals("a, b, c, def, , g", text);
    }

    @Test
    public void joiningWithDelimiterPrefixAndSuffixEmpty() {
        String text = Stream.of(Collections.<String>emptyList())
                .collect(Collectors.joining(", ", "prefix|", "|suffix", "empty"));
        assertEquals("empty", text);
    }

    @Test
    public void joiningWithDelimiterPrefixAndSuffixEmptyStream() {
        String text = Stream.of(Collections.<String>emptyList())
                .collect(Collectors.joining(", ", "prefix|", "|suffix"));
        assertEquals("prefix||suffix", text);
    }

    @Test
    public void joiningWithDelimiterPrefixAndSuffix() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Collectors.joining(", ", "prefix|", "|suffix"));
        assertEquals("prefix|a, b, c, def, , g|suffix", text);
    }

    @Test
    public void averaging() {
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
    public void counting() {
        long count = Stream.ofRange(0, 20).collect(Collectors.counting());
        assertEquals(20, count);
    }
    
    @Test
    public void groupingBy() {
        final Integer partitionItem = 1;
        List<Integer> items = Arrays.asList(1, 2, 3, 1, 2, 3, 1, 2, 3);

        Map<Boolean, List<Integer>> groupedBy = Stream.of(items)
                .collect(Collectors.groupingBy(new Function<Integer, Boolean>() {

            @Override
            public Boolean apply(Integer value) {
                return value.equals(partitionItem);
            }
        }));
        
        assertArrayEquals(new Integer[] {1, 1, 1}, groupedBy.get(true).toArray());
        assertArrayEquals(new Integer[] {2, 3, 2, 3, 2, 3}, groupedBy.get(false).toArray());
    }
    
    @Test
    public void groupingByCounting() {
        Map<Integer, Long> byCounting = Stream.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)
                .collect(Collectors.groupingBy(UnaryOperator.Util.<Integer>identity(), Collectors.counting()));
        for (int i = 1; i <= 4; i++) {
            assertEquals(i, byCounting.get(i).longValue());
        }
    }
    
    @Test
    public void groupingByStudent() {
        final Student STEVE_CS_4 = new Student("Steve", "CS", 4);
        final Student MARIA_ECONOMICS_1 = new Student("Maria", "Economics", 1);
        final Student VICTORIA_CS_3 = new Student("Victoria", "CS", 3);
        final Student JOHN_CS_2 = new Student("John", "CS", 2);
        final Student SERGEY_ECONOMICS_2 = new Student("Sergey", "Economics", 2);
        final Student GEORGE_LAW_3 = new Student("George", "Law", 3);
        final Student SERGEY_LAW_1 = new Student("Sergey", "Law", 1);
        final Student SOPHIA_ECONOMICS_2 = new Student("Sophia", "Economics", 2);
        final Student MARIA_CS_1 = new Student("Maria", "CS", 1);
        final List<Student> students = Arrays.asList(
                STEVE_CS_4, MARIA_ECONOMICS_1, VICTORIA_CS_3, JOHN_CS_2, SERGEY_ECONOMICS_2,
                GEORGE_LAW_3, SERGEY_LAW_1, SOPHIA_ECONOMICS_2, MARIA_CS_1
        );
        
        final Function<Student, String> speciality = new Function<Student, String>() {
            @Override
            public String apply(Student student) {
                return student.getSpeciality();
            }
        };
        final Function<Student, Integer> course = new Function<Student, Integer>() {
            @Override
            public Integer apply(Student student) {
                return student.getCourse();
            }
        };
        
        Map<String, List<Student>> bySpeciality = Stream.of(students)
                .collect(Collectors.groupingBy(speciality));
        assertArrayEquals(
                new Student[] {STEVE_CS_4, VICTORIA_CS_3, JOHN_CS_2, MARIA_CS_1},
                bySpeciality.get("CS").toArray());
        
        assertArrayEquals(
                new Student[] {MARIA_ECONOMICS_1, SERGEY_ECONOMICS_2, SOPHIA_ECONOMICS_2},
                bySpeciality.get("Economics").toArray());
        
        assertArrayEquals(
                new Student[] {GEORGE_LAW_3, SERGEY_LAW_1},
                bySpeciality.get("Law").toArray());
        
        
        Map<Integer, List<Student>> byCourse = Stream.of(students)
                .collect(Collectors.groupingBy(course));
        assertArrayEquals(
                new Student[] {MARIA_ECONOMICS_1, SERGEY_LAW_1, MARIA_CS_1},
                byCourse.get(1).toArray());
        
        assertArrayEquals(
                new Student[] {JOHN_CS_2, SERGEY_ECONOMICS_2, SOPHIA_ECONOMICS_2},
                byCourse.get(2).toArray());
        
        assertArrayEquals(
                new Student[] {VICTORIA_CS_3, GEORGE_LAW_3},
                byCourse.get(3).toArray());
        
        assertArrayEquals(
                new Student[] {STEVE_CS_4},
                byCourse.get(4).toArray());
        
        
        Map<String, Map<Integer, List<Student>>> bySpecialityAndCourse = Stream.of(students)
                .collect(Collectors.groupingBy(speciality, Collectors.groupingBy(course)));
        assertArrayEquals(
                new Student[] {SERGEY_ECONOMICS_2, SOPHIA_ECONOMICS_2},
                bySpecialityAndCourse.get("Economics").get(2).toArray());
        
        
        Map<Integer, Long> byCourseCounting = Stream.of(students)
                .collect(Collectors.groupingBy(course, Collectors.counting()));
        assertEquals(3, byCourseCounting.get(1).longValue());
        assertEquals(3, byCourseCounting.get(2).longValue());
        assertEquals(2, byCourseCounting.get(3).longValue());
        assertEquals(1, byCourseCounting.get(4).longValue());
    }
}
