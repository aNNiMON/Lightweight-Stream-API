package com.annimon.stream;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.ToDoubleFunction;
import com.annimon.stream.function.ToIntFunction;
import com.annimon.stream.function.ToLongFunction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ComparatorCompatTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(ComparatorCompat.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testReversed() {
        int[] expected = {13, 8, 5, 3, 2, 1};
        IntStream stream = IntStream.of(1, 2, 3, 5, 8, 13)
                .sorted(ComparatorCompat.<Integer>reversed());
        assertThat(stream.toArray(), is(expected));
    }

    @Test
    public void testReversedComparator() {
        int[] expected = {1, -2, 4, -8, 16};
        IntStream stream = IntStream.of(-8, -2, 1, 4, 16)
                .sorted(ComparatorCompat.reversed(
                        Functions.descendingAbsoluteOrder()
                ));
        assertThat(stream.toArray(), is(expected));
    }

    @Test
    public void testThenComparing() {
        int[] expected = {16, -16, 4, -4, -2, 1};
        IntStream stream = IntStream.of(-16, -4, -2, 1, 4, 16)
                .sorted(ComparatorCompat.thenComparing(
                        Functions.descendingAbsoluteOrder(),
                        ComparatorCompat.reversed()
                ));
        assertThat(stream.toArray(), is(expected));
    }

    @Test
    public void testComparing() {
        List<String> expected = Arrays.asList("abcd", "abc", "ab", "a");
        Stream<String> stream = Stream.of("abc", "ab", "abcd", "a")
                .sorted(ComparatorCompat.comparing(
                        new Function<String, Integer>() {
                            @Override
                            public Integer apply(String str) {
                                return str.length();
                            }
                        },
                        ComparatorCompat.reversed()
                ));
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testComparingComparable() {
        List<String> expected = Arrays.asList("a", "ab", "abc", "abcd");
        Stream<String> stream = Stream.of("abc", "ab", "abcd", "a")
                .sorted(ComparatorCompat.comparing(
                        new Function<String, Integer>() {
                            @Override
                            public Integer apply(String str) {
                                return str.length();
                            }
                        }
                ));
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testComparingInt() {
        List<String> expected = Arrays.asList("a", "ab", "abc", "abcd");
        Stream<String> stream = Stream.of("abc", "ab", "abcd", "a")
                .sorted(ComparatorCompat.comparingInt(
                        new ToIntFunction<String>() {
                            @Override
                            public int applyAsInt(String str) {
                                return str.length();
                            }
                        }
                ));
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testComparingLong() {
        List<String> expected = Arrays.asList("a", "ab", "abc", "abcd");
        Stream<String> stream = Stream.of("abc", "ab", "abcd", "a")
                .sorted(ComparatorCompat.comparingLong(
                        new ToLongFunction<String>() {
                            @Override
                            public long applyAsLong(String str) {
                                return str.length() * 10000000L;
                            }
                        }
                ));
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testComparingDouble() {
        String[] expected = {"a", "ab", "abc", "abcd"};
        Stream<String> stream = Stream.of("abc", "ab", "abcd", "a")
                .sorted(ComparatorCompat.comparingDouble(
                        new ToDoubleFunction<String>() {
                            @Override
                            public double applyAsDouble(String str) {
                                return str.length() / 0.01d;
                            }
                        }
                ));
        assertThat(stream, elements(is(Arrays.asList(expected))));
    }

    @Test
    public void testChain_CourseReversed() {
        Comparator<Student> comparator = ComparatorCompat
                .chain(ComparatorCompat.comparing(Students.course))
                .reversed()
                .comparator();

        List<Student> input = Arrays.asList(
                Students.MARIA_CS_1,
                Students.STEVE_CS_4,
                Students.VICTORIA_CS_3
        );
        List<Student> expected = Arrays.asList(
                Students.STEVE_CS_4,
                Students.VICTORIA_CS_3,
                Students.MARIA_CS_1
        );
        Stream<Student> stream = Stream.of(input)
                .sorted(comparator);
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testChain_CourseThenName() {
        Comparator<Student> comparator = ComparatorCompat
                .chain(ComparatorCompat.comparing(Students.course))
                .thenComparing(Students.studentName)
                .comparator();

        List<Student> input = Arrays.asList(
                Students.MARIA_CS_1,
                Students.STEVE_CS_4,
                Students.VICTORIA_CS_3,
                Students.SERGEY_LAW_1
        );
        List<Student> expected = Arrays.asList(
                Students.MARIA_CS_1,
                Students.SERGEY_LAW_1,
                Students.VICTORIA_CS_3,
                Students.STEVE_CS_4
        );
        Stream<Student> stream = Stream.of(input)
                .sorted(comparator);
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testChain_SpecialityThenCourseThenName() {
        Comparator<Student> comparator = ComparatorCompat
                .chain(ComparatorCompat.comparing(Students.speciality))
                .thenComparingInt(new ToIntFunction<Student>() {
                    @Override
                    public int applyAsInt(Student student) {
                        return student.getCourse();
                    }
                })
                .thenComparing(Students.studentName)
                .comparator();

        List<Student> input = Arrays.asList(
                Students.STEVE_CS_4,
                Students.SERGEY_LAW_1,
                Students.MARIA_CS_1,
                Students.SOPHIA_ECONOMICS_2,
                Students.GEORGE_LAW_3,
                Students.VICTORIA_CS_3
        );
        List<Student> expected = Arrays.asList(
                Students.MARIA_CS_1,
                Students.VICTORIA_CS_3,
                Students.STEVE_CS_4,
                Students.SOPHIA_ECONOMICS_2,
                Students.SERGEY_LAW_1,
                Students.GEORGE_LAW_3
        );
        Stream<Student> stream = Stream.of(input)
                .sorted(comparator);
        assertThat(stream, elements(is(expected)));
    }

    @Test
    public void testChain_NameReversedThenCourseThenSpeciality() {
        Comparator<Student> comparator = ComparatorCompat
                .chain(ComparatorCompat.comparing(Students.studentName))
                .reversed()
                .thenComparing(Students.course)
                .thenComparing(Students.speciality)
                .comparator();
        testStudentComparator(comparator);
    }

    @Test
    public void testChain_NameReversedThenCourseThenSpecialityDoubleReversed() {
        Comparator<Student> comparator = ComparatorCompat
                .chain(ComparatorCompat.comparing(Students.studentName))
                .thenComparing(ComparatorCompat.reversed())
                .thenComparingLong(new ToLongFunction<Student>() {
                    @Override
                    public long applyAsLong(Student student) {
                        return student.getCourse() * 100000L;
                    }
                })
                .thenComparing(Students.speciality, ComparatorCompat.<String>reversed())
                .reversed()
                .comparator();
        testStudentComparator(comparator);
    }

    @Test
    public void testChain_NameReversedThenCourseThenSpeciality2() {
        Comparator<Student> comparator = ComparatorCompat
                .chain(ComparatorCompat.comparing(Students.studentName))
                .reversed()
                .thenComparingDouble(new ToDoubleFunction<Student>() {
                    @Override
                    public double applyAsDouble(Student student) {
                        return student.getCourse() / 0.001;
                    }
                })
                .thenComparing(Students.speciality)
                .comparator();
        testStudentComparator(comparator);
    }

    public void testStudentComparator(Comparator<Student> comparator) {
        List<Student> input = Arrays.asList(
                Students.STEVE_CS_4,
                Students.SERGEY_LAW_1,
                Students.MARIA_CS_1,
                Students.SOPHIA_ECONOMICS_2,
                Students.MARIA_ECONOMICS_1
        );
        List<Student> expected = Arrays.asList(
                Students.STEVE_CS_4,
                Students.SOPHIA_ECONOMICS_2,
                Students.SERGEY_LAW_1,
                Students.MARIA_CS_1,
                Students.MARIA_ECONOMICS_1
        );
        Stream<Student> stream = Stream.of(input)
                .sorted(comparator);
        assertThat(stream, elements(is(expected)));
    }
}

