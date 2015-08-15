package com.annimon.stream;

/**
 *
 * @author aNNiMON
 */
public final class Student {

    private String name;
    private String speciality;
    private int course;

    public Student(String name, String speciality, int course) {
        this.name = name;
        this.speciality = speciality;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Student{" + "name=" + name + ", speciality=" + speciality + ", course=" + course + '}';
    }
}
