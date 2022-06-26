package com.revature.courseapp.course;

import com.revature.courseapp.user.Student;
import com.revature.courseapp.utils.*;

public class Course {
    protected int courseId;
    protected String courseName;
    protected String semester;
    protected int capacity;
    protected List<Student> enrolledStudents = new LinkedList<Student>();

    public int getId () {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Course (int id, String name, String semester, int capacity) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
    }

    public Course (int id, String name, String semester, int capacity, List<Student> students) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
        this.enrolledStudents = students;
    }

    public boolean isFull () {
        return enrolledStudents.size() >= capacity;
    }

    public int getNumberOfStudents () {
        return enrolledStudents.size();
    }

    public void setEnrolledStudents (List<Student> students) {
        enrolledStudents = students;
    }

    public boolean isStudentInCourse (Student student) {
        return enrolledStudents.getIndex (student) != -1;
    }

    public void registerCourse (Student student) {
        if (!isStudentInCourse(student)) {
            enrolledStudents.add(student);
        } 
    }
}
