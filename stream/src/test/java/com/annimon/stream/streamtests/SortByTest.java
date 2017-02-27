package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.Student;
import com.annimon.stream.Students;
import com.annimon.stream.function.Function;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SortByTest {

    @Test
    public void testSortByStringLength() {
        List<String> expected = Arrays.asList("a", "is", "This", "test");
        Stream<String> stream = Stream.of("This", "is", "a", "test")
                .sortBy(new Function<String, Integer>() {

                    @Override
                    public Integer apply(String value) {
                        return value.length();
                    }
                });
        assertThat(stream, elements(is(expected)));
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

        Stream<Student> stream = Stream.of(students)
                .sortBy(new Function<Student, String>() {
                    @Override
                    public String apply(Student student) {
                        return student.getName();
                    }
                });
        assertThat(stream, elements(is(expected)));
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
        Stream<Student> byCourseDesc = Stream.of(students)
                .sortBy(new Function<Student, Integer>() {
                    @Override
                    public Integer apply(Student student) {
                        return -student.getCourse();
                    }
                });
        assertThat(byCourseDesc, elements(is(expected)));
    }
}
