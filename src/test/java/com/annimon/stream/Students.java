package com.annimon.stream;

import com.annimon.stream.function.Function;
import java.util.Arrays;
import java.util.List;

/**
 * Holds data for testing purposes.
 */
public final class Students {

    public static final Student STEVE_CS_4 = new Student("Steve", "CS", 4);
    public static final Student MARIA_ECONOMICS_1 = new Student("Maria", "Economics", 1);
    public static final Student VICTORIA_CS_3 = new Student("Victoria", "CS", 3);
    public static final Student JOHN_CS_2 = new Student("John", "CS", 2);
    public static final Student SERGEY_ECONOMICS_2 = new Student("Sergey", "Economics", 2);
    public static final Student GEORGE_LAW_3 = new Student("George", "Law", 3);
    public static final Student SERGEY_LAW_1 = new Student("Sergey", "Law", 1);
    public static final Student SOPHIA_ECONOMICS_2 = new Student("Sophia", "Economics", 2);
    public static final Student MARIA_CS_1 = new Student("Maria", "CS", 1);
    
    public static final List<Student> ALL = Arrays.asList(
            STEVE_CS_4, MARIA_ECONOMICS_1, VICTORIA_CS_3, JOHN_CS_2, SERGEY_ECONOMICS_2,
            GEORGE_LAW_3, SERGEY_LAW_1, SOPHIA_ECONOMICS_2, MARIA_CS_1
    );
    
    
    public static final Function<Student, String> speciality = new Function<Student, String>() {
        @Override
        public String apply(Student student) {
            return student.getSpeciality();
        }
    };
    
    public static final Function<Student, Integer> course = new Function<Student, Integer>() {
        @Override
        public Integer apply(Student student) {
            return student.getCourse();
        }
    };
    
    public static final Function<Student, String> studentName = new Function<Student, String>() {
        @Override
        public String apply(Student student) {
            return student.getName();
        }
    };
}
