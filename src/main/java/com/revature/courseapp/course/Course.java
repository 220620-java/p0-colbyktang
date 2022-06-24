package com.revature.courseapp.course;

import com.revature.courseapp.user.Student;

public class Course {
    protected String courseId;
    protected String courseName;


    protected String semester;
    protected int capacity;
    // protected List<Student> enrolledStudents = new ArrayList<Student>();

    public String getId () {
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

    public Course (String id, String name, String semester, int capacity) {
        this.courseId = id;
        this.courseName = name;
        this.semester = semester;
        this.capacity = capacity;
    }

    // public boolean IsFull () {
    //     return enrolledStudents.size() >= capacity;
    // }
}
